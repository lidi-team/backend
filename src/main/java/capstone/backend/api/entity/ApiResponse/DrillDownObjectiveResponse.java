package capstone.backend.api.entity.ApiResponse;

import capstone.backend.api.entity.ApiResponse.Objective.ChildObjectiveResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DrillDownObjectiveResponse {
    private String title;
    private List<ChildObjectiveResponse> childObjectives;
}


