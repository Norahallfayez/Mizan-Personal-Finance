package mizan.ui;

import mizan.DatabaseHelper;
import mizan.model.Category;
import mizan.model.TransactionRecord;
import mizan.model.User;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class TransactionScreen extends JFrame {

    private static final Color BACKGROUND_COLOR = new Color(246, 248, 252);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(45, 55, 72);
    private static final Color ACCENT_COLOR = new Color(78, 115, 223);
    private static final Color BORDER_COLOR = new Color(226, 232, 240);
    private static final Color DANGER_COLOR = new Color(220, 53, 69);

    private final User user;
    private final Runnable onClose;

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Date", "Category", "Type", "Amount", "Description", "Payment"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable table = new JTable(tableModel);

    private final JComboBox<Category> categoryCombo = new JComboBox<>();
    private final JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Income", "Expense"});
    private final JTextField amountField = new JTextField(10);
    private final JTextField dateField = new JTextField(10);
    private final JTextField descriptionField = new JTextField(15);
    private final JTextField paymentMethodField = new JTextField(10);

    private final DatabaseHelper helper = DatabaseHelper.getInstance();
    private List<TransactionRecord> currentRecords = new ArrayList<>();

    public TransactionScreen(User user, Runnable onClose) {
        this.user = user;
        this.onClose = onClose;
        setTitle("Transactions - " + user.username());
        setSize(880, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        buildUI();
        loadCategories();
        loadTransactions();
    }

    private void buildUI() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(new EmptyBorder(16, 16, 16, 16));
        content.setBackground(BACKGROUND_COLOR);
        setContentPane(content);

        JLabel header = new JLabel("Manage Transactions", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(18f));
        header.setForeground(TEXT_PRIMARY);
        header.setBorder(new EmptyBorder(0, 0, 16, 0));
        content.add(header, BorderLayout.NORTH);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.setFillsViewportHeight(true);
        table.setRowHeight(24);
        table.setShowGrid(false);
        table.setBackground(CARD_COLOR);
        table.setForeground(TEXT_PRIMARY);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setBackground(new Color(237, 242, 247));
        table.getTableHeader().setForeground(TEXT_PRIMARY);
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                new EmptyBorder(0, 0, 0, 0)));
        content.add(tableScrollPane, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(true);
        formPanel.setBackground(CARD_COLOR);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BORDER_COLOR), "Transaction Details"),
                new EmptyBorder(16, 16, 16, 16)));
        content.add(formPanel, BorderLayout.SOUTH);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel categoryLabel = new JLabel("Category");
        categoryLabel.setForeground(TEXT_PRIMARY);
        formPanel.add(categoryLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(categoryCombo, gbc);
        JButton addCategoryButton = new JButton("Add Category");
        styleSecondaryButton(addCategoryButton);
        gbc.gridx = 2;
        gbc.weightx = 0;
        formPanel.add(addCategoryButton, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel typeLabel = new JLabel("Type");
        typeLabel.setForeground(TEXT_PRIMARY);
        formPanel.add(typeLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(typeCombo, gbc);
        gbc.weightx = 0;

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel amountLabel = new JLabel("Amount");
        amountLabel.setForeground(TEXT_PRIMARY);
        formPanel.add(amountLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(amountField, gbc);
        gbc.weightx = 0;

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD)");
        dateLabel.setForeground(TEXT_PRIMARY);
        formPanel.add(dateLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(dateField, gbc);
        gbc.weightx = 0;

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel descriptionLabel = new JLabel("Description");
        descriptionLabel.setForeground(TEXT_PRIMARY);
        formPanel.add(descriptionLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(descriptionField, gbc);
        gbc.weightx = 0;

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel paymentLabel = new JLabel("Payment Method");
        paymentLabel.setForeground(TEXT_PRIMARY);
        formPanel.add(paymentLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(paymentMethodField, gbc);
        gbc.weightx = 0;

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(true);
        buttonPanel.setBackground(CARD_COLOR);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton closeButton = new JButton("Close");
        stylePrimaryButton(addButton);
        stylePrimaryButton(updateButton);
        styleDangerButton(deleteButton);
        styleSecondaryButton(closeButton);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        formPanel.add(buttonPanel, gbc);
        gbc.weightx = 0;

        table.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                fillFormFromSelection();
            }
        });

        addButton.addActionListener(event -> handleAdd());
        updateButton.addActionListener(event -> handleUpdate());
        deleteButton.addActionListener(event -> handleDelete());
        closeButton.addActionListener(event -> dispose());
        addCategoryButton.addActionListener(event -> handleAddCategory());

        dateField.setText(helper.getServerDate().toString());
    }

    private void stylePrimaryButton(JButton button) {
        button.setBackground(ACCENT_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setOpaque(true);
    }

    private void styleSecondaryButton(JButton button) {
        button.setBackground(Color.WHITE);
        button.setForeground(TEXT_PRIMARY);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                new EmptyBorder(7, 16, 7, 16)));
        button.setOpaque(true);
    }

    private void styleDangerButton(JButton button) {
        button.setBackground(DANGER_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setOpaque(true);
    }

    private void loadCategories() {
        categoryCombo.removeAllItems();
        List<Category> categories = helper.getCategories(user.userId());
        for (Category category : categories) {
            categoryCombo.addItem(category);
        }
        if (categoryCombo.getItemCount() > 0) {
            categoryCombo.setSelectedIndex(0);
        }
    }

    private void loadTransactions() {
        tableModel.setRowCount(0);
        currentRecords = helper.getTransactions(user.userId());
        for (TransactionRecord record : currentRecords) {
            tableModel.addRow(new Object[]{
                    record.transactionId(),
                    record.date(),
                    resolveCategoryName(record.categoryId()),
                    record.type(),
                    record.amount(),
                    record.description(),
                    record.paymentMethod()
            });
        }
        tableModel.fireTableDataChanged();
    }

    private String resolveCategoryName(int categoryId) {
        for (int i = 0; i < categoryCombo.getItemCount(); i++) {
            Category category = categoryCombo.getItemAt(i);
            if (category != null && category.categoryId() == categoryId) {
                return category.name();
            }
        }
        return "Category #" + categoryId;
    }

    private void fillFormFromSelection() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            return;
        }

        TransactionRecord record = currentRecords.get(selectedRow);
        selectCategoryById(record.categoryId());

        typeCombo.setSelectedItem(record.type());
        amountField.setText(record.amount().toPlainString());
        dateField.setText(record.date().toString());
        descriptionField.setText(record.description() != null ? record.description() : "");
        paymentMethodField.setText(record.paymentMethod() != null ? record.paymentMethod() : "");
    }

    private void selectCategoryById(int categoryId) {
        for (int i = 0; i < categoryCombo.getItemCount(); i++) {
            Category category = categoryCombo.getItemAt(i);
            if (category.categoryId() == categoryId) {
                categoryCombo.setSelectedIndex(i);
                return;
            }
        }
    }

    private TransactionRecord gatherFormData(Integer transactionId) {
        Category category = (Category) categoryCombo.getSelectedItem();
        if (category == null) {
            JOptionPane.showMessageDialog(this, "Please select a category.", "Validation", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        BigDecimal amount;
        try {
            amount = new BigDecimal(amountField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount value.", "Validation", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        LocalDate date;
        try {
            date = LocalDate.parse(dateField.getText().trim());
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Date must be in YYYY-MM-DD format.", "Validation", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        String type = (String) typeCombo.getSelectedItem();
        String description = descriptionField.getText().trim();
        String paymentMethod = paymentMethodField.getText().trim();

        return new TransactionRecord(transactionId, user.userId(), category.categoryId(), amount, type, date, description, paymentMethod);
    }

    private void handleAdd() {
        TransactionRecord record = gatherFormData(null);
        if (record == null) {
            return;
        }
        boolean success = helper.addTransaction(record);
        if (success) {
            JOptionPane.showMessageDialog(this, "Transaction added.", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadTransactions();
            clearForm();
            if (onClose != null) {
                onClose.run();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add transaction.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdate() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select a transaction to update.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer transactionId = currentRecords.get(selectedRow).transactionId();
        TransactionRecord record = gatherFormData(transactionId);
        if (record == null) {
            return;
        }
        boolean success = helper.updateTransaction(record);
        if (success) {
            JOptionPane.showMessageDialog(this, "Transaction updated.", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadTransactions();
            if (onClose != null) {
                onClose.run();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update transaction.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select a transaction to delete.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Delete selected transaction?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        Integer transactionId = currentRecords.get(selectedRow).transactionId();
        boolean success = helper.deleteTransaction(transactionId);
        if (success) {
            JOptionPane.showMessageDialog(this, "Transaction deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadTransactions();
            clearForm();
            if (onClose != null) {
                onClose.run();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete transaction.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAddCategory() {
        String name = JOptionPane.showInputDialog(this, "Category name");
        if (name == null || name.trim().isEmpty()) {
            return;
        }
        String[] options = {"Income", "Expense"};
        String type = (String) JOptionPane.showInputDialog(this, "Category type", "Type",
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (type == null) {
            return;
        }
        helper.createCategory(user.userId(), name.trim(), type);
        loadCategories();
    }

    private void clearForm() {
        if (categoryCombo.getItemCount() > 0) {
            categoryCombo.setSelectedIndex(0);
        } else {
            categoryCombo.setSelectedIndex(-1);
        }
        typeCombo.setSelectedIndex(0);
        amountField.setText("");
        dateField.setText(helper.getServerDate().toString());
        descriptionField.setText("");
        paymentMethodField.setText("");
        table.clearSelection();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (onClose != null) {
            onClose.run();
        }
    }
}

