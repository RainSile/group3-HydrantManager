import axios from 'axios'
import { ElMessage } from 'element-plus'

// 创建axios实例
const request = axios.create({
    baseURL: '/api',
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json'
    }
})

// 请求拦截器
request.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token')
        if (token) {
            config.headers['token'] = token
        }
        return config
    },
    (error) => {
        return Promise.reject(error)
    }
)

// 响应拦截器
request.interceptors.response.use(
    (response) => {
        return response.data
    },
    (error) => {
        const { response } = error
        let message = '请求失败'

        if (response) {
            message = response.data?.message || '请求错误'

            if (response.status === 401) {
                ElMessage.error('无权限')
                // localStorage.removeItem('token')
                // localStorage.removeItem('userInfo')
            }
        } else {
            message = error.code === 'ECONNABORTED' ? '请求超时' : '网络连接失败'
        }

        // ElMessage.error(message)
        return Promise.reject(error)
    }
)

export default request