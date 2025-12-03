// Code with one-line commentary before each function

import java.io.*;
import java.net.*;
import java.util.*;

public class Node {
    private String username;
    private int port;
    private List<PeerInfo> connectedPeers;
    private List<PeerConnection> connections;
    private List<Message> messageHistory;
    private List<ChatGroup> myGroups;
    private PeerListener listener;
    private boolean running;
    private Scanner scanner;

    // Initializes the node 
    public Node(String username, int port) {
        this.username = username;
        this.port = port;
        this.connectedPeers = new ArrayList<>();
        this.connections = new ArrayList<>();
        this.messageHistory = new ArrayList<>();
        this.myGroups = new ArrayList<>();
        this.running = false;
    }

    // Starts the node listener to accept incoming connections
    public boolean start() {
        try {
            this.listener = new PeerListener(port, this);
            this.listener.start();
            this.running = true;
            return true;
        } catch (IOException e) {
            System.out.println("[Error] Failed to start listener: " + e.getMessage());
            return false;
        }
    }

    // Connects to a peer using IP and port
    public void connectToPeer(String ip, int port) {
        try {
            System.out.println("[System] Connecting to " + ip + ":" + port + "...");
            Socket socket = new Socket(ip, port);

            PeerConnection connection = new PeerConnection(socket, this, true);

            PeerInfo peerInfo = connection.getPeerInfo();
            if (peerInfo != null) {
                connectedPeers.add(peerInfo);
                connections.add(connection);
                connection.start();

                System.out.println("[System] Successfully connected to " + peerInfo.getUsername());
            }
        } catch (IOException e) {
            System.out.println("[Error] Failed to connect: " + e.getMessage());
        }
    }

    // Handles an incoming connection from another peer
    public void handleIncomingConnection(Socket socket) {
        try {
            PeerConnection connection = new PeerConnection(socket, this, false);

            if (connection.getPeerInfo() != null) {
                PeerInfo peerInfo = connection.getPeerInfo();
                connectedPeers.add(peerInfo);
                connections.add(connection);
                connection.start();

                System.out.println("\n[System] " + peerInfo.getUsername() + " connected to you!");
            }
        } catch (IOException e) {
            System.out.println("[Error] Failed to handle incoming connection: " + e.getMessage());
        }
    }

    // Sends a broadcast message to all connected peers
    public void broadcastMessage(String text) {
        Message message = new Message(MessageType.BROADCAST, username, null, text);
        messageHistory.add(message);

        for (PeerConnection connection : connections) {
            try {
                connection.sendMessage(message);
            } catch (IOException e) {
                System.out.println("[Error] Failed to send to " + connection.getPeerInfo().getUsername());
            }
        }
        System.out.println("[System] Message sent to all peers");
    }

    // Sends a private message to a specific peer
    public void sendToPeer(String peerName, String text) {
        for (PeerConnection connection : connections) {
            PeerInfo peerInfo = connection.getPeerInfo();
            if (peerInfo != null && peerInfo.getUsername().equals(peerName)) {
                Message message = new Message(MessageType.PRIVATE, username, peerName, text);
                messageHistory.add(message);
                try {
                    connection.sendMessage(message);
                    System.out.println("[System] Message sent to " + peerName);
                } catch (IOException e) {
                    System.out.println("[Error] Failed to send to " + peerName);
                }
                return;
            }
        }
        System.out.println("[Error] Peer " + peerName + " not found");
    }

    // Sends a group message to all members of a group
    public void sendToGroup(String groupName, String text) {
        ChatGroup group = findGroup(groupName);
        if (group == null) {
            System.out.println("[Error] Group " + groupName + " not found");
            return;
        }

        Message message = new Message(MessageType.GROUP, username, groupName, text);
        messageHistory.add(message);

        int sentCount = 0;
        for (String member : group.getMembers()) {
            for (PeerConnection connection : connections) {
                PeerInfo peerInfo = connection.getPeerInfo();
                if (peerInfo != null && peerInfo.getUsername().equals(member)) {
                    try {
                        connection.sendMessage(message);
                        sentCount++;
                    } catch (IOException e) {
                        System.out.println("[Error] Failed to send to " + member);
                    }
                    break;
                }
            }
        }
        System.out.println("[System] Message sent to " + sentCount + " members of " + groupName);
    }

    // Handles incoming messages from peers
    public void receiveMessage(Message message, PeerConnection connection) {
        switch (message.getType()) {
            case BROADCAST:
                System.out.println("\n[Broadcast] " + message.getFormattedMessage());
                break;
            case PRIVATE:
                System.out.println("\n[Private from " + message.getSender() + "] " + message.getContent());
                break;
            case GROUP:
                System.out.println("\n[Group " + message.getRecipient() + "] " + message.getFormattedMessage());
                break;
            case SYSTEM:
                System.out.println("\n[System] " + message.getContent());
                break;
        }
        messageHistory.add(message);

        System.out.print("\nEnter choice: ");
    }

    // Creates a new chat group with selected members
    public void createGroup(String groupName, List<String> members) {
        ChatGroup group = new ChatGroup(groupName, username);
        for (String member : members) {
            group.addMember(member);
        }
        myGroups.add(group);
        System.out.println("[System] Group \"" + groupName + "\" created with members: " + members);
    }

    // Adds a peer to an existing group
    public void addToGroup(String groupName, String peerName) {
        ChatGroup group = findGroup(groupName);
        if (group != null && group.getCreator().equals(username)) {
            group.addMember(peerName);
            System.out.println("[System] Added " + peerName + " to group " + groupName);
        } else {
            System.out.println("[Error] Cannot modify group " + groupName);
        }
    }

    // Removes a peer from a group
    public void removeFromGroup(String groupName, String peerName) {
        ChatGroup group = findGroup(groupName);
        if (group != null && group.getCreator().equals(username)) {
            group.removeMember(peerName);
            System.out.println("[System] Removed " + peerName + " from group " + groupName);
        } else {
            System.out.println("[Error] Cannot modify group " + groupName);
        }
    }

    // Finds a group by its name
    private ChatGroup findGroup(String groupName) {
        for (ChatGroup group : myGroups) {
            if (group.getGroupName().equals(groupName)) {
                return group;
            }
        }
        return null;
    }

    // Removes a connection when a peer disconnects
    public void removeConnection(PeerConnection connection) {
        PeerInfo peerInfo = connection.getPeerInfo();
        if (peerInfo != null) {
            connectedPeers.remove(peerInfo);
            System.out.println("\n[System] " + peerInfo.getUsername() + " disconnected");
        }
        connections.remove(connection);
    }

    // Shuts down the entire node and closes all connections
    public void shutdown() {
        running = false;
        if (listener != null) {
            listener.stop();
        }
        for (PeerConnection connection : connections) {
            connection.close();
        }
    }

    // Getters for public access
    public String getUsername() { return username; }
    public int getPort() { return port; }
    public List<PeerInfo> getConnectedPeers() { return new ArrayList<>(connectedPeers); }
    public List<ChatGroup> getMyGroups() { return new ArrayList<>(myGroups); }
    public boolean isRunning() { return running; }
}