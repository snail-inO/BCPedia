package portfolioof.me.bcpedia.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portfolioof.me.bcpedia.DAO.LikesDAO;
import portfolioof.me.bcpedia.DAO.UserDAO;
import portfolioof.me.bcpedia.Entity.Likes;
import portfolioof.me.bcpedia.Entity.User;
import portfolioof.me.bcpedia.Service.TokensServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/like")
public class LikeController {
    private final LikesDAO likesDAO;
    private final UserDAO userDAO;

    public LikeController(LikesDAO likesDAO, UserDAO userDAO) {
        this.likesDAO = likesDAO;
        this.userDAO = userDAO;
    }

    @PostMapping("/init")
    public ResponseEntity<?> init(@RequestParam String addr, @RequestParam String account, @RequestParam String key) {
        TokensServiceImpl.init(addr, account, key);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> submitLike(@PathVariable(name = "id") String id, @RequestParam String uid) throws Exception {
        if (userDAO.findById(uid).isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        int register = Integer.parseInt(TokensServiceImpl.balanceOfBCPC(uid));
        if (register == 0) {
            return ResponseEntity.badRequest().build();
        }
        Likes likes = likesDAO.findById(id).orElseThrow();
        likes.setCounts(likes.getCounts() + 1);
        likesDAO.save(likes);
        userDAO.save(new User(uid, id));

        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> createLike(@RequestBody Likes likes) {
        likes.setCounts(0L);
        likes.setDate(null);
        return ResponseEntity.ok(likesDAO.save(likes));
    }

    @GetMapping
    public List<Likes> retrieveAll() {
        return likesDAO.findAll();
    }
}
