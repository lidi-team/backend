package capstone.backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ObjectvieDto {

    private long id;
    private String title;
    private long projectId;
    private long parentId;
    private int type;
    private double weight;
    private long cycleId;
    private List<Long> alignmentObjectives;
    private List<KeyResultDto> keyResults;
}
