package capstone.backend.api.entity.ApiResponse.Report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestCheckin {
    private long id;
    private String fullName;
}
