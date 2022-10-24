package com.uwefuchs.demo.spring.security.tutorialspoint.formlogin.security;

import com.uwefuchs.demo.spring.security.tutorialspoint.formlogin.model.Attempts;
import com.uwefuchs.demo.spring.security.tutorialspoint.formlogin.model.User;
import com.uwefuchs.demo.spring.security.tutorialspoint.formlogin.repository.AttemptsRepository;
import com.uwefuchs.demo.spring.security.tutorialspoint.formlogin.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthProvider implements AuthenticationProvider {
  private static final int ATTEMPTS_LIMIT = 3;

  @Autowired private SecurityUserDetailsService userDetailsService;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private AttemptsRepository attemptsRepository;
  @Autowired private UserRepository userRepository;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    final String username = authentication.getName();

    final Optional<User> result = userRepository.findUserByUsername(username);
    if (result.isEmpty()) {
      throw new BadCredentialsException("wrong username or password");
    }

    final User user = result.get();
    final String password = authentication.getCredentials().toString();
    if (!passwordEncoder.matches(password, user.getPassword())) {
      processFailedAttempts(username, user);
      throw new BadCredentialsException("Invalid username or password");
    }

    resetAttempts(username);

    return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
  }

  private void processFailedAttempts(String username, User user) {
    Optional<Attempts> userAttempts = attemptsRepository.findAttemptsByUsername(username);

    if (userAttempts.isEmpty()) {
      Attempts attempts = new Attempts();
      attempts.setUsername(username);
      attempts.setAttempts(1);
      attemptsRepository.save(attempts);
    } else {
      Attempts attempts = userAttempts.get();
      attempts.setAttempts(attempts.getAttempts() + 1);
      attemptsRepository.save(attempts);

      if (attempts.getAttempts() >= ATTEMPTS_LIMIT) {
        user.setAccountNonLocked(false);
        userRepository.save(user);
        throw new LockedException("Too many invalid attempts. Account is locked!!");
      }
    }
  }

  private void resetAttempts(String username) {
    final Optional<Attempts> userAttempts = attemptsRepository.findAttemptsByUsername(username);
    if (userAttempts.isPresent()) {
      Attempts attempts = userAttempts.get();
      attempts.setAttempts(0);
      attemptsRepository.save(attempts);
    } else {
      Attempts attempts = new Attempts();
      attempts.setUsername(username);
      attempts.setAttempts(0);
      attemptsRepository.save(attempts);
    }
  }

  @Override public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}