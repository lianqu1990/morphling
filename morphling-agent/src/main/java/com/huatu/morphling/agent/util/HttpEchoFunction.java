package com.huatu.morphling.agent.util;

import com.huatu.morphling.util.EchoFunction;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author hanchao
 * @date 2017/12/18 11:07
 */
public class HttpEchoFunction implements EchoFunction {
    private PrintWriter writer;
    public HttpEchoFunction(HttpServletResponse response) throws IOException {
        this.writer = response.getWriter();
    }

    @Override
    public void print(String logId, String line) {
        writer.print(line+"\r\n");
        writer.flush();
    }

    @Override
    public void close(String logId) {

    }
}
