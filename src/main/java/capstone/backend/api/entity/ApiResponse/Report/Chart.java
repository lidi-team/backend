package capstone.backend.api.entity.ApiResponse.Report;

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
public class Chart {
    private List<Double> progress;
    private List<Date> checkinAt;
}
