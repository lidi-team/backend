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
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "objectiveId")
    private Objective objective;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @NonNull
    private String content;

    @NonNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Builder.Default()
    private Date createAt = new Date();

    private String history;

    @Builder.Default
    private boolean isDelete = false;
}
