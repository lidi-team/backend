package capstone.backend.api.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "key_results")
public class KeyResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    private String content;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "obj_id")
    private Objective objective;
}
