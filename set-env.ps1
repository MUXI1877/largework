# 项目路径和环境变量设置脚本
# 使用方法: . .\set-env.ps1

$env:ROOT_PKG = "com.it.quanxianguanli"
$env:API = "/src/main/java/$env:ROOT_PKG"
$env:RES = "/src/main/resources"

Write-Host "环境变量已设置:" -ForegroundColor Green
Write-Host "  ROOT_PKG = $env:ROOT_PKG"
Write-Host "  API = $env:API"
Write-Host "  RES = $env:RES"
