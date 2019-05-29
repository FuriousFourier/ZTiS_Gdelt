package spring.repository;

import org.springframework.data.repository.CrudRepository;
import spring.model.Location;

public interface LocationRepository extends CrudRepository<Location, Long> {
}
