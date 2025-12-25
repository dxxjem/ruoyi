@echo off
chcp 65001 > nul
echo ========================================
echo    SnailJob 任务调度服务启动脚本
echo ========================================
echo.

cd /d "%~dp0RuoYi-Vue-Plus-v5.5.1\ruoyi-extend\ruoyi-snailjob-server"

echo [1/1] 正在启动 SnailJob 服务...
echo 端口: 8800
echo 配置: dev
echo.

mvn spring-boot:run

pause
