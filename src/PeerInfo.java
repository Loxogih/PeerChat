public class PeerInfo {
    private String username;
    private String ipAddress;
    private int port;
    
    public PeerInfo(String username, String ipAddress, int port) {
        this.username = username;
        this.ipAddress = ipAddress;
        this.port = port;
    }
    
    public String getUsername() { return username; }
    public String getIpAddress() { return ipAddress; }
    public int getPort() { return port; }
    
    @Override
    public String toString() {
        return username + " (" + ipAddress + ":" + port + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PeerInfo peerInfo = (PeerInfo) obj;
        return port == peerInfo.port && 
               username.equals(peerInfo.username) && 
               ipAddress.equals(peerInfo.ipAddress);
    }
    
    @Override
    public int hashCode() {
        return username.hashCode() + ipAddress.hashCode() + port;
    }
}