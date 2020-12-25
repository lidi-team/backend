package capstone.backend.api.entity.ApiResponse.Project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDetailResponse {
    private long id;
    private String name;
    private Date startDate;
    private Date endDate;
    private boolean isActive;
    private String description;
    private Map<String,Object> pm;
    private int weight;
    private long parentId;
}
