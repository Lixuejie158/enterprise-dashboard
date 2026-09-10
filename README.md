# 企业经营数据分析平台 (Enterprise Operation Dashboard)

企业经营数据分析平台是一个面向企业管理层的经营数据可视化分析平台，通过数据大屏和管理后台两种形式，直观展示企业经营状况，辅助管理决策。

## 技术栈

### 后端
- Java 8
- Spring Boot 2.7.18
- MyBatis-Plus 3.5.3
- MySQL 8.0
- SpringDoc OpenAPI 1.7.0
- Lombok

### 前端
- Vue 3 (Composition API)
- Element Plus 2.4.0
- ECharts 5.4.3
- Axios
- Vite 4.4.9

## 项目结构

```
enterprise-dashboard/
├── backend/                          # 后端项目
│   ├── pom.xml
│   └── src/main/java/com/enterprise/dashboard/
│       ├── DashboardApplication.java
│       ├── config/                   # 配置类
│       ├── controller/               # 控制器
│       ├── service/                  # 业务逻辑层
│       ├── mapper/                   # MyBatis Mapper
│       ├── entity/                   # 实体类
│       ├── dto/                      # 数据传输对象
│       └── common/                   # 通用类
├── frontend/                         # 前端项目
│   ├── package.json
│   ├── vite.config.js
│   └── src/
│       ├── App.vue
│       ├── main.js
│       ├── router/index.js
│       ├── api/                      # 接口封装
│       ├── views/                    # 页面
│       ├── components/               # 公共组件
│       └── styles/                   # 样式文件
├── sql/
│   ├── 01_schema.sql                 # 建表语句
│   ├── 02_mock_data.sql              # 模拟数据
│   └── 03_indexes.sql                # 索引
├── docker-compose.yml                # Docker Compose 配置
├── Dockerfile.backend                # 后端 Docker 镜像
├── Dockerfile.frontend               # 前端 Docker 镜像
└── README.md
```

## 快速开始

### 方式一：Docker Compose 一键启动（推荐）

```bash
# 克隆或下载项目后，在项目根目录执行
docker-compose up -d
```

启动后访问：
- 前端页面：http://localhost
- 后端 API：http://localhost:8080
- Swagger 文档：http://localhost:8080/swagger-ui.html

### 方式二：本地开发

#### 1. 数据库准备

```bash
# 使用 MySQL 8.0 创建数据库并导入数据
mysql -u root -p < sql/01_schema.sql
mysql -u root -p enterprise_dashboard < sql/02_mock_data.sql
mysql -u root -p enterprise_dashboard < sql/03_indexes.sql
```

#### 2. 启动后端

```bash
cd backend
mvn spring-boot:run
```

#### 3. 启动前端

```bash
cd frontend
npm install
npm run dev
```

访问 http://localhost:3000

## 功能模块

### 1. 经营总览大屏 (/dashboard)
- 全屏深色主题数据可视化
- 核心 KPI 一屏展示（营收、净利润、毛利率、订单数）
- 营收/成本/利润趋势分析
- 区域销售排名
- 客户销售额 TOP10
- 产品销量排行
- 风险预警汇总
- 实时数据刷新（30秒间隔）

### 2. 财务分析 (/finance)
- 月度财务报表（营收、成本、利润趋势）
- 资产负债结构分析
- 成本构成分析
- 应收/应付账款对比

### 3. 销售分析 (/sales)
- 区域销售分析
- 渠道销售占比
- 客户排名
- 产品销量排行
- 月度销售同比对比

### 4. 风险预警 (/risk)
- 风险预警统计
- 预警类型分布
- 预警等级分布
- 预警列表管理（支持筛选）

## API 接口

所有接口统一返回格式：`{ "code": 200, "message": "success", "data": {} }`

### 经营总览接口
- `GET /api/dashboard/overview` - 经营总览 KPI
- `GET /api/dashboard/revenue-trend` - 营收趋势
- `GET /api/dashboard/sales-by-region` - 区域销售
- `GET /api/dashboard/top-customers` - 客户销售额 TOP10
- `GET /api/dashboard/product-sales` - 产品销量排行
- `GET /api/dashboard/risk-summary` - 风险预警汇总

### 财务分析接口
- `GET /api/finance/monthly-report` - 月度财务报表
- `GET /api/finance/balance-structure` - 资产负债结构
- `GET /api/finance/cost-analysis` - 成本构成分析

### 销售分析接口
- `GET /api/sales/region-analysis` - 区域销售分析
- `GET /api/sales/channel-analysis` - 渠道销售占比
- `GET /api/sales/customer-ranking` - 客户排名
- `GET /api/sales/product-ranking` - 产品销量排行
- `GET /api/sales/monthly-comparison` - 月度销售对比

### 风险预警接口
- `GET /api/risk/list` - 预警列表（分页）
- `GET /api/risk/statistics` - 预警统计

## 数据库

### 表结构
- `sys_department` - 部门表
- `sys_employee` - 员工表
- `biz_customer` - 客户表
- `biz_product` - 产品表
- `biz_sales_order` - 销售订单表
- `biz_order_item` - 订单明细表
- `biz_financial_monthly` - 月度财务汇总表
- `biz_risk_alert` - 风险预警表

### 模拟数据
- 8 个月数据（2025-01 至 2025-08）
- 8 个部门，50 名员工
- 60 家客户，覆盖 7 个区域
- 20 个产品，4 个分类
- 2000 条销售订单
- 30 条风险预警

## 开发说明

### 前端开发
```bash
cd frontend
npm install
npm run dev     # 开发模式
npm run build   # 生产构建
```

### 后端开发
```bash
cd backend
mvn clean compile
mvn spring-boot:run
```

### 运行测试
```bash
cd backend
mvn test
```

## Docker 说明

### 构建镜像
```bash
# 构建所有镜像
docker-compose build

# 只构建后端
docker-compose build backend

# 只构建前端
docker-compose build frontend
```

### 常见操作
```bash
# 启动所有服务
docker-compose up -d

# 查看日志
docker-compose logs -f

# 停止所有服务
docker-compose down

# 重启某个服务
docker-compose restart backend
```

## 注意事项

1. 数据库连接信息：
   - MySQL 8.0
   - 用户名：root
   - 密码：root123
   - 数据库：enterprise_dashboard

2. 前端开发服务器代理配置：
   - API 请求代理到 http://localhost:8080

3. 大屏页面适配：
   - 设计基准分辨率：1920×1080
   - 使用 rem/vw/vh 做自适应

## License

MIT License
# enterprise-dashboard
