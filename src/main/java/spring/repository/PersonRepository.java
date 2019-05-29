package spring.repository;

import org.springframework.data.repository.CrudRepository;
import spring.model.Person;

public interface PersonRepository extends CrudRepository<Person, Long> {
}
