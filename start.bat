@echo off
echo 启动后端服务...
start /b java -jar target\quanxianguanli-0.0.1-SNAPSHOT.jar > app.log 2>&1
echo 后端服务已启动，日志文件：app.log

