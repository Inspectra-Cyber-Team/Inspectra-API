package co.istad.inspectra.domain;

import co.istad.inspectra.config.jpa.Auditable;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "projects")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder

public class Project extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String uuid;

    @Column(nullable = false, length = 100)
    private String projectName;


    private Boolean isUsed;
    private Boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
