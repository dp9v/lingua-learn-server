package com.cp9v.lingua_learn_server.api.service;

import com.cp9v.lingua_learn_server.api.dto.UserDto;
import com.cp9v.lingua_learn_server.api.models.User;
import com.cp9v.lingua_learn_server.api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User %s not found".formatted(username)));
    }

    public void create(UserDto userDto) {
        var user = User.builder()
                .email(userDto.email())
                .name(userDto.name())
                .surname(userDto.surname())
                .password(passwordEncoder.encode(userDto.password()))
                .build();
        userRepository.save(user);
    }
}
