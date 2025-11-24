// api/hydrantApi.js
import request from './request'

// 登录接口
export const login = (data) => {
    return request.post('/login', data)
}

// 登出接口
export const logout = () => {
    return request.post('/logout')
}

// 用户管理接口
export const userApi = {
    // 添加用户
    addUser: (data) => request.post('/user/add', data),

    // 修改密码
    updatePassword: (data) => request.put('/user/password', null, {
        params: data
    }),

    // 账号状态管理
    updateStatus: (status, id) => request.put(`/user/status/${status}?id=${id}`),

    // 查询用户
    listUsers: (params) => request.get('/user/query', { params }),

    // 修改用户
    updateUser: (data) => request.put('/user/change', data)
}

// 消防栓管理接口
export const hydrantApi = {
    // 新增消防栓
    addHydrant: (data) => request.post('/hydrant/add', data),

    // 更新消防栓
    changeHydrant: (data) => request.put('/hydrant/change', data),

    // 查询消防栓
    listHydrants: (params) => request.get('/hydrant/query', { params }),

    // 删除消防栓
    removeHydrant: (id) => request.delete(`/hydrant/remove/${id}`)
}

// 维修管理接口
export const maintenanceApi = {
    // 新增维修项
    addMaintenance: (data) => request.post('/maintenance/add', data),

    // 完成维修项
    removeMaintenance: (id) => request.put(`/maintenance/remove/${id}`),

    // 查询维修项
    listMaintenance: (params) => request.get('/maintenance/query', { params })
}

// 日志管理接口
export const logApi = {
    // 添加维修日志
    addLog: (data) => request.post('/log/add', data),

    // 删除维修日志
    removeLog: (id) => request.delete(`/log/remove/${id}`),

    // 查询维修日志
    listLog: (params) => request.get('/log/query', { params })
}

export default {
    login,
    logout,
    userApi,
    hydrantApi,
    maintenanceApi,
    logApi
}