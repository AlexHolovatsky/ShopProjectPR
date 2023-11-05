package com.shop.ua.services;

import com.shop.ua.enums.Role;
import com.shop.ua.models.User;
import com.shop.ua.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.shop.ua.enums.Role.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final TokenService tokenService;

    public boolean createUser (User user){
        String email = user.getEmail();
        if (userRepository.findByEmail(email) != null) return false;
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.ROLE_USER);

        String verificationToken = tokenService.generateToken();
        tokenService.saveTokenToUser(user, verificationToken);

        log.info("Saving new User with email: {}", email);
        userRepository.save(user);

        emailService.sendConfirmationEmail(email, verificationToken);

        return true;
    }

    public boolean confirmEmail(String token) {
        User user = userRepository.findByEmailVerificationToken(token);
        if (user != null) {
            user.setEmailVerified(true);
            user.setEmailVerificationToken(null);
            userRepository.save(user);
            log.info("Email verification successful for user with email: {}", user.getEmail());
            return true;
        }
        return false;
    }

    public List<User> list(){
        return userRepository.findAll();
    }

    public void banUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null){
            user.setActive(false);
            log.info("User with id: {} has banned", user.getId(), user.getEmail());
        }
        userRepository.save(user);

    }

    public void unbanUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null){
            user.setActive(true);
            log.info("User with id: {} has unbanned", user.getId(), user.getEmail());
        }
        userRepository.save(user);

    }

    public void assignAdminRole(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.getRoles().remove(Role.ROLE_USER);
            user.getRoles().add(Role.ROLE_ADMIN);
            userRepository.save(user);
            log.info("User with id: {} has been assigned the ROLE_ADMIN role", user.getId());
        }
    }

    public void removeAdminRole(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.getRoles().remove(Role.ROLE_ADMIN);
            user.getRoles().add(Role.ROLE_USER);
            userRepository.save(user);
            log.info("ROLE_ADMIN role has been removed from user with id: {}", user.getId());
        }
    }

}
