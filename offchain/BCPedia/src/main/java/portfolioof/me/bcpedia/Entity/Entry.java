package portfolioof.me.bcpedia.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Entry {
    @Id
    private String id;
    private String category;
    private String keyword;
    private String content;
    private String hash;
    private Long likes;

    public Entry() {
    }

    public Entry(String id, String category, String keyword, String content, String hash, Long likes) {
        this.id = id;
        this.category = category;
        this.keyword = keyword;
        this.content = content;
        this.hash = hash;
        this.likes = likes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }
}
