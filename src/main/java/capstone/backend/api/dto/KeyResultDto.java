package capstone.backend.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class KeyResultDto {

    private long id;

    @NotNull
    private String content;
}
