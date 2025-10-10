package com.snugplace.demo.Repository;

import com.snugplace.demo.Model.Enums.UserStatus;
import com.snugplace.demo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> email(String email);

    boolean existsByEmailAndStatus(String email, UserStatus status);

    Optional<User> findByEmailAndStatus(String email, UserStatus status);
}