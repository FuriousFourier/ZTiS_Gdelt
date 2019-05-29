package spring.repository;

import org.springframework.data.repository.CrudRepository;
import spring.model.EventType;

public interface EventTypeRepository extends CrudRepository<EventType, Long> {
}
