package com.g02.handyShare.User.Repository;

import com.g02.handyShare.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Method to find a user by email
    User findByEmail(String email);

    User findByVerificationToken(String token);
    User findByResetToken(String token);
}
