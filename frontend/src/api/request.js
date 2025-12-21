import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'

const request = axios.create({
    baseURL: '/api',
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
        const res = response.data
        if (res.code === 200) {
            return res
        } else {
            ElMessage.error(res.msg || res.message || '请求失败')
            return Promise.reject(new Error(res.msg || res.message || '请求失败'))
        }
    },
    error => {
        if (error.response && error.response.status === 401) {
            localStorage.removeItem('token')
            router.push('/login')
            ElMessage.error('登录已过期，请重新登录')
        } else {
            const errorMsg = error.response?.data?.msg || error.response?.data?.message || error.message || '请求失败'
            ElMessage.error(errorMsg)
        }
        return Promise.reject(error)
    }
)

export default request