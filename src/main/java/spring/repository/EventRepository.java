package spring.repository;

import org.springframework.data.repository.CrudRepository;
import spring.model.Event;

public interface EventRepository extends CrudRepository<Event, Long> {
}
