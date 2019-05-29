package spring.repository;

import org.springframework.data.repository.CrudRepository;
import spring.model.Source;

public interface SourceRepository extends CrudRepository<Source, Long> {
}
