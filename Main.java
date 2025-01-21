package UserManagementConsoleSystem;

import UserManagementConsoleSystem.controller.TelegramService;
import UserManagementConsoleSystem.controller.UserController;
import UserManagementConsoleSystem.model.UserModel;
import UserManagementConsoleSystem.view.UserView;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserModel model = new UserModel();
        UserView view = new UserView();
        TelegramService telegramService = new TelegramService();
        UserController controller = new UserController(model, view, telegramService);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nUser Management Console:");
            System.out.println("1. Create User");
            System.out.println("2. Search User by UUID");
            System.out.println("3. Update User by UUID");
            System.out.println("4. Delete User by UUID");
            System.out.println("5. Display All Users");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> controller.createUser();
                case 2 -> controller.searchUserByUUID(scanner);
                case 3 -> controller.updateUserByUUID(scanner);
                case 4 -> controller.deleteUserByUUID(scanner);
                case 5 -> controller.displayAllUsers();
                case 6 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
