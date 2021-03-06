package capstone.backend.api.entity.ApiResponse.Project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffInformation {
    private long id;
    private String name;
    private long position;
    private long department;
    private long reviewerId;
    private String email;
}
