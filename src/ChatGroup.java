import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChatGroup implements Serializable {
    private String groupName;
    private String creator;
    private List<String> members;
    
    public ChatGroup(String groupName, String creator) {
        this.groupName = groupName;
        this.creator = creator;
        this.members = new ArrayList<>();
    }
    
    public void addMember(String username) {
        if (!members.contains(username)) {
            members.add(username);
        }
    }
    
    public void removeMember(String username) {
        members.remove(username);
    }
    
    public boolean hasMember(String username) {
        return members.contains(username);
    }
    
    public String getGroupName() { return groupName; }
    public String getCreator() { return creator; }
    public List<String> getMembers() { return new ArrayList<>(members); }
    
    @Override
    public String toString() {
        return groupName + " (" + String.join(", ", members) + ")";
    }
}