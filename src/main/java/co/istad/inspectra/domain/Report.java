package co.istad.inspectra.domain;

import co.istad.inspectra.config.jpa.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reports")
@Setter
@Getter
@NoArgsConstructor

public class Report extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100,nullable = false)
    private String uuid;

    private String message;

    @ManyToOne
    @JoinColumn(name = "blog_id",nullable = false)
    private Blog blog;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;


}
