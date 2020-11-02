package capstone.backend.api.entity.ApiResponse.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFailResponse {
    private int numberOfSuccess;
    private int numberOfFailed;
    private List<UserFailedItem> list;
}
