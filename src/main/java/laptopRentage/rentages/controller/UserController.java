package laptopRentage.rentages.controller;

import laptopRentage.rentages.ApiPathExclusion;
import laptopRentage.rentages.security.JwtUtils;
import laptopRentage.rentages.service.UserService;
import laptopRentage.rentages.userDto.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/hello")
public class UserController {


   private final UserService userService;


   private final JwtUtils jwtUtils;

    @PostMapping(value = "/sign-up", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
//    @Operation(summary = "Creates a user account in the system")
    public ResponseEntity<?> userAccountCreationHandler(@RequestBody UserSignUpDto userSignUpDto) {
        log.info(userSignUpDto.toString());
        return userService.createAccount(userSignUpDto);
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
   // @Operation(summary = "Endpoint to authenticate users credentials")
    public ResponseEntity<?> userLoginHandler(
            @RequestBody UserLoginRequestDto userLoginRequestDto) {
        log.info("User successfully log in");
        return userService.login(userLoginRequestDto);
    }

    @PostMapping(value = "/verify-otp", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
   // @Operation(summary = "verifies OTP and returns JWT corresponding to the user")
    public ResponseEntity<UserLoginResponseDto> otpVerificationHandler(
            @RequestBody OtpVerificationDto otpVerificationRequestDto) {
        log.info("User successfully verify");
        return userService.verifyOtp(otpVerificationRequestDto);
    }

    @PutMapping(value = "/refresh-token", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    //@Operation(summary = "Returns new access_token")
    public ResponseEntity<?> tokenRefresherHandler(
            @RequestBody TokenRefreshDto tokenRefreshRequestDto) {
        return userService.refreshToken(tokenRefreshRequestDto);
    }

    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> loggedInUserDetailsRetrievalHandler(
            @RequestHeader( name = "Authorization") String header) {
        return userService.getDetails(jwtUtils.extractUserId(header));
    }

    @DeleteMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> userAccountDeletionHandler(@RequestHeader( name = "Authorization")
                                                             String header) {
        return userService.deleteAccount(jwtUtils.extractUserId(header));
    }
}
