package com.jinniu.interfaces.websocket;

import com.jinniu.commonjn.util.JsonResultY;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * 专门向客户端发送消息
 */
@Component
public class MessageManager {

    private static final Logger log = LoggerFactory
            .getLogger(MessageManager.class);

    private static final ExecutorService executor = Executors
            .newFixedThreadPool(20);

    @Autowired
    private SessionManager sessionManager;

    public void send(WebSocketSession session, JsonResultY jsonResultY) {
        if (!session.isOpen()) {
            sessionManager.remove(session);
            return;
        }

        SendMessageRunnable runnable = new SendMessageRunnable();
        runnable.setMessageManager(this);
        runnable.setSession(session);
        runnable.setJsonResultY(jsonResultY);
        runnable.setLock(sessionManager.getLock(session));
        executor.submit(runnable);
    }

    /**
     * 给所有在线用户发送消息
     */
    public void sendToAllUsers(JsonResultY jsonResultY) {
        for (WebSocketSession onlineUser : sessionManager.getOnlineSessions()) {
            send(onlineUser, jsonResultY);
        }
    }

    /**
     * 给某个房间里的用户发送消息
     *
     * @param roomId      roomId
     * @param jsonResultY jsonResultY
     */
    public void sendMessageToRoomUsers(String roomId, JsonResultY jsonResultY) {
        Collection<WebSocketSession> roomUsers = sessionManager
                .getRoomSessions(roomId);
        if (roomUsers == null) {
            log.error("给房间id为[{}]里的用户发送消息失败，从roomSessions中没有此房间id的信息");
            return;
        }

        for (WebSocketSession session : roomUsers) {
            send(session, jsonResultY);
        }

    }

    /**
     * 给某个房间里的除了自己的用户发送消息
     *
     * @param roomId      roomId
     * @param jsonResultY jsonResultY
     */
    public void sendMessageToOtherRoomUsers(String roomId, Integer userId, JsonResultY jsonResultY) {
        Collection<WebSocketSession> roomUsers = sessionManager
                .getRoomSessions(roomId);
        if (roomUsers == null) {
            log.error("给房间id为[{}]里的用户发送消息失败，从roomSessions中没有此房间id的信息");
            return;
        }

        WebSocketSession userSession = sessionManager.getByUserId(userId);

        for (WebSocketSession session : roomUsers) {
            if (userSession != session) {
                send(session, jsonResultY);
            }
        }

    }

    /**
     * 给某个用户发送消息
     *
     * @param userId      userId
     * @param jsonResultY jsonResultY
     */
    public void sendMessageByUserId(Integer userId, JsonResultY jsonResultY) {
        WebSocketSession userSession = sessionManager.getByUserId(userId);
        if (userSession == null) {
            log.error("给userId为[{}]的用户发送消息失败，从userIdSessions中没有id的信息", userId);
            return;
        }
        send(userSession, jsonResultY);
    }

    private static class SendMessageRunnable implements Runnable {

        private MessageManager messageManager;

        private WebSocketSession session;

        private JsonResultY jsonResultY;

        private Lock lock;

        public void setMessageManager(MessageManager messageManager) {
            this.messageManager = messageManager;
        }

        public void setLock(Lock lock) {
            this.lock = lock;
        }

        public void setSession(WebSocketSession session) {
            this.session = session;
        }

        public JsonResultY getJsonResultY() {
            return jsonResultY;
        }

        public void setJsonResultY(JsonResultY jsonResultY) {
            this.jsonResultY = jsonResultY;
        }

        @Override
        public void run() {
            try {
                if (lock != null) {
                    try {
                        lockAndSend();
                    } catch (InterruptedException e) {
                        log.error(e.getMessage(), e);
                    }
                } else {
                    session.sendMessage(new TextMessage(jsonResultY.toString()));
                    log();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }

        private void lockAndSend() throws InterruptedException {
            if (lock.tryLock(50, TimeUnit.MILLISECONDS)) {
                try {
                    session.sendMessage(new TextMessage(jsonResultY.toString()));
                    log();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                } finally {
                    lock.unlock();
                }
            } else {
                messageManager.send(session, jsonResultY);
            }
        }

        private void log() {
            int pid = jsonResultY.getPid();
            log.info("给客户端[sessionId:{}]发送消息：pid={}||{}", session.getId(), pid, jsonResultY.toString());
        }
    }

}
