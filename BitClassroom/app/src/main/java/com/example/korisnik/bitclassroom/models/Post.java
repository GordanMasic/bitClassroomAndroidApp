package com.example.korisnik.bitclassroom.models;

import java.util.UUID;

/**
 * This class represents one post on some course.
 */
public class Post {

    //Post properties
    private UUID id;
    private String name;
    private String content;
    private String timeStamp;
    private String dueDate;
    private String user;

    /**
     * Constructor for creating post object.
     * @param id - Post id.
     * @param name - Post name.
     * @param content - Post content.
     * @param timeStamp - Post time stamp.
     * @param dueDate - Post due date(If post is announcement than this has value "null null").
     * @param user - Post creator.
     */
    public Post(UUID id, String name, String content, String timeStamp, String dueDate, String user) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.timeStamp = timeStamp;
        this.dueDate = dueDate;
        this.user = user;
    }

    //Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
