-- ============================================
-- Enterprise Dashboard Database Schema
-- ============================================

CREATE DATABASE IF NOT EXISTS enterprise_dashboard DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE enterprise_dashboard;

-- ============================================
-- 1. Department Table
-- ============================================
CREATE TABLE sys_department (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    dept_name VARCHAR(50) NOT NULL COMMENT '部门名称',
    parent_id BIGINT DEFAULT 0 COMMENT '上级部门ID，0为顶级',
    leader VARCHAR(50) COMMENT '负责人',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态：1启用 0停用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- ============================================
-- 2. Employee Table
-- ============================================
CREATE TABLE sys_employee (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    emp_name VARCHAR(50) NOT NULL COMMENT '员工姓名',
    dept_id BIGINT NOT NULL COMMENT '所属部门ID',
    position VARCHAR(50) COMMENT '职位',
    hire_date DATE COMMENT '入职日期',
    status TINYINT DEFAULT 1 COMMENT '状态：1在职 0离职',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (dept_id) REFERENCES sys_department(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工表';

-- ============================================
-- 3. Customer Table
-- ============================================
CREATE TABLE biz_customer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_name VARCHAR(100) NOT NULL COMMENT '客户名称',
    region VARCHAR(50) COMMENT '所属区域：华东/华南/华北/华中/西南/西北/东北',
    industry VARCHAR(50) COMMENT '所属行业',
    level VARCHAR(10) COMMENT '客户等级：A/B/C',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户表';

-- ============================================
-- 4. Product Table
-- ============================================
CREATE TABLE biz_product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(100) NOT NULL COMMENT '产品名称',
    category VARCHAR(50) COMMENT '产品分类',
    unit_price DECIMAL(12,2) COMMENT '单价',
    unit VARCHAR(20) DEFAULT '件' COMMENT '单位',
    status TINYINT DEFAULT 1 COMMENT '状态：1上架 0下架',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品表';

-- ============================================
-- 5. Sales Order Table
-- ============================================
CREATE TABLE biz_sales_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(32) NOT NULL UNIQUE COMMENT '订单编号',
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    order_date DATE NOT NULL COMMENT '下单日期',
    total_amount DECIMAL(14,2) NOT NULL COMMENT '订单总金额',
    payment_status TINYINT DEFAULT 0 COMMENT '付款状态：0未付款 1部分付款 2已付清',
    salesperson_id BIGINT COMMENT '销售员ID',
    region VARCHAR(50) COMMENT '所属区域',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES biz_customer(id),
    FOREIGN KEY (salesperson_id) REFERENCES sys_employee(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售订单表';

-- ============================================
-- 6. Order Item Table
-- ============================================
CREATE TABLE biz_order_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL COMMENT '订单ID',
    product_id BIGINT NOT NULL COMMENT '产品ID',
    quantity INT NOT NULL COMMENT '数量',
    unit_price DECIMAL(12,2) NOT NULL COMMENT '成交单价',
    subtotal DECIMAL(14,2) NOT NULL COMMENT '小计金额',
    FOREIGN KEY (order_id) REFERENCES biz_sales_order(id),
    FOREIGN KEY (product_id) REFERENCES biz_product(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';

-- ============================================
-- 7. Monthly Financial Summary Table
-- ============================================
CREATE TABLE biz_financial_monthly (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    `year_month` VARCHAR(7) NOT NULL COMMENT '年月，格式：2025-01',
    dept_id BIGINT COMMENT '部门ID，NULL表示公司整体',
    revenue DECIMAL(16,2) DEFAULT 0 COMMENT '营业收入（万元）',
    cost DECIMAL(16,2) DEFAULT 0 COMMENT '营业成本（万元）',
    gross_profit DECIMAL(16,2) DEFAULT 0 COMMENT '毛利润（万元）',
    operating_expense DECIMAL(16,2) DEFAULT 0 COMMENT '运营费用（万元）',
    net_profit DECIMAL(16,2) DEFAULT 0 COMMENT '净利润（万元）',
    total_assets DECIMAL(16,2) DEFAULT 0 COMMENT '总资产（万元）',
    total_liabilities DECIMAL(16,2) DEFAULT 0 COMMENT '总负债（万元）',
    cash_flow DECIMAL(16,2) DEFAULT 0 COMMENT '现金流（万元）',
    accounts_receivable DECIMAL(16,2) DEFAULT 0 COMMENT '应收账款（万元）',
    accounts_payable DECIMAL(16,2) DEFAULT 0 COMMENT '应付账款（万元）',
    inventory_value DECIMAL(16,2) DEFAULT 0 COMMENT '库存金额（万元）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_year_month_dept (`year_month`, dept_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='月度财务汇总表';

-- ============================================
-- 8. Risk Alert Table
-- ============================================
CREATE TABLE biz_risk_alert (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    alert_type VARCHAR(50) NOT NULL COMMENT '预警类型：OVERDUE/库存积压/成本异常/现金流预警',
    alert_level TINYINT NOT NULL COMMENT '预警等级：1低 2中 3高',
    title VARCHAR(200) NOT NULL COMMENT '预警标题',
    content TEXT COMMENT '预警详情',
    dept_id BIGINT COMMENT '关联部门ID',
    status TINYINT DEFAULT 0 COMMENT '处理状态：0未处理 1处理中 2已解决',
    alert_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '预警时间',
    resolve_time DATETIME COMMENT '解决时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险预警表';
