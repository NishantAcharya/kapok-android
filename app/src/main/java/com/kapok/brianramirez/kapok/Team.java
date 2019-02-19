package com.kapok.brianramirez.kapok;

import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Team extends RealmObject {

    @PrimaryKey
    @Required
    public String id;
    private int joinCode;
    @Required
    private String name;
    @Required
    private String location;
    private RealmList<Person> members;

    public Team(){
        this.id = UUID.randomUUID().toString();
        this.joinCode = 0;
        this.name = null;
        this.members = new RealmList<>();
        this.location= null;
    }

    public Team(int joinCode, String name, String location){
        this.id = UUID.randomUUID().toString();
        this.joinCode = joinCode;
        this.name = name;
        this.members = new RealmList<>();
        this.location= location;
    }

    public void addMemmber(Person person) {
        members.add(person);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTeam_join_code() {
        return joinCode;
    }

    public void setTeam_join_code(int team_join_code) {
        this.joinCode = team_join_code;
    }

    public String getTeam_name() {
        return name;
    }

    public void setTeam_name(String team_name) {
        this.name = team_name;
    }

    public RealmList<Person> getMembers() {
        return members;
    }

    public void setMembers(RealmList<Person> members) {
        this.members = members;
    }
}
