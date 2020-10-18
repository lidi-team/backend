package capstone.backend.api.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table( name = "Reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "authorizedUserId")
    private User authorizedUser;

    @NonNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date deadline;

    @NonNull
    private String content;

    // "DRAFT","SUBMITTED","REJECTED","APPROVED"
    @NonNull
    private String status;

    @NonNull
    private String attachmentURL;

    private String history;

    @NonNull
    private int confidentLevel;

    @Builder.Default
    private boolean isDelete = false;
}
