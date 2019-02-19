package com.kapok.brianramirez.kapok;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Person extends RealmObject {


    @PrimaryKey
    @Required
    public String id;
    public Team team;
    @Required
    private String fullName;
    @Required
    private String occupation;
    @Required
    private String contactInformation;
    @Required
    private String aboutMe;
    private boolean isAdmin;
    private String status;



    public Person(){
        this.id = null;
        this.team = null;
        this.fullName = null;
        this.occupation = null;
        this.contactInformation = null;
        this.aboutMe = null;
        this.isAdmin = false;
        this.status = null;
    }

    public Person(String id, String fullName, String occupation, String contactInformation, String aboutMe, Boolean isAdmin, String status){
        this.id = id;
        this.team = null;
        this.fullName = fullName;
        this.occupation = occupation;
        this.contactInformation = contactInformation;
        this.aboutMe = aboutMe;
        this.isAdmin = isAdmin;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
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
