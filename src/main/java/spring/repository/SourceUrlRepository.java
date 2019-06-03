package spring.repository;

import org.springframework.data.repository.CrudRepository;
import spring.model.Source;
import spring.model.SourceUrl;

import java.util.Optional;
import java.util.Set;

public interface SourceUrlRepository extends CrudRepository<SourceUrl, Long> {

    Optional<SourceUrl> findByExactUrl(String exactUrl);
}
