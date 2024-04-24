package hexlet.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        properties = {
                "spring.jpa.defer-datasource-initialization=false",
                "spring.sql.init.mode=never"
        }
)
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper om;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelGenerator modelGenerator;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;

    @BeforeEach
    public void beforeEach() {
        testUser = Instancio.of(modelGenerator.getUserModel())
                .create();
        userRepository.save(testUser);
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/api/users")
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser)))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {

        userRepository.save(testUser);

        var request = get("/api/users/{id}", testUser.getId())
                .with(SecurityMockMvcRequestPostProcessors.user(testUser));
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("firstName").isEqualTo(testUser.getFirstName()),
                v -> v.node("lastName").isEqualTo(testUser.getLastName()),
                v -> v.node("email").isEqualTo(testUser.getEmail())
        );
    }

    @Test
    public void testCreate() throws Exception {
        var testUserCreateDTO = Instancio.of(modelGenerator.getUserCreateDTOModel()).create();

        var request = post("/api/users").with(SecurityMockMvcRequestPostProcessors.user(testUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(testUserCreateDTO));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var user = userRepository.findByEmail(testUserCreateDTO.getEmail())
                .orElseThrow(() -> new AssertionError("User not found"));

        assertThat(user).isNotNull();
        assertThat(user.getFirstName()).isEqualTo(testUserCreateDTO.getFirstName());
        assertThat(user.getLastName()).isEqualTo(testUserCreateDTO.getLastName());
    }

    @Test
    public void testCreateWithWrongEmail() throws Exception {
        var dto = userMapper.map(testUser);
        dto.setEmail("123gmail.ru");

        var request = post("/api/users").with(SecurityMockMvcRequestPostProcessors.user(testUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdate() throws Exception {
        userRepository.save(testUser);

        var userUpdateDto = Instancio.of(modelGenerator.getUserUpdateDTOModel()).create();

        var request = put("/api/users/{id}", testUser.getId())
                .with(SecurityMockMvcRequestPostProcessors.user(testUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(userUpdateDto));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var user = userRepository.findById(testUser.getId())
                .orElseThrow(() -> new AssertionError("User not found"));

        assertThat(user.getFirstName()).isEqualTo(userUpdateDto.getFirstName().get());
        assertThat(user.getLastName()).isEqualTo(userUpdateDto.getLastName().get());
        assertThat(user.getEmail()).isEqualTo(userUpdateDto.getEmail().get());
        assertThat(passwordEncoder.matches(userUpdateDto.getPassword().get(), user.getPasswordDigest())).isTrue();
    }

    @Test
    public void testWithoutEmailUpdate() throws Exception {
        userRepository.save(testUser);

        var emailOriginal = testUser.getEmail();

        var userUpdateDto = Instancio.of(modelGenerator.getUserUpdateDTOModel()).create();
        userUpdateDto.setEmail(null);

        var request = put("/api/users/{id}", testUser.getId())
                .with(SecurityMockMvcRequestPostProcessors.user(testUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(userUpdateDto));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var user = userRepository.findById(testUser.getId())
                .orElseThrow(() -> new AssertionError("User not found"));

        assertThat(user.getEmail()).isEqualTo(emailOriginal);
        assertThat(user.getFirstName()).isEqualTo(userUpdateDto.getFirstName().get());
        assertThat(user.getLastName()).isEqualTo(userUpdateDto.getLastName().get());
        assertThat(passwordEncoder.matches(userUpdateDto.getPassword().get(), user.getPasswordDigest())).isTrue();
    }

    @Test
    public void testWithoutFirstNamedUpdate() throws Exception {
        userRepository.save(testUser);

        var firstNameOriginal = testUser.getFirstName();

        var userUpdateDto = Instancio.of(modelGenerator.getUserUpdateDTOModel()).create();
        userUpdateDto.setFirstName(null);

        var request = put("/api/users/{id}", testUser.getId())
                .with(SecurityMockMvcRequestPostProcessors.user(testUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(userUpdateDto));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var user = userRepository.findById(testUser.getId())
                .orElseThrow(() -> new AssertionError("User not found"));

        assertThat(user.getFirstName()).isEqualTo(firstNameOriginal);
        assertThat(user.getLastName()).isEqualTo(userUpdateDto.getLastName().get());
        assertThat(user.getEmail()).isEqualTo(userUpdateDto.getEmail().get());
        assertThat(passwordEncoder.matches(userUpdateDto.getPassword().get(), user.getPasswordDigest())).isTrue();
    }

    @Test
    public void testWithoutLastNameUpdate() throws Exception {
        userRepository.save(testUser);

        var lastNameOriginal = testUser.getLastName();

        var userUpdateDto = Instancio.of(modelGenerator.getUserUpdateDTOModel()).create();
        userUpdateDto.setLastName(null);

        var request = put("/api/users/{id}", testUser.getId())
                .with(SecurityMockMvcRequestPostProcessors.user(testUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(userUpdateDto));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var user = userRepository.findById(testUser.getId())
                .orElseThrow(() -> new AssertionError("User not found"));

        assertThat(user.getLastName()).isEqualTo(lastNameOriginal);
        assertThat(user.getFirstName()).isEqualTo(userUpdateDto.getFirstName().get());
        assertThat(user.getEmail()).isEqualTo(userUpdateDto.getEmail().get());
        assertThat(passwordEncoder.matches(userUpdateDto.getPassword().get(), user.getPasswordDigest())).isTrue();
    }

    //не работает
    @Test
    public void destroyTest() throws Exception {
        var request = delete("/api/users/{id}", testUser.getId())
                .with(SecurityMockMvcRequestPostProcessors.user(testUser));
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(userRepository.existsById(testUser.getId())).isEqualTo(false);
    }
}
