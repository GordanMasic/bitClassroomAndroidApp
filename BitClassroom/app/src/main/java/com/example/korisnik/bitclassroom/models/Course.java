package com.example.korisnik.bitclassroom.models;

import java.util.UUID;

/**
 * This class represents one course with all his properties.
 */
public class Course {

    //Course properties
    private UUID id;
    private String name;
    private String description;
    private String teacher;
    private String webId;
    private String picURL;

    /**
     * Constructor for creating course object.
     * @param id - Course id.
     * @param name - Course name.
     * @param description - Course description.
     * @param teacher - Course teacher name and last name.
     * @param webId - Course id used on web application.
     * @param picURL - Cloudinary path to course image.
     */
    public Course(UUID id,String name, String description, String teacher, String webId, String picURL) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.teacher = teacher;
        this.webId = webId;
        this.picURL = picURL;
    }

    //Getters and setters
    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getWebId() {
        return webId;
    }

    public void setWebId(String webId) {
        this.webId = webId;
    }

    public String getPicURL() {
        return picURL;
    }

    public void setPicURL(String picURL) {
        this.picURL = picURL;
    }
}
