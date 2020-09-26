package capstone.backend.api.entity.ApiResponse;

import capstone.backend.api.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ObjectiveResponse {

    private long id;

    private String title;

    private  String content;

    private long userId;

    private List<KeyResultResponse> keyResults;

}
