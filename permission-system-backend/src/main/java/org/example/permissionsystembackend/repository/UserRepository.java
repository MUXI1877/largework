package org.example.permissionsystembackend.repository;

import org.example.permissionsystembackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户Repository
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByIdCard(String idCard);

    Optional<User> findByPhone(String phone);

    Optional<User> findByUsername(String username);

    boolean existsByIdCard(String idCard);

    boolean existsByPhone(String phone);

    boolean existsByUsername(String username);

    List<User> findByStatus(User.UserStatus status);
}
