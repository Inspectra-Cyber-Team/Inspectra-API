package co.istad.inspectra.domain;

import co.istad.inspectra.config.jpa.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "keywords")
@Setter
@Getter
@NoArgsConstructor

public class Keyword extends Auditable {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String uuid;

    @Column(nullable = false, length = 100)
    private String keyword;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToMany(mappedBy = "keywords")
    private List<Document> documents;


}
