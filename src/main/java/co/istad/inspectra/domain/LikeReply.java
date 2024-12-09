package co.istad.inspectra.domain;

import co.istad.inspectra.config.jpa.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "like_replies")
@Setter
@Getter
@NoArgsConstructor


public class LikeReply extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", nullable = false)
    private String uuid;

    @ManyToOne
    @JoinColumn(name = "reply_id", nullable = false)
    private Reply reply;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


}
