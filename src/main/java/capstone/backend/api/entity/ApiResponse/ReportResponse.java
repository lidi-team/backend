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
public class ReportResponse {
    private long id;
    private String title;
    private Date checkinDate;
    private Date nextCheckinDate;
    private String status;
}
