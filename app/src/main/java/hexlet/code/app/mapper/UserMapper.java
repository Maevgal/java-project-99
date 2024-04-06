package hexlet.code.app.mapper;

import hexlet.code.app.dto.UserCeateDTO;
import hexlet.code.app.dto.UserDTO;
import hexlet.code.app.dto.UserUpdateDTO;
import hexlet.code.app.model.User;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeMapping
    public void encryptPasswordCreate(UserCeateDTO data) {
        var password = data.getPassword();
        data.setPassword(passwordEncoder.encode(password));
    }

    @BeforeMapping
    public void encryptPasswordUpdate(UserUpdateDTO data) {
        var password = data.getPassword();
        if (password != null && password.isPresent()) {
            data.setPassword(JsonNullable.of(passwordEncoder.encode(password.get())));
        }
    }

    @Mapping(source = "password", target = "passwordDigest")
    public abstract User map(UserCeateDTO dto);

    public abstract UserDTO map(User model);

    @Mapping(source = "password", target = "passwordDigest")
    public void update(UserUpdateDTO dto, @MappingTarget User model) {
        if (dto.getFirstName().isPresent()) {
            model.setFirstName(dto.getFirstName().get());
        }
        if (dto.getLastName().isPresent()) {
            model.setLastName(dto.getLastName().get());
        }
        if (dto.getEmail().isPresent()) {
            model.setEmail(dto.getEmail().get());
        }
        if (dto.getPassword().isPresent()) {
            model.setPasswordDigest(passwordEncoder.encode(dto.getPassword().get()));
        }
    }

    ;

}
