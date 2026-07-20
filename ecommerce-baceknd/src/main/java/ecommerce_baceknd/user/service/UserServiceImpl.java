package ecommerce_baceknd.user.service;
import ecommerce_baceknd.config.UserMapper;
import ecommerce_baceknd.exception.exceptionname.ApplicationException;
import ecommerce_baceknd.securtiy.JwtService;
import ecommerce_baceknd.user.Dto.LoginRequest;
import ecommerce_baceknd.user.Dto.LoginResponse;
import ecommerce_baceknd.user.Dto.RegisterRequest;
import ecommerce_baceknd.user.Dto.UserResponse;
import ecommerce_baceknd.user.UserEntity;
import ecommerce_baceknd.user.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    @Override
    public UserResponse userRegister(RegisterRequest request) {
        log.info("User registration started for email: {}", request.getEmail());
        String email = request.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            log.warn("Email already registered: {}", request.getEmail());
            throw new ApplicationException(
                    HttpStatus.CONFLICT,
                    "Email already registered",
                    "USER_EMAIL_ALREADY_EXISTS"
            );
        }

        if (request.getPhone() != null && userRepository.existsByPhone(request.getPhone())) {
            throw new ApplicationException(HttpStatus.CONFLICT,
                    "Phone number already registered",
                    "USER_PHONE_ALREADY_EXISTS"
            );
        }

        UserEntity user = userMapper.toEntity(request);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        UserEntity savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());
        return userMapper.toResponse(savedUser);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail()
                .trim()
                .toLowerCase();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApplicationException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid email or password",
                        "INVALID_CREDENTIALS"
                ));

        if (!passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPassword())) {

            throw new ApplicationException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password",
                    "INVALID_CREDENTIALS"
            );
        }

        String accessToken = jwtService.generateToken(user.getEmail());

        return new LoginResponse(accessToken, "Bearer");
    }

}
