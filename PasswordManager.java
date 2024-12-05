import java.util.Scanner;

public class PasswordManager {
    public static void main(String[] args) {
        AuthenticationSystem auth = new AuthenticationSystem();
        if (auth.authenticate()) {
            MainMenu menu = new MainMenu();
            menu.start();
        }
    }
} 