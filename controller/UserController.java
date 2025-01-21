package UserManagementConsoleSystem.controller;

import UserManagementConsoleSystem.model.User;
import UserManagementConsoleSystem.model.UserModel;
import UserManagementConsoleSystem.view.UserView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class UserController {

    private final UserModel model;
    private final UserView view;
    private final TelegramService telegramService;

    private static final int MAX_NAME_LENGTH = 19;
    private static final int MAX_EMAIL_LENGTH = 25;

    public UserController(UserModel model, UserView view, TelegramService telegramService) {
        this.model = model;
        this.view = view;
        this.telegramService = telegramService;
    }

    private String truncateText(String text, int maxLength) {
        return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
    }

    private String truncateEmail(String email, int maxLength) {
        if (email.length() <= maxLength) {
            return email;
        }

        int atIndex = email.indexOf('@');
        if (atIndex == -1 || atIndex >= maxLength) {
            return truncateText(email, maxLength);
        }

        String namePart = email.substring(0, atIndex);
        String domainPart = email.substring(atIndex);
        int availableLength = maxLength - domainPart.length() - 3;
        if (availableLength > 0) {
            namePart = namePart.substring(0, Math.min(namePart.length(), availableLength));
            return namePart + "..." + domainPart;
        } else {
            return truncateText(email, maxLength);
        }
    }

    public void createUser() {
        Scanner scanner = new Scanner(System.in);
        String name = view.getInput("Enter Name: ");
        String email = view.getInput("Enter Email: ");
        User newUser = model.createUser(name, email);
        view.displayMessage("User created successfully!");

        String message = "New user created:\nName: " + name + "\nEmail: " + email + "\nUUID: " + newUser.getUuid() + "\nID: " + newUser.getId();
        boolean success = telegramService.sendNotification(message);
        if (success) {
            view.displayMessage("Notification sent to Telegram.");
        } else {
            view.displayMessage("Failed to send notification to Telegram.");
        }
    }

    public void searchUserByUUID(Scanner scanner) {
        String uuid = view.getInput("Enter UUID: ");
        User user = model.findUserByUUID(uuid);

        if (user != null) {
            view.displayMessage("+-----+--------------------------------------+---------------------+---------------------------+-----------------+");
            view.displayMessage("| ID  | UUID                                 | Name                | Email                     | isDeleted       |");
            view.displayMessage("+-----+--------------------------------------+---------------------+---------------------------+-----------------+");
            view.displayMessage(String.format("| %-3d | %-36s | %-19s | %-25s | %-15s |",
                    user.getId(),
                    user.getUuid(),
                    truncateText(user.getName(), MAX_NAME_LENGTH),
                    truncateEmail(user.getEmail(), MAX_EMAIL_LENGTH),
                    user.isDeleted() ? "true" : "false"));
            view.displayMessage("+-----+--------------------------------------+---------------------+---------------------------+-----------------+");
        } else {
            view.displayMessage("User not found with UUID: " + uuid);
        }
    }

    public void updateUserByUUID(Scanner scanner) {
        String uuid = view.getInput("Enter UUID to update: ");
        User user = model.findUserByUUID(uuid);

        if (user != null) {
            String name = view.getInput("Enter new name (leave blank to keep current): ");
            String email = view.getInput("Enter new email (leave blank to keep current): ");
            String isDeletedStr = view.getInput("Set isDeleted (true/false): ");
            boolean isDeleted = Boolean.parseBoolean(isDeletedStr);

            boolean success = model.updateUser(uuid, name, email, isDeleted);
            if (success) {
                view.displayMessage("User updated successfully!");
            } else {
                view.displayMessage("User update failed.");
            }
        } else {
            view.displayMessage("User not found with UUID: " + uuid);
        }
    }

    public void deleteUserByUUID(Scanner scanner) {
        String uuid = view.getInput("Enter UUID to delete: ");
        boolean success = model.deleteUser(uuid);
        if (success) {
            view.displayMessage("User deleted successfully!");
        } else {
            view.displayMessage("User not found with UUID: " + uuid);
        }
    }

    public void displayAllUsers() {
        Collection<User> usersCollection = model.getAllUsers();
        List<User> users = new ArrayList<>(usersCollection);

        users.removeIf(User::isDeleted);

        if (users.isEmpty()) {
            view.displayMessage("No active users found.");
            return;
        }

        int pageSize = 5;
        int totalPages = (int) Math.ceil(users.size() / (double) pageSize);
        int currentPage = 1;

        while (true) {
            int startIndex = (currentPage - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, users.size());

            view.displayMessage("+-----+--------------------------------------+---------------------+---------------------------+-----------------+");
            view.displayMessage("| ID  | UUID                                 | Name                | Email                     | isDeleted       |");
            view.displayMessage("+-----+--------------------------------------+---------------------+---------------------------+-----------------+");

            for (int i = startIndex; i < endIndex; i++) {
                User user = users.get(i);
                view.displayMessage(String.format("| %-3d | %-36s | %-19s | %-25s | %-15s |",
                        user.getId(),
                        user.getUuid(),
                        truncateText(user.getName(), MAX_NAME_LENGTH),
                        truncateEmail(user.getEmail(), MAX_EMAIL_LENGTH),
                        user.isDeleted() ? "true" : "false"));
            }

            view.displayMessage("+-----+--------------------------------------+---------------------+---------------------------+-----------------+");
            view.displayMessage("Page " + currentPage + " of " + totalPages);

            String choice = view.getInput("Enter 'n' for next page, 'p' for previous page, or 'q' to quit: ");
            if (choice.equals("n") && currentPage < totalPages) {
                currentPage++;
            } else if (choice.equals("p") && currentPage > 1) {
                currentPage--;
            } else if (choice.equals("q")) {
                break;
            }
        }
    }
}
