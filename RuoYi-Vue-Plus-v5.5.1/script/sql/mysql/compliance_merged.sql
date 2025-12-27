-- =====================================================
-- 智能文档合规性检查平台 - MySQL完整数据库脚本
-- 数据库: ry
-- 版本: 1.0 (合并版本)
-- 日期: 2025-12-27
-- 说明: 本脚本合并了业务表创建、修复和清理功能
-- 执行方法: mysql -h localhost -u root -p ry < compliance_merged.sql
-- =====================================================

USE ry;

-- =====================================================
-- 第一部分: 创建业务表
-- =====================================================

-- =====================================================
-- 1. 待检查文档表
-- =====================================================
CREATE TABLE IF NOT EXISTS compliance_document (
    document_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    document_name VARCHAR(200) NOT NULL COMMENT '文档名称',
    file_url VARCHAR(500) NOT NULL COMMENT '文件路径',
    file_size BIGINT COMMENT '文件大小（字节）',
    file_type VARCHAR(20) DEFAULT 'PDF' COMMENT '文件类型',
    upload_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
    uploader BIGINT COMMENT '上传人',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态（PENDING待处理 PROCESSING处理中 COMPLETED已完成 FAILED失败）',
    remark TEXT COMMENT '备注',
    create_dept BIGINT COMMENT '创建部门',
    create_by BIGINT COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by BIGINT COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
    INDEX idx_status (status),
    INDEX idx_uploader (uploader),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='待检查文档表';

-- =====================================================
-- 2. 检查任务表
-- =====================================================
CREATE TABLE IF NOT EXISTS compliance_check_task (
    task_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_name VARCHAR(200) NOT NULL COMMENT '任务名称',
    document_id BIGINT NOT NULL COMMENT '文档ID',
    rule_ids TEXT COMMENT '规则ID列表(JSON数组)',
    check_type VARCHAR(20) DEFAULT 'HARD' COMMENT '检查类型（HARD硬规则 RAG语义规则）',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '任务状态（PENDING待处理 RUNNING运行中 COMPLETED已完成 FAILED失败）',
    progress INT DEFAULT 0 COMMENT '执行进度(0-100)',
    result_summary JSON COMMENT '检查结果摘要',
    error_message TEXT COMMENT '错误信息',
    execute_time DATETIME COMMENT '开始执行时间',
    complete_time DATETIME COMMENT '完成时间',
    create_dept BIGINT COMMENT '创建部门',
    create_by BIGINT COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by BIGINT COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (document_id) REFERENCES compliance_document(document_id),
    INDEX idx_document_id (document_id),
    INDEX idx_status (status),
    INDEX idx_check_type (check_type),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='检查任务表';

-- =====================================================
-- 3. 检查结果明细表
-- =====================================================
CREATE TABLE IF NOT EXISTS compliance_check_result (
    result_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_id BIGINT NOT NULL COMMENT '任务ID',
    document_id BIGINT NOT NULL COMMENT '文档ID',
    rule_id BIGINT COMMENT '规则ID',
    rule_name VARCHAR(200) COMMENT '规则名称',
    rule_type VARCHAR(20) COMMENT '规则类型（HARD RAG）',
    violated BOOLEAN DEFAULT FALSE COMMENT '是否违规',
    severity VARCHAR(20) COMMENT '严重程度（HIGH MEDIUM LOW）',
    location JSON COMMENT '违规位置（页码、段落等）',
    matched_content TEXT COMMENT '匹配内容',
    violation_desc TEXT COMMENT '违规描述',
    suggestion TEXT COMMENT '修改建议',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (task_id) REFERENCES compliance_check_task(task_id) ON DELETE CASCADE,
    FOREIGN KEY (document_id) REFERENCES compliance_document(document_id),
    INDEX idx_task_id (task_id),
    INDEX idx_document_id (document_id),
    INDEX idx_rule_id (rule_id),
    INDEX idx_violated (violated),
    INDEX idx_severity (severity),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='检查结果明细表';

-- =====================================================
-- 第二部分: 修复和防御性检查
-- =====================================================

-- 防御性检查: 确保所有必需字段都存在（如果表已存在但缺少字段）
ALTER TABLE compliance_check_task
ADD COLUMN IF NOT EXISTS create_dept BIGINT COMMENT '创建部门' AFTER complete_time;

-- =====================================================
-- 第三部分: 清理测试数据
-- =====================================================

-- 清理检查结果明细表中的测试数据
DELETE FROM compliance_check_result WHERE task_id IN (
    SELECT task_id FROM (
        SELECT task_id FROM compliance_check_task
        WHERE task_name LIKE '%测试%' OR task_name LIKE '%Test%' OR task_name LIKE '%test%'
    ) AS temp
);

-- 清理检查任务表中的测试数据
DELETE FROM compliance_check_task
WHERE task_name LIKE '%测试%' OR task_name LIKE '%Test%' OR task_name LIKE '%test%';

-- 清理文档表中的测试数据
DELETE FROM compliance_document
WHERE document_name LIKE '%测试%' OR document_name LIKE '%Test%' OR document_name LIKE '%test%';

-- 清理法规表中的测试数据（如果表存在）
DELETE FROM compliance_regulation
WHERE regulation_code LIKE '%TEST%'
   OR regulation_code LIKE '%BATCH%'
   OR regulation_code LIKE '%test%'
   OR regulation_name LIKE '%测试%';

-- 清理规则表中的测试数据（如果表存在）
DELETE FROM compliance_rule
WHERE rule_code LIKE '%TEST%'
   OR rule_code LIKE '%BATCH%'
   OR rule_code LIKE '%test%'
   OR rule_name LIKE '%测试%';

-- =====================================================
-- 第四部分: 初始化示例数据
-- =====================================================

-- 示例文档（仅在表为空时插入）
INSERT IGNORE INTO compliance_document (document_name, file_url, file_size, file_type, uploader, status, create_by)
VALUES ('示例建筑设计标书.pdf', '/upload/compliance/sample-architecture-tender.pdf', 2048576, 'PDF', 1, 'PENDING', 1);

-- =====================================================
-- 第五部分: 验证脚本执行结果
-- =====================================================

-- 显示表结构
SELECT '========== compliance_check_task 表结构 ==========' AS '';
DESCRIBE compliance_check_task;

SELECT '========== compliance_document 表结构 ==========' AS '';
DESCRIBE compliance_document;

SELECT '========== compliance_check_result 表结构 ==========' AS '';
DESCRIBE compliance_check_result;

-- 显示剩余测试数据统计
SELECT '========== 清理验证 ==========' AS '';
SELECT COUNT(*) AS remaining_test_tasks
FROM compliance_check_task
WHERE task_name LIKE '%测试%' OR task_name LIKE '%Test%' OR task_name LIKE '%test%';

SELECT COUNT(*) AS remaining_test_docs
FROM compliance_document
WHERE document_name LIKE '%测试%' OR document_name LIKE '%Test%' OR document_name LIKE '%test%';

-- 显示当前数据统计
SELECT '========== 数据统计 ==========' AS '';
SELECT
    (SELECT COUNT(*) FROM compliance_document) AS total_documents,
    (SELECT COUNT(*) FROM compliance_check_task) AS total_tasks,
    (SELECT COUNT(*) FROM compliance_check_result) AS total_results;

SELECT '========== 脚本执行完成! ==========' AS '';
