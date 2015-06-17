package com.github.diegopacheco.sandbox.java.websockets;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class StockServiceWebSocket {
 
    private Session session;
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
 
    // called when the socket connection with the browser is established
    @OnWebSocketConnect
    public void handleConnect(Session session) {
        this.session = session;
    }
 
    // called when the connection closed
    @OnWebSocketClose
    public void handleClose(int statusCode, String reason) {
        System.out.println("Connection closed with statusCode=" 
            + statusCode + ", reason=" + reason);
    }
 
    // called when a message received from the browser
    @OnWebSocketMessage
    public void handleMessage(String message) {
        switch (message) {
            case "start":
                send("Stock service started!");
                executor.scheduleAtFixedRate(() -> 
                    send(StockService.getStockInfo()), 0, 5, TimeUnit.SECONDS);
                break;
            case "stop":
                this.stop();
                break;
        }
    }
 
    // called in case of an error
    @OnWebSocketError
    public void handleError(Throwable error) {
        error.printStackTrace();    
    }
 
    // sends message to browser
    private void send(String message) {
        try {
            if (session.isOpen()) {
                session.getRemote().sendString(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    // closes the socket
    private void stop() {
        try {
            session.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}