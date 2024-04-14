package hexlet.code.app.component;

import hexlet.code.app.dto.TaskStatusCreateDTO;
import hexlet.code.app.dto.UserCeateDTO;
import hexlet.code.app.mapper.TaskStatusMapper;
import hexlet.code.app.mapper.UserMapper;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {
    //private final Faker faker;

    private final UserRepository userRepository;
    private UserMapper userMapper;
    private final TaskStatusMapper taskStatusMapper;
    private final TaskStatusRepository taskStatusRepository;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        User defaultUser = userRepository.findByEmail("hexlet@example.com").orElse(null);
        if (defaultUser != null) {
            return;
        }
        var userData = new UserCeateDTO();
        userData.setEmail("hexlet@example.com");
        userData.setPassword("qwerty");
        var user = userMapper.map(userData);
        userRepository.save(user);

        var taskStatusCreateDTO = new TaskStatusCreateDTO();
        taskStatusCreateDTO.setName("Draft");
        taskStatusCreateDTO.setSlug("draft");
        var taskStatus1 = taskStatusMapper.map(taskStatusCreateDTO);
        taskStatusRepository.save(taskStatus1);

        taskStatusCreateDTO.setName("ToReview");
        taskStatusCreateDTO.setSlug("to_review");
        var taskStatus2 = taskStatusMapper.map(taskStatusCreateDTO);
        taskStatusRepository.save(taskStatus2);

        taskStatusCreateDTO.setName("ToBeFixed");
        taskStatusCreateDTO.setSlug("to_be_fixed");
        var taskStatus3 = taskStatusMapper.map(taskStatusCreateDTO);
        taskStatusRepository.save(taskStatus3);

        taskStatusCreateDTO.setName("ToPublish");
        taskStatusCreateDTO.setSlug("to_publish");
        var taskStatus4 = taskStatusMapper.map(taskStatusCreateDTO);
        taskStatusRepository.save(taskStatus4);

        taskStatusCreateDTO.setName("Published");
        taskStatusCreateDTO.setSlug("published");
        var taskStatus5 = taskStatusMapper.map(taskStatusCreateDTO);
        taskStatusRepository.save(taskStatus5);
    }
}
