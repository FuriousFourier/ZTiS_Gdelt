package spring.controller;

import java.util.List;

public class Select2Model {

    private List<Option> results;

    public Select2Model() {
    }

    public Select2Model(List<Option> results) {
        this.results = results;
    }

    public List<Option> getResults() {
        return results;
    }

    public void setResults(List<Option> results) {
        this.results = results;
    }

    public static class Option {
        private String id;
        private String text;

        public Option() {
        }

        public Option(String id, String text) {
            this.id = id;
            this.text = text;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

}
