package capstone.backend.api.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table( name = "KindOfKeyResult",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name")
        })
public class KindOfKeyResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    private String name;

    private String description;

    @Builder.Default
    private boolean isDelete = false;
}
