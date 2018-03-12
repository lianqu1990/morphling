## 缓存管理组件


>* 注解demo:


```java

    @Cached(name = "课程列表v3",
            key = "T(com.lianqu1990.tiku.course.util.CourseCacheKey).courseListV3(T(com.lianqu1990.common.utils.web.RequestUtil).getParamSign(#map))",
            params = {@Cached.Param(name="查询参数",value = "map",type = Map.class)})
    @Async
    public ListenableFuture<CourseListV3DTO> getCourseListV3(Map<String,Object> params){
        params.remove("username");

        String cacheKey = CourseCacheKey.courseListV3(buildMapKey(params));
        CourseListV3DTO result = (CourseListV3DTO) valueOperations.get(cacheKey);
        if(result == null){
            NetSchoolResponse response = courseServiceV3.findLiveList(params);
            result = ResponseUtil.build(response,CourseListV3DTO.class,false);
            if(result != null){
                result.set_timestamp(System.currentTimeMillis());
                valueOperations.set(cacheKey,result,10, TimeUnit.SECONDS);
            }
            //非fallback获取到，设置到fallback缓存
            if(!result.is_cache()){
                courseServiceV3Fallback.setLiveList(params,response);
            }
        }else{
            result.set_cache(true);
        }
        return new AsyncResult<>(result);
    }

```

- 启动中会通过beanprocessor扫描到所有注解的bean，集中管理，并将他们在springboot监控端点“cache-manage”暴露出来


- 通过post访问,路由参数名为"_action"

- 一共支持四种方法：

>* query，获取进程内所有业务缓存
>* get，针对集中式缓存使用，响应的消息体为执行后获得的"key",需自行连接到对应服务器删除或者获取
>* getInside，针对进程内缓存使用
>* delInside，针对进程内缓存使用


- get访问方式为/_monitor/cacheManage?_action=get&id=1,消息体如下(两种方式兼容):

```json
{
  "map":
  {
    "categoryid":1000,
    "priceid":2
  }
}


```

```json
//兼容

{
  "map":"categoryid=1000&priceid=2"
}

```