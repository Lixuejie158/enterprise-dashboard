-- ============================================
-- Enterprise Dashboard Indexes
-- ============================================

USE enterprise_dashboard;

-- Sales Order Indexes
CREATE INDEX idx_order_customer ON biz_sales_order(customer_id);
CREATE INDEX idx_order_salesperson ON biz_sales_order(salesperson_id);
CREATE INDEX idx_order_date ON biz_sales_order(order_date);
CREATE INDEX idx_order_region ON biz_sales_order(region);
CREATE INDEX idx_order_payment_status ON biz_sales_order(payment_status);

-- Order Item Indexes
CREATE INDEX idx_item_order ON biz_order_item(order_id);
CREATE INDEX idx_item_product ON biz_order_item(product_id);

-- Financial Monthly Indexes
CREATE INDEX idx_financial_year_month ON biz_financial_monthly(year_month);
CREATE INDEX idx_financial_dept ON biz_financial_monthly(dept_id);

-- Risk Alert Indexes
CREATE INDEX idx_alert_dept ON biz_risk_alert(dept_id);
CREATE INDEX idx_alert_status ON biz_risk_alert(status);
CREATE INDEX idx_alert_level ON biz_risk_alert(alert_level);
CREATE INDEX idx_alert_type ON biz_risk_alert(alert_type);
CREATE INDEX idx_alert_time ON biz_risk_alert(alert_time);

-- Employee Indexes
CREATE INDEX idx_emp_dept ON sys_employee(dept_id);

-- Customer Indexes
CREATE INDEX idx_customer_region ON biz_customer(region);
CREATE INDEX idx_customer_level ON biz_customer(level);
