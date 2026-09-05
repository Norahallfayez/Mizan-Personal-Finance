package mizan.ui;

import mizan.DatabaseHelper;

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

public class RegisterScreen extends JFrame {

    private static final Color BACKGROUND_COLOR = new Color(246, 248, 252);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(45, 55, 72);
    private static final Color ACCENT_COLOR = new Color(78, 115, 223);
    private static final Color BORDER_COLOR = new Color(226, 232, 240);

    private final JTextField usernameField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JPasswordField confirmPasswordField = new JPasswordField(20);
    private final JTextField phoneField = new JTextField(20);

    private final LoginScreen parent;

    public RegisterScreen(LoginScreen parent) {
        this.parent = parent;
        setTitle("Mizan - Register");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(420, 320);
        setLocationRelativeTo(null);
        buildUI();
    }

    private void buildUI() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        content.setBackground(BACKGROUND_COLOR);
        setContentPane(content);

        JLabel header = new JLabel("Create Your Account", SwingConstants.CENTER);
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

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(TEXT_PRIMARY);
        formCard.add(usernameLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formCard.add(usernameField, gbc);
        gbc.weightx = 0;

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(TEXT_PRIMARY);
        formCard.add(emailLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formCard.add(emailField, gbc);
        gbc.weightx = 0;

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(TEXT_PRIMARY);
        formCard.add(passwordLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formCard.add(passwordField, gbc);
        gbc.weightx = 0;

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setForeground(TEXT_PRIMARY);
        formCard.add(confirmPasswordLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formCard.add(confirmPasswordField, gbc);
        gbc.weightx = 0;

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setForeground(TEXT_PRIMARY);
        formCard.add(phoneLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formCard.add(phoneField, gbc);
        gbc.weightx = 0;

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton registerButton = new JButton("Register");
        stylePrimaryButton(registerButton);
        formCard.add(registerButton, gbc);

        registerButton.addActionListener(event -> handleRegister());
    }

    private void handleRegister() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String phone = phoneField.getText().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username, email, and password are required.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean success = DatabaseHelper.getInstance().registerUser(username, email, password, phone);
        if (success) {
            JOptionPane.showMessageDialog(this, "Registration successful! You can now login.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            parent.returnFromRegister();
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. Email might already exist.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void stylePrimaryButton(JButton button) {
        button.setBackground(ACCENT_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 24, 10, 24));
        button.setOpaque(true);
    }
}

