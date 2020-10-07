package capstone.backend.api.entity.ApiResponse;

import capstone.backend.api.entity.Cycle;
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

    private int progress;

    private boolean isRootObjective;

    private String title;

    private  String content;

    private long userId;

    private long parentObjectiveId;

    private CycleResponse cycle;

    private List<KeyResultResponse> keyResults;

}
