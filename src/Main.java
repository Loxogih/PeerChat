import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Clear screen
        System.out.print("\033[H\033[2J");
        System.out.flush();
        
        System.out.println("=== P2P CHAT SYSTEM ===");
        System.out.println("=======================\n");
        
        // Get username
        String username = "";
        while (username.trim().isEmpty()) {
            System.out.print("Enter your username: ");
            username = scanner.nextLine().trim();
            if (username.isEmpty()) {
                System.out.println("[Error] Username cannot be empty!");
            }
        }
        
        // Get port
        int port = 0;
        while (port < 1024 || port > 65535) {
            try {
                System.out.print("Enter port to listen on (1024-65535): ");
                port = Integer.parseInt(scanner.nextLine());
                if (port < 1024 || port > 65535) {
                    System.out.println("[Error] Port must be between 1024 and 65535!");
                }
            } catch (NumberFormatException e) {
                System.out.println("[Error] Please enter a valid number!");
            }
        }
        
        // Create and start node
        Node node = new Node(username, port);
        boolean started = node.start();
        
        if (!started) {
            System.out.println("[Error] Failed to start node on port " + port);
            System.out.println("Please try a different port.");
            scanner.close();
            return;
        }
        
        System.out.println("\n[System] Starting " + username + "'s node on port " + port);
        System.out.println("[System] Ready! Listening for connections...\n");
        
        // Show main menu
        MenuManager.showMainMenu(node, scanner);
        
        // Cleanup
        node.shutdown();
        scanner.close();
        System.out.println("\n[System] Program terminated.");
    }
}