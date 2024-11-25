package co.istad.inspectra.domain;

import co.istad.inspectra.config.jpa.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "documents")
@Setter
@Getter
@NoArgsConstructor


public class Document extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String uuid;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private DocumentCategory category;


//    //relationships with keywords
//    @ManyToMany
//    @JoinTable(
//            name = "document_keywords",
//            joinColumns = @JoinColumn(name = "document_id"),
//            inverseJoinColumns = @JoinColumn(name = "keyword_id")
//    )
//    private List<Keyword> keywords;


    //relationships with images
    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocumentImages> images;

}
