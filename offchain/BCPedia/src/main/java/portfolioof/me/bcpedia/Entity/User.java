package portfolioof.me.bcpedia.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Entity
public class User {
    @Id
    private String id;
    private String likeId;
    @CreatedDate
    private Date date;

    public User() {
    }

    public User(String id, String likeId) {
        this.id = id;
        this.likeId = likeId;
    }

    public User(String id, String likeId, Date date) {
        this.id = id;
        this.likeId = likeId;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLikeId() {
        return likeId;
    }

    public void setLikeId(String likeId) {
        this.likeId = likeId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
