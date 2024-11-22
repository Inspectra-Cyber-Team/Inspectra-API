package co.istad.inspectra.domain;

import co.istad.inspectra.config.jpa.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "blog_images")
@Setter
@Getter
@NoArgsConstructor


public class BlogImages extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String uuid;

    private String thumbnail;

    @ManyToOne
    @JoinColumn(name = "blog_id", nullable = false)
    private Blog blog;


}
