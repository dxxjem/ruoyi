# CLAUDE.md

此文件为 Claude Code (claude.ai/code) 在此代码库中工作时提供指导。

## 项目概述

这是一个 **RuoYi-Vue-Plus** 多租户管理系统项目（版本 5.5.1），基于 Spring Boot 3.5.7 和 Vue 3.5.13 + TypeScript + Element Plus 构建。

> **重要说明**: 本框架针对分布式集群与多租户场景进行了全方位升级，与原 RuoYi-Vue 框架不兼容。

项目包含两个主要部分：

- **后端**: `RuoYi-Vue-Plus-v5.5.1/` - Java Spring Boot 应用程序
- **前端**: `plus-ui-v5.5.1-v2.5.1/` - Vue 3 + TypeScript 应用程序

## 开发命令

### 后端 (Java/Maven)
进入 `RuoYi-Vue-Plus-v5.5.1/` 目录：

```bash
# 编译项目
mvn clean compile

# 运行测试
mvn test

# 生产环境打包
mvn clean package -Pprod

# 本地运行应用程序（默认使用 dev 配置）
mvn spring-boot:run -pl ruoyi-admin

# 使用指定配置运行
mvn spring-boot:run -pl ruoyi-admin -Plocal

# 跳过测试构建（默认）
mvn clean package -DskipTests
```

### 前端 (Vue.js/TypeScript)
进入 `plus-ui-v5.5.1-v2.5.1/` 目录：

```bash
# 安装依赖
npm install --registry=https://registry.npmmirror.com

# 启动开发服务器（运行在 http://localhost:80）
npm run dev

# 生产环境构建
npm run build:prod

# 开发环境构建
npm run build:dev

# 预览生产构建
npm run preview

# 代码检查和修复
npm run lint:eslint:fix

# 使用 Prettier 格式化代码
npm run prettier
```

### 生产环境部署

```bash
# 后端部署脚本
./RuoYi-Vue-Plus-v5.5.1/script/bin/ry.sh start|stop|restart|status

# Docker 部署
cd RuoYi-Vue-Plus-v5.5.1/script/docker/
docker-compose up -d
```

## 项目架构

### 后端结构 (`RuoYi-Vue-Plus-v5.5.1/`)
```
ruoyi-admin/              # 应用程序入口点和 Web 控制器
ruoyi-common/             # 共享工具类、常量和基础类
├── ruoyi-common-bom/     # Bill of Materials（依赖管理）
├── ruoyi-common-core/    # 核心工具
├── ruoyi-common-web/     # Web 相关工具
├── ruoyi-common-redis/   # Redis 集成
├── ruoyi-common-mybatis/ # MyBatis 工具
├── ruoyi-common-satoken/ # Sa-Token 安全
├── ruoyi-common-tenant/  # 多租户支持
└── ... (其他专用模块)
ruoyi-extend/             # 扩展模块
├── ruoyi-monitor-admin/  # 系统监控
└── ruoyi-snailjob-server/ # 分布式作业调度
ruoyi-modules/            # 核心业务模块
├── ruoyi-system/         # 系统管理（用户、角色、权限、菜单）
├── ruoyi-generator/      # 代码生成工具
├── ruoyi-demo/           # 示例实现和演示
├── ruoyi-job/            # SnailJob 分布式调度
└── ruoyi-workflow/       # 工作流引擎集成（Warm-Flow）
script/                   # 部署脚本和 SQL 数据库脚本
```

### 前端结构 (`plus-ui-v5.5.1-v2.5.1/`)
```
src/
├── api/               # API 服务定义
│   ├── system/        # 系统 API
│   ├── demo/          # 演示 API
│   ├── monitor/       # 监控 API
│   ├── workflow/      # 工作流 API
│   └── tool/          # 工具 API
├── components/        # 可复用的 Vue 组件
├── views/             # 页面组件和路由视图
├── store/             # Pinia 状态管理
├── router/            # Vue Router 配置
├── utils/             # 工具函数和帮助器
├── assets/            # 静态资源（图片、样式、图标）
├── layout/            # 布局组件
├── directive/         # 自定义指令
└── lang/              # 国际化
```

### 核心技术栈
- **后端**: Spring Boot 3.5.7、Sa-Token 1.44.0、MyBatis-Plus 3.5.14、Redis、Redisson 3.51.0、HikariCP、Undertow
- **前端**: Vue 3.5.13、TypeScript 5.8.3、Element Plus 2.9.8、Vite 6.3.2、Pinia 3.0.2、Vue Router 4.5.0
- **数据库**: 支持 MySQL、PostgreSQL、Oracle、SQL Server
- **缓存**: Redis 配合 Redisson 客户端
- **作业调度**: SnailJob 1.8.0（分布式）
- **文档**: SpringDoc 2.8.13 (OpenAPI 3)

