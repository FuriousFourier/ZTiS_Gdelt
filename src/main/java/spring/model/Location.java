package spring.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Location {

    @Id
    @GeneratedValue
    private long id;

    @Column
    private String country;

    @Column
    private String city;

    @ManyToMany(mappedBy = "locations")
    private Set<Event> events;

    public Location() {
    }

    public Location(String country, String city) {
        this.country = country;
        this.city = city;
        this.events = new HashSet<>();
    }

    public Location(String country, String city, Set<Event> events) {
        this.country = country;
        this.city = city;
        this.events = events;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return country.equals(location.country) &&
                city.equals(location.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, city);
    }
}
