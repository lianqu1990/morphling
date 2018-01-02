Morphling
================

## 简介
Morphling是一套基于Spring Boot 1.5开发的部署系统，依赖简单，一套Mysql即可运行，操作简单明了，适用于百台规模以下机器的部署运维操作

## 联系我

[![点击这里给我发消息](http://wpa.qq.com/pa?p=2:55375829:51)](http://wpa.qq.com/msgrd?v=3&uin=55375829&site=qq&menu=yes)

### 框架概览

- 主框架：spring boot
- 权限控制：spring security
- ORM框架：spring data jpa
- 操作日志回显：websocket
- 前后端分离：angularjs+bootstrap


### 功能概述

#### 系统部署

>* 多角色配置，不同角色区分环境和菜单权限。不同环境可以做不同配置，满足一般公司的基本开发部署需求。
>* 分为服务端，客户端两个服务。客户端安装在每个服务器上。部署时候服务端通知需要部署的机器，得到通知的客户端从服务端远程获取安装包部署。
>* 应用创建，分为网关型应用以及服务型应用，部署方式预留了docker方式(待实现)，应用+客户端实例 = 应用实例，可以为用户自由分配所拥有的应用
>* 注册中心上下线（目前只实现了网关方式从NGINX上下线的，基于自研的openresty+lua+etcd，建议使用新浪的upsync。服务注册中心比如zookeeper,consul待实现）

#### 缓存管理

>* 基于spring-boot-starter方式引入缓存管理组件（基于注解），通过endpoint向外暴露服务的所有缓存，从而实现集中统一管理。

#### 服务降级

>* 基于配置中心（apollo），引入spring-boot-starter-degrade组件，声明需要走降级的方法，通过endpoint向外暴露所有可降级的方法以及该方法关联的配置键，从而实现降级统一管理。

### 快速安装
>* 创建数据库，命名为morphling，导入scripts/morphling.sql
>* 根目录运行 mvn clean package -DskipTests=true -Pproduct,获取morphling-agent.tar.gz和获取morphling-server.tar.gz安装包
>* server.gz安装包解压到服务器，运行server.sh start
>* 打开http://ip:11110(打包如果不加-P参数，端口为80，方便本地调试)，默认管理员账号admin，密码000000

### Tips

- 默认agent安装包需要放在服务器"/app/data/morphling-server/morphling-agent.tar.gz"下，这个再DeployConst里面配置，可以自行修改
- 客户端默认使用部署用户为deploy(前端限制死)，需要调整，可以修改client.js > ClientAddController，修改deploy为所需用户即可


### 示例
#### 客户端管理
![客户端管理](https://gitee.com/lianqu1990/morphling/raw/master/doc/images/1.png)
![客户端管理](https://gitee.com/lianqu1990/morphling/raw/master/doc/images/2.png)
#### 用户管理
![用户管理](https://gitee.com/lianqu1990/morphling/raw/master/doc/images/3.png)
![用户管理](https://gitee.com/lianqu1990/morphling/raw/master/doc/images/4.png)
#### 应用配置
![应用配置](https://gitee.com/lianqu1990/morphling/raw/master/doc/images/5.png)
#### 应用发布
![应用发布](https://gitee.com/lianqu1990/morphling/raw/master/doc/images/6.png)
![应用发布](https://gitee.com/lianqu1990/morphling/raw/master/doc/images/7.png)
![应用发布](https://gitee.com/lianqu1990/morphling/raw/master/doc/images/8.png)
![应用发布](https://gitee.com/lianqu1990/morphling/raw/master/doc/images/9.png)

#### 业务降级
![业务降级](https://gitee.com/lianqu1990/morphling/raw/master/doc/images/10.png)

#### 缓存管理
![业务降级](https://gitee.com/lianqu1990/morphling/raw/master/doc/images/11.png)

#### SpringBoot端点监控
![监控](https://gitee.com/lianqu1990/morphling/raw/master/doc/images/12.png)

