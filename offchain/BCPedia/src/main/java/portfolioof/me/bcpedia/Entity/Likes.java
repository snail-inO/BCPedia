package portfolioof.me.bcpedia.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;
import java.util.List;

@Entity
public class Likes {
    @Id
    private String id;
    private Long counts;
    @CreatedDate
    private Date date;

    public Likes() {
    }

    public Likes(String id, Long counts, Date date) {
        this.id = id;
        this.counts = counts;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCounts() {
        return counts;
    }

    public void setCounts(Long counts) {
        this.counts = counts;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
