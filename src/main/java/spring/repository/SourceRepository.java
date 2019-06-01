package spring.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import spring.model.Source;

import java.util.List;
import java.util.Optional;

public interface SourceRepository extends CrudRepository<Source, Long> {

    @Query(value = "select distinct base_url from source", nativeQuery = true)
    List<String> getAllSources();

    Optional<Source> findByBaseUrl(String baseUrl);

}
