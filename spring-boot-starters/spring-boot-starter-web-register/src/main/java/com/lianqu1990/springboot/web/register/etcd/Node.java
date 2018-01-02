package com.lianqu1990.springboot.web.register.etcd;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author hanchao
 * @date 2017/9/21 23:04
 */
public class Node {
    // 201 created
    // 200 update ok

    @Data
    @Builder
    public static class Request{
        private String value;
        private boolean dir;// default to false
        private Integer ttl;//null will not send
    }


    /**
     * 有errorCode为失败
     */
    @Data
    public static class Response{
        private String key;
        private String value;
        private Date expiration;
        private Integer ttl;
        private int modifiedIndex;
        private int createdIndex;
    }
}
