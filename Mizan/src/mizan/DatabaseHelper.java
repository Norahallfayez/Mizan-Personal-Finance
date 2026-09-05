package mizan;

import mizan.model.Category;
import mizan.model.TransactionRecord;
import mizan.model.User;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

public class DatabaseHelper {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/mizan";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";

    private static final DatabaseHelper INSTANCE = new DatabaseHelper();

    private DatabaseHelper() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("MySQL Driver not found.", e);
        }
    }

    public static DatabaseHelper getInstance() {
        return INSTANCE;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }

    public boolean registerUser(String username, String email, String password, String phoneNumber) {
        String sql = "INSERT INTO USER (username, email, password, phone_number, created_at) VALUES (?, ?, ?, ?, CURDATE())";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, email);
            statement.setString(3, hashPassword(password));
            statement.setString(4, phoneNumber);

            return statement.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public Optional<User> login(String email, String password) {
        String sql = "SELECT * FROM USER WHERE email = ? AND password = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            statement.setString(2, hashPassword(password));

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone_number"),
                        rs.getDate("created_at").toLocalDate()
                );
                return Optional.of(user);
            }
            return Optional.empty();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return Optional.empty();
        }
    }

    public List<Category> getCategories(int userId) {
        String sql = "SELECT category_id, user_id, name, type, icon FROM CATEGORY WHERE user_id = ? ORDER BY name";
        List<Category> categories = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                categories.add(new Category(
                        rs.getInt("category_id"),
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("icon")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return categories;
    }

    public boolean addTransaction(TransactionRecord record) {
        String sql = "INSERT INTO TRANSACTIONS (user_id, category_id, amount, type, date, description, payment_method) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, record.userId());
            statement.setInt(2, record.categoryId());
            statement.setBigDecimal(3, record.amount());
            statement.setString(4, record.type());
            statement.setDate(5, Date.valueOf(record.date()));
            statement.setString(6, record.description());
            statement.setString(7, record.paymentMethod());
            return statement.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public List<TransactionRecord> getTransactions(int userId) {
        String sql = "SELECT transaction_id, user_id, category_id, amount, type, date, description, payment_method "
                + "FROM TRANSACTIONS WHERE user_id = ? ORDER BY date DESC";
        List<TransactionRecord> transactions = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                transactions.add(new TransactionRecord(
                        rs.getInt("transaction_id"),
                        rs.getInt("user_id"),
                        rs.getInt("category_id"),
                        rs.getBigDecimal("amount"),
                        rs.getString("type"),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("description"),
                        rs.getString("payment_method")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return transactions;
    }

    public boolean updateTransaction(TransactionRecord record) {
        String sql = "UPDATE TRANSACTIONS SET amount = ?, category_id = ?, type = ?, date = ?, description = ?, payment_method = ? "
                + "WHERE transaction_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setBigDecimal(1, record.amount());
            statement.setInt(2, record.categoryId());
            statement.setString(3, record.type());
            statement.setDate(4, Date.valueOf(record.date()));
            statement.setString(5, record.description());
            statement.setString(6, record.paymentMethod());
            statement.setInt(7, record.transactionId());

            return statement.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean deleteTransaction(int transactionId) {
        String sql = "DELETE FROM TRANSACTIONS WHERE transaction_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, transactionId);
            return statement.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public BigDecimal getMonthlyIncome(int userId, YearMonth yearMonth) {
        String sql = "SELECT COALESCE(SUM(amount), 0) AS total_income "
                + "FROM TRANSACTIONS WHERE user_id = ? AND type = 'Income' "
                + "AND YEAR(date) = ? AND MONTH(date) = ?";
        return sumForMonth(userId, yearMonth, sql);
    }

    public BigDecimal getMonthlyExpense(int userId, YearMonth yearMonth) {
        String sql = "SELECT COALESCE(SUM(amount), 0) AS total_expense "
                + "FROM TRANSACTIONS WHERE user_id = ? AND type = 'Expense' "
                + "AND YEAR(date) = ? AND MONTH(date) = ?";
        return sumForMonth(userId, yearMonth, sql);
    }

    private BigDecimal sumForMonth(int userId, YearMonth yearMonth, String sql) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, yearMonth.getYear());
            statement.setInt(3, yearMonth.getMonthValue());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal(1) != null ? rs.getBigDecimal(1) : BigDecimal.ZERO;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    public boolean ensureCategoryExists(int userId, String name, String type) {
        String select = "SELECT category_id FROM CATEGORY WHERE user_id = ? AND name = ?";
        String insert = "INSERT INTO CATEGORY (user_id, name, type) VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(select)) {

            selectStatement.setInt(1, userId);
            selectStatement.setString(2, name);
            ResultSet rs = selectStatement.executeQuery();
            if (rs.next()) {
                return true;
            }

            try (PreparedStatement insertStatement = connection.prepareStatement(insert)) {
                insertStatement.setInt(1, userId);
                insertStatement.setString(2, name);
                insertStatement.setString(3, type);
                return insertStatement.executeUpdate() == 1;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public Optional<Category> getCategoryByName(int userId, String name) {
        String sql = "SELECT category_id, user_id, name, type, icon FROM CATEGORY WHERE user_id = ? AND name = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setString(2, name);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(new Category(
                        rs.getInt("category_id"),
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("icon")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Category> createCategory(int userId, String name, String type) {
        String sql = "INSERT INTO CATEGORY (user_id, name, type) VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, userId);
            statement.setString(2, name);
            statement.setString(3, type);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                return Optional.empty();
            }
            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                return Optional.of(new Category(keys.getInt(1), userId, name, type, null));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    public LocalDate getServerDate() {
        String sql = "SELECT CURRENT_DATE() AS currentDate";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                Date date = rs.getDate("currentDate");
                if (date != null) {
                    return date.toLocalDate();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return LocalDate.now();
    }
}
