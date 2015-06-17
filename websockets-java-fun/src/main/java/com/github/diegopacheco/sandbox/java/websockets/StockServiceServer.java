package com.github.diegopacheco.sandbox.java.websockets;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
 
public class StockServiceServer {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8090);
 
        ServletContextHandler ctx = new ServletContextHandler();
        ctx.setContextPath("/");
        ctx.addServlet(StockServiceSocketServlet.class, "/stocks");
 
        server.setHandler(ctx);
 
        server.start();
        server.join();
    }
 
    public static class StockServiceSocketServlet extends WebSocketServlet {
        @Override
        public void configure(WebSocketServletFactory factory) {
            factory.register(StockServiceWebSocket.class);
        }
    }
}