package com.lianqu1990.morphling.util;

/**
 * @author hanchao
 * @date 2017/11/20 15:15
 */
public interface EchoFunction {
    void print(String logId,String line);
    void close(String logId);
}
