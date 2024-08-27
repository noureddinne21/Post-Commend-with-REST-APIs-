package com.nouroeddinne.restapi;

public class Model {
    String title,body,userId,name,_id;

    public Model(String title, String body, String userId, String name, String _id) {
        this.title = title;
        this.body = body;
        this.userId = userId;
        this.name = name;
        this._id = _id;
    }

    public Model(String title, String body, String userId) {
        this.title = title;
        this.body = body;
        this.userId = userId;
    }

    public Model(String userId, String name, String title, String body) {
        this.userId = userId;
        this.title = title;
        this.body = body;
        this.name = name;
    }


    public Model() {}

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }



    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
