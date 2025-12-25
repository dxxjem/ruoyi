# VS Code 调试配置说明

本项目已配置好 VS Code 的调试环境，包含前端、后端和 SnailJob 的调试配置。

## 目录结构

```
D:\test\ruoyi\
├── .vscode/                          # 后端调试配置
│   ├── launch.json                   # 后端 Java 应用启动配置
│   ├── settings.json                 # VS Code 设置
│   └── extensions.json               # 推荐扩展
└── plus-ui-v5.5.1-v2.5.1/           # 前端目录
    └── .vscode/                      # 前端调试配置
        ├── launch.json               # 前端 Vue 应用启动配置
        ├── tasks.json                # npm 任务配置
        ├── settings.json             # VS Code 设置
        └── extensions.json           # 推荐扩展
```

## 使用前准备

### 1. 安装 VS Code 扩展

打开项目后，VS Code 会提示安装推荐的扩展，主要包括：

**后端扩展（在根目录安装）：**
- Extension Pack for Java (Microsoft)
- Spring Boot Extension Pack (VMware)

**前端扩展（在前端目录安装）：**
- Vue - Official (Vue)
- ESLint (Microsoft)
- Prettier - Code formatter (Prettier)
- UnoCSS (antfu)

### 2. 编译后端项目

在首次调试前，需要先编译后端项目：

```bash
cd D:\test\ruoyi\RuoYi-Vue-Plus-v5.5.1
mvn clean compile
```

### 3. 安装前端依赖

```bash
cd D:\test\ruoyi\plus-ui-v5.5.1-v2.5.1
npm install
```

## 调试配置说明

### 后端调试配置

在根目录 `D:\test\ruoyi` 打开 VS Code，按 `F5` 或点击调试面板，可选择以下配置：

1. **RuoYi Backend (ruoyi-admin)** - 主后端应用
   - 主类: `org.dromara.RuoYiApplication`
   - 端口: 8080
   - 配置: dev
   - JVM 参数: `-Xms512m -Xmx1024m`

2. **SnailJob Server** - 任务调度服务器
   - 主类: `com.aizuda.snailjob.server.SnailJobServerApplication`
   - 端口: 8800
   - 配置: dev
   - JVM 参数: `-Xms256m -Xmx512m`

3. **RuoYi Monitor Admin** - 监控中心
   - 主类: `org.dromara.monitor.MonitorApplication`
   - 端口: 9090
   - 配置: dev
   - JVM 参数: `-Xms256m -Xmx512m`

### 前端调试配置

在前端目录 `D:\test\ruoyi\plus-ui-v5.5.1-v2.5.1` 打开 VS Code，可使用以下配置：

1. **Frontend: Chrome** - 使用 Chrome 调试
2. **Frontend: Edge** - 使用 Edge 调试
3. **Frontend: Attach to Chrome** - 附加到已运行的 Chrome
4. **Launch Frontend Dev Server** - 启动开发服务器并打开浏览器

## 启动顺序

### 完整启动（推荐）

1. **启动数据库**
   - 确保 MySQL 正在运行
   - 确保 Redis 正在运行

2. **启动后端**（在根目录）
   - 按 `F5` 选择 "RuoYi Backend"
   - 等待应用启动完成（看到 "Started RuoYiApplication"）

3. **启动 SnailJob**（在根目录）
   - 新开一个 VS Code 窗口或终端
   - 按 `F5` 选择 "SnailJob Server"

4. **启动前端**（在前端目录）
   - 打开 `D:\test\ruoyi\plus-ui-v5.5.1-v2.5.1`
   - 按 `F5` 选择 "Launch Frontend Dev Server"

### 快速启动（仅开发）

如果只是开发前端功能，只需要：

1. 确保后端已启动（或使用远程后端）
2. 在前端目录按 `F5` 启动前端

## 调试技巧

### 后端调试

1. **设置断点**: 在 Java 代码行号左侧点击
2. **条件断点**: 右键断点 → "Edit Breakpoint" → 添加条件
3. **日志断点**: 右键断点 → "Add Logpoint"
4. **表达式求值**: 调试时在 "DEBUG CONSOLE" 输入表达式

### 前端调试

1. **设置断点**: 在 `.vue` 或 `.ts` 文件中设置断点
2. **Vue DevTools**: 安装 Vue DevTools 浏览器扩展
3. **Network 监控**: 在浏览器开发者工具中查看网络请求

## 常见问题

### 1. 找不到主类

确保已经编译项目：
```bash
cd D:\test\ruoyi\RuoYi-Vue-Plus-v5.5.1
mvn clean compile
```

### 2. 前端无法连接后端

检查后端是否启动，端口是否正确：
- 后端地址: http://localhost:8080
- 前端地址: http://localhost:80

### 3. SnailJob 连接失败

修改数据库配置：
- 文件: `RuoYi-Vue-Plus-v5.5.1/ruoyi-extend/ruoyi-snailjob-server/src/main/resources/application-dev.yml`
- 确保用户名、密码与主应用一致

### 4. Java 虚拟机内存不足

修改 `.vscode/launch.json` 中的 `vmArgs`：
```json
"vmArgs": "-Xms512m -Xmx2048m"  // 增加最大内存
```

## 多工作区配置

如果想在同一个 VS Code 窗口同时调试前后端，可以使用多工作区：

1. 文件 → "打开文件夹" → 选择 `D:\test\ruoyi`
2. 文件 → "将文件夹添加到工作区" → 选择 `D:\test\ruoyi\plus-ui-v5.5.1-v2.5.1`
3. 这样可以在同一个窗口切换前后端调试配置

## 热重载

### 后端热重载
Spring Boot DevTools 已集成，保存 Java 文件后会自动重启。

### 前端热重载
Vite 已配置，保存 `.vue` 或 `.ts` 文件后浏览器会自动刷新。

## 访问地址

启动成功后，可访问：

- **前端应用**: http://localhost:80
- **后端 API**: http://localhost:8080
- **接口文档**: http://localhost:8080/doc.html
- **监控中心**: http://localhost:9090/admin
- **SnailJob 控制台**: http://localhost:8800/snail-job

## 默认账号

根据配置文件，默认的数据库账号：
- 用户名: `root`
- 密码: `123456` (在 application-dev.yml 中配置)
