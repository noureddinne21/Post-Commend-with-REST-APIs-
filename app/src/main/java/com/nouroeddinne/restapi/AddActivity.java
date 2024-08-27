package com.nouroeddinne.restapi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddActivity extends AppCompatActivity {

    Button button;
    EditText title,body;

    String name,id;

    SharedPreferences sharedPreferences;

    private static final String BASE_URL = "https://crudcrud.com/api/2a53c836f12f4901b1bc64f1b52dc566/";
    private ApiService apiService;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        button = findViewById(R.id.button_add_post);
        title = findViewById(R.id.editText_add_post_tital);
        body = findViewById(R.id.editText_add_post_body);

        sharedPreferences = getSharedPreferences("USER", Context.MODE_PRIVATE);
        name = sharedPreferences.getString("name",null);
        id = sharedPreferences.getString("id",null);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create a post object with updated data
                Model updatedPost = new Model(title.getText().toString(),body.getText().toString(),id);

                // Update the post with ID 1
                apiService.createPost(updatedPost).enqueue(new Callback<Model>() {
                    @Override
                    public void onResponse(Call<Model> call, Response<Model> response) {
                        Toast.makeText(AddActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Model> call, Throwable throwable) {
                        Toast.makeText(AddActivity.this, ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }




















}