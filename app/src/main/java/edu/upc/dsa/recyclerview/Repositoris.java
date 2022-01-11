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

public class Repositoris extends AppCompatActivity {
    ImageView imageView;
    TextView followers;
    TextView following;
    TextView name;
    RecyclerView recyclerView;
    String user;

    SwaggerAPI swaggerAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repositoris);

        imageView= findViewById(R.id.imageView2);
        followers= findViewById(R.id.followers);
        following=findViewById(R.id.following);
        name=findViewById(R.id.userName);
        recyclerView=findViewById(R.id.recyclerView2);

        Glide.with(this).load(getIntent().getStringExtra("image")).into(imageView);
        followers.setText("Followers: "+ getIntent().getStringExtra("followers"));
        following.setText("Following: "+ getIntent().getStringExtra("following"));
        user= getIntent().getStringExtra("user");
        name.setText(user);

        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        swaggerAPI= retrofit.create(SwaggerAPI.class);

        Call<List<GitRepos>> call = swaggerAPI.getRepos(user);
        call.enqueue(new Callback<List<GitRepos>>() {
            @Override
            public void onResponse(Call<List<GitRepos>>call, Response<List<GitRepos>> response) {
                if(!response.isSuccessful()){
                    Log.d("MYAPP", "Error "+response.code());
                    return;
                }
                List<GitRepos> repos = response.body();
                Log.d("MYAPP", "RETROFIT "+response.code());
                MyAdapter myAdapter= new MyAdapter(Repositoris.this,repos);
                recyclerView.setAdapter(myAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(Repositoris.this));
                findViewById(R.id.indicator).setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<List<GitRepos>> call, Throwable t) {
                Log.d("MYAPP", "Error conexion servidor:"+t.getMessage());
            }
        });

    }
}