package iit.ritika.newsgateway;

import java.io.Serializable;

public class Article implements Serializable{
    private String authorName;
    private String newsTitle;
    private String newsDescription;
    private String articleUrl;
    private String image;
    private String publishedAt;


    public Article() {
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsDescription() {
        return newsDescription;
    }

    public void setNewsDescription(String newsDescription) {
        this.newsDescription = newsDescription;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public String getUrlToImage() {
        return image;
    }

    public void setUrlToImage(String urltoimage) {
        this.image = urltoimage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }
}
