package com.vs.runner.auth.services;

import com.vs.runner.auth.entities.ConfirmRegistrationToken;
import com.vs.runner.auth.models.AuthenticationRequest;
import com.vs.runner.auth.models.AuthenticationResponse;
import com.vs.runner.auth.models.ConfirmRegisterRequest;
import com.vs.runner.auth.models.RegisterRequest;
import com.vs.runner.auth.repositories.ConfirmRegistrationTokenRepository;
import com.vs.runner.exceptions.BadRequestException;
import com.vs.runner.exceptions.InternalServerError;
import com.vs.runner.exceptions.ResourceNotFoundException;
import com.vs.runner.notification.services.EmailService;
import com.vs.runner.user.entities.User;
import com.vs.runner.user.entities.UserInfo;
import com.vs.runner.user.enums.Role;
import com.vs.runner.user.repositories.UserInfoRepository;
import com.vs.runner.user.repositories.UserRepository;
import com.vs.runner.util.TokenGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final ConfirmRegistrationTokenRepository confirmRegistrationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final UserInfoRepository userInfoRepository;

    @Transactional
    public void register(RegisterRequest request) {
        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .isActivated(false)
                .build();
        userRepository.save(user);

        var token = ConfirmRegistrationToken.builder()
                .token(TokenGenerator.generateToken())
                .user(user)
                .build();
        var userInfo = UserInfo.builder()
                .user(user)
                .build();
        userInfoRepository.save(userInfo);
        confirmRegistrationTokenRepository.save(token);
        var success = emailService.sendRegistrationConfirmationEmail(user.getEmail(), token.getToken());
        if (!success) {
            throw new InternalServerError("Email sending error");
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User with email " + request.getEmail() + " not found"));
        if (!user.getIsActivated()) {
            throw new BadRequestException("Account is not activated");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }

    public void confirmRegistration(ConfirmRegisterRequest request) {
        var token = confirmRegistrationTokenRepository.findByToken(request.getToken()).orElseThrow(() -> new ResourceNotFoundException("Token with given value " + request.getToken() + " not found"));
        var user = token.getUser();
        user.setIsActivated(true);
        userRepository.save(user);
        confirmRegistrationTokenRepository.delete(token);
    }
}
