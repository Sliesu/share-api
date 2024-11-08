<img src="/doc/img/rbc-logo.png" style="width: 40%;margin-bottom: -60px" alt="RBC Logo"/>


###  知识分享应用——Spring Cloud Alibaba 学习项目

![Spring Boot 3.2.4](https://img.shields.io/badge/Spring%20Boot-3.2.4-brightgreen.svg)
![Spring Cloud 2023.0.1](https://img.shields.io/badge/Spring%20Cloud-2023.0.1-blue.svg)
![Spring Cloud Alibaba 2023.0.1.0](https://img.shields.io/badge/Spring%20Cloud%20Alibaba-2023.0.1.0-brightgreen.svg)
![JDK 17](https://img.shields.io/badge/JDK-17-brightgreen.svg)
![Maven](https://img.shields.io/badge/Maven-3.9.6-yellowgreen.svg)


@RainbowCloud_CrispShark

------

**项目介绍**

本项目旨在学习和实践如何使用 Spring Cloud Alibaba 构建微服务架构，结合了多个 Spring Cloud 生态系统中的关键技术，包括 Spring Boot 3.x、Spring Cloud、Nacos、Sentinel 和 RocketMQ。本项目涵盖了服务注册与发现、流量控制、消息队列等功能，适用于构建高可用、可扩展的知识分享应用。

![Nacos 2.3.2](https://img.shields.io/badge/Nacos-2.3.2-red.svg) ![Sentinel 1.8.6](https://img.shields.io/badge/Sentinel-1.8.6-red.svg) ![RocketMQ 2.3.0](https://img.shields.io/badge/RocketMQ-2.3.0-red.svg) 


模块名称：`share-user`

模块描述：
本模块处理与用户相关的操作，负责管理用户的注册、登录、资料更新等功能。通过 Spring Cloud Alibaba 集成 Nacos 进行服务的注册与发现，实现了微服务架构下的用户服务。模块中还包含了用户认证和授权的功能，通过 JWT 实现用户身份验证。

------

模块名称：`share-common`

模块描述：
本模块包含项目中多个模块共享的公共功能，如工具类、常量类、公共响应格式等。该模块旨在减少代码重复，提高代码的复用性和可维护性。

------

模块名称：`share-gateway`

模块描述：
本模块实现了 API 网关的功能，负责所有外部请求的路由、转发和权限控制。通过 Spring Cloud Gateway 提供的路由功能和 Spring Security 结合，实现了安全认证、访问控制等功能。

------

模块名称：`share-content`

模块描述：
本模块负责内容相关的管理，包含分享内容的上传、编辑、审核等功能。利用 Nacos 进行服务的注册与发现，通过 RocketMQ 实现分享内容的异步处理与通知。



---

## 快速开始

### 克隆项目

```bash
git clone https://github.com/Sliesu/share-api.git
cd share-api
```

### 配置环境

1. **Nacos**：配置并启动 Nacos 作为服务注册与发现中心。
2. **Sentinel**：配置 Sentinel 控制流量，保护微服务的稳定性。
3. **RocketMQ**：配置 RocketMQ 实现消息队列。
4. **JDK 17**：确保本地环境安装 JDK 17。

### 启动服务

首先启动 Nacos 服务注册中心：

```bash
docker run -d -p 8848:8848 nacos/nacos-server:2.3.2
```

然后，启动微服务：

```bash
mvn clean spring-boot:run
```

### 测试接口

调用各个微服务接口，确保各个服务之间可以成功通信。你可以使用 Postman 或者 cURL 测试接口。

```bash
POST http://localhost:8000/content-service/share/admin/audit
Content-Type: application/json
token: {{token}}

{
  "shareId": 1854800103272030209
}
```

---

## 贡献

如果你有任何问题或者建议，欢迎提交 Issue 或 Pull Request。我们欢迎社区的贡献！

---

## License

[MIT License](https://opensource.org/licenses/MIT)

---

**感谢您使用本项目！**