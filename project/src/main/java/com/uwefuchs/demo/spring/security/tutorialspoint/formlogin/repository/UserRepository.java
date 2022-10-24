package com.uwefuchs.demo.spring.security.tutorialspoint.formlogin.repository;

import com.uwefuchs.demo.spring.security.tutorialspoint.formlogin.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
  Optional<User> findUserByUsername(String username);
}