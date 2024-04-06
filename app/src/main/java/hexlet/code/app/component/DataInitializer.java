package hexlet.code.app.component;

import hexlet.code.app.dto.UserCeateDTO;
import hexlet.code.app.mapper.UserMapper;
import hexlet.code.app.repository.UserRepository;
import lombok.AllArgsConstructor;
import net.datafaker.Faker;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {
    private final Faker faker;

    private final UserRepository userRepository;
    private UserMapper userMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var userData = new UserCeateDTO();
        userData.setEmail("hexlet@example.com");
        userData.setPassword("qwerty");
        var user = userMapper.map(userData);
        userRepository.save(user);
    }
}
