package com.capstone.kots.entity;

public class FacebookResource {
    private String id;
    private String userName;
    private String firstName;
    private String lastName;
    private String avatar;
    private String email;
    private String birthday;
    private Boolean gender;
    private String linkDonate;


//    public FacebookResource(String id, String userName, String firstName, String lastName, String avatarUrl, String email, Byte sex) {
//        this.id = id;
//        this.userName = userName;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.avatarUrl = avatarUrl;
//        this.email = email;
//        this.sex = sex;
//    }
    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLinkDonate() {
        return linkDonate;
    }

    public void setLinkDonate(String linkDonate) {
        this.linkDonate = linkDonate;
    }
}
