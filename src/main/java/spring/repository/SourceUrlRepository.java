package spring.repository;

import org.springframework.data.repository.CrudRepository;
import spring.model.SourceUrl;

public interface SourceUrlRepository extends CrudRepository<SourceUrl, Long> {
}
