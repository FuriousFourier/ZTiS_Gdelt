package spring.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import spring.model.Location;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends CrudRepository<Location, Long> {
    public static final String UNKNOWN_CITY = "UNKNOWN CITY";

    @Query(value = "select distinct country from location", nativeQuery = true)
    List<String> getAllCountries();

    @Query(value = "select distinct city from location", nativeQuery = true)
    List<String> getAllCities();

    @Query(value = "select distinct city from location where country = :country", nativeQuery = true)
    List<String> getCitiesForCountry(@Param("country") String country);

    @Query(value = "select distinct city from location where country = :country AND city LIKE CONCAT(:search,'%')", nativeQuery = true)
    List<String> getCitiesForCountryWithSearch(@Param("country") String country, @Param("search") String search);

    Optional<Location> findByCountryAndCity(String country, String city);
}
