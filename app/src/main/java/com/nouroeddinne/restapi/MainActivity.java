package com.nouroeddinne.restapi;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab ;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    private List<Model> listPosts;

    private static final String TAG = "MainActivity";
    private static final String BASE_URL = "https://crudcrud.com/api/2a53c836f12f4901b1bc64f1b52dc566/";
    private ApiService apiService;

    SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPreferences = getSharedPreferences("USER", Context.MODE_PRIVATE);

        if (sharedPreferences.getString("name",null)==null){
            Intent intent = new Intent(MainActivity.this,loginActivity.class);
            startActivity(intent);
            finish();
        }

        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        allPosts();



    }




    private void allPosts() {

        Call<List<Model>> call = apiService.getPosts();
        call.enqueue(new Callback<List<Model>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {
                if (response.isSuccessful()) {

                    listPosts = response.body();
                    adapter = new AdapterPost(MainActivity.this,listPosts);
                    recyclerView.setAdapter(adapter);

                } else {
                    Log.e("MainActivity", "Request not successful: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Model>> call, Throwable t) {
                Log.e("MainActivity", "Error fetching posts", t);
            }
        });

    }





















}