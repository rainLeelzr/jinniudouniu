package com.jinniu.interfaces.websocket;

import com.jinniu.commonjn.model.User;
import com.jinniu.commonjn.util.CommonError;
import com.jinniu.commonjn.util.JsonResultY;
import com.jinniu.interfaces.websocket.Mapping.ActionRouter;
import com.jinniu.interfaces.websocket.Mapping.LoginResource;
import com.jinniu.interfaces.websocket.Mapping.RouterHelper;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.lang.reflect.Method;


@Component
public class SystemWebSocketHandler implements WebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(SystemWebSocketHandler.class);

    @Autowired
    private ActionRouter actionRouter;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private MessageManager messageManager;

    public SystemWebSocketHandler() {
        super();
    }

    // 连接关闭
    public void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus closeStatus) throws Exception {
        log.debug("websocket[sessionId={}] cause[{}] connection closed......",
                session.getId(),
                closeStatus.toString());
        sessionManager.connectionClosed(session);
    }


    // 连接上
    public void afterConnectionEstablished(WebSocketSession session)
            throws Exception {
        log.debug("[sessionId={}] connect to the websocket success......", session.getId());
        sessionManager.addSession(session);
        // 处理离线消息

    }

    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("received client  message:{}", message.getPayload());
        String clientMessage = (String) message.getPayload();

        JsonResultY jsonResultY = null;
        int pid = -1;
        try {
            // 请求参数解析
            JSONObject jsonObject = JSONObject.fromObject(clientMessage);
            JSONObject data = JSONObject.fromObject(jsonObject.get("data"));

            pid = jsonObject.getInt("pid");
            Method m = RouterHelper.from(pid);

            //用户登录检查，如果是需要登录才能访问的资源，但是客户端未登录，则不允许继续访问业务逻辑
            checkLogin(m, session);

            //通过了登录检查，继续执行逻辑
            Object result = m.invoke(actionRouter, session, data);
            if (result != null) {
                if (result instanceof JsonResultY) {
                    jsonResultY = (JsonResultY) result;
                } else {
                    throw new RuntimeException(String.format("%s方法返回值不支持！请修改！", m.getName()));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            CommonError error = parse(e);
            if (error != null) {
                jsonResultY = new JsonResultY.Builder()
                        .setPid(pid)
                        .setError(error)
                        .build();
            } else {
                jsonResultY = new JsonResultY.Builder()
                        .setPid(pid)
                        .setCode(CommonError.SYS_ERR.getCode())
                        .setData(String.format("系统异常--%s)",
                                e.getCause() == null ? e.getMessage() : e.getCause().getMessage()))
                        .build();
            }
        }

        // 响应客户端
        if (jsonResultY != null) {
            messageManager.send(session, jsonResultY);
        }
    }

    /**
     * 用户登录检查
     * 思路：判断method有没有LoginResource注解
     * 如果没有LoginResource注解，则放行，执行具体的方法
     * 如果有LoginResource注解，则判断webSocketSession的id有没有对应的user
     * 如果有对应的user，则证明已登录。如果没有user，则返回未登录的信息
     */
    private void checkLogin(Method method, WebSocketSession session) {
        if (method.isAnnotationPresent(LoginResource.class)) {
            User user = sessionManager.getUser(session.getId());
            if (user == null) {
                throw CommonError.USER_UnLogin.newException();
            }
        }
    }

    /**
     * 解析异常是否为自定义的异常
     */
    private CommonError parse(Throwable e) {
        CommonError error = CommonError.exceptions.get(
                e.getClass().getSimpleName());
        if (error == null && e.getCause() != null) {
            error = CommonError.exceptions.get(
                    e.getCause().getClass().getSimpleName());
        }
        return error;
    }

    public void handleTransportError(WebSocketSession session,
                                     Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        log.debug("websocket [sessionId={}] connection closed......", session.getId());
        sessionManager.connectionClosed(session);
    }

    public boolean supportsPartialMessages() {
        return false;
    }

}
