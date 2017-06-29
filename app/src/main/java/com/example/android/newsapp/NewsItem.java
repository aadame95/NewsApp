package com.example.android.newsapp;

/**
 * Created by Andres on 6/27/17.
 */

public class NewsItem {

    String author;
    String title;
    String description;
    String url;
    String urlToImage;
    String publishedAt;

    public NewsItem (String title, String description, String url, String publishedAt){
        this.title = title;
        this.description = description;
        this.url = url;
        this.publishedAt = publishedAt;

    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }
}