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

public class loginActivity extends AppCompatActivity {

    EditText name;
    Button button;

    private static final String BASE_URL = "https://crudcrud.com/api/2a53c836f12f4901b1bc64f1b52dc566/";
    private ApiService apiService;

    SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        name = findViewById(R.id.editTextText2_name);
        button = findViewById(R.id.button2_login_login);

        sharedPreferences = getSharedPreferences("USER", Context.MODE_PRIVATE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User user = new User();
                user.setName(name.getText().toString());

                apiService.addUser(user).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                        sharedPreferences.edit().putString("id",response.body().getId().toString()).apply();
                        sharedPreferences.edit().putString("name",name.getText().toString()).apply();
                        Intent intent = new Intent(loginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable throwable) {
                        Toast.makeText(loginActivity.this, ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });



























    }


//
//    public String getId(){
//
//
//
//    }

















































}