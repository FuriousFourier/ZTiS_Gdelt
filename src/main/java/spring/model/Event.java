package spring.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
public class Event {

    @Id
    @GeneratedValue
    private long id;

    @Column
    private LocalDate date;

    @Column
    private long count;

    @Column
    private int articlesNumber;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "event_type_id")
    private EventType eventType;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "source_event",
            joinColumns = @JoinColumn(name = "source_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private Set<Source> sources;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "person_event",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private Set<Person> persons;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "location_event",
            joinColumns = @JoinColumn(name = "location_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private Set<Location> locations;

    public Event(LocalDate date, long count, int articlesNumber, Organization organization, EventType eventType, Set<Source> sources, Set<Person> persons, Set<Location> locations) {
        this.date = date;
        this.count = count;
        this.articlesNumber = articlesNumber;
        this.organization = organization;
        this.eventType = eventType;
        this.sources = sources;
        this.persons = persons;
        this.locations = locations;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public int getArticlesNumber() {
        return articlesNumber;
    }

    public void setArticlesNumber(int articlesNumber) {
        this.articlesNumber = articlesNumber;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Set<Source> getSources() {
        return sources;
    }

    public void setSources(Set<Source> sources) {
        this.sources = sources;
    }

    public Set<Person> getPersons() {
        return persons;
    }

    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }
}
