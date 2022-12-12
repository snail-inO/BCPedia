package portfolioof.me.bcpedia.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolioof.me.bcpedia.Entity.Likes;

public interface LikesDAO extends JpaRepository<Likes, String> {
}
