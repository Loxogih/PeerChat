import java.io.*;
import java.net.*;

public class PeerConnection implements Runnable {
    private Socket socket;
    private PeerInfo peerInfo;
    private Node node;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean running;
    private Thread thread;
    
    public PeerConnection(Socket socket, Node node, boolean isInitiator) throws IOException {
        this.socket = socket;
        this.node = node;
        this.running = true;
        
        if (isInitiator) {
            //  Output FIRST
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.out.flush();
            this.in = new ObjectInputStream(socket.getInputStream());
            
            // Send handshake 
            sendHandshake();
            
            // Wait for handshake reply
            try {
                Message reply = (Message) in.readObject();
                this.peerInfo = new PeerInfo(reply.getSender(), 
                                           socket.getInetAddress().getHostAddress(), 
                                           socket.getPort());
            } catch (ClassNotFoundException e) {
                throw new IOException("Invalid handshake reply");
            }
        } else {
            // THEY connect to us: Input FIRST
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.out.flush();
            
            // Wait for handshake from them
            try {
                Message handshake = (Message) in.readObject();
                this.peerInfo = new PeerInfo(handshake.getSender(), 
                                           socket.getInetAddress().getHostAddress(), 
                                           socket.getPort());
                
                // Send handshake reply
                sendHandshake();
            } catch (ClassNotFoundException e) {
                throw new IOException("Invalid handshake");
            }
        }
    }
    
    private void sendHandshake() throws IOException {
        Message handshake = new Message(MessageType.HANDSHAKE, node.getUsername(), null, "HELLO");
        sendMessage(handshake);
    }
    
    public void setPeerInfo(PeerInfo peerInfo) {
        this.peerInfo = peerInfo;
    }
    
    public void start() {
        thread = new Thread(this);
        thread.start();
    }
    
    public void sendMessage(Message message) throws IOException {
        if (out != null && running) {
            synchronized (out) {
                out.writeObject(message);
                out.flush();
            }
        }
    }
    
    public void run() {
        try {
            while (running) {
                Message message = (Message) in.readObject();
                
                // Skip handshake messages (already handled)
                if (message.getType() == MessageType.HANDSHAKE) {
                    continue;
                }
                
                node.receiveMessage(message, this);
            }
        } catch (EOFException e) {
            // Connection closed
        } catch (IOException | ClassNotFoundException e) {
            if (running) {
                System.out.println("\n[System] Connection lost with " + 
                                 (peerInfo != null ? peerInfo.getUsername() : "peer"));
            }
        } finally {
            close();
        }
    }
    
    public void close() {
        running = false;
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            // Ignore
        }
        node.removeConnection(this);
    }
    
    public PeerInfo getPeerInfo() { return peerInfo; }
    public boolean isConnected() { return running && socket != null && !socket.isClosed(); }
}