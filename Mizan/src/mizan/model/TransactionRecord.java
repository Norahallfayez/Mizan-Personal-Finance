package mizan.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRecord(
        Integer transactionId,
        int userId,
        int categoryId,
        BigDecimal amount,
        String type,
        LocalDate date,
        String description,
        String paymentMethod
        ) {

    public TransactionRecord withTransactionId(int id) {
        return new TransactionRecord(id, userId, categoryId, amount, type, date, description, paymentMethod);
    }

    public TransactionRecord withCategoryId(int newCategoryId) {
        return new TransactionRecord(transactionId, userId, newCategoryId, amount, type, date, description, paymentMethod);
    }

    public TransactionRecord withAmount(BigDecimal newAmount) {
        return new TransactionRecord(transactionId, userId, categoryId, newAmount, type, date, description, paymentMethod);
    }

    public TransactionRecord withType(String newType) {
        return new TransactionRecord(transactionId, userId, categoryId, amount, newType, date, description, paymentMethod);
    }

    public TransactionRecord withDate(LocalDate newDate) {
        return new TransactionRecord(transactionId, userId, categoryId, amount, type, newDate, description, paymentMethod);
    }

    public TransactionRecord withDescription(String newDescription) {
        return new TransactionRecord(transactionId, userId, categoryId, amount, type, date, newDescription, paymentMethod);
    }

    public TransactionRecord withPaymentMethod(String newPaymentMethod) {
        return new TransactionRecord(transactionId, userId, categoryId, amount, type, date, description, newPaymentMethod);
    }
}
