package com.huatu.morphling.web.socket;

import com.huatu.morphling.common.consts.WebParamConsts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hanchao
 * @date 2017/11/20 11:35
 */
@Slf4j
public class LogSubscriberHolder {
    //hashbimap
    private static Map<String,WebSocketSession> sessions = new ConcurrentHashMap();
    private static Map<WebSocketSession,String> sessionInverse = new ConcurrentHashMap();
    static {
        try {
            Thread thread = new Thread(new SessionMonitor());
            thread.setDaemon(true);
            thread.start();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void addSession(String logId,WebSocketSession session){
        sessions.put(logId,session);
        sessionInverse.put(session,logId);
    }

    public static WebSocketSession getSession(String logId){
        return sessions.get(logId);
    }

    public static void removeSession(String logId){
        WebSocketSession session = sessions.remove(logId);
        if(session != null){
            sessionInverse.remove(session);
        }
    }

    public static void removeSession(WebSocketSession session){
        String logId = sessionInverse.remove(session);
        if(logId != null){
            sessions.remove(logId);
        }
    }

    private static class SessionMonitor implements Runnable {
        @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    Thread.sleep(60000);
                    log.info("begin clear session,{}...",sessionInverse.keySet().size());
                    for (WebSocketSession session : sessionInverse.keySet()) {
                        Object last = session.getAttributes().get(WebParamConsts.WEBSOCKET_LAST_PACKET);
                        if(last != null && last instanceof Long){
                            long passed = System.currentTimeMillis() - (long)last;
                            if(passed <= 60000){
                                continue;
                            }
                        }
                        log.info("remove session,logId is {}...",sessionInverse.get(session));
                        removeSession(session);
                        session.close();
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

}
