package org.gl.authservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.gl.authservice.dto.AuthResponse;
import org.gl.authservice.dto.LoginRequest;
import org.gl.authservice.dto.RegisterRequest;
import org.gl.authservice.entity.User;
import org.gl.authservice.exception.EmailAlreadyExistsException;
import org.gl.authservice.exception.InvalidCredentialsException;
import org.gl.authservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        User user = User.builder()
                .fullName(request.fullName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .phoneNumber(request.phoneNumber())
                .role(request.role())
                .enabled(true)
                .build();

        User saved = userRepository.save(user);
        String token = jwtService.generateToken(saved);

        return AuthResponse.of(token, saved.getId(), saved.getFullName(), saved.getEmail(), saved.getRole());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtService.generateToken(user);

        return AuthResponse.of(token, user.getId(), user.getFullName(), user.getEmail(), user.getRole());
    }


}
