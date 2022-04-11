package example.greetings.interfaces;

import example.greetings.Models.ChatMessage;
import example.greetings.Models.Message;
import example.greetings.Models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChatMessageRepo extends CrudRepository<ChatMessage, Long> {
    List<Message> findBySender(User user);
}
