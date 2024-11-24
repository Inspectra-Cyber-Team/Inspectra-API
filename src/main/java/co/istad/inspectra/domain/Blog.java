package co.istad.inspectra.domain;

import co.istad.inspectra.config.jpa.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "blogs")
@Setter
@Getter
@NoArgsConstructor


public class Blog extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String uuid;


    @Column(nullable = false, length = 100)
    private String title;

    private int viewsCount;

    private int likesCount;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //relation with blog_images
    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BlogImages> blogImages;

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replies ;

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikeBlog> likeBlogs;

    public int getTotalInteractions() {
        int commentsCount = (comments != null) ? comments.size() : 0;
        int repliesCount =  (comments != null) ? comments.stream().mapToInt(comment -> comment.getReplies().size()).sum() : 0;
        return commentsCount + repliesCount;
    }


}
