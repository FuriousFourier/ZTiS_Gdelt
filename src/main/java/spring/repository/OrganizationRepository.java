package spring.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import spring.model.Organization;

import java.util.List;

public interface OrganizationRepository extends CrudRepository<Organization, Long> {

    @Query(value = "select distinct name from organization", nativeQuery = true)
    List<String> getAllOrganizations();

}
