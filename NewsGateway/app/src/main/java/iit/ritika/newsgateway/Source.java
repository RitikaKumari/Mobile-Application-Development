package iit.ritika.newsgateway;

import java.io.Serializable;

public class Source implements Serializable, Comparable<Source> {

    private String id;
    private String name;
    private String url;
    private String category;

    public Source() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int compareTo(Source other) {
        return name.compareTo(other.name);
    }


}
