package capstone.backend.api.entity.ApiResponse;

import capstone.backend.api.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersResponse {

    private int page;

    private int size;

    private int totalPage;

    private Page<User> users;
}
