package ru.solargateteam.galnetru.db;

import java.io.Serializable;

public class DBItem implements Serializable {

    private int    id;
    private String title;
    private String link;
    private String description;
    private String guid;
    private long   pubDate;
    private String imagePath;
    private int    newPost;

    public DBItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public long getPubDate() {
        return pubDate;
    }

    public void setPubDate(long pubDate) {
        this.pubDate = pubDate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getNewPost() {
        return newPost;
    }

    public void setNewPost(int newPost) {
        this.newPost = newPost;
    }
}
