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
        String name = view.getInput("Enter Name: ");
        String email = view.getInput("Enter Email: ");
        User newUser = model.createUser(name, email);
        view.displayMessage("User created successfully!");

        String message = "*New user created*\n" +
                "-ID: " + newUser.getId() + "\n" +
                "-Name: " + newUser.getName() + "\n" +
                "-Email: " + newUser.getEmail() + "\n" +
                "-UUID: " + newUser.getUuid();

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
            System.out.println("+-----+--------------------------------------+---------------------+---------------------------+");
            System.out.println("| ID  | UUID                                 | Name                | Email                     |");
            System.out.println("+-----+--------------------------------------+---------------------+---------------------------+");

            System.out.printf("| %-3d | %-36s | %-19s | %-25s |\n",
                    user.getId(),
                    user.getUuid(),
                    truncateText(user.getName(), MAX_NAME_LENGTH),
                    truncateEmail(user.getEmail(), MAX_EMAIL_LENGTH));

            System.out.println("+-----+--------------------------------------+---------------------+---------------------------+");
        } else {
            view.displayMessage("User not found with UUID: " + uuid);
        }
    }

    public void updateUserByUUID(Scanner scanner) {
        String uuid = view.getInput("Enter UUID to update: ");
        String newName = view.getInput("Enter new Name: ");
        String newEmail = view.getInput("Enter new Email: ");
        boolean isDeleted = view.getInput("Is this user deleted? (true/false): ").equalsIgnoreCase("true");

        boolean success = model.updateUser(uuid, newName, newEmail, isDeleted);
        if (success) {
            view.displayMessage("User updated successfully!");

            User updatedUser = model.findUserByUUID(uuid);
            String message = "*User updated*\n" +
                    "-ID: " + updatedUser.getId() + "\n" +
                    "-Name: " + updatedUser.getName() + "\n" +
                    "-Email: " + updatedUser.getEmail() + "\n" +
                    "-UUID: " + updatedUser.getUuid();

            boolean notificationSent = telegramService.sendNotification(message);
            if (notificationSent) {
                view.displayMessage("Notification sent to Telegram.");
            } else {
                view.displayMessage("Failed to send notification to Telegram.");
            }
        } else {
            view.displayMessage("User not found or update failed.");
        }
    }

    public void deleteUserByUUID(Scanner scanner) {
        String uuid = view.getInput("Enter UUID to delete: ");

        boolean success = model.deleteUser(uuid);
        if (success) {
            view.displayMessage("User deleted successfully!");

            User deletedUser = model.findUserByUUID(uuid);
            String message = "*User deleted*\n" +
                    "-ID: " + deletedUser.getId() + "\n" +
                    "-Name: " + deletedUser.getName() + "\n" +
                    "-Email: " + deletedUser.getEmail() + "\n" +
                    "-UUID: " + deletedUser.getUuid();

            boolean notificationSent = telegramService.sendNotification(message);
            if (notificationSent) {
                view.displayMessage("Notification sent to Telegram.");
            } else {
                view.displayMessage("Failed to send notification to Telegram.");
            }
        } else {
            view.displayMessage("User not found or deletion failed.");
        }
    }

    public void displayAllUsers() {
        Collection<User> usersCollection = model.getAllUsers();
        List<User> users = new ArrayList<>(usersCollection);
        users.removeIf(User::isDeleted);

        System.out.println("+-----+--------------------------------------+---------------------+---------------------------+");
        System.out.println("| ID  | UUID                                 | Name                | Email                     |");
        System.out.println("+-----+--------------------------------------+---------------------+---------------------------+");

        for (User user : users) {
            System.out.printf("| %-3d | %-36s | %-19s | %-25s |\n",
                    user.getId(),
                    user.getUuid(),
                    truncateText(user.getName(), MAX_NAME_LENGTH),
                    truncateEmail(user.getEmail(), MAX_EMAIL_LENGTH));
        }

        System.out.println("+-----+--------------------------------------+---------------------+---------------------------+");
    }
}
