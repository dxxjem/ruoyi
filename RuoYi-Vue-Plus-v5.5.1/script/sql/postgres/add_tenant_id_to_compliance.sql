-- =====================================================
-- 修复脚本: 为合规性检查表添加租户字段
-- 数据库: compliance_vector_db
-- 版本: 1.0
-- 日期: 2025-12-27
-- 说明: 为 compliance_regulation 和 compliance_rule 表添加 tenant_id 字段
-- =====================================================

\c compliance_vector_db;

-- =====================================================
-- 第一部分: 添加 tenant_id 字段
-- =====================================================

-- 为 compliance_regulation 表添加 tenant_id
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'compliance_regulation'
        AND column_name = 'tenant_id'
    ) THEN
        ALTER TABLE compliance_regulation ADD COLUMN tenant_id VARCHAR(20) DEFAULT '000000';
        COMMENT ON COLUMN compliance_regulation.tenant_id IS '租户编号';

        -- 为现有记录设置默认租户ID
        UPDATE compliance_regulation SET tenant_id = '000000' WHERE tenant_id IS NULL;

        RAISE NOTICE 'compliance_regulation表已添加tenant_id字段';
    ELSE
        RAISE NOTICE 'compliance_regulation表已存在tenant_id字段';
    END IF;
END $$;

-- 为 compliance_rule 表添加 tenant_id
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'compliance_rule'
        AND column_name = 'tenant_id'
    ) THEN
        ALTER TABLE compliance_rule ADD COLUMN tenant_id VARCHAR(20) DEFAULT '000000';
        COMMENT ON COLUMN compliance_rule.tenant_id IS '租户编号';

        -- 为现有记录设置默认租户ID
        UPDATE compliance_rule SET tenant_id = '000000' WHERE tenant_id IS NULL;

        RAISE NOTICE 'compliance_rule表已添加tenant_id字段';
    ELSE
        RAISE NOTICE 'compliance_rule表已存在tenant_id字段';
    END IF;
END $$;

-- =====================================================
-- 第二部分: 创建索引
-- =====================================================

CREATE INDEX IF NOT EXISTS idx_regulation_tenant_id ON compliance_regulation(tenant_id);
CREATE INDEX IF NOT EXISTS idx_rule_tenant_id ON compliance_rule(tenant_id);

-- =====================================================
-- 第三部分: 验证结果
-- =====================================================

-- 显示表结构
SELECT '========== compliance_regulation 表结构 ==========' AS '';
\d compliance_regulation

SELECT '========== compliance_rule 表结构 ==========' AS '';
\d compliance_rule

-- 验证数据
SELECT
    '法规记录数' AS description,
    COUNT(*) AS count,
    COUNT(CASE WHEN tenant_id = '000000' THEN 1 END) AS default_tenant_count
FROM compliance_regulation

UNION ALL

SELECT
    '规则记录数' AS description,
    COUNT(*) AS count,
    COUNT(CASE WHEN tenant_id = '000000' THEN 1 END) AS default_tenant_count
FROM compliance_rule;

SELECT '========== 修复完成! ==========' AS '';
