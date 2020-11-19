package capstone.backend.api.entity.ApiResponse.Objective;

import capstone.backend.api.entity.ApiResponse.KeyResult.KeyResultResponse;
import capstone.backend.api.entity.ApiResponse.MetaDataResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ObjectiveDetailResponse {
    private long id;
    private String title;
    private double weight;
    private double progress;
    private MetaDataResponse user;
    private MetaDataResponse project;
    private MetaDataResponse parentObjective;
    private List<MetaDataResponse> childObjectives;
    private List<MetaDataResponse> alignmentObjectives;
    private List<KeyResultResponse> keyResults;

}
