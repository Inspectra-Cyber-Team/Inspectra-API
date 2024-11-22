package co.istad.inspectra.domain;

import co.istad.inspectra.config.jpa.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "scan_histories")
@Getter
@Setter
@NoArgsConstructor

public class ScanHistory extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String uuid;

    @Column(nullable = false, length = 100)
    private String projectName;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
