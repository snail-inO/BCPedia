package portfolioof.me.bcpedia.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portfolioof.me.bcpedia.DAO.EntryDAO;
import portfolioof.me.bcpedia.Entity.Entry;

import java.util.List;

@RestController
@RequestMapping("/entries")
public class EntryController {
    private final EntryDAO entryDAO;

    public EntryController(EntryDAO entryDAO) {
        this.entryDAO = entryDAO;
    }

    @GetMapping
    public List<Entry> retrieveAll() {
        return entryDAO.findAll();
    }

    @GetMapping("/{id}")
    public Entry retrieve(@PathVariable(name = "id") String id) {
        return entryDAO.findById(id).orElseThrow();
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody Entry entry) {
        return ResponseEntity.ok(entryDAO.save(entry));
    }

    @PatchMapping
    public ResponseEntity<?> update(@RequestBody Entry entry) {
        entryDAO.findById(entry.getId()).orElseThrow();

        return ResponseEntity.ok(entryDAO.save(entry));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") String id) {
        entryDAO.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
