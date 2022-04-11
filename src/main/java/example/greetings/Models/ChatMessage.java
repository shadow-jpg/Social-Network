package example.greetings.Models;

import example.greetings.interfaces.UserRepo;

import javax.persistence.*;


@Entity
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private MessageType type;
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name= "user_id")
    private User sender;

    public ChatMessage() {
    }

    public ChatMessage(String content, User sender) {

        this.content = content;
        this.sender = sender;
    }

        public ChatMessage(UserRepo u, MessageType type, String sender) {
        this.type = type;
        User user =u.findByUsername(sender);
        this.sender = user ;
    }

    public ChatMessage(MessageType type, User sender) {
        this.type = type;
        this.sender = sender;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }


    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;

    }

    public String getSender() {
        return sender.getUsername();

    }

    public void setSender(User sender) {
        this.sender = sender;
    }

}
