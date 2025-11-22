// 模拟消防栓数据
export const hydrantsData = [
    {
        id: '001',
        address: '人民路与中山路交叉口东南角',
        status: 'normal',
        type: '地上式',
        pressure: '0.35',
        lastCheck: '2023-10-15',
        manager: '张三',
        area: 'center'
    },
    {
        id: '002',
        address: '解放大道128号门前',
        status: 'warning',
        type: '地下式',
        pressure: '0.28',
        lastCheck: '2023-09-20',
        manager: '李四',
        area: 'east'
    },
    {
        id: '003',
        address: '新华西路与建设路交叉口',
        status: 'normal',
        type: '地上式',
        pressure: '0.40',
        lastCheck: '2023-10-10',
        manager: '王五',
        area: 'west'
    },
    {
        id: '004',
        address: '环城北路56号对面',
        status: 'error',
        type: '地上式',
        pressure: '0.15',
        lastCheck: '2023-08-05',
        manager: '赵六',
        area: 'north'
    },
    {
        id: '005',
        address: '城南新区幸福广场',
        status: 'normal',
        type: '地下式',
        pressure: '0.38',
        lastCheck: '2023-10-18',
        manager: '钱七',
        area: 'south'
    },
    {
        id: '006',
        address: '工业区创业大道88号',
        status: 'warning',
        type: '地上式',
        pressure: '0.25',
        lastCheck: '2023-09-28',
        manager: '孙八',
        area: 'east'
    }
]

// 区域选项
export const areaOptions = [
    { label: '东城区', value: 'east' },
    { label: '西城区', value: 'west' },
    { label: '南城区', value: 'south' },
    { label: '北城区', value: 'north' },
    { label: '中心区', value: 'center' }
]