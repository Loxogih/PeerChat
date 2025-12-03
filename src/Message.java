import java.io.*;
import java.util.Date;

public class Message implements Serializable {
    private MessageType type;
    private String sender;
    private String recipient;
    private String content;
    private Date timestamp;
    
    public Message(MessageType type, String sender, String recipient, String content) {
        this.type = type;
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.timestamp = new Date();
    }
    
    public byte[] serialize() throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(this);
        objectStream.flush();
        return byteStream.toByteArray();
    }
    
    public static Message deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
        ObjectInputStream objectStream = new ObjectInputStream(byteStream);
        return (Message) objectStream.readObject();
    }
    
    public String getFormattedMessage() {
        String time = new java.text.SimpleDateFormat("HH:mm:ss").format(timestamp);
        return "[" + time + "] " + sender + ": " + content;
    }
    
    public MessageType getType() { return type; }
    public String getSender() { return sender; }
    public String getRecipient() { return recipient; }
    public String getContent() { return content; }
    public Date getTimestamp() { return timestamp; }
}