package UserManagementConsoleSystem.view;

import java.util.Scanner;

public class UserView {
    public void displayMessage(String message) {
        System.out.println(message);
    }

    public String getInput(String prompt) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(prompt);
        return scanner.nextLine();
    }
}
