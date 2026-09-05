package mizan.ui;

import mizan.DatabaseHelper;
import mizan.model.User;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Optional;

public class LoginScreen extends JFrame {

    private static final Color BACKGROUND_COLOR = new Color(246, 248, 252);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(45, 55, 72);
    private static final Color ACCENT_COLOR = new Color(78, 115, 223);
    private static final Color BORDER_COLOR = new Color(226, 232, 240);

    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);

    public LoginScreen() {
        setTitle("Mizan - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);

        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        content.setBackground(BACKGROUND_COLOR);
        setContentPane(content);

        JLabel header = new JLabel("Welcome to Mizan", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(18f));
        header.setForeground(TEXT_PRIMARY);
        header.setBorder(new EmptyBorder(0, 0, 16, 0));
        content.add(header, BorderLayout.NORTH);

        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setOpaque(true);
        formCard.setBackground(CARD_COLOR);
        formCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                new EmptyBorder(20, 24, 20, 24)));
        content.add(formCard, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(TEXT_PRIMARY);
        formCard.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formCard.add(emailField, gbc);
        gbc.weightx = 0;

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(TEXT_PRIMARY);
        formCard.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formCard.add(passwordField, gbc);
        gbc.weightx = 0;

        JPanel actions = new JPanel();
        actions.setOpaque(true);
        actions.setBackground(BACKGROUND_COLOR);
        actions.setBorder(new EmptyBorder(20, 0, 0, 0));
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        stylePrimaryButton(loginButton);
        styleSecondaryButton(registerButton);
        actions.add(loginButton);
        actions.add(registerButton);
        content.add(actions, BorderLayout.SOUTH);

        loginButton.addActionListener(event -> handleLogin());
        registerButton.addActionListener(event -> openRegisterScreen());
    }

    private void stylePrimaryButton(JButton button) {
        button.setBackground(ACCENT_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setOpaque(true);
    }

    private void styleSecondaryButton(JButton button) {
        button.setBackground(Color.WHITE);
        button.setForeground(TEXT_PRIMARY);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                new EmptyBorder(9, 20, 9, 20)));
        button.setOpaque(true);
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both email and password.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Optional<User> user = DatabaseHelper.getInstance().login(email, password);
        if (user.isPresent()) {
            JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            DashboardScreen dashboard = new DashboardScreen(user.get());
            dashboard.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openRegisterScreen() {
        RegisterScreen registerScreen = new RegisterScreen(this);
        registerScreen.setVisible(true);
        setVisible(false);
    }

    public void returnFromRegister() {
        setVisible(true);
    }
}

