# G3 消防栓管理系统

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
[![Vue.js](https://img.shields.io/badge/Vue.js-3.5.22-4FC08D.svg)](https://vuejs.org/)
[![Element Plus](https://img.shields.io/badge/Element%20Plus-2.11.8-409EFF.svg)](https://element-plus.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.3.0-blue.svg)](https://www.mysql.com/)
[![MyBatis](https://img.shields.io/badge/MyBatis--Plus-3.5.6-blue.svg)](https://baomidou.com/)

一个现代化的全栈城市消防栓管理系统，采用前后端分离架构，提供消防栓设备的全生命周期管理、实时监控和运维管理功能。

## 📋 项目概述

G3消防栓管理系统是专为城市消防设施管理而设计的企业级应用。系统通过数字化手段管理消防栓设备的位置信息、状态监控、维护记录等核心数据，为消防部门提供高效的设备管理解决方案。

### 系统特色

- 🌐 **前后端分离** - Vue.js + Spring Boot 现代化架构
- 🔐 **JWT无状态认证** - 安全可靠的用户认证机制
- 🗺️ **地理信息管理** - 基于经纬度的消防栓定位服务
- 📊 **实时数据监控** - 水压、流速等关键指标监控
- 📱 **响应式界面** - Element Plus 现代化UI组件
- 🚀 **高性能架构** - Redis缓存 + Druid连接池优化

## 🏗️ 技术架构

### 整体架构

```
g3-HydrantManager/
├── frontend/                   # Vue.js 前端应用
│   ├── src/
│   │   ├── api/               # API接口定义
│   │   ├── components/        # Vue组件
│   │   ├── data/             # 数据配置
│   │   └── App.vue           # 根组件
│   ├── package.json          # 前端依赖配置
│   └── vite.config.js        # Vite构建配置
├── hydrant-common/            # 公共工具模块
├── hydrant-domain/            # 领域实体模块
├── hydrant-web/               # Spring Boot后端应用
└── pom.xml                   # Maven父项目配置
```

### 技术栈

| 层次 | 技术 | 版本 | 说明 |
|------|------|------|------|
| **前端** | Vue.js | 3.5.22 | 渐进式JavaScript框架 |
| | Element Plus | 2.11.8 | Vue 3桌面端组件库 |
| | Vite | 7.1.11 | 现代化前端构建工具 |
| | Axios | 1.13.2 | HTTP客户端库 |
| **后端** | Java | 17 | 核心开发语言 |
| | Spring Boot | 3.3.5 | 应用框架 |
| | MyBatis | 3.0.5 | ORM框架 |
| | MyBatis-Plus | 3.5.6 | MyBatis增强工具 |
| | MySQL | 8.3.0 | 关系型数据库 |
| | Druid | 1.2.23 | 数据库连接池 |
| | Redis | 3.3.5 | 缓存数据库 |
| | JWT | 0.11.5 | 无状态认证 |
| | Knife4j | 4.4.0 | API文档工具 |

## 🚀 快速开始

### 环境要求

- **Node.js**: ^20.19.0 || >=22.12.0
- **JDK**: 17+
- **Maven**: 3.6+
- **MySQL**: 8.0+
- **Redis**: 6.0+
- **IDE**: IntelliJ IDEA 2023+ (推荐)

### 数据库配置

1. **创建数据库**
   ```sql
   CREATE DATABASE hydrant_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. **用户表结构**
   ```sql
   CREATE TABLE user_table (
       id BIGINT PRIMARY KEY,
       username VARCHAR(50) NOT NULL UNIQUE,
       password VARCHAR(255) NOT NULL,
       phone VARCHAR(20),
       priv TINYINT DEFAULT 0 COMMENT '权限: 0-普通用户, 1-管理员',
       status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
       create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
   );
   ```

3. **消防栓表结构**
   ```sql
   CREATE TABLE hydrant_table (
       id BIGINT PRIMARY KEY,
       location VARCHAR(255) NOT NULL COMMENT '详细位置',
       longitude DECIMAL(10,7) COMMENT '经度',
       latitude DECIMAL(10,7) COMMENT '纬度',
       status TINYINT DEFAULT 0 COMMENT '状态: 0-正常, 1-维护中, 2-故障',
       pressure DECIMAL(5,2) COMMENT '当前水压(MPa)',
       flow_rate DECIMAL(6,2) COMMENT '当前流速(L/S)',
       create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
       data TEXT COMMENT '备注信息'
   );
   ```

### 后端启动

1. **配置文件修改**

   编辑 `hydrant-web/src/main/resources/application.yaml`:
   ```yaml
   spring:
     datasource:
       driver-class-name: com.mysql.cj.jdbc.Driver
       url: jdbc:mysql://localhost:3306/hydrant_db?serverTimezone=Asia/Shanghai
       username: your_username
       password: your_password
       type: com.alibaba.druid.pool.DruidDataSource
     data:
       redis:
         host: localhost
         port: 6379

   server:
     port: 11000

   hydrant:
     jwt:
       secret-key: your-secret-key
       ttl: 7200000
       token-name: token
   ```

2. **编译运行**
   ```bash
   # 进入项目根目录
   cd g3-HydrantManager-java

   # 编译项目
   mvn clean compile

   # 启动后端服务
   mvn spring-boot:run -pl hydrant-web
   ```

3. **访问后端服务**
   - API服务: http://localhost:11000
   - API文档: http://localhost:11000/doc.html
   - Druid监控: http://localhost:11000/druid

### 前端启动

1. **安装依赖**
   ```bash
   cd frontend
   npm install
   ```

2. **开发环境启动**
   ```bash
   npm run dev
   ```

3. **生产环境构建**
   ```bash
   npm run build
   ```

4. **访问前端应用**
   - 开发环境: http://localhost:5173
   - 生产环境: 构建后部署到Web服务器

## 📚 API接口文档

### 认证接口

| 方法 | 路径 | 说明 | 参数 |
|------|------|------|------|
| POST | `/login` | 用户登录 | username, password |
| POST | `/logout` | 用户登出 | 无 |

### 用户管理

| 方法 | 路径 | 说明 | 参数 |
|------|------|------|------|
| GET | `/user/query` | 分页查询用户 | page, size, username, phone |
| POST | `/user/add` | 添加用户 | 用户信息JSON |
| PUT | `/user/status/{stat}` | 修改用户状态 | 用户ID, 状态 |
| PUT | `/user/password` | 修改密码 | oldPassword, newPassword |

### 消防栓管理

| 方法 | 路径 | 说明 | 参数 |
|------|------|------|------|
| GET | `/hydrant/query` | 分页查询消防栓 | page, size, location, status |
| POST | `/hydrant/add` | 新增消防栓 | 消防栓信息JSON |
| PUT | `/hydrant/change` | 更新消防栓 | 消防栓信息JSON |
| DELETE | `/hydrant/remove/{id}` | 删除消防栓 | 消防栓ID |

### 认证方式

所有API接口（除登录外）都需要在请求头中携带JWT Token：
```http
Authorization: Bearer <your-jwt-token>
```

## 🗄️ 数据库设计

### 用户表 (user_table)

| 字段 | 类型 | 说明 | 约束 |
|------|------|------|------|
| id | BIGINT | 主键 | 雪花算法生成 |
| username | VARCHAR(50) | 用户名 | 唯一，非空 |
| password | VARCHAR(255) | 密码 | BCrypt加密 |
| phone | VARCHAR(20) | 联系电话 | 可选 |
| priv | TINYINT | 权限级别 | 0-普通用户,1-管理员 |
| status | TINYINT | 账户状态 | 0-禁用,1-启用 |
| create_time | TIMESTAMP | 创建时间 | 默认当前时间 |
| update_time | TIMESTAMP | 更新时间 | 自动更新 |

### 消防栓表 (hydrant_table)

| 字段 | 类型 | 说明 | 约束 |
|------|------|------|------|
| id | BIGINT | 主键 | 雪花算法生成 |
| location | VARCHAR(255) | 详细位置 | 非空 |
| longitude | DECIMAL(10,7) | 经度 | 地理坐标 |
| latitude | DECIMAL(10,7) | 纬度 | 地理坐标 |
| status | TINYINT | 设备状态 | 0-正常,1-维护,2-故障 |
| pressure | DECIMAL(5,2) | 水压 | 单位：MPa |
| flow_rate | DECIMAL(6,2) | 流速 | 单位：L/S |
| create_time | TIMESTAMP | 创建时间 | 默认当前时间 |
| update_time | TIMESTAMP | 更新时间 | 自动更新 |
| data | TEXT | 备注信息 | 可选 |

## 🔧 开发指南

### 项目结构详解

#### 后端模块结构

```
hydrant-common/                 # 公共模块
├── exceptions/                 # 异常处理类
├── utils/                     # 工具类(JwtUtil,雪花算法等)
├── properties/                # 配置属性类
└── constants/                 # 常量定义

hydrant-domain/                # 领域模块
├── entity/                    # JPA实体类
├── dto/                       # 数据传输对象
├── vo/                        # 视图对象
└── query/                     # 查询条件对象

hydrant-web/                   # Web应用模块
├── controller/                # REST控制器
├── service/                   # 业务逻辑层
│   └── impl/                  # 业务实现类
├── mapper/                    # MyBatis映射器
├── config/                    # 配置类
├── interceptor/               # 拦截器(JWT拦截器)
├── handler/                   # 全局异常处理
└── initializer/               # 数据初始化
```

#### 前端模块结构

```
frontend/src/
├── api/                       # API接口封装
├── components/                # Vue组件
│   ├── common/               # 通用组件
│   ├── hydrant/              # 消防栓相关组件
│   └── user/                 # 用户相关组件
├── data/                     # 数据配置
├── App.vue                   # 根组件
└── main.js                   # 应用入口
```

### 开发规范

1. **代码规范**
   - 遵循阿里巴巴Java开发手册
   - 使用Lombok简化代码
   - 统一异常处理机制
   - RESTful API设计规范

2. **提交规范**
   - feat: 新功能
   - fix: 修复bug
   - docs: 文档更新
   - style: 代码格式调整
   - refactor: 代码重构

3. **测试要求**
   - 单元测试覆盖率 > 80%
   - 集成测试覆盖核心业务流程
   - API接口测试文档完整

## 🛡️ 安全特性

- **JWT认证**: 无状态令牌认证，支持令牌续期
- **密码安全**: BCrypt强哈希加密存储
- **权限控制**: 基于角色的访问控制(RBAC)
- **SQL注入防护**: MyBatis参数化查询
- **XSS防护**: 前端输入过滤和后端参数校验
- **CORS安全**: 跨域请求白名单控制
- **请求限流**: Redis实现的API调用频率限制

## 📊 监控与运维

### 应用监控

- **健康检查**: `/actuator/health`
- **系统信息**: `/actuator/info`
- **性能指标**: `/actuator/metrics`

### 数据库监控

访问 `http://localhost:11000/druid` 查看:
- 数据源状态监控
- SQL执行性能分析
- 慢查询统计
- 连接池使用情况

### 日志管理

- **应用日志**: Spring Boot Logback
- **SQL日志**: MyBatis控制台输出
- **访问日志**: 请求响应记录
- **错误日志**: 全局异常捕获

## 🚀 部署指南

### 生产环境部署

1. **后端部署**
   ```bash
   # 打包应用
   mvn clean package -pl hydrant-web

   # 运行应用
   java -jar hydrant-web/target/hydrant-web-1.0.0.jar
   ```

2. **前端部署**
   ```bash
   cd frontend
   npm run build

   # 将dist目录部署到Nginx或其他Web服务器
   ```

3. **Docker部署**
   ```dockerfile
   # Dockerfile示例
   FROM openjdk:17-jdk-slim
   COPY hydrant-web/target/hydrant-web-1.0.0.jar app.jar
   EXPOSE 11000
   ENTRYPOINT ["java", "-jar", "/app.jar"]
   ```

### 环境配置

| 环境 | 配置文件 | 说明 |
|------|----------|------|
| 开发环境 | `application-dev.yml` | 本地开发配置 |
| 测试环境 | `application-test.yml` | 测试服务器配置 |
| 生产环境 | `application-prod.yml` | 生产服务器配置 |

## 🤝 贡献指南

1. **Fork项目** 到个人仓库
2. **创建功能分支** `git checkout -b feature/AmazingFeature`
3. **提交代码** `git commit -m 'feat: add some AmazingFeature'`
4. **推送分支** `git push origin feature/AmazingFeature`
5. **创建Pull Request** 到主分支

### 代码审查流程

- 代码风格检查
- 功能测试验证
- 性能影响评估
- 安全漏洞扫描
- 文档同步更新

## 📝 版本历史

### v1.0.0 (2024-12-17)
- ✨ 完整的前后端分离架构实现
- ✨ 用户管理和权限控制系统
- ✨ 消防栓设备CRUD操作
- ✨ JWT无状态认证机制
- ✨ 地理位置信息管理
- ✨ Element Plus响应式界面
- ✨ Redis缓存优化
- ✨ Druid数据库监控
- ✨ Swagger API文档
- ✨ 全局异常处理

## 📄 许可证

本项目采用 [MIT 许可证](LICENSE) - 详情请查看LICENSE文件

## 👥 开发团队

- **项目负责人**: rainsilent
- **后端架构**: Spring Boot + MyBatis Plus
- **前端架构**: Vue.js + Element Plus
- **数据库设计**: MySQL + Redis
- **DevOps**: Docker + Nginx

## 📞 联系方式

- **项目地址**: [GitHub Repository](https://github.com/your-username/g3-HydrantManager)
- **问题反馈**: [GitHub Issues](https://github.com/your-username/g3-HydrantManager/issues)
- **技术交流**: rainsilent233@gmail.com

---

⭐ 如果这个项目对你有帮助，请给它一个星标！

🚀 欢迎贡献代码，共同完善消防栓管理系统！