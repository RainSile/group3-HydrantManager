# group3-HydrantManager


## git相关

**不要推送到公共分支，以避免代码污染**

推送先新建一个个人分支再推送，命名格式：`g3-java/front-name`



## 架构说明

### 整体

采用前后端分离架构，前端与后端单独部署，联调工具使用apifox

### 前端

- Vue
- vue-router
- pinia
- axios
- element-plus

### 后端

- Springboot Alibaba

- Mysql
- Maven
- WebSocket
- Mybatis-plus
- Knife4j



### 其他

前端数据传输载体为DTO对象，后端数据传输载体为VO对象(将VO对象封装为JsonVO<T>返回前端)

实体类命名为xxxDO

mvc三层架构：(使用knife4j注解标注)

xxxController

xxxService(Impl)

xxxMapper

数据类型转换对象：MsxxxMapper

