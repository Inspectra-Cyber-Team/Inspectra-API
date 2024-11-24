package co.istad.inspectra.features.userlikeblog;

import co.istad.inspectra.domain.LikeBlog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserLikeBlogRepository extends JpaRepository<LikeBlog,Long> {

    List<LikeBlog> findByBlogUuid(String blogUuid);

    //find user if user already like blog
    Optional<LikeBlog> findByBlogUuidAndUserUuid(String blogUuid, String userUuid);



}
