package OreMowaiye.SpringBoot.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class FilmTitles {


    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String titleId;
    private int ordering;
    private String title;
    private String region;
    private String language;
    private String types;
    private String attributes;
    private boolean isOriginalTitle;
    private String imdbUrl;



    public FilmTitles() {}


    public FilmTitles(String titleId, int ordering, String title, String region, String language, String types, String attributes, boolean isOriginalTitle) {
        this.titleId = titleId;
        this.ordering = ordering;
        this.title = title;
        this.region = region;
        this.language = language;
        this.types = types;
        this.attributes = attributes;
        this.isOriginalTitle = isOriginalTitle;
        this.imdbUrl = imdbUrl;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public int getOrdering() {
        return ordering;
    }

    public void setOrdering(int ordering) {
        this.ordering = ordering;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public boolean isOriginalTitle() {
        return isOriginalTitle;
    }

    public void setOriginalTitle(boolean isOriginalTitle) {
        this.isOriginalTitle = isOriginalTitle;
    }


    public String getImdbUrl() {
        return "https://www.imdb.com/title/" + titleId + "/";
    }

    @Override
    public String toString() {
        return String.format("Film[id='%s' ,titleId='%s', title='%s', ordering='%d']", titleId, title, ordering);
    }

}
