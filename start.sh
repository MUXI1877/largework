#!/bin/bash

# 启动后端服务
echo "启动后端服务..."
nohup java -jar quanxianguanli-0.0.1-SNAPSHOT.jar > app.log 2>&1 &

echo "后端服务已启动，日志文件：app.log"
echo "使用 tail -f app.log 查看日志"

