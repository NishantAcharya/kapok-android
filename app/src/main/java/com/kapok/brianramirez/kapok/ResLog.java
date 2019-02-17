package com.kapok.brianramirez.kapok;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class ResLog extends RealmObject {

    @PrimaryKey
    @Required
    public String id;
    @Required
    private String Location;
    @Required
    private String Category;
    @Required
    private String Info;
    private Boolean IsSensitive;

    public ResLog(){
        this.id = null;
        this.Location = null;
        this.Category = null;
        this.Info = null;
        this.IsSensitive = false;
    }
    public ResLog(String id, String Location, String Category, String Info, Boolean IsSensitive) {
        this.id = id;
        this.Location = Location;
        this.Category = Category;
        this.Info = Info;
        this.IsSensitive = IsSensitive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String info) {
        Info = info;
    }

    public boolean isSensitive() {
        return IsSensitive;
    }

    public void setSensitive(boolean sensitive) {
        IsSensitive = sensitive;
    }


}
