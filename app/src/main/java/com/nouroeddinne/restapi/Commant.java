package com.nouroeddinne.restapi;
public class Commant {

    String postId,body,userId,_id;

    public Commant(String postId, String body, String userId) {
        this.postId = postId;
        this.body = body;
        this.userId = userId;
    }

    public Commant(String postId, String body, String userId, String _id) {
        this.postId = postId;
        this.body = body;
        this.userId = userId;
        this._id = _id;
    }

    public Commant(String body, String userId) {
        this.body = body;
        this.userId = userId;
    }

    public Commant() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
