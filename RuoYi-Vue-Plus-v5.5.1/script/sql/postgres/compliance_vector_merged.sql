-- =====================================================
-- 智能文档合规性检查平台 - PostgreSQL向量数据库完整脚本
-- 数据库: compliance_vector_db
-- 版本: 2.0 (合并版本)
-- 日期: 2025-12-27
-- 说明: 本脚本合并了表创建、修复和防御性检查功能
-- 执行方法: psql -h localhost -p 5432 -U postgres -d compliance_vector_db -f compliance_vector_merged.sql
-- =====================================================

-- =====================================================
-- 前置说明
-- =====================================================
-- 1. 请确保已安装 pgvector 扩展
-- 2. 如需创建新数据库，请取消注释下面的 CREATE DATABASE 语句
-- 3. 如数据库已存在，请保持 CREATE DATABASE 语句注释状态

-- 创建数据库（可选）
-- CREATE DATABASE compliance_vector_db
--     ENCODING = 'UTF8'
--     LC_COLLATE = 'zh_CN.UTF-8'
--     LC_CTYPE = 'zh_CN.UTF-8'
--     TEMPLATE = template0;

-- 连接到目标数据库
\c compliance_vector_db;

-- =====================================================
-- 第一部分: 启用pgvector扩展（带错误处理）
-- =====================================================

-- 检查并启用pgvector扩展
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_extension WHERE extname = 'vector') THEN
        CREATE EXTENSION vector;
        RAISE NOTICE 'pgvector扩展已成功安装';
    ELSE
        RAISE NOTICE 'pgvector扩展已存在';
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        RAISE EXCEPTION '无法创建vector扩展，请先安装pgvector: %', SQLERRM;
END $$;

-- =====================================================
-- 第二部分: 创建表结构
-- =====================================================

-- -----------------------------------------------------
-- 1. 法规库表
-- -----------------------------------------------------
DROP TABLE IF EXISTS compliance_regulation CASCADE;

