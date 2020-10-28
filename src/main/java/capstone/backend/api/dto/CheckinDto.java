package capstone.backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckinDto {
    private long id;
    private long objectiveId;
    private Date nextCheckinDate;
    private String status;
    private List<CheckinDetailDto> checkinDetails;
}
