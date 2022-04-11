package example.greetings.Models;

import javax.persistence.*;

import com.sun.istack.NotNull;
import example.greetings.Models.User;
import net.bytebuddy.implementation.bind.annotation.Default;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Set;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String tag;
    private String text;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name= "user_id")
    private User author;

    private String filename;


    // не пашет авто распределение 1
    @Value("${visibility}")
    @NotNull
    private boolean visibility;


    public  Message(){

    }

    public Message( String tag, String text,User user) {
        this.tag = tag;
        this.text = text;
        this.author =user;
    }

    public String getAuthorName(){
        return author !=null ?author.getUsername() : " <none> ";
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public String getTag() {
        return tag;
    }

    public String getText() {
        return text;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getFilename() {

        return filename;
    }

    public void setFilename(String filename) {

        this.filename = filename;
    }
}
