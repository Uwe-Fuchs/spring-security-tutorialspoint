package com.uwefuchs.demo.spring.security.tutorialspoint.formlogin.security;

import com.uwefuchs.demo.spring.security.tutorialspoint.formlogin.model.User;
import com.uwefuchs.demo.spring.security.tutorialspoint.formlogin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityUserDetailsService implements UserDetailsService {
  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException {
    return userRepository.findUserByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not present"));
  }

  public void createUser(UserDetails user) {
    userRepository.save((User) user);
  }
}