package capstone.backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ObjectvieDto {

    private long id;

    private int progress;

    private boolean isRootObjective;
    @NotNull
    private String title;
    @NotNull
    private String content;
    @NotNull
    private long userId;

    private long parentObjectiveId;

    private long cycleId;

    private List<KeyResultDto> keyResults;
}
