package capstone.backend.api.entity.ApiResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    @NotNull
    private int code;
    @NotNull
    private String message;

    private T data;

}
