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
    private String title;
    private String content;
    private long userId;
    private long projectId;
    private long parentId;
    private int type;
    private int weight;
    private long cycleId;
    private int changing;
    private int progress;
    private String status;
    private List<Long> alignmentObjectives;
    private List<KeyResultDto> keyResults;
}
