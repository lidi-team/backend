package capstone.backend.api.entity.ApiResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CycleResponse {

    private long id;

    private String name;

    private Date startDate;

    private Date endDate;
}
