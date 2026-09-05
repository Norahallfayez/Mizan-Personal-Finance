package mizan;

import mizan.ui.LoginScreen;
import javax.swing.SwingUtilities;

public class Mizan {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginScreen screen = new LoginScreen();
            screen.setVisible(true);
        });
    }

}
