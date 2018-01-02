package com.lianqu1990.springboot.executor.endpoint;

import lombok.Builder;
import lombok.Data;

/**
 * @author hanchao
 * @date 2017/10/4 14:56
 */
@Data
@Builder
public class ExecutorInfo {
    private int coreSize;
    private int maxSize;
    private int active;
    private long taskCount;
    private long completedTaskCount;
    private int largestPoolSize;
    private String status;

    private long queueSize;
    private String queueClass;


    public ExecutorInfo setStatus(boolean terminated){
        if(terminated){
            this.status = "DOWN";
        }else{
            this.status = "UP";
        }
        return this;
    }
}
