package edu.upc.dsa.recyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Followers extends AppCompatActivity {
    ImageView imageView;
    TextView repos;
    TextView following;
    RecyclerView recyclerView;
    String user;

    SwaggerAPI swaggerAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        imageView= findViewById(R.id.imageView2);
        repos= findViewById(R.id.repositories);
        following=findViewById(R.id.following);
        recyclerView=findViewById(R.id.recyclerView2);

        Glide.with(this).load(getIntent().getStringExtra("image")).into(imageView);
        repos.setText("Repositories: "+ getIntent().getStringExtra("repos"));
        following.setText("Following: "+ getIntent().getStringExtra("following"));
        user= getIntent().getStringExtra("user");

        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        swaggerAPI= retrofit.create(SwaggerAPI.class);

        Call<List<GitFollowers>> call = swaggerAPI.getFollowers(user);
        call.enqueue(new Callback<List<GitFollowers>>() {
            @Override
            public void onResponse(Call<List<GitFollowers>>call, Response<List<GitFollowers>> response) {
                if(!response.isSuccessful()){
                    Log.d("MYAPP", "Error "+response.code());
                    return;
                }
                List<GitFollowers> followers = response.body();
                Log.d("MYAPP", "RETROFIT "+response.code());
                MyAdapter myAdapter= new MyAdapter(Followers.this,followers);
                recyclerView.setAdapter(myAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(Followers.this));
                findViewById(R.id.indicator).setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<GitFollowers>> call, Throwable t) {
                Log.d("MYAPP", "Error conexion servidor:"+t.getMessage());
            }
        });

    }
}