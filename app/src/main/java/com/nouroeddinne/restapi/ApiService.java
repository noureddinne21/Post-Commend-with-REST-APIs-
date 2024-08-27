package com.nouroeddinne.restapi;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import java.util.List;

public interface ApiService {

    @POST("posts")
    Call<Model> createPost(@Body Model post);

    @POST("comments")
    Call<Model> createCommand(@Body Commant commant);

    @GET("posts")
    Call<List<Model>> getPosts();

    @GET("comments")
    Call<List<Commant>> getComments();

    @GET("posts/{id}")
    Call<Model> getPost(@Path("id") String id);

    @GET("users/{id}")
    Call<User> getUser(@Path("id") String id);

    @PUT("posts/{id}")
    Call<Model> updatePost(@Path("id") String id , @Body Model post);

    @POST("users")
    Call<User> addUser(@Body User user);

    @DELETE("posts/{id}")
    Call<Model> delePost(@Path("id") String id);

    @DELETE("comments/{id}")
    Call<Commant> deleComment(@Path("id") String id);
}
























