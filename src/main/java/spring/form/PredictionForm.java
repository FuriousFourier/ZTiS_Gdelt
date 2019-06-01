package spring.form;

public class PredictionForm {

    private String formDate = "01/01/2019";

    private String formEventType = "";

    private String formSource = "";

    private String formSourceUrl = "";

    private String formCountry = "";

    private String formCity = "";

    private String formOrganization = "";

    private String formPerson = "";

    public String getFormDate() {
        return formDate;
    }

    public void setFormDate(String formDate) {
        this.formDate = formDate;
    }

    public String getFormSourceUrl() {
        return formSourceUrl;
    }

    public void setFormSourceUrl(String formSourceUrl) {
        this.formSourceUrl = formSourceUrl;
    }

    public String getFormCity() {
        return formCity;
    }

    public void setFormCity(String formCity) {
        this.formCity = formCity;
    }

    public String getFormEventType() {
        return formEventType;
    }

    public void setFormEventType(String formEventType) {
        this.formEventType = formEventType;
    }

    public String getFormSource() {
        return formSource;
    }

    public void setFormSource(String formSource) {
        this.formSource = formSource;
    }

    public String getFormCountry() {
        return formCountry;
    }

    public void setFormCountry(String formCountry) {
        this.formCountry = formCountry;
    }

    public String getFormOrganization() {
        return formOrganization;
    }

    public void setFormOrganization(String formOrganization) {
        this.formOrganization = formOrganization;
    }

    public String getFormPerson() {
        return formPerson;
    }

    public void setFormPerson(String formPerson) {
        this.formPerson = formPerson;
    }

    @Override
    public String toString() {
        return "PredictionForm{" +
                "formDate='" + formDate + '\'' +
                ", formEventType='" + formEventType + '\'' +
                ", formSource='" + formSource + '\'' +
                ", formSourceUrl='" + formSourceUrl + '\'' +
                ", formCountry='" + formCountry + '\'' +
                ", formCity='" + formCity + '\'' +
                ", formOrganization='" + formOrganization + '\'' +
                ", formPerson='" + formPerson + '\'' +
                '}';
    }

}
