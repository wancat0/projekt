package com.wanca.aplikacja.service;

import com.wanca.aplikacja.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(username)
                .map(u -> new com.wanca.aplikacja.security.User(u.getId(), u.getEmail(), u.getName(), u.getSurname(), u.getPassword(), "USER"))
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
