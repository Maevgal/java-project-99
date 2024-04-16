package hexlet.code.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class TaskUpdateDTO {
    @NotBlank
    private JsonNullable<String> title;

    private JsonNullable<String> content;
}
