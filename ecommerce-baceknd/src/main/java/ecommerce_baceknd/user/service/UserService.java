package ecommerce_baceknd.user.service;

import ecommerce_baceknd.user.Dto.RegisterRequest;
import ecommerce_baceknd.user.Dto.UserResponse;
import ecommerce_baceknd.user.UserEntity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserService {

    public UserResponse userRegister(RegisterRequest registerRequest);
}
