@echo off
chcp 65001 > nul
echo ========================================
echo    RuoYi-Vue-Plus 后端启动脚本
echo ========================================
echo.

cd /d "%~dp0RuoYi-Vue-Plus-v5.5.1"

echo [1/3] 正在启动后端服务...
echo 端口: 8080
echo 配置: dev
echo.

mvn spring-boot:run -pl ruoyi-admin

pause
