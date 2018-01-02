package com.lianqu1990.morphling.util;

import com.lianqu1990.common.utils.web.ANSIUtil;
import com.lianqu1990.morphling.web.socket.LogSubscriberHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author hanchao
 * @date 2017/11/20 15:33
 */
@Slf4j
public class WebsocketEcho implements EchoFunction {
    @Override
    public void print(String logId, String line) {
        line = HtmlUtils.htmlEscape(line);
        WebSocketSession session = LogSubscriberHolder.getSession(logId);
        if(session == null){
            OperateLogHolder.append(logId,line);
        }else{
            try {
                session.sendMessage(new TextMessage(ANSIUtil.convertHtml(line)));
            } catch (IOException e) {
                log.error("",e);
            }
        }
    }

    @Override
    public void close(String logId) {
        try {
            TimeUnit.SECONDS.sleep(1);//休息一秒，防止报错情况下，关闭的时候客户端尚未连接
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebSocketSession session = LogSubscriberHolder.getSession(logId);
        if(session != null){
            try {
                LogSubscriberHolder.removeSession(logId);
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
