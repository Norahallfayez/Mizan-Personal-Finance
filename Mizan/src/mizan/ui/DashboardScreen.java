package mizan.ui;

import mizan.DatabaseHelper;
import mizan.model.User;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.time.YearMonth;

public class DashboardScreen extends JFrame {

    private static final Color BACKGROUND_COLOR = new Color(246, 248, 252);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(45, 55, 72);
    private static final Color ACCENT_COLOR = new Color(78, 115, 223);
    private static final Color BORDER_COLOR = new Color(226, 232, 240);

    private final User user;
    private final JLabel incomeLabel = new JLabel("0.00", SwingConstants.CENTER);
    private final JLabel expenseLabel = new JLabel("0.00", SwingConstants.CENTER);
    private final JLabel balanceLabel = new JLabel("0.00", SwingConstants.CENTER);

    public DashboardScreen(User user) {
        this.user = user;
        setTitle("Mizan - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 320);
        setLocationRelativeTo(null);
        buildUI();
        refreshSummary();
    }

    private void buildUI() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        content.setBackground(BACKGROUND_COLOR);
        setContentPane(content);

        JLabel header = new JLabel("Hello, " + user.username(), SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(20f));
        header.setForeground(TEXT_PRIMARY);
        header.setBorder(new EmptyBorder(0, 0, 16, 0));
        content.add(header, BorderLayout.NORTH);

        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 15, 15));
        summaryPanel.setOpaque(false);
        summaryPanel.add(buildTile("Monthly Income", incomeLabel));
        summaryPanel.add(buildTile("Monthly Expenses", expenseLabel));
        summaryPanel.add(buildTile("Balance", balanceLabel));
        content.add(summaryPanel, BorderLayout.CENTER);

        JPanel actions = new JPanel();
        actions.setBackground(BACKGROUND_COLOR);
        actions.setBorder(new EmptyBorder(20, 0, 0, 0));
        JButton refreshButton = new JButton("Refresh");
        JButton transactionsButton = new JButton("Manage Transactions");
        JButton logoutButton = new JButton("Logout");
        stylePrimaryButton(refreshButton);
        stylePrimaryButton(transactionsButton);
        styleSecondaryButton(logoutButton);
        actions.add(refreshButton);
        actions.add(transactionsButton);
        actions.add(logoutButton);
        content.add(actions, BorderLayout.SOUTH);

        refreshButton.addActionListener(event -> refreshSummary());
        transactionsButton.addActionListener(event -> openTransactionsScreen());
        logoutButton.addActionListener(event -> logout());
    }

    private JPanel buildTile(String title, JLabel valueLabel) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                new EmptyBorder(16, 16, 16, 16)));
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(14f));
        titleLabel.setForeground(TEXT_PRIMARY);
        valueLabel.setFont(valueLabel.getFont().deriveFont(22f));
        valueLabel.setForeground(ACCENT_COLOR);
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        return panel;
    }

    private void stylePrimaryButton(JButton button) {
        button.setBackground(ACCENT_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        button.setOpaque(true);
    }

    private void styleSecondaryButton(JButton button) {
        button.setBackground(Color.WHITE);
        button.setForeground(TEXT_PRIMARY);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                new EmptyBorder(9, 18, 9, 18)));
        button.setOpaque(true);
    }

    private void refreshSummary() {
        DatabaseHelper helper = DatabaseHelper.getInstance();
        YearMonth currentMonth = YearMonth.now();
        BigDecimal income = helper.getMonthlyIncome(user.userId(), currentMonth);
        BigDecimal expense = helper.getMonthlyExpense(user.userId(), currentMonth);
        BigDecimal balance = income.subtract(expense);

        incomeLabel.setText(income.toPlainString());
        expenseLabel.setText(expense.toPlainString());
        balanceLabel.setText(balance.toPlainString());
    }

    private void openTransactionsScreen() {
        TransactionScreen transactionScreen = new TransactionScreen(user, this::refreshSummary);
        transactionScreen.setVisible(true);
    }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this, "Do you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            LoginScreen loginScreen = new LoginScreen();
            loginScreen.setVisible(true);
        }
    }
}