CREATE TABLE compliance_regulation (
    regulation_id BIGSERIAL PRIMARY KEY,
    regulation_code VARCHAR(100) NOT NULL UNIQUE,
    regulation_name VARCHAR(500) NOT NULL,
    category VARCHAR(100),
    effective_date DATE,
    status CHAR(1) DEFAULT '0',
    file_url VARCHAR(500),
    file_size BIGINT,
    remark TEXT,
    create_dept BIGINT,
    create_by BIGINT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by BIGINT,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE compliance_regulation IS '法规库表';
COMMENT ON COLUMN compliance_regulation.regulation_id IS '法规ID';
COMMENT ON COLUMN compliance_regulation.regulation_code IS '法规编码';
COMMENT ON COLUMN compliance_regulation.regulation_name IS '法规名称';
COMMENT ON COLUMN compliance_regulation.category IS '分类';
COMMENT ON COLUMN compliance_regulation.effective_date IS '生效日期';
COMMENT ON COLUMN compliance_regulation.status IS '状态（0正常 1停用）';
COMMENT ON COLUMN compliance_regulation.file_url IS '文件URL';
COMMENT ON COLUMN compliance_regulation.file_size IS '文件大小（字节）';
COMMENT ON COLUMN compliance_regulation.remark IS '备注';
COMMENT ON COLUMN compliance_regulation.create_dept IS '创建部门';
COMMENT ON COLUMN compliance_regulation.create_by IS '创建者';
COMMENT ON COLUMN compliance_regulation.create_time IS '创建时间';
COMMENT ON COLUMN compliance_regulation.update_by IS '更新者';
COMMENT ON COLUMN compliance_regulation.update_time IS '更新时间';

-- -----------------------------------------------------
-- 2. 规则配置表 (硬规则)
-- -----------------------------------------------------
DROP TABLE IF EXISTS compliance_rule CASCADE;

CREATE TABLE compliance_rule (
    rule_id BIGSERIAL PRIMARY KEY,
    rule_code VARCHAR(100) NOT NULL UNIQUE,
    rule_name VARCHAR(200) NOT NULL,
    rule_type VARCHAR(20) NOT NULL, -- 'HARD' or 'RAG'
    category VARCHAR(100),
    keywords TEXT[], -- PostgreSQL数组类型
    description TEXT,
    severity VARCHAR(20) DEFAULT 'MEDIUM', -- 'HIGH', 'MEDIUM', 'LOW'
    status CHAR(1) DEFAULT '0',
    rule_order INT DEFAULT 0,
    create_dept BIGINT,
    create_by BIGINT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by BIGINT,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE compliance_rule IS '规则配置表';
COMMENT ON COLUMN compliance_rule.rule_id IS '规则ID';
COMMENT ON COLUMN compliance_rule.rule_code IS '规则编码';
COMMENT ON COLUMN compliance_rule.rule_name IS '规则名称';
COMMENT ON COLUMN compliance_rule.rule_type IS '规则类型（HARD-硬规则 RAG-语义规则）';
COMMENT ON COLUMN compliance_rule.category IS '分类';
COMMENT ON COLUMN compliance_rule.keywords IS '关键词数组（硬规则用）';
COMMENT ON COLUMN compliance_rule.description IS '描述';
COMMENT ON COLUMN compliance_rule.severity IS '严重程度（HIGH高 MEDIUM中 LOW低）';
COMMENT ON COLUMN compliance_rule.status IS '状态（0正常 1停用）';
COMMENT ON COLUMN compliance_rule.rule_order IS '排序号';
COMMENT ON COLUMN compliance_rule.create_dept IS '创建部门';
COMMENT ON COLUMN compliance_rule.create_by IS '创建者';
COMMENT ON COLUMN compliance_rule.create_time IS '创建时间';
COMMENT ON COLUMN compliance_rule.update_by IS '更新者';
COMMENT ON COLUMN compliance_rule.update_time IS '更新时间';

-- -----------------------------------------------------
-- 3. 文档切块表 (含向量)
-- -----------------------------------------------------
DROP TABLE IF EXISTS compliance_document_chunk CASCADE;

CREATE TABLE compliance_document_chunk (
    chunk_id BIGSERIAL PRIMARY KEY,
    regulation_id BIGINT NOT NULL,
    chunk_index INT NOT NULL,
    content TEXT NOT NULL,
    embedding vector(1536), -- OpenAI text-embedding-3-small
    metadata JSONB,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (regulation_id) REFERENCES compliance_regulation(regulation_id) ON DELETE CASCADE
);

COMMENT ON TABLE compliance_document_chunk IS '文档切块表';
COMMENT ON COLUMN compliance_document_chunk.chunk_id IS '切块ID';
COMMENT ON COLUMN compliance_document_chunk.regulation_id IS '法规ID';
COMMENT ON COLUMN compliance_document_chunk.chunk_index IS '切块序号';
COMMENT ON COLUMN compliance_document_chunk.content IS '文本内容';
COMMENT ON COLUMN compliance_document_chunk.embedding IS '向量嵌入（1536维）';
COMMENT ON COLUMN compliance_document_chunk.metadata IS '元数据（JSON格式）';
COMMENT ON COLUMN compliance_document_chunk.create_time IS '创建时间';

-- =====================================================
-- 第三部分: 创建索引
-- =====================================================

-- 向量索引（HNSW高性能索引）- 仅在pgvector可用时创建
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM pg_extension WHERE extname = 'vector') THEN
        CREATE INDEX IF NOT EXISTS idx_chunk_embedding
        ON compliance_document_chunk
        USING hnsw (embedding vector_cosine_ops)
        WITH (m = 16, ef_construction = 64);

        EXECUTE 'COMMENT ON INDEX idx_chunk_embedding IS ''向量余弦相似度HNSW索引''';
        RAISE NOTICE '向量索引已创建';
    ELSE
        RAISE NOTICE '跳过创建向量索引，因为pgvector扩展未安装';
    END IF;
END $$;

-- 常规索引
CREATE INDEX IF NOT EXISTS idx_regulation_code ON compliance_regulation(regulation_code);
CREATE INDEX IF NOT EXISTS idx_regulation_category ON compliance_regulation(category);
CREATE INDEX IF NOT EXISTS idx_regulation_status ON compliance_regulation(status);

CREATE INDEX IF NOT EXISTS idx_rule_code ON compliance_rule(rule_code);
CREATE INDEX IF NOT EXISTS idx_rule_type ON compliance_rule(rule_type);
CREATE INDEX IF NOT EXISTS idx_rule_category ON compliance_rule(category);
CREATE INDEX IF NOT EXISTS idx_rule_status ON compliance_rule(status);

CREATE INDEX IF NOT EXISTS idx_chunk_regulation_id ON compliance_document_chunk(regulation_id);
CREATE INDEX IF NOT EXISTS idx_chunk_index ON compliance_document_chunk(chunk_index);

-- =====================================================
-- 第四部分: 初始化示例数据（带冲突处理）
-- =====================================================

-- 示例法规
INSERT INTO compliance_regulation (regulation_code, regulation_name, category, effective_date, status, create_by) VALUES
('GB50016-2014', '建筑设计防火规范', '消防', '2014-08-27', '0', 1),
('JGJ36-2016', '宿舍建筑设计规范', '建筑', '2016-10-01', '0', 1)
ON CONFLICT (regulation_code) DO NOTHING;

-- 示例规则（硬规则）
INSERT INTO compliance_rule (rule_code, rule_name, rule_type, category, keywords, description, severity, status, rule_order, create_dept, create_by) VALUES
('RULE-HARD-001', '防火间距检查', 'HARD', '消防', ARRAY['防火间距', '不应小于', '米'], '检查文档中是否包含防火间距要求', 'HIGH', '0', 1, 103, 1),
('RULE-HARD-002', '疏散通道宽度检查', 'HARD', '消防', ARRAY['疏散通道', '净宽度', '不应小于'], '检查疏散通道净宽度要求', 'HIGH', '0', 2, 103, 1),
('RULE-HARD-003', 'RTO指标检查', 'HARD', '运维', ARRAY['RTO', '恢复时间', '目标'], '检查是否包含RTO指标要求', 'MEDIUM', '0', 3, 103, 1)
ON CONFLICT (rule_code) DO NOTHING;

-- =====================================================
-- 第五部分: 防御性检查和修复
-- =====================================================

-- 防御性检查: 确保create_dept字段存在（处理已存在的表）
DO $$
BEGIN
    -- 检查 compliance_rule 表是否有 create_dept 字段
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'compliance_rule'
        AND column_name = 'create_dept'
    ) THEN
        ALTER TABLE compliance_rule ADD COLUMN create_dept BIGINT;
        COMMENT ON COLUMN compliance_rule.create_dept IS '创建部门';
        RAISE NOTICE 'compliance_rule表已添加create_dept字段';
    END IF;

    -- 检查 compliance_regulation 表是否有 create_dept 字段
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'compliance_regulation'
        AND column_name = 'create_dept'
    ) THEN
        ALTER TABLE compliance_regulation ADD COLUMN create_dept BIGINT;
        COMMENT ON COLUMN compliance_regulation.create_dept IS '创建部门';
        RAISE NOTICE 'compliance_regulation表已添加create_dept字段';
    END IF;
END $$;

-- =====================================================
-- 第六部分: 验证脚本执行结果
-- =====================================================

-- 显示表结构
SELECT '========== compliance_regulation 表结构 ==========' AS '';
\d compliance_regulation

SELECT '========== compliance_rule 表结构 ==========' AS '';
\d compliance_rule

SELECT '========== compliance_document_chunk 表结构 ==========' AS '';
\d compliance_document_chunk

-- 显示数据统计
SELECT '========== 数据统计 ==========' AS '';
SELECT
    '法规记录数' AS description,
    COUNT(*) AS count
FROM compliance_regulation

UNION ALL

SELECT
    '规则记录数' AS description,
    COUNT(*) AS count
FROM compliance_rule

UNION ALL

SELECT
    '文档切块数' AS description,
    COUNT(*) AS count
FROM compliance_document_chunk;

-- 显示pgvector扩展状态
SELECT '========== 扩展状态 ==========' AS '';
SELECT
    extname AS extension_name,
    extversion AS version
FROM pg_extension
WHERE extname = 'vector';

SELECT '========== 脚本执行完成! ==========' AS '';
