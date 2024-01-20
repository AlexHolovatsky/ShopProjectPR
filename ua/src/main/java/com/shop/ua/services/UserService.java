package com.shop.ua.services;

import com.shop.ua.component.RepositoryManager;
import com.shop.ua.enums.Role;
import com.shop.ua.models.UserImage;
import com.shop.ua.models.User;
import com.shop.ua.repositories.UserImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private RepositoryManager repositoryManager;
    @Autowired
    private final UserImageRepository userImageRepository;


    public User findByEmail(String email) {
        return repositoryManager.getUserRepository().findByEmail(email);
    }

    public boolean createUser (User user){
        String email = user.getEmail();
        if (repositoryManager.getUserRepository().findByEmail(email) != null) return false;
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.ROLE_ADMIN);

        String verificationToken = repositoryManager.getTokenService().generateToken();
        repositoryManager.getTokenService().saveTokenToUser(user, verificationToken);

        MultipartFile userImageFile = user.getUserImageFile();
        if (userImageFile != null && !userImageFile.isEmpty()) {
            try {
                String imagePath = repositoryManager.getUserService().saveImageToFileSystem(userImageFile);
                UserImage userImage = repositoryManager.getUserService().toImageEntity(userImageFile, imagePath);
                user.addImageToUser(userImage);
            } catch (IOException e) {
                e.printStackTrace(); // Додайте логіку обробки помилок, якщо потрібно
            }
        }

        logger.info("Saving new User with email: {}", email);
        repositoryManager.getUserRepository().save(user);

        repositoryManager.getEmailService().sendConfirmationEmail(email, verificationToken);

        return true;
    }

    public boolean confirmEmail(String token) {
        User user = repositoryManager.getUserRepository().findByEmailVerificationToken(token);
        if (user != null) {
            user.setEmailVerified(true);
            user.setEmailVerificationToken(null);
            repositoryManager.getUserRepository().save(user);
            logger.info("Email verification successful for user with email: {}", user.getEmail());
            return true;
        }
        return false;
    }

    public List<User> list(){
        return repositoryManager.getUserRepository().findAll();
    }

    public void banUser(Long id) {
        User user = repositoryManager.getUserRepository().findById(id).orElse(null);
        if (user != null){
            user.setActive(false);
            logger.info("User with id: {} has banned", user.getId(), user.getEmail());
        }
        repositoryManager.getUserRepository().save(user);

    }

    public void unbanUser(Long id) {
        User user = repositoryManager.getUserRepository().findById(id).orElse(null);
        if (user != null){
            user.setActive(true);
            logger.info("User with id: {} has unbanned", user.getId(), user.getEmail());
        }
        repositoryManager.getUserRepository().save(user);

    }

    public void assignAdminRole(Long id) {
        User user = repositoryManager.getUserRepository().findById(id).orElse(null);
        if (user != null) {
            user.getRoles().remove(Role.ROLE_USER);
            user.getRoles().add(Role.ROLE_ADMIN);
            repositoryManager.getUserRepository().save(user);
            logger.info("User with id: {} has been assigned the ROLE_ADMIN role", user.getId());
        }
    }

    public void removeAdminRole(Long id) {
        User user = repositoryManager.getUserRepository().findById(id).orElse(null);
        if (user != null) {
            user.getRoles().remove(Role.ROLE_ADMIN);
            user.getRoles().add(Role.ROLE_USER);
            repositoryManager.getUserRepository().save(user);
            logger.info("ROLE_ADMIN role has been removed from user with id: {}", user.getId());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repositoryManager.getUserRepository().findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Користувача не знайдено з електронною поштою: " + email);
        }
        return user;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return (User) authentication.getPrincipal();
    }


    public UserImage getUserImageById(Long id) {
        return userImageRepository.findById(id).orElse(null);
    }

    public String saveImageToFileSystem(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String filePath = "C:\\javastudy\\project\\ShopProject\\ua\\src\\main\\resources\\static\\user-images\\" + fileName;
        file.transferTo(new File(filePath));
        log.debug("Saved image file to path: {}", filePath);
        return filePath;
    }

//    public void saveUserImage(User user, MultipartFile file) throws IOException {
//        if (file.getSize() != 0) {
//            UserImage userImage = toImageEntity(file);
//            user.addImageToUser(userImage);
//            repositoryManager.getUserRepository().save(user);
//        }
//    }

    private UserImage toImageEntity(MultipartFile file, String filePath) throws IOException {
        UserImage image = new UserImage();
        image.setOriginalFileName(file.getOriginalFilename());
        image.setImagePath(filePath);
        return image;
    }


}
