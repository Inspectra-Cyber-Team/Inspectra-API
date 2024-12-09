package co.istad.inspectra.domain;
import co.istad.inspectra.config.jpa.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "like_comments")
@Setter
@Getter
@NoArgsConstructor

public class LikeComment extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", nullable = false)
    private String uuid;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
