package spring.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class EventType {

    @Id
    @GeneratedValue
    private long id;

    @Column
    private String name;

    @OneToMany(mappedBy = "eventType")
    private Set<Event> events;

    public EventType() {
    }

    public EventType(String name) {
        this.name = name;
        this.events = new HashSet<>();
    }

    public EventType(String name, Set<Event> events) {
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
        EventType eventType = (EventType) o;
        return name.equals(eventType.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
