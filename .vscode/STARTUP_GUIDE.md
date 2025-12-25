# 启动步骤

## 首次运行前的准备工作

### 1. 编译后端项目

在命令行中执行：

```bash
cd D:\test\ruoyi\RuoYi-Vue-Plus-v5.5.1
mvn clean compile -DskipTests
```

等待编译完成（可能需要几分钟）。

### 2. 检查数据库连接

确保 MySQL 和 Redis 正在运行。

如果遇到数据库连接错误，修改以下文件：

**主应用配置**:
`RuoYi-Vue-Plus-v5.5.1\ruoyi-admin\src\main\resources\application-dev.yml`

**SnailJob 配置**:
`RuoYi-Vue-Plus-v5.5.1\ruoyi-extend\ruoyi-snailjob-server\src\main\resources\application-dev.yml`

确保用户名、密码和数据库名一致。

### 3. 安装前端依赖

```bash
cd D:\test\ruoyi\plus-ui-v5.5.1-v2.5.1
npm install
```

## 在 VS Code 中启动

### 方法一：使用 VS Code 调试功能

1. **启动后端**
   - 在 VS Code 中打开 `D:\test\ruoyi` 目录
   - 按 `F5` 或点击左侧 "Run and Debug"
   - 选择 "RuoYi Backend (ruoyi-admin)"

2. **启动 SnailJob**（可选）
   - 在 VS Code 中打开 `D:\test\ruoyi` 目录
   - 按 `F5`
   - 选择 "SnailJob Server"

3. **启动前端**
   - 在 VS Code 中打开 `D:\test\ruoyi\plus-ui-v5.5.1-v2.5.1` 目录
   - 按 `F5`
   - 选择 "Launch Frontend Dev Server"

### 方法二：使用命令行（推荐用于开发）

**启动后端**:
```bash
cd D:\test\ruoyi\RuoYi-Vue-Plus-v5.5.1
mvn spring-boot:run -pl ruoyi-admin
```

**启动 SnailJob**（新开一个终端）:
```bash
cd D:\test\ruoyi\RuoYi-Vue-Plus-v5.5.1\ruoyi-extend\ruoyi-snailjob-server
mvn spring-boot:run
```

**启动前端**（新开一个终端）:
```bash
cd D:\test\ruoyi\plus-ui-v5.5.1-v2.5.1
npm run dev
```

## 访问地址

启动成功后访问：
- 前端: http://localhost:80
- 后端 API: http://localhost:8080
- 接口文档: http://localhost:8080/doc.html
- SnailJob: http://localhost:8800/snail-job

## 常见问题

### 找不到主类
确保已经执行 `mvn clean compile` 编译项目。

### 数据库连接失败
检查 MySQL 是否启动，用户名密码是否正确（默认 root/123456）。

### 前端无法连接后端
确保后端已启动，检查后端控制台是否看到 "Started RuoYiApplication"。
