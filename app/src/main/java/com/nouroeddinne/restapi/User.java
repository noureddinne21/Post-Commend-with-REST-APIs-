package com.nouroeddinne.restapi;

public class User {

    String name,_id;

    public User(String name, String id) {
        this.name = name;
        this._id = _id;
    }

    public User(String name) {
        this.name = name;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }
}
