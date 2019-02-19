package com.kapok.brianramirez.kapok;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class ResLog extends RealmObject {

    @PrimaryKey
    @Required
    public String id;
    public Team team;
    public Person creator;
    @Required
    private String location;
    @Required
    private String category;
    @Required
    private String info;
    private Boolean isSensitive;



    public ResLog() {
        this.id = UUID.randomUUID().toString();
        this.team = null;
        this.creator = null;
        this.location = null;
        this.category = null;
        this.info = null;
        this.isSensitive = null;
    }


    public ResLog(Team team, Person creator, String location, String category, String info, Boolean isSensitive) {
        this.id = UUID.randomUUID().toString();
        this.team = team;
        this.creator = creator;
        this.location = location;
        this.category = category;
        this.info = info;
        this.isSensitive = isSensitive;
    }

    public Person getCreator() {
        return creator;
    }

    public void setCreator(Person creator) {
        this.creator = creator;
    }

    public Boolean getSensitive() {
        return isSensitive;
    }

    public void setSensitive(Boolean sensitive) {
        isSensitive = sensitive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        category = category;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        info = info;
    }



}
