package spring.controller;

public class ResultModel {

    private long events;

    private long articles;

    public ResultModel(long events, long articles) {
        this.events = 13 * events;
        this.articles = 13 * articles;
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
