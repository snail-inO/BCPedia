package portfolioof.me.bcpedia.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolioof.me.bcpedia.Entity.User;

public interface UserDAO extends JpaRepository<User, String> {
}
