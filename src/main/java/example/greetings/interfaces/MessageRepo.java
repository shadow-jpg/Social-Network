package example.greetings.interfaces;

import example.greetings.Models.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MessageRepo  extends CrudRepository<Message, Long> {
    Page<Message> findByTag(String tag, Pageable pageable );

    Page<Message> findAll(Pageable pageable );

    @Override
    Optional<Message> findById(Long id);

    @Override
    void deleteById(Long id);
}
