package ecommerce_baceknd.config;
import ecommerce_baceknd.user.Dto.RegisterRequest;
import ecommerce_baceknd.user.Dto.UserResponse;
import ecommerce_baceknd.user.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity toEntity(RegisterRequest request);

    UserResponse toResponse(UserEntity user);
}
