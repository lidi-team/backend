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
public class ProjectListResponse {
    private long id;
    private String name;
    private Date fromDate;
    private Date toDate;
}
