package co.istad.inspectra.domain;

import co.istad.inspectra.config.jpa.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Table(name = "authorities")
@Entity


public class Authority extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String uuid;

    @Column(nullable = false, length = 50)
    private String authorityName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToMany(mappedBy = "authorities")
    private List<User> users;
}
