package capstone.backend.api.entity.ApiResponse.Objective;

import capstone.backend.api.entity.ApiResponse.KeyResultResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ObjectiveResponse {

    private long id;
    private String title;
    private long userId;
    private long projectId;
    private long parentId;
    private int type;
    private double weight;
    private long cycleId;
    private double changing;
    private double progress;
    private String status;
    private List<Long> alignmentObjectives;
    private List<KeyResultResponse> keyResults;

}
