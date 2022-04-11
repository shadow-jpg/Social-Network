package example.greetings.interfaces;

import example.greetings.Models.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepo  extends CrudRepository<Message, Long> {
    List<Message> findByTag(String tag);

    @Override
    Optional<Message> findById(Long id);

    @Override
    void deleteById(Long id);
}
