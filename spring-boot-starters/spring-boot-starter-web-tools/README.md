### Web Tools

- 响应结果自动包装（需要添加排除可以自行定义AdviceExcluder的有参构造函数并注册到spring容器）;分为包装未Result类型，以及包装未Jsonp类型

- converter，支持application/x-www-form-urlencoded消息体的生成

- 异常处理相关,先交与exceptionResolver处理，然后传递给exceptionHandler,方便扩展

- StringTemplateView，重写spring boot自带的error view，由handler自动forward。

- 统一异常响应页面提供(ex-handler未捕捉的情况下，具体逻辑参考源码)


