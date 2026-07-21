package ecommerce_baceknd.user;

import ecommerce_baceknd.common.ApiResponse;
import ecommerce_baceknd.user.Dto.*;
import ecommerce_baceknd.user.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("Received user registration request for email: {}", registerRequest.getEmail());
        UserResponse userResponse  = userService.userRegister(registerRequest);
        ApiResponse<UserResponse> response  = new ApiResponse<>(
                true,
                "User registered successfully",
                userResponse
        );
        log.info("Received user registration request for email: {}", registerRequest.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest) {

        log.info("Login request received for email: {}", loginRequest.getEmail());

        LoginResponse loginResponse = userService.login(loginRequest);

        log.info("User logged in successfully for email: {}", loginRequest.getEmail());

        ApiResponse<LoginResponse> response = new ApiResponse<>(
                true,
                "Login successful",
                loginResponse
        );

        return ResponseEntity.ok(response);
    }
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<String>> profile() {

        ApiResponse<String> response = new ApiResponse<>(
                true,
                "Protected API accessed successfully",
                "Welcome to your profile"
        );

        return ResponseEntity.ok(response);
    }
}