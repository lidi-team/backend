package capstone.backend.api.entity.ApiResponse.Project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPagingResponse {
    private long id;
    private String name;
    private Date startDate;
    private Date endDate;
    private int status;
    private String description;
    private String pm;
    private int weight;
    private long parentId;
}
