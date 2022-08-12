package laptopRentage.rentages.service;


import com.google.common.cache.LoadingCache;
import laptopRentage.rentages.constant.OtpContext;
import laptopRentage.rentages.mail.EmailService;
import laptopRentage.rentages.model.User;
import laptopRentage.rentages.repository.UserRepository;
import laptopRentage.rentages.security.JwtUtils;
import laptopRentage.rentages.userDto.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoadingCache<String, Integer> oneTimePasswordCache;
    private final EmailService emailService;
    private final JwtUtils jwtUtils;

    public ResponseEntity<?> createAccount( UserSignUpDto
                                                   userAccountCreationRequestDto) {
        if (userRepository.existsByEmail(userAccountCreationRequestDto.getEmail()))
            throw new ResponseStatusException
                    (HttpStatus.CONFLICT, "User account already exists for provided email-id");

        final var user = new User();
        user.setFirstname(userAccountCreationRequestDto.getFirstname());
        user.setLastname(userAccountCreationRequestDto.getLastname());
        user.setUsername(userAccountCreationRequestDto.getUsername());
        user.setPhoneNumber(userAccountCreationRequestDto.getPhoneNumber());
        user.setEmail(userAccountCreationRequestDto.getEmail());
        user.setConfirmPassword(passwordEncoder.encode(userAccountCreationRequestDto.getConfirmPassword()));
        user.setPassword(passwordEncoder.encode(userAccountCreationRequestDto.getPassword()));
        user.setEmailVerified(false);
        user.setActive(true);

        final var savedUser = userRepository.save(user);

        sendOtp(savedUser, "Verify your account");
        log.info("not saved");
        return ResponseEntity.ok(getOtpSendMessage());
    }

    public ResponseEntity<?> login(final UserLoginRequestDto userLoginRequestDto) {
        final User user = userRepository.findUserByEmail(userLoginRequestDto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid login credentials"));

        if (!passwordEncoder.matches(userLoginRequestDto.getPassword(), user.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid login credentials");

        if (!user.isActive())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Account not active");

        sendOtp(user, "2FA: Request to log in to your account");
        return ResponseEntity.ok(getOtpSendMessage());
    }

    public ResponseEntity<UserLoginResponseDto> verifyOtp(final OtpVerificationDto otpVerificationRequestDto){
        User user = userRepository.findUserByEmail(otpVerificationRequestDto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email-id"));

        Integer storedOneTimePassword = null;
        try {
            storedOneTimePassword = oneTimePasswordCache.get(user.getEmail());
        } catch (ExecutionException e) {
            log.error("FAILED TO FETCH PAIR FROM OTP CACHE: {}", e);
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
        }

        if (storedOneTimePassword != null) {
            if (storedOneTimePassword.equals(otpVerificationRequestDto.getOneTimePassword())) {
                if (otpVerificationRequestDto.getOtpContext().equals(OtpContext.SIGN_UP)) {
                    user.setEmailVerified(true);
                    user = userRepository.save(user);
                    return ResponseEntity
                            .ok(UserLoginResponseDto.builder().accessToken(jwtUtils.generateAccessToken(user))
                                    .refreshToken(jwtUtils.generateRefreshToken(user)).build());
                }
                if (otpVerificationRequestDto.getOtpContext().equals(OtpContext.ACCOUNT_DELETION)) {
                    user.setActive(false);
                    user = userRepository.save(user);
                    return ResponseEntity.ok().build();
                }
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> deleteAccount(final UUID userId) {
        final var user = userRepository.findById(userId).get();
        sendOtp(user, "2FA: Confirm account Deletion");
        return ResponseEntity.ok(getOtpSendMessage());
    }

    public ResponseEntity<?> getDetails(final UUID userId) {
        final var user = userRepository.findById(userId).get();
        final var response = new HashMap<String, String>();
        response.put("email", user.getEmail());
        response.put("created_at", user.getCreatedAt().toString());
        return ResponseEntity.ok(response);
    }

    private void sendOtp(final User user, final String subject) {
        try {
            if (oneTimePasswordCache.get(user.getEmail()) != null) oneTimePasswordCache.invalidate(user.getEmail());
        } catch (ExecutionException e) {
            log.error("FAILED TO FETCH PAIR FROM OTP CACHE: {}", e);
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
        }

        final var otp = new Random().ints(1, 100000, 999999).sum();
        oneTimePasswordCache.put(user.getEmail(), otp);

        CompletableFuture.supplyAsync(() -> {
            emailService.sendEmail(user.getEmail(), subject, "OTP: " + otp);
            return HttpStatus.OK;
        });
    }

    private Map<String, String> getOtpSendMessage() {
        final var response = new HashMap<String, String>();
        response.put("message",
                "OTP sent successfully sent to your registered email-address. verify it using /verify-otp endpoint");
        return response;
    }

    public ResponseEntity<?> refreshToken(final TokenRefreshDto tokenRefreshRequestDto) {
        if (jwtUtils.isTokenExpired(tokenRefreshRequestDto.getRefreshToken()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token has expired");
        final var user = userRepository.findUserByEmail(jwtUtils.extractEmail(tokenRefreshRequestDto.getRefreshToken()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        return ResponseEntity.ok(UserLoginResponseDto.builder().refreshToken(tokenRefreshRequestDto.getRefreshToken())
                .accessToken(jwtUtils.generateAccessToken(user)).build());
    }

}
