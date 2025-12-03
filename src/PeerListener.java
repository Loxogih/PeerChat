import java.io.*;
import java.net.*;

public class PeerListener implements Runnable {
    private ServerSocket serverSocket;
    private Node node;
    private boolean running;
    private Thread thread;
    
    public PeerListener(int port, Node node) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.node = node;
        this.running = true;
    }
    
    public void start() {
        thread = new Thread(this);
        thread.start();
    }
    
    public void run() {
        System.out.println("[System] Listening for connections on port " + serverSocket.getLocalPort());
        
        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                node.handleIncomingConnection(clientSocket);
            } catch (IOException e) {
                if (running) {
                    System.out.println("[Error] Failed to accept connection: " + e.getMessage());
                }
            }
        }
    }
    
    public void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            // Ignore
        }
    }
}