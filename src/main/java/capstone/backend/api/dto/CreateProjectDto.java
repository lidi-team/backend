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
    private long id;
    private String name;
    private long pmId;
    private long parentId;
    private String startDate;
    private String endDate;
    private String description;
    private boolean active;
    private int weight;
}
