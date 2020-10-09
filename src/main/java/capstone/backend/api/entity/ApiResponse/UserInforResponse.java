package capstone.backend.api.entity.ApiResponse;

import capstone.backend.api.entity.Execute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInforResponse {

    private long id;

    private String fullName;

    private String email;

    private Set<String> roles;

    private String avatarUrl;

    private Date dob;

    private int gender;

    private int point;

    private DepartmentResponse department;

    private ArrayList<ProjectResponse> projects;

    public DepartmentResponse departmentResponse(long id, String name) {
        return new DepartmentResponse(id, name);
    }

    public ArrayList<ProjectResponse> projectResponses(ArrayList<Execute> executes) {
        ArrayList<ProjectResponse> projectResponses = new ArrayList<>();
        if(executes == null || executes.size() == 0){
            return null;
        }
        executes.forEach(execute -> {
            projectResponses.add(ProjectResponse.builder()
                    .id(execute.getProject().getId())
                    .name(execute.getProject().getName())
                    .isPm(execute.isPm()).build());
        });
        return projectResponses;
    }
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class DepartmentResponse {
    private long id;
    private String name;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class ProjectResponse {
    private long id;
    private String name;
    private boolean isPm;
}