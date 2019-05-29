package spring.repository;

import org.springframework.data.repository.CrudRepository;
import spring.model.Organization;

public interface OrganizationRepository extends CrudRepository<Organization, Long> {
}
