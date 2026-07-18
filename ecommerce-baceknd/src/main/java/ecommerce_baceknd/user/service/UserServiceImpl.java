package ecommerce_baceknd.user.service;

import ecommerce_baceknd.config.UserMapper;
import ecommerce_baceknd.exception.exceptionname.ApplicationException;
import ecommerce_baceknd.user.Dto.RegisterRequest;
import ecommerce_baceknd.user.Dto.UserResponse;
import ecommerce_baceknd.user.UserEntity;
import ecommerce_baceknd.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepo userRepository;

    @Override
    public UserResponse userRegister(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ApplicationException(
                    HttpStatus.CONFLICT,
                    "Email already registered",
                    "USER_EMAIL_ALREADY_EXISTS"
            );
        }

        if (request.getPhone() != null &&
                userRepository.existsByPhone(request.getPhone())) {

            throw new ApplicationException(
                    HttpStatus.CONFLICT,
                    "Phone number already registered",
                    "USER_PHONE_ALREADY_EXISTS"
            );
        }

        UserEntity user = userMapper.toEntity(request);

        UserEntity savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

}
