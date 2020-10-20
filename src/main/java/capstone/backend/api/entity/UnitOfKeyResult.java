package capstone.backend.api.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table( name = "UnitOfKeyResult",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name")
        })
public class UnitOfKeyResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    private String name;

    private String preset;

    private int measureIndex;

    @Builder.Default
    private boolean isDelete = false;
}
