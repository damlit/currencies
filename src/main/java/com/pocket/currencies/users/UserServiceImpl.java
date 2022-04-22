package com.pocket.currencies.users;

import com.pocket.currencies.pocket.entity.Pocket;
import com.pocket.currencies.pocket.repository.PocketRepository;
import com.pocket.currencies.registration.ConfirmationTokenService;
import com.pocket.currencies.users.entity.User;
import com.pocket.currencies.users.entity.UserDto;
import com.pocket.currencies.users.entity.UserRequestDto;
import com.pocket.currencies.users.entity.UserRole;
import com.pocket.currencies.users.exception.EmailAlreadyExistsException;
import com.pocket.currencies.users.repository.UserRepository;
import com.pocket.currencies.registration.entity.ConfirmationToken;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {

    private final static String USER_NOT_FOUND_MSG = "user with email %s not found";

    private final UserRepository userRepository;
    private final PocketRepository pocketRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findFirstByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    public String signUpUser(UserRequestDto userRequestDto) {
        return userRepository.findFirstByEmail(userRequestDto.getEmail())
                .map(this::createTokenForExistingUser)
                .orElseGet(() -> createNewToken(createNewUserAccount(userRequestDto)));
    }

    public int enableUser(String email) {
        return userRepository.enableAppUser(email);
    }

    public User getActiveUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName());
    }

    public UserDto getActiveUserDto() {
        User currentUser = getActiveUser();
        return new UserDto(currentUser);
    }

    public UserRole getActiveUserRole() {
        return getActiveUser().getUserRole();
    }

    public boolean isAdminUser() {
        return UserRole.ADMIN.equals(getActiveUserRole());
    }

    private String createTokenForExistingUser(User userFromDb) {
        if (userFromDb.getEnabled()) {
            throw new EmailAlreadyExistsException();
        }
        return createNewToken(userFromDb);
    }

    private User createNewUserAccount(UserRequestDto userRequestDto) {
        Pocket newPocket = new Pocket();
        String encodedPassword = bCryptPasswordEncoder.encode(userRequestDto.getPassword());
        User user = User.builder()
                .password(encodedPassword)
                .email(userRequestDto.getEmail())
                .userRole(UserRole.USER)
                .pocket(newPocket)
                .locked(false)
                .build();
        newPocket.setUserId(user);
        pocketRepository.save(newPocket);
        userRepository.save(user);
        return user;
    }

    private String createNewToken(User user) {
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(30),
                user
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }
}
