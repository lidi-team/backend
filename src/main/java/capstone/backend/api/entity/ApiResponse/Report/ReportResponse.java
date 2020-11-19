package capstone.backend.api.entity.ApiResponse.Report;

import capstone.backend.api.entity.ApiResponse.MetaDataResponse;
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
    private Date checkinAt;
    private Date nextCheckinDate;
    private String status;
    private long teamLeaderId;
    private MetaDataResponse objective;
}
