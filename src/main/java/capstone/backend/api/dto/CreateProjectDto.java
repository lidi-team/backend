package capstone.backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProjectDto {
    private String name;
    private long pmId;
    private long parentProjectId;
    private String startDate;
    private String endDate;
    private String description;
}
