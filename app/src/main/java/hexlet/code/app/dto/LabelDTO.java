package hexlet.code.app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LabelDTO {
    private Long id;
    private String name;
    private String slug;
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

}
