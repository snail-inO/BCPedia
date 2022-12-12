package portfolioof.me.bcpedia.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolioof.me.bcpedia.Entity.Proposal;

public interface ProposalDAO extends JpaRepository<Proposal, String> {
}
