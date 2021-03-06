package capstone.backend.api.entity.ApiResponse.Objective;

import capstone.backend.api.entity.ApiResponse.KeyResult.KeyResultResponse;
import capstone.backend.api.entity.ApiResponse.MetaDataResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ObjectiveCheckin {
    private long id;
    private double progress;
    private String title;
    private double change;
    private String status;
    private long checkinId;
    private List<Map<String,Object>> keyResults;
    private MetaDataResponse project;
}
