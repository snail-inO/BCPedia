package portfolioof.me.bcpedia.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portfolioof.me.bcpedia.DAO.ProposalDAO;
import portfolioof.me.bcpedia.Entity.Proposal;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/proposal")
public class ProposalController {
    private final ProposalDAO proposalDAO;

    public ProposalController(ProposalDAO proposalDAO) {
        this.proposalDAO = proposalDAO;
    }

    @GetMapping
    public List<Proposal> retrieveAll() {
        return proposalDAO.findAll();
    }

    @GetMapping("/{pid}")
    public Proposal retrieve(@PathVariable(name = "pid") String pid) {
        return proposalDAO.findById(pid).orElseThrow();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Proposal proposal) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(proposal.getContent().getBytes(StandardCharsets.UTF_8));

        BigInteger number = new BigInteger(1, hash);

        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 64) {
            hexString.insert(0, '0');
        }
        hexString.insert(0, "0x");
        proposal.setHash(hexString.toString());
        proposal = proposalDAO.save(proposal);

        return ResponseEntity.ok(proposal);
    }

    @DeleteMapping("/{pid}")
    public ResponseEntity<?> delete(@PathVariable(name = "pid") String pid) {
        proposalDAO.deleteById(pid);
        return ResponseEntity.noContent().build();
    }
}
