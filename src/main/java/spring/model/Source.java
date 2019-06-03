package spring.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Source {

    @Id
    @GeneratedValue
    private long id;

    @Column
    private String baseUrl;

    @ManyToMany(mappedBy = "sources")
    private Set<Event> events;

    @OneToMany(mappedBy = "source")
    private Set<SourceUrl> sourceUrls;

    public Source() {
    }

    public Source(String baseUrl) {
        this.baseUrl = baseUrl;
        this.events = new HashSet<>();
        this.sourceUrls = new HashSet<>();
    }

    public Source(String baseUrl, Set<Event> events, Set<SourceUrl> sourceUrls) {
        this.baseUrl = baseUrl;
        this.events = events;
        this.sourceUrls = sourceUrls;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public Set<SourceUrl> getSourceUrls() {
        return sourceUrls;
    }

    public void setSourceUrls(Set<SourceUrl> sourceUrls) {
        this.sourceUrls = sourceUrls;
    }
}
