package com.nouroeddinne.restapi;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
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

public class AdapterPost extends RecyclerView.Adapter<AdapterPost.ViweHolder> {

    private Context context;
    private List<Model> listPosts;
    private String name;

    private ApiService apiService;
    private static final String BASE_URL = "https://crudcrud.com/api/2a53c836f12f4901b1bc64f1b52dc566/";
    SharedPreferences sharedPreferences;

    public AdapterPost(Context context, List<Model> listPosts) {
        this.context = context;
        this.listPosts = listPosts;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        sharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE);


    }

    @NonNull
    @Override
    public AdapterPost.ViweHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viwe = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViweHolder(viwe);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPost.ViweHolder holder, int position) {

        Model m = listPosts.get(position);

        holder.tital.setText(m.getTitle());
        holder.body.setText(m.getBody());

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

        Call<List<Commant>> call2 = apiService.getComments();
        call2.enqueue(new Callback<List<Commant>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<Commant>> call2, Response<List<Commant>> response) {
                if (response.isSuccessful()) {

                    List<Commant> posts = response.body();
                    int count = 0;

                    for (Commant c : posts) {
                        if (c.getPostId().equals(m.get_id().toString())) {
                            count++;
                        }
                    }

                    holder.numComment.setText(String.valueOf(count));

                } else {
                    Log.e("MainActivity", "comment not successful: " + response.message());
                    holder.numComment.setText("0");
                }
            }

            @Override
            public void onFailure(Call<List<Commant>> call2, Throwable t) {
                Log.e("MainActivity", "Error fetching comment", t);
            }
        });


        holder.imageView_optoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(context, holder.imageView_optoin);
                popup.inflate(R.menu.menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getItemId()==R.id.item1){
                            editPost(m.get_id(),m.getTitle(),m.getBody(),position);
                        }

                        if (item.getItemId()==R.id.item2){
                            delete(m.get_id());
                        }

                        return false;
                    }
                });

                popup.show();

            }
        });



        holder.linearComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,CommentActivity.class);
                intent.putExtra("id",m.get_id().toString());
                intent.putExtra("numComment",holder.numComment.getText().toString());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listPosts.size();
    }

    public class ViweHolder extends RecyclerView.ViewHolder{
        private TextView tital,body,name,numComment;
        private LinearLayout linearComment;
        ImageView imageView_optoin;
        public ViweHolder(@NonNull View itemView) {
            super(itemView);
            tital = itemView.findViewById(R.id.textView_post_text_tital);
            body = itemView.findViewById(R.id.textView_post_text_body);
            name = itemView.findViewById(R.id.textView3);
            numComment = itemView.findViewById(R.id.textView_post_num_comment);
            linearComment = itemView.findViewById(R.id.linear_comment);
            imageView_optoin = itemView.findViewById(R.id.imageView_optoin);
        }
    }





    public void editPost(String id,String title,String body,int position){
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        View mView = LayoutInflater.from(context).inflate(R.layout.custom_dialog,null);

        EditText inputTitle = mView.findViewById(R.id.editText_add_post_tital2);
        EditText inputBody = mView.findViewById(R.id.editText_add_post_body2);
        ImageView btn_cancel = mView.findViewById(R.id.imageView_close);
        Button btn_okay = mView.findViewById(R.id.button_add_post);

        inputTitle.setText(title);
        inputBody.setText(body);

        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Model updatedPost = new Model(inputTitle.getText().toString(),inputBody.getText().toString(),id);

                apiService.updatePost(id,updatedPost).enqueue(new Callback<Model>() {
                    @Override
                    public void onResponse(Call<Model> call, Response<Model> response) {
                        Toast.makeText(context, "onResponse "+response, Toast.LENGTH_SHORT).show();

                        Call<Model> cal = apiService.getPost(id);
                        cal.enqueue(new Callback<Model>() {
                            @Override
                            public void onResponse(Call<Model> cal, Response<Model> response) {
                                if (response.isSuccessful()) {
                                    listPosts.set(position,response.body());
                                } else {
                                    Log.e("MainActivity", "Request not successful: " + response.message());
                                }
                            }

                            @Override
                            public void onFailure(Call<Model> cal, Throwable t) {
                                Log.e("MainActivity", "Error fetching post", t);
                            }
                        });

                        alertDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Model> call, Throwable throwable) {
                        Toast.makeText(context, "onFailure "+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "onFailure: "+throwable.getMessage()+" "+call);
                    }
                });

            }
        });

        alertDialog.show();

    }





    public void delete(String id){

        Call<Model> call = apiService.delePost(id);
        call.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Commant deleted successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to delete Commant. Response code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

























    public String getNameUserFromPost(String id){

        Call<User> call = apiService.getUser(id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user != null) {
                        name =  user.getName();

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
        return name;

    }



    private int numCommants(String commantId) {

        final int[] length = {0};
        Call<List<Commant>> call = apiService.getComments();
        call.enqueue(new Callback<List<Commant>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<Commant>> call, Response<List<Commant>> response) {
                if (response.isSuccessful()) {
                    List<Commant> posts = response.body();
                    length[0] =posts.size();
                } else {
                    Log.e("MainActivity", "Request not successful: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Commant>> call, Throwable t) {
                Log.e("MainActivity", "Error fetching posts", t);
            }
        });
        return length[0];

    }






















}
