package capstone.backend.api.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "logs")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "toUserId")
    private User toUser;

    @NonNull
    private String content;

    private Date createAt;

    private String url;

    private String type;

    @Builder.Default
    private boolean isDelete = false;
}
