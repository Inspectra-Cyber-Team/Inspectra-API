package co.istad.inspectra.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "anonymous_user")
@Setter
@Getter
@NoArgsConstructor

public class AnonymousUser {

    @Id
    private Long id;

    private Boolean hasGeneratedProjects;

}
