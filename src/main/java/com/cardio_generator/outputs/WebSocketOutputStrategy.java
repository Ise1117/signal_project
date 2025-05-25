package com.cardio_generator.outputs;

import org.java_websocket.WebSocket;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class WebSocketOutputStrategy implements OutputStrategy {

    private WebSocketServer server;
     /**
     * Initializes the WebSocket server on the given port.
     *
     * @param port the port on which the WebSocket server should listen
     */

    public WebSocketOutputStrategy(int port) {
        server = new SimpleWebSocketServer(new InetSocketAddress(port));
        System.out.println("WebSocket server created on port: " + port + ", listening for connections...");
        server.start();
    }
     /**
     * Sends formatted data to all connected WebSocket clients.
     *
     * @param patientId the patient ID
     * @param timestamp the time of the measurement
     * @param label     the type of data (e.g., HeartRate, Temperature)
     * @param data      the measurement value
     */

    @Override
    public void output(int patientId, long timestamp, String label, String data) {

        String message = String.format("%d,%s,%s,%d", patientId, data, label, timestamp);

        for (WebSocket conn : server.getConnections()) {
            try {
                if (conn.isOpen()) {
                    conn.send(message);
                } else {
                    System.err.println("Skipping closed connection: " + conn.getRemoteSocketAddress());
                }
            } catch (Exception e) {
                System.err.println("Failed to send message to " + conn.getRemoteSocketAddress());
                e.printStackTrace();
            }
        }
    }

    // websocket server that handles basic connection events
    private static class SimpleWebSocketServer extends WebSocketServer {

        public SimpleWebSocketServer(InetSocketAddress address) {
            super(address);
        }

        @Override
        public void onOpen(WebSocket conn, org.java_websocket.handshake.ClientHandshake handshake) {
            System.out.println("New connection: " + conn.getRemoteSocketAddress());
        }

        @Override
        public void onClose(WebSocket conn, int code, String reason, boolean remote) {
            System.out.println("Closed connection: " + conn.getRemoteSocketAddress());
        }

        @Override
        public void onMessage(WebSocket conn, String message) {
            System.out.println("Received message from client: " + message);
        }

        @Override
        public void onError(WebSocket conn, Exception ex) {
            System.err.println("WebSocket error occurred.");
            if (conn != null) {
                System.err.println("Connection: " + conn.getRemoteSocketAddress());
            }
            ex.printStackTrace();
        }
        @Override
        public void onStart() {
            System.out.println("Server started successfully");
        }
    }
}
