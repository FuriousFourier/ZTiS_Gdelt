package spring.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import spring.model.Person;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends CrudRepository<Person, Long> {

    @Query(value = "select distinct name from person", nativeQuery = true)
    List<String> getAllPersons();

    Optional<Person> findByName(String name);
}
