package capstone.backend.api.entity.ApiResponse.Project;

import capstone.backend.api.entity.ApiResponse.Objective.ObjectiveCheckin;
import capstone.backend.api.entity.ApiResponse.Objective.ObjectiveResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectObjectiveResponse {
    private long id;
    private String name;
    private List<ObjectiveCheckin> objectives;
}

