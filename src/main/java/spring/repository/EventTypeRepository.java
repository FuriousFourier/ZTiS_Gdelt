package spring.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import spring.model.EventType;

import java.util.List;

public interface EventTypeRepository extends CrudRepository<EventType, Long> {

    @Query(value = "select distinct name from event_type", nativeQuery = true)
    List<String> getAllTypes();

}
