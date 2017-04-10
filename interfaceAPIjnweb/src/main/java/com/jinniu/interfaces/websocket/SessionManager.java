package com.jinniu.interfaces.websocket;

import com.jinniu.commonjn.model.Room;
import com.jinniu.commonjn.model.User;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class SessionManager {

    public static final Map<String, Lock> sessionLocks =
            new ConcurrentHashMap<String, Lock>(100);

    /**
     * 房间里的user的session
     */
    private static final Map<String, List<WebSocketSession>> roomSessions =
            new ConcurrentHashMap<String, List<WebSocketSession>>(100);

    /**
     * 所有在线webSocket
     * key: sessionId
     * value: session
     */
    private static final Map<String, WebSocketSession> sessions =
            new ConcurrentHashMap<String, WebSocketSession>(100);

    /**
     * 所有已登录用户的webSocket
     * key: userId
     * value: sessionTent
     */
    private static final Map<String, WebSocketSession> userIdSessions =
            new ConcurrentHashMap<String, WebSocketSession>(100);

    /**
     * sessionId对应的user
     * key:sessionId
     * value: user
     */
    private static final Map<String, User> sessionIdUsers =
            new ConcurrentHashMap<String, User>(100);
    /**
     * sessionId对应的room
     * key:sessionId
     * value: room
     */
    private static final Map<String, Room> sessionIdRooms =
            new ConcurrentHashMap<String, Room>(100);

    /**
     * 获取所有在线的session
     */
    public Collection<WebSocketSession> getOnlineSessions() {
        return sessions.values();
    }

    /**
     * 根据userId获取session
     */
    public WebSocketSession getByUserId(Integer userId){
        return userIdSessions.get(userId.toString());
    }

    /**
     * 获取房间里的所有user session
     */
    public Collection<WebSocketSession> getRoomSessions(String roomId) {
        return roomSessions.get(roomId);
    }

    /**
     * 用户登录
     */
    public void userLogin(User user, WebSocketSession session) {
        sessions.put(session.getId(), session);
        if (sessionLocks.get(session.getId()) == null) {
            sessionLocks.put(session.getId(), new ReentrantLock());
        }
        userIdSessions.put(user.getId().toString(), session);
        sessionIdUsers.put(session.getId(), user);
    }
    /**
     * 更新session中的user信息
     */
    public void userUpdate(User user, WebSocketSession session) {

        sessionIdUsers.put(session.getId(), user);
    }

    /**
     * 用户登出
     */
    public void userLogout(User user, WebSocketSession session) {
        sessionLocks.remove(session.getId());
        userIdSessions.remove(user.getId().toString());
        sessionIdUsers.remove(session.getId());
    }

    /**
     * 用户断线
     */
    public void userDisconnection(User user, WebSocketSession session) {

    }

    /**
     * 用户加入房间
     */
    public void userJoinRoom(Room room, WebSocketSession session) {
        // 添加userId到房间
        List<WebSocketSession> webSocketSessions = roomSessions.get(room.getId().toString());
        if (webSocketSessions == null) {
            webSocketSessions = new ArrayList<>(4);
            roomSessions.put(room.getId().toString(), webSocketSessions);
        }
        webSocketSessions.add(session);
        sessionIdRooms.put(session.getId(), room);
    }

    /**
     * 用户退出房间
     */
    public void userExitRoom(String roomId, WebSocketSession session) {
        //删除userId所在的房间
        List<WebSocketSession> webSocketSessions = roomSessions.get(roomId);
        if (webSocketSessions != null) {
            webSocketSessions.remove(session);
        }
        sessionIdRooms.remove(session.getId());

    }

    /**
     * 新webSocket链接时，添加到缓存
     */
    public void addSession(WebSocketSession session) {
        sessions.put(session.getId(), session);
        sessionLocks.put(session.getId(), new ReentrantLock());
    }

    /**
     * webSocket链接关闭时，释放缓存资源
     */
    public void connectionClosed(WebSocketSession session) {
        //删除WebSocketSession缓存
        sessions.remove(session.getId());

        //删除session锁缓存
        sessionLocks.remove(session.getId());

        //删除已登录用户的webSocket
        User user = sessionIdUsers.get(session.getId());
        if (user != null) {
            userIdSessions.remove(user.getId().toString());
        }

        //删除sessionId对应的user
        sessionIdUsers.remove(session.getId());

        //删除userId所在的房间
        roomSessions.remove(session.getId());

    }

    /**
     * 删除各个缓存中存放的session
     */
    public void remove(WebSocketSession session) {
        //删除WebSocketSession缓存
        sessions.remove(session.getId());

        //删除已登录用户的webSocket
        User user = sessionIdUsers.get(session.getId());
        if (user != null) {
            userIdSessions.remove(user.getId().toString());
        }

        //删除sessionId对应的user
        sessionIdUsers.remove(session.getId());

        //删除userId所在的房间
        roomSessions.remove(session.getId());

    }

    /**
     * 获取锁对象
     */
    public Lock getLock(WebSocketSession session) {
        return sessionLocks.get(session.getId());
    }

    /**
     * 根据sessionId获取用户
     */
    public User getUser(String sessionId) {
        return sessionIdUsers.get(sessionId);
    }

    /**
     * 根据sessionId获取房间
     */
    public Room getRoom(String sessionId) {
        return sessionIdRooms.get(sessionId);
    }
}
