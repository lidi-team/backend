package capstone.backend.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tradings")
public class Trading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "giftId")
    private Gift gift;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    private int quantity;

    private int totalStar;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date tradingTime;

}
