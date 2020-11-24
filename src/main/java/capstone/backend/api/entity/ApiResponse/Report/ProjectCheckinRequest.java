package capstone.backend.api.entity.ApiResponse.Report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectCheckinRequest {
    private long id;
    private String name;
    private List<Map<String,Object>> requests;
}
