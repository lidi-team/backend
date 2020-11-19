package capstone.backend.api.entity.ApiResponse.Report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chart {
    private double progress;
    private Date checkinAt;
}
