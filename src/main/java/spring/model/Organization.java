package spring.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Organization {

    @Id
    @GeneratedValue
    private long id;

    @Column
    private String name;

    @ManyToMany(mappedBy = "organizations")
    private Set<Event> events;

    public Organization(){}

    public Organization(String name) {
        this.name = name;
        this.events = new HashSet<>();
    }

    public Organization(String name, Set<Event> events) {
        this.name = name;
        this.events = events;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        Organization that = (Organization) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
