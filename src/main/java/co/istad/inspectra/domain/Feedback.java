package co.istad.inspectra.domain;

import co.istad.inspectra.config.jpa.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "feedbacks")
@Getter
@Setter
@NoArgsConstructor

public class Feedback extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 100)
    private String uuid;

    @Column(nullable = false,length = 100)
    private String email;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
