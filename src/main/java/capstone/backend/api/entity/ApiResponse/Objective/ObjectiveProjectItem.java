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
public class ObjectiveProjectItem {
    private long id;
    private String title;
    private int type;
    private double weight;
    private double changing;
    private double progress;
    private List<KeyResultResponse> keyResults;
    private List<ObjectiveProjectItem> childObjectives;
}