### 多租户与特色功能
- **多租户架构** 具备租户隔离功能
- **动态数据源** 支持同时连接多种数据库
- **工作流引擎** 用于复杂审批流程
- **代码生成器** 支持多数据源代码生成
- **文件存储** 支持 Minio 和 S3 兼容提供商
- **短信邮件集成** 通过 sms4j 实现
- **全面监控** 使用 Spring Boot Admin
- **安全特性**: 数据加密、数据脱敏、传输加密

## 技术亮点详解

**后端核心技术:**
- Spring Boot 3.5.7 + Undertow 高性能容器
- Sa-Token 1.44.0 权限认证框架，支持多种认证方式
- MyBatis-Plus 3.5.14 ORM 框架，简化数据库操作
- Redis + Redisson 3.51.0 分布式缓存和锁机制
- HikariCP 高性能数据库连接池
- SnailJob 1.8.0 分布式任务调度系统
- Warm-Flow 1.8.2 工作流引擎支持

**前端核心技术:**
- Vue 3 Composition API 组合式 API
- TypeScript 提供类型安全
- Element Plus UI 组件库
- Vite 现代化构建工具
- Pinia 状态管理
- UnoCSS 原子化 CSS 框架

## 配置文件

### 后端配置
- **application.yml**: 主配置文件，位于 `ruoyi-admin/src/main/resources/`
- **application-dev.yml**: 开发环境设置
- **application-prod.yml**: 生产环境设置
- **Maven 配置文件**: `local`、`dev`（默认）、`prod`

### 前端配置
- **vite.config.ts**: Vite 构建配置
- **.env.development**: 开发环境变量
- **.env.production**: 生产环境变量
- **tsconfig.json**: TypeScript 配置
- **uno.config.ts**: UnoCSS 配置

## 数据库设置
SQL 脚本位于 `RuoYi-Vue-Plus-v5.5.1/script/sql/` 目录：
- **mysql/**: MySQL 数据库脚本

## 架构设计要点

### 模块化设计
后端采用**插件化 + 扩展包**形式的结构解耦，各模块通过 Maven BOM 统一管理依赖版本。`ruoyi-common` 包含多个专用子模块，每个模块提供特定功能（Redis、MyBatis、租户等）。

### 多租户架构
- 租户管理通过 `ruoyi-common-tenant` 模块实现
- 支持租户套餐管理、租户隔离
- MyBatis-Plus 多租户插件自动处理 SQL 租户条件


### 安全认证流程
- Sa-Token 替代 Spring Security，配置更简洁
- JWT 静态使用，支持多种认证方式
- 支持三方登录（JustAuth）
- 接口传输加密（动态 AES + RSA）

### 权限系统
- 基于注解的权限校验：`@SaCheckPermission`、`@SaCheckRole`
- 支持复杂表达式（AND、OR、权限 OR 角色）
- 数据权限通过 MyBatis-Plus 插件实现，无感式 SQL 过滤
- 支持部门、角色、自定义等多种数据权限规则

### 前后端交互
- 前端使用 VXE Table 作为主要表格组件
- API 定义集中在 `src/api/` 目录，按模块分类
- 请求/响应通过 Axios 拦截器统一处理加密、解密
- 使用 Pinia 进行状态管理，模块化 store


## 测试

- **后端**: 使用 JUnit 和 Spring Boot Test 框架
- **前端**: 使用 Vitest 进行单元测试
- **测试配置**: 测试根据激活的 Maven 配置文件运行（`local`、`dev`、`prod`）

## 代码质量

- **后端**: 强制执行阿里巴巴 Java 编码规范
- **前端**: 使用 ESLint + Prettier 进行代码格式化
- **类型检查**: 严格的 TypeScript 配置
- **构建优化**: 生产构建包含压缩和混淆

## 快速开始指南

1. **环境准备**
   - 安装 JDK 17 或 21
   - 安装 Node.js >=18.18.0
   - 准备 MySQL/PostgreSQL/Oracle 数据库

2. **数据库初始化**
   ```bash
   # 执行对应数据库的 SQL 脚本
   cd RuoYi-Vue-Plus-v5.5.1/script/sql/
   # 选择对应数据库目录执行 SQL 文件
   ```

3. **启动后端**
   ```bash
   cd RuoYi-Vue-Plus-v5.5.1/
   mvn spring-boot:run -pl ruoyi-admin
   ```

4. **启动前端**
   ```bash
   cd plus-ui-v5.5.1-v2.5.1/
   npm install
   npm run dev
   ```

5. **访问应用**
   - 前端: http://localhost:80
   - 后端 API: http://localhost:8080
   - 接口文档: http://localhost:8080/doc.html
   - 监控中心: http://localhost:8080/admin

## 常见开发任务

### 添加新的业务模块
1. 在 `ruoyi-modules/` 下创建新模块
2. 添加对 `ruoyi-common-bom` 的依赖
3. 在 `ruoyi-admin/pom.xml` 中添加模块依赖
4. 在前端 `src/api/` 和 `src/views/` 下添加对应页面

### 使用代码生成器
1. 在系统管理 -> 代码生成 中配置数据源
2. 选择表并配置生成参数
3. 生成代码并导入到项目中
