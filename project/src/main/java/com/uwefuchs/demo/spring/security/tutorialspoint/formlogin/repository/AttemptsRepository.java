package com.uwefuchs.demo.spring.security.tutorialspoint.formlogin.repository;

import com.uwefuchs.demo.spring.security.tutorialspoint.formlogin.model.Attempts;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttemptsRepository extends JpaRepository<Attempts, Integer> {
  Optional<Attempts> findAttemptsByUsername(String username);
}