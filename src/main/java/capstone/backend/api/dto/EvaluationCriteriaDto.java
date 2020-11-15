package capstone.backend.api.dto;

import lombok.*;

import javax.persistence.Entity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationCriteriaDto {
    private String content;
    private String type;
    private int numberOfStar;
}
