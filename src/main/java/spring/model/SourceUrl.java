package spring.model;

import javax.persistence.*;

@Entity
public class SourceUrl {

    @Id
    @GeneratedValue
    private long id;

    @Column
    private String exactUrl;

    @ManyToOne
    @JoinColumn(name = "source_id")
    private Source source;

    public SourceUrl(String exactUrl, Source source) {
        this.exactUrl = exactUrl;
        this.source = source;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getExactUrl() {
        return exactUrl;
    }

    public void setExactUrl(String exactUrl) {
        this.exactUrl = exactUrl;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }
}
