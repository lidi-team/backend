package capstone.backend.api.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentDto {
    private long id;
    private String name;
    private String description;
}
