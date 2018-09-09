package com.kapok.brianramirez.kapok;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class User extends RealmObject {


    @PrimaryKey
    @Required
    public String id;

    private String fullName;
    private String occupation;
    private String contactInformation;
    private String aboutMe;

    public User(){
        this.id = null;
        this.fullName = null;
        this.occupation = null;
        this.contactInformation = null;
        this.aboutMe = null;
    }

    public User(String id, String fullName, String occupation, String contactInformation, String aboutMe) {
        this.id = id;
        this.fullName = fullName;
        this.occupation = occupation;
        this.contactInformation = contactInformation;
        this.aboutMe = aboutMe;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }


}
