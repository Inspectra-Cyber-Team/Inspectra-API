package co.istad.inspectra.domain.role;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String uuid;

    @Column(unique = true)
    @Enumerated(EnumType.STRING)

    private EnumRole roleName;


}
