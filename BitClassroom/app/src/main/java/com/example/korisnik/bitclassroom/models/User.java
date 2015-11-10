package com.example.korisnik.bitclassroom.models;

import java.util.UUID;

/**
 * This class represents application user.
 */
public class User {

    //User properties
    private UUID id;
    private String name;
    private String lastname;
    private String role;
    private String token;
    private String webId;
    private String userPic;

    /**
     * Constructor for creating user.
     * @param id - User id.
     * @param name - User name.
     * @param lastname - User last name.
     * @param role - User role.
     * @param token - User token.
     * @param webId - User id on web application.
     * @param pic - Cloudinary path for user image.
     */
    public User(UUID id,String name, String lastname, String role, String token, String webId, String pic) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.role = role;
        this.token = token;
        this.webId = webId;
        this.userPic = pic;
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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getWebId() {
        return webId;
    }

    public void setWebId(String webId) {
        this.webId = webId;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }
}
