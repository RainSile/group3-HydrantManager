# G3 消防栓管理系统 (Java版)

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
[![MySQL](https://img.shields.io/badge/MySQL-8.3.0-blue.svg)](https://www.mysql.com/)
[![MyBatis](https://img.shields.io/badge/MyBatis--Plus-3.5.6-blue.svg)](https://baomidou.com/)

一个现代化的城市消防栓管理系统，采用Spring Boot + MyBatis Plus构建，提供消防栓设备的全生命周期管理、实时监控和运维管理功能。

## 📋 项目简介

G3消防栓管理系统是专为城市消防设施管理而设计的企业级应用。系统通过数字化手段管理消防栓设备的位置信息、状态监控、维护记录等核心数据，为消防部门提供高效的设备管理解决方案。

### 主要功能

- 🔐 **用户管理** - 支持用户注册、登录、权限控制和状态管理
- 🚰 **消防栓管理** - 完整的消防栓CRUD操作，支持地理位置信息
- 📊 **实时监控** - 水压、流速等关键指标的实时监控
- 🔍 **智能查询** - 多条件组合查询和分页功能
- 🗺️ **定位服务** - 基于经纬度的设备定位和管理
- ⚙️ **状态管理** - 正常、维护中、故障等状态的全程跟踪

## 🏗️ 系统架构

### 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 17 | 核心开发语言 |
| Spring Boot | 3.3.5 | 应用框架 |
| MyBatis | 3.0.5 | ORM框架 |
| MyBatis-Plus | 3.5.6 | MyBatis增强工具 |
| MySQL | 8.3.0 | 关系型数据库 |
| Druid | 1.2.23 | 数据库连接池 |
| JWT | 0.11.5 | 无状态认证 |
| Knife4j | 4.4.0 | API文档工具 |
| Lombok | 1.18.32 | 代码简化工具 |

### 架构设计

项目采用**多模块Maven架构** + **分层设计**：

```
g3-HydrantManager-java/
├── hydrant-common/          # 公共工具模块
│   ├── exceptions/          # 异常处理体系
│   ├── utils/              # 工具类
│   ├── properties/         # 配置属性
│   └── constants/          # 常量定义
├── hydrant-domain/          # 领域实体模块
│   ├── entity/             # 数据库实体
│   ├── dto/                # 数据传输对象
│   ├── vo/                 # 视图对象
│   └── query/              # 查询对象
└── hydrant-web/             # Web应用模块
    ├── controller/         # 控制器层
    ├── service/            # 业务逻辑层
    ├── mapper/             # 数据访问层
    └── config/             # 配置类
```

### 分层架构

- **表现层 (Web)**: 处理HTTP请求，参数校验，返回响应
- **业务层 (Service)**: 核心业务逻辑处理，事务管理
- **数据层 (Mapper)**: 数据库CRUD操作，查询优化
- **领域层 (Domain)**: 数据模型定义，业务对象封装

## 🚀 快速开始

### 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- IDE: IntelliJ IDEA 2023+ (推荐)

### 本地部署

1. **克隆项目**
   ```bash
   git clone https://github.com/your-username/g3-HydrantManager-java.git
   cd g3-HydrantManager-java
   ```

2. **数据库配置**
   ```sql
   -- 创建数据库
   CREATE DATABASE hydrant_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   
   -- 创建用户表
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
   
   -- 创建消防栓表
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

3. **配置文件**

   修改 `hydrant-web/src/main/resources/application.yml`:
   ```yaml
   spring:
     datasource:
       druid:
         url: jdbc:mysql://localhost:3306/hydrant_db?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
         username: your_username
         password: your_password
         driver-class-name: com.mysql.cj.jdbc.Driver
   ```

4. **构建运行**
   ```bash
   # 编译项目
   mvn clean compile
   
   # 运行测试
   mvn test
   
   # 启动应用
   mvn spring-boot:run -pl hydrant-web
   ```

5. **访问应用**
   - 应用地址: http://localhost:8080
   - API文档: http://localhost:8080/doc.html
   - 健康检查: http://localhost:8080/actuator/health

## 📚 API文档

系统集成了Swagger UI，提供完整的API文档：

### 认证接口
- `POST /login` - 用户登录
- `POST /logout` - 用户登出

### 用户管理
- `GET /user/query` - 分页查询用户
- `POST /user/add` - 添加用户
- `PUT /user/status/{stat}` - 修改用户状态
- `PUT /user/password` - 修改密码

### 消防栓管理
- `GET /hydrant/query` - 分页查询消防栓
- `POST /hydrant/add` - 新增消防栓
- `PUT /hydrant/change` - 更新消防栓
- `DELETE /hydrant/remove/{id}` - 删除消防栓

### 认证方式
所有API接口（除登录外）都需要在请求头中携带JWT Token：
```
Authorization: Bearer <your-jwt-token>
```

## 🗄️ 数据库设计

### 用户表 (user_table)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键(雪花算法) |
| username | VARCHAR(50) | 用户名(唯一) |
| password | VARCHAR(255) | 密码(加密) |
| phone | VARCHAR(20) | 联系电话 |
| priv | TINYINT | 权限(0-普通,1-管理员) |
| status | TINYINT | 状态(0-禁用,1-启用) |
| create_time | TIMESTAMP | 创建时间 |
| update_time | TIMESTAMP | 更新时间 |

### 消防栓表 (hydrant_table)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键(雪花算法) |
| location | VARCHAR(255) | 详细位置 |
| longitude | DECIMAL(10,7) | 经度 |
| latitude | DECIMAL(10,7) | 纬度 |
| status | TINYINT | 状态(0-正常,1-维护,2-故障) |
| pressure | DECIMAL(5,2) | 水压(MPa) |
| flow_rate | DECIMAL(6,2) | 流速(L/S) |
| create_time | TIMESTAMP | 创建时间 |
| update_time | TIMESTAMP | 更新时间 |
| data | TEXT | 备注信息 |

## 🔧 开发指南

### 代码规范
- 使用Lombok简化代码
- 统一异常处理机制
- RESTful API设计规范
- JWT无状态认证
- MyBatis-Plus代码生成

### 项目结构说明
```
hydrant-common/
├── exceptions/           # 自定义异常类
├── utils/               # 工具类(JwtUtil等)
├── properties/          # 配置属性类
└── constants/           # 常量定义

hydrant-domain/
├── entity/              # JPA实体类
├── dto/                 # 数据传输对象
├── vo/                  # 视图对象
└── query/               # 查询条件对象

hydrant-web/
├── controller/          # REST控制器
├── service/             # 业务逻辑接口及实现
├── mapper/              # MyBatis映射器
├── config/              # 配置类
├── interceptor/         # 拦截器
└── HydrantWebApplication.java  # 启动类
```

### 构建部署
```bash
# 打包
mvn clean package -pl hydrant-web

# Docker构建(可选)
docker build -t hydrant-manager:latest .

# 运行
java -jar hydrant-web/target/hydrant-web-1.0.0.jar
```

## 🛡️ 安全特性

- **JWT认证**: 无状态令牌认证机制
- **密码加密**: BCrypt强哈希加密
- **权限控制**: 基于角色的访问控制(RBAC)
- **SQL注入防护**: MyBatis参数化查询
- **XSS防护**: 请求参数过滤
- **CORS配置**: 跨域请求安全控制

## 📊 监控与运维

### 健康检查
- 应用健康状态: `/actuator/health`
- 数据库连接状态: `/actuator/db`
- 系统信息: `/actuator/info`

### Druid监控
访问 `http://localhost:8080/druid` 查看:
- 数据源监控
- SQL监控
- 慢查询分析
- 连接池状态

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

### 开发规范
- 遵循阿里巴巴Java开发手册
- 提交信息格式: `type: description`
- 代码审查必须通过
- 单元测试覆盖率 > 80%

## 📝 更新日志

### v1.0.0 (2024-11-22)
- ✨ 实现用户管理模块
- ✨ 实现消防栓管理模块
- ✨ 集成JWT认证机制
- ✨ 添加Swagger API文档
- ✨ 实现全局异常处理
- ✨ 支持多条件分页查询

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 👥 团队

- **项目负责人**: rainsilent
- **技术架构**: Spring Boot + MyBatis Plus
- **数据库设计**: MySQL 8.0
- **前端技术**: Vue.js + Element UI (分离开发)

## 📞 联系方式

- 项目地址: [https://github.com/your-username/g3-HydrantManager-java](https://github.com/your-username/g3-HydrantManager-java)
- 问题反馈: [Issues](https://github.com/your-username/g3-HydrantManager-java/issues)
- 邮箱: rainsilent233@gmail.com

---

⭐ 如果这个项目对你有帮助，请给它一个星标！