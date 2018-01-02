package com.lianqu1990.common.test;

/**
 * @author hanchao
 * @date 2017/10/10 20:06
 */
public class FiddlerWebTest extends BaseWebTest {
    static {
        System.setProperty("http.proxyHost", "localhost");
        System.setProperty("http.proxyPort", "8888");
    }
}
