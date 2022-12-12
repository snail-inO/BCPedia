package portfolioof.me.bcpedia.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolioof.me.bcpedia.Entity.Entry;

public interface EntryDAO extends JpaRepository<Entry, String> {
}
