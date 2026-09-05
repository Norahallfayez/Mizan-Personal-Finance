package mizan.model;

import java.time.LocalDate;

public record User(
        int userId,
        String username,
        String email,
        String passwordHash,
        String phoneNumber,
        LocalDate createdAt
        ) {

}
