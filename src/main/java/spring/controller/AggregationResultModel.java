package spring.controller;

public class AggregationResultModel {

    private long events;

    private long articles;

    public AggregationResultModel(long events, long articles) {
        this.events = events;
        this.articles = articles;
    }

    public long getEvents() {
        return events;
    }

    public void setEvents(long events) {
        this.events = events;
    }

    public long getArticles() {
        return articles;
    }

    public void setArticles(long articles) {
        this.articles = articles;
    }
}
