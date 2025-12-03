import java.util.*;

public class MenuManager {
    
    public static void showMainMenu(Node node, Scanner scanner) {
        while (node.isRunning()) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Send Message");
            System.out.println("2. Manage Groups");
            System.out.println("3. View Connected Peers");
            System.out.println("4. Connect to Peer");
            System.out.println("5. Exit");
            System.out.print("\nEnter choice [1-5]: ");
            
            String input = scanner.nextLine().trim();
            
            switch (input) {
                case "1":
                    showSendMessageMenu(node, scanner);
                    break;
                case "2":
                    showManageGroupsMenu(node, scanner);
                    break;
                case "3":
                    showConnectedPeers(node, scanner);
                    break;
                case "4":
                    showConnectToPeerMenu(node, scanner);
                    break;
                case "5":
                    System.out.println("\n[System] Goodbye " + node.getUsername() + "!");
                    return;
                default:
                    System.out.println("[Error] Invalid choice! Please enter 1-5.");
                    waitForEnter(scanner);
            }
        }
    }
    
    private static void showSendMessageMenu(Node node, Scanner scanner) {
     System.out.println("\n=== SEND MESSAGE ===");
     System.out.println("1. Send to ALL connected peers");
     System.out.println("2. Send to specific peer");
     System.out.println("3. Send to group");
       System.out.println("4. Back to Main Menu");
    System.out.print("\nEnter choice [1-4]: ");
        
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                System.out.print("Enter message: ");
              String broadcastMsg = scanner.nextLine();
              node.broadcastMessage(broadcastMsg);
               waitForEnter(scanner);
              break;
            case "2":
              showPeerSelectionMenu(node, scanner);
              break;
            case "3":
             showGroupSelectionMenu(node, scanner);
             break;
            case "4":
            return;
            default:
             System.out.println("[Error] Invalid choice!");
            waitForEnter(scanner);
        }
    }
    
    private static void showPeerSelectionMenu(Node node, Scanner scanner) {
     List<PeerInfo> peers = node.getConnectedPeers();
      if (peers.isEmpty()) {
         System.out.println("\n[System] No peers connected!");
        waitForEnter(scanner);
       return;
        }
        
        System.out.println("\n=== SELECT PEER ===");
        System.out.println("Connected peers:");
        for (int i = 0; i < peers.size(); i++) {
          System.out.println((i + 1) + ". " + peers.get(i));
        }
        System.out.println((peers.size() + 1) + ". Back");

        System.out.print("\nSelect peer [1-" + (peers.size() + 1) + "]: ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == peers.size() + 1) {
                return;
            } else if (choice >= 1 && choice <= peers.size()) {
                PeerInfo selectedPeer = peers.get(choice - 1);
                System.out.print("Enter message for " + selectedPeer.getUsername() + ": ");
                String message = scanner.nextLine();
                node.sendToPeer(selectedPeer.getUsername(), message);
            } else {
                System.out.println("[Error] Invalid selection!");
            }
        } catch (NumberFormatException e) {
            System.out.println("[Error] Please enter a number!");
        }
        waitForEnter(scanner);
    }
    
    private static void showGroupSelectionMenu(Node node, Scanner scanner) {
        List<ChatGroup> groups = node.getMyGroups();
        if (groups.isEmpty()) {
            System.out.println("\n[System] You haven't created any groups yet!");
            waitForEnter(scanner);
            return;
        }
        
        System.out.println("\n=== SELECT GROUP ===");
        System.out.println("Your groups:");
        for (int i = 0; i < groups.size(); i++) {
            System.out.println((i + 1) + ". " + groups.get(i));
        }
        System.out.println((groups.size() + 1) + ". Back");
        
        System.out.print("\nSelect group [1-" + (groups.size() + 1) + "]: ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == groups.size() + 1) {
                return;
            } else if (choice >= 1 && choice <= groups.size()) {
                ChatGroup selectedGroup = groups.get(choice - 1);
                System.out.print("Enter message for \"" + selectedGroup.getGroupName() + "\": ");
                String message = scanner.nextLine();
                node.sendToGroup(selectedGroup.getGroupName(), message);
            } else {
                System.out.println("[Error] Invalid selection!");
            }
        } catch (NumberFormatException e) {
            System.out.println("[Error] Please enter a number!");
        }
        waitForEnter(scanner);
    }
    
    private static void showConnectToPeerMenu(Node node, Scanner scanner) {
        System.out.println("\n=== CONNECT TO PEER ===");
        System.out.print("Enter peer's IP address (localhost for same computer): ");
        String ip = scanner.nextLine().trim();
        
        System.out.print("Enter peer's port number: ");
        try {
            int port = Integer.parseInt(scanner.nextLine());
            node.connectToPeer(ip, port);
        } catch (NumberFormatException e) {
            System.out.println("[Error] Invalid port number!");
        }
        waitForEnter(scanner);
    }
    
    private static void showManageGroupsMenu(Node node, Scanner scanner) {
        System.out.println("\n=== MANAGE GROUPS ===");
        System.out.println("1. Create new group");
        System.out.println("2. View my groups");
        System.out.println("3. Add member to group");
        System.out.println("4. Remove member from group");
        System.out.println("5. Back to Main Menu");
        System.out.print("\nEnter choice [1-5]: ");
        
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                showCreateGroupMenu(node, scanner);
                break;
            case "2":
                showViewGroupsMenu(node, scanner);
                break;
            case "3":
                showAddToGroupMenu(node, scanner);
                break;
            case "4":
                showRemoveFromGroupMenu(node, scanner);
                break;
            case "5":
                return;
            default:
                System.out.println("[Error] Invalid choice!");
                waitForEnter(scanner);
        }
    }
    
    private static void showCreateGroupMenu(Node node, Scanner scanner) {
        System.out.println("\n=== CREATE NEW GROUP ===");
        System.out.print("Enter group name: ");
        String groupName = scanner.nextLine().trim();
        
        if (groupName.isEmpty()) {
            System.out.println("[Error] Group name cannot be empty!");
            waitForEnter(scanner);
            return;
        }
        
        List<PeerInfo> peers = node.getConnectedPeers();
        if (peers.isEmpty()) {
            System.out.println("[System] No peers available to add to group!");
            waitForEnter(scanner);
            return;
        }
        
        System.out.println("\nAvailable peers to add:");
        for (int i = 0; i < peers.size(); i++) {
            System.out.println((i + 1) + ". " + peers.get(i).getUsername());
        }
        
        System.out.print("\nSelect peers (comma separated, e.g., 1,2): ");
        String selection = scanner.nextLine().trim();
        
        List<String> selectedMembers = new ArrayList<>();
        if (!selection.isEmpty()) {
            String[] indices = selection.split(",");
            for (String indexStr : indices) {
                try {
                    int index = Integer.parseInt(indexStr.trim()) - 1;
                    if (index >= 0 && index < peers.size()) {
                        selectedMembers.add(peers.get(index).getUsername());
                    }
                } catch (NumberFormatException e) {
                    // Ignore invalid entries
                }
            }
        }
        
        node.createGroup(groupName, selectedMembers);
        waitForEnter(scanner);
    }
    
    private static void showViewGroupsMenu(Node node, Scanner scanner) {
        List<ChatGroup> groups = node.getMyGroups();
        if (groups.isEmpty()) {
            System.out.println("\n[System] You haven't created any groups yet.");
        } else {
            System.out.println("\n=== YOUR GROUPS ===");
            for (int i = 0; i < groups.size(); i++) {
                System.out.println((i + 1) + ". " + groups.get(i));
            }
        }
        waitForEnter(scanner);
    }
    
    private static void showAddToGroupMenu(Node node, Scanner scanner) {
        List<ChatGroup> groups = node.getMyGroups();
        if (groups.isEmpty()) {
            System.out.println("\n[System] You haven't created any groups yet.");
            waitForEnter(scanner);
            return;
        }
        
        System.out.println("\n=== ADD MEMBER TO GROUP ===");
        System.out.println("Select group:");
        for (int i = 0; i < groups.size(); i++) {
            System.out.println((i + 1) + ". " + groups.get(i).getGroupName());
        }
        System.out.println((groups.size() + 1) + ". Back");
        
        System.out.print("\nSelect group [1-" + (groups.size() + 1) + "]: ");
        try {
            int groupChoice = Integer.parseInt(scanner.nextLine());
            if (groupChoice == groups.size() + 1) {
                return;
            } else if (groupChoice >= 1 && groupChoice <= groups.size()) {
                ChatGroup selectedGroup = groups.get(groupChoice - 1);
                
                // Show available peers not in group
                List<PeerInfo> allPeers = node.getConnectedPeers();
                List<String> availablePeers = new ArrayList<>();
                for (PeerInfo peer : allPeers) {
                    if (!selectedGroup.hasMember(peer.getUsername())) {
                        availablePeers.add(peer.getUsername());
                    }
                }
                
                if (availablePeers.isEmpty()) {
                    System.out.println("[System] No available peers to add.");
                    waitForEnter(scanner);
                    return;
                }
                
                System.out.println("\nAvailable peers to add:");
                for (int i = 0; i < availablePeers.size(); i++) {
                    System.out.println((i + 1) + ". " + availablePeers.get(i));
                }
                System.out.print("\nSelect peer: ");
                int peerChoice = Integer.parseInt(scanner.nextLine());
                
                if (peerChoice >= 1 && peerChoice <= availablePeers.size()) {
                    node.addToGroup(selectedGroup.getGroupName(), availablePeers.get(peerChoice - 1));
                } else {
                    System.out.println("[Error] Invalid selection!");
                }
            } else {
                System.out.println("[Error] Invalid selection!");
            }
        } catch (NumberFormatException e) {
            System.out.println("[Error] Please enter a number!");
        }
        waitForEnter(scanner);
    }
    
    private static void showRemoveFromGroupMenu(Node node, Scanner scanner) {
        List<ChatGroup> groups = node.getMyGroups();
        if (groups.isEmpty()) {
            System.out.println("\n[System] You haven't created any groups yet.");
            waitForEnter(scanner);
            return;
        }
        
        System.out.println("\n=== REMOVE MEMBER FROM GROUP ===");
        System.out.println("Select group:");
        for (int i = 0; i < groups.size(); i++) {
            System.out.println((i + 1) + ". " + groups.get(i).getGroupName());
        }
        System.out.println((groups.size() + 1) + ". Back");
        
        System.out.print("\nSelect group [1-" + (groups.size() + 1) + "]: ");
        try {
            int groupChoice = Integer.parseInt(scanner.nextLine());
            if (groupChoice == groups.size() + 1) {
                return;
            } else if (groupChoice >= 1 && groupChoice <= groups.size()) {
                ChatGroup selectedGroup = groups.get(groupChoice - 1);
                
                List<String> members = selectedGroup.getMembers();
                if (members.isEmpty()) {
                    System.out.println("[System] Group has no members.");
                    waitForEnter(scanner);
                    return;
                }
                
                System.out.println("\nGroup members:");
                for (int i = 0; i < members.size(); i++) {
                    System.out.println((i + 1) + ". " + members.get(i));
                }
                System.out.print("\nSelect member to remove: ");
                int memberChoice = Integer.parseInt(scanner.nextLine());
                
                if (memberChoice >= 1 && memberChoice <= members.size()) {
                    node.removeFromGroup(selectedGroup.getGroupName(), members.get(memberChoice - 1));
                } else {
                    System.out.println("[Error] Invalid selection!");
                }
            } else {
                System.out.println("[Error] Invalid selection!");
            }
        } catch (NumberFormatException e) {
            System.out.println("[Error] Please enter a number!");
        }
        waitForEnter(scanner);
    }
    
    private static void showConnectedPeers(Node node, Scanner scanner) {
        List<PeerInfo> peers = node.getConnectedPeers();
        System.out.println("\n=== CONNECTED PEERS ===");
        if (peers.isEmpty()) {
            System.out.println("No peers connected.");
        } else {
            System.out.println("Currently connected to " + peers.size() + " peer(s):");
            for (int i = 0; i < peers.size(); i++) {
                System.out.println((i + 1) + ". " + peers.get(i));
            }
        }
        waitForEnter(scanner);
    }
    
    private static void waitForEnter(Scanner scanner) {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}