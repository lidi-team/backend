package capstone.backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCfrDto {
    private String content;
    private long evaluationCriteriaId;
    private long objectiveId;
    private long checkinId;
    private long receiverId;
    private long senderId;
}
