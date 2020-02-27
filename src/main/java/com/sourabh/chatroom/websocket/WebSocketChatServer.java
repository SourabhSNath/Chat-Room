package com.sourabh.chatroom.websocket;

import com.alibaba.fastjson.JSON;
import com.sourabh.chatroom.model.Message;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * https://javaee.github.io/tutorial/websocket005.html
 */

@Component
@ServerEndpoint(value = "/chat/{username}")
public class WebSocketChatServer {

    private static final Map<String, Session> onlineSessions = new ConcurrentHashMap<>();

    private static void sendMessageToAll(Message messageObj) {

        var message = JSON.toJSONString(messageObj);
        onlineSessions.forEach((userKey, sessionVal) -> {

            try {
                sessionVal.getBasicRemote().sendText(message); //send message through that session, synchronously
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    /**
     * Open connection, 1) add session, 2) add user.
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String userName) {

        onlineSessions.put(userName, session);
        System.out.println("Connection Opened for user: " + userName + ", Session: " + session);

        var message = new Message(userName, "Joined the server.", onlineSessions.size(),
                Message.Type.ENTER);

        sendMessageToAll(message);

        System.out.println("WebSocketChatServer - Message " + JSON.toJSONString(message) +
                " received from WebSocketChatServer with username " + message.getUserName() +
                ", message " + message.getMessage() +
                ", type " + message.getType() +
                ", onlineCount " + message.getOnlineCount() + " and session " + session);
    }

    /**
     * Send message, 1) get username and session, 2) send message to all.
     */
    @OnMessage
    public void onMessage(Session session, String jsonMessage) {
        // https://www.baeldung.com/fastjson to convert json object back to message object

        var message = JSON.parseObject(jsonMessage, Message.class);
        message.setOnlineCount(onlineSessions.size());

        sendMessageToAll(message);
    }


    /**
     * Close connection, 1) remove session, 2) update user.
     */
    @OnClose
    public void onClose(Session session, @PathParam("username") String userName) {
        onlineSessions.remove(userName, session);
        System.out.println("Connection closed for " + userName);

        var message = new Message(userName, "Left the server.", onlineSessions.size()
                , Message.Type.LEAVE);

        sendMessageToAll(message);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
}
