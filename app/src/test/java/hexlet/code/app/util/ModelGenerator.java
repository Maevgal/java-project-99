package hexlet.code.app.util;

import hexlet.code.app.dto.UserCeateDTO;
import hexlet.code.app.dto.UserUpdateDTO;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ModelGenerator {
    private Model<User> userModel;
    private Model<UserCeateDTO> userCreateDTOModel;
    private Model<UserUpdateDTO> userUpdateDTOModel;
    private Model<TaskStatus> taskStatusModel;
    private Model<Task> taskModel;
    @Autowired
    private Faker faker;

    @PostConstruct
    private void init() {
        userModel = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .supply(Select.field(User::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .toModel();

        userCreateDTOModel = Instancio.of(UserCeateDTO.class)
                .supply(Select.field(UserCeateDTO::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(UserCeateDTO::getLastName), () -> faker.name().lastName())
                .supply(Select.field(UserCeateDTO::getEmail), () -> faker.internet().emailAddress())
                .supply(Select.field(UserCeateDTO::getPassword), () -> faker.internet().password(3, 9))
                .toModel();

        userUpdateDTOModel = Instancio.of(UserUpdateDTO.class)
                .supply(Select.field(UserUpdateDTO::getFirstName), () -> JsonNullable
                        .of(faker.name().firstName()))
                .supply(Select.field(UserUpdateDTO::getLastName), () -> JsonNullable
                        .of(faker.name().lastName()))
                .supply(Select.field(UserUpdateDTO::getEmail), () -> JsonNullable
                        .of(faker.internet().emailAddress()))
                .supply(Select.field(UserUpdateDTO::getPassword), () -> JsonNullable
                        .of(faker.internet().password(3, 9)))
                .toModel();

        taskStatusModel = Instancio.of(TaskStatus.class)
                .ignore(Select.field(TaskStatus::getId))
                .supply(Select.field(TaskStatus::getName), () -> faker.name().firstName())
                .supply(Select.field(TaskStatus::getSlug), () -> faker.name().title())
                .toModel();

        taskModel = Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .supply(Select.field(Task::getName), () -> faker.name().firstName())
                .supply(Select.field(Task::getIndex), () -> faker.number().positive())
                .supply(Select.field(Task::getDescription), () -> faker.text().text())
                .toModel();
    }

    public Model<User> getUserModel() {
        return userModel;
    }
}
