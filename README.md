Morphling
================

## 简介
Morphling是一套基于Spring Boot 1.5开发的部署系统，依赖简单，一套Mysql即可运行，操作简单明了，适用于百台规模几下机器的运维操作

### 功能概述

#### 系统部署

>* 多角色配置，不同角色区分环境和菜单权限。不同环境可以做不同配置，满足一般公司的基本开发部署需求。
>* 分为服务端，客户端两个服务。客户端安装在每个服务器上。部署时候服务端通知需要部署的机器，得到通知的客户端从服务端远程获取安装包部署。

#### 缓存管理

>* 基于spring-boot-starter方式引入缓存管理组件（基于注解），通过endpoint向外暴露服务的所有缓存，从而实现集中统一管理。

#### 服务降级

>* 基于配置中心（apollo），引入spring-boot-starter-degrade组件，声明需要走降级的方法，通过endpoint向外暴露所有可降级的方法以及该方法关联的配置键，从而实现降级统一管理。

### 示例

![客户端管理](https://gitee.com/lianqu1990/morphling/raw/master/doc/images/1.png)
![应用发布](https://gitee.com/lianqu1990/morphling/raw/master/doc/images/2.png)
![业务降级](https://gitee.com/lianqu1990/morphling/raw/master/doc/images/3.png)
![缓存管理](https://gitee.com/lianqu1990/morphling/raw/master/doc/images/4.png)