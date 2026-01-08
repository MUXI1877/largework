import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'

// 直接指向后端服务，避免在非 Vite 代理场景下出现 404
const API_BASE = import.meta.env.VITE_API_BASE || 'http://localhost:8080/api'

const request = axios.create({
    baseURL: API_BASE,
    timeout: 10000
})

// 请求拦截器
request.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token')
        console.log('请求拦截器 - URL:', config.url, 'Token存在:', !!token)

        // ✅ 添加请求数据调试
        if (config.data) {
            console.log('请求数据:', JSON.stringify(config.data, null, 2))
        }
        // ✅ 添加请求参数调试（GET 请求）
        if (config.params) {
            console.log('请求参数:', JSON.stringify(config.params, null, 2))
        }

        // 注册、登录、身份证验证和选项接口不需要token
        const noAuthUrls = ['/auth/', '/user/register', '/user/verify', '/option/list']
        const isNoAuthUrl = noAuthUrls.some(url => config.url.includes(url))

        console.log('是否免token接口:', isNoAuthUrl)

        if (isNoAuthUrl) {
            console.log('跳过添加Authorization头')
            return config
        }
        if (token) {
            config.headers.Authorization = `Bearer ${token}`
            console.log('添加Authorization头')
        }
        return config
    },
    error => {
        return Promise.reject(error)
    }
)
// 响应拦截器
request.interceptors.response.use(
    response => {
        // 部分接口直接返回数据（Page、List 等），没有 code 字段
        const res = response.data
        if (res && typeof res.code !== 'undefined') {
            if (res.code === 200) {
                return res
            }
            ElMessage.error(res.message || '请求失败')
            return Promise.reject(new Error(res.message || '请求失败'))
        }
        // 无 code 字段（如 Spring Data Page），包装成 { data: res } 以兼容现有调用
        return { data: res }
    },
    error => {
        if (error.response && error.response.status === 401) {
            localStorage.removeItem('token')
            router.push('/login')
            ElMessage.error('登录已过期，请重新登录')
        } else {
            ElMessage.error(error.message || '请求失败')
        }
        return Promise.reject(error)
    }
)

export default request