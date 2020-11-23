package capstone.backend.api.entity.ApiResponse.Project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDetailResponse {
    private long id;
    private String name;
    private Date startDate;
    private Date endDate;
    private String status;
    private String description;
    private StaffInformation pm;
    private int weight;
    private List<StaffInformation> staffs;
}
