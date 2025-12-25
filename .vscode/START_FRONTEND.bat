@echo off
chcp 65001 > nul
echo ========================================
echo    RuoYi-Vue-Plus 前端启动脚本
echo ========================================
echo.

cd /d "%~dp0plus-ui-v5.5.1-v2.5.1"

echo [1/2] 检查依赖...
if not exist "node_modules" (
    echo 首次运行，正在安装依赖...
    call npm install --registry=https://registry.npmmirror.com
)

echo.
echo [2/2] 正在启动前端开发服务器...
echo 端口: 80
echo.

npm run dev

pause
