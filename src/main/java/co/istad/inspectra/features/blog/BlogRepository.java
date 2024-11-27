package co.istad.inspectra.features.blog;

import co.istad.inspectra.domain.Blog;
import co.istad.inspectra.domain.LikeBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog,Long> {

   Optional<Blog> findByUuid(String uuid);

   List<Blog> findByUserUuid(String userUuid);

   Optional<Blog> findByUuidAndUserUuid(String blogUuid, String userUuid);

   Page<Blog> findAllByIsVerified(Boolean isVerified, PageRequest pageRequest);


}
