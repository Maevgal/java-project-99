package hexlet.code.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCreateDTO {
    private Integer index;
    @JsonProperty("assignee_id")
    private Long assigneeId;
    @NotBlank
    private String title;
    private String content;
    private String status;
}
