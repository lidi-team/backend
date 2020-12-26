package capstone.backend.api.entity.ApiResponse.Objective;

import capstone.backend.api.entity.KeyResult;
import capstone.backend.api.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChildObjectiveResponse {
    private long id;
    private String title;
    private String type;
    private double progress;
    private double changing;
    private long cycleId;
    private long parentObjectiveId;
    private ArrayList<KeyResultOfChildObjective> keyResults;
    private ArrayList<Long> alignmentObjectives;
    private AuthorOfChildObjective author;

    public String setObjectiveType(int type, String projectName,User user) {
        // type = 0: objective company
        if (type == 0) {
            return "Mục tiêu công ty";
            // type = 2: objective personal
        } else if (type == 2) {
            return "Mục tiêu cá nhân của "+ user.getFullName();
        } else {
            return "Mục tiêu của " + projectName;
        }
    }

    public AuthorOfChildObjective authorOfChildObjective(User user) {
        return AuthorOfChildObjective.builder()
                .id(user.getId())
                .fullName(user.getFullName()).build();
    }

    public ArrayList<KeyResultOfChildObjective> keyResultOfChildObjectives(ArrayList<KeyResult> keyResults) {
        ArrayList<KeyResultOfChildObjective> responses = new ArrayList<>();
        keyResults.forEach(keyResult -> {
            responses.add(
                    KeyResultOfChildObjective.builder()
                            .id(keyResult.getId())
                            .startValue(keyResult.getFromValue())
                            .valueObtained(keyResult.getValueObtained())
                            .targetValue(keyResult.getToValue())
                            .content(keyResult.getContent())
                            .reference(keyResult.getReference())
                            .measureUnitName(keyResult.getUnitOfKeyResult() == null ? " "
                                    : keyResult.getUnitOfKeyResult().getName())
                            .build()
            );
        });
        return responses;
    }
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class KeyResultOfChildObjective {
    private long id;
    private double startValue;
    private double valueObtained;
    private double targetValue;
    private String content;
    private String reference;
    private String measureUnitName;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class AuthorOfChildObjective {
    long id;
    String fullName;
}