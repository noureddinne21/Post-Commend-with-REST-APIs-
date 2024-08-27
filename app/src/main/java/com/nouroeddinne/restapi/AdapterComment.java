package com.nouroeddinne.restapi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdapterComment extends RecyclerView.Adapter<AdapterComment.ViweHolder> {

    private Context context;
    private List<Commant> listCommant;
    private String idPost;

    private ApiService apiService;
    private static final String BASE_URL = "https://crudcrud.com/api/2a53c836f12f4901b1bc64f1b52dc566/";

    public AdapterComment(Context context, List<Commant> listCommant,String idPost) {
        this.context = context;
        this.listCommant = listCommant;
        this.idPost = idPost;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

    }

    @NonNull
    @Override
    public AdapterComment.ViweHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viwe = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new ViweHolder(viwe);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterComment.ViweHolder holder, int position) {

        Commant m = listCommant.get(position);

        if (listCommant.size() > 0) {
            if (m.getPostId().equals(idPost)){
                holder.tital.setText(m.getBody());
                Call<User> call = apiService.getUser(m.getUserId());
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            User user = response.body();
                            if (user != null) {
                                holder.name.setText(user.getName());
                            } else {
                                Log.d("MainActivity", "user is null");
                            }
                        } else {
                            Log.e("MainActivity", "get user is not successful: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.e("MainActivity", "Error fetching post", t);
                    }
                });
            }else {
                listCommant.remove(position);
            }
        }

    }

    @Override
    public int getItemCount() {
        return listCommant.size();
    }

    public class ViweHolder extends RecyclerView.ViewHolder{
        private TextView tital,name,numComment;
        public ViweHolder(@NonNull View itemView) {
            super(itemView);
            tital = itemView.findViewById(R.id.textView_post_text);
            name = itemView.findViewById(R.id.textView3);
        }
    }




}

