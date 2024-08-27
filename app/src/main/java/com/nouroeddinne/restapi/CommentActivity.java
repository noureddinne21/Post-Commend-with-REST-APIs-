package com.nouroeddinne.restapi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class  CommentActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://crudcrud.com/api/2a53c836f12f4901b1bc64f1b52dc566/";
    private ApiService apiService;

    TextView textViewName,textViewTitle,numComment;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    EditText comment;
    Button sent;
    List<Commant> listCommant;

    SharedPreferences sharedPreferences;
    String idPost;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_comment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textViewName = findViewById(R.id.textView_name);
        textViewTitle = findViewById(R.id.textView_title);
        numComment = findViewById(R.id.textView_post_num_comment);
        recyclerView = findViewById(R.id.recyclerView);
        sent = findViewById(R.id.buttonSend);
        comment = findViewById(R.id.editTextComment);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sharedPreferences = getSharedPreferences("USER", Context.MODE_PRIVATE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            idPost = extras.getString("id");
            numComment.setText(extras.getString("numComment"));
            getTitleFromPost(idPost);

            Call<List<Commant>> call = apiService.getComments();
            call.enqueue(new Callback<List<Commant>>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<List<Commant>> call, Response<List<Commant>> response) {
                    if (response.isSuccessful()) {

                        listCommant = response.body();
                        adapter = new AdapterComment(CommentActivity.this,listCommant,idPost);
                        recyclerView.setAdapter(adapter);

                    } else {
                        Log.e("MainActivity", "Request not successful: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<List<Commant>> call, Throwable t) {
                    Log.e("MainActivity", "Error fetching posts", t);
                }
            });



        }

        sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Commant commant = new Commant();
                commant.setUserId(sharedPreferences.getString("id",null));
                commant.setPostId(idPost);
                commant.setBody(comment.getText().toString());

                apiService.createCommand(commant).enqueue(new Callback<Model>() {
                    @Override
                    public void onResponse(Call<Model> call, Response<Model> response) {
                        Log.d("TAG", "onFailure: "+response);
                    }

                    @Override
                    public void onFailure(Call<Model> call, Throwable throwable) {
                        Log.d("TAG", "onFailure: "+throwable.getMessage());
                    }
                });

            }
        });






    }

    public void getNameUserFromPost(String id){

        Call<User> call = apiService.getUser(id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user != null) {
                        textViewName.setText(user.getName());
                    } else {
                        Log.d("MainActivity", "name is null");
                    }
                } else {
                    Log.e("MainActivity", "get name is not successful: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("MainActivity", "Error fetching name", t);
            }
        });
    }




    public void getTitleFromPost(String id){

        Call<Model> call = apiService.getPost(id);
        call.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if (response.isSuccessful()) {
                    Model post = response.body();
                    if (post != null) {
                        textViewTitle.setText(post.getTitle());
                        getNameUserFromPost(post.getUserId());
                    } else {
                        Log.d("MainActivity", "title not found");
                    }
                } else {
                    Log.e("MainActivity", "Request title not successful: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {

                Log.e("MainActivity", "Error fetching title", t);
            }
        });

    }













}