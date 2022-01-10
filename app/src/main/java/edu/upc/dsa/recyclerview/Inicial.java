package edu.upc.dsa.recyclerview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Inicial extends AppCompatActivity {
    SwaggerAPI swaggerAPI;
    TextView user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);
        findViewById(R.id.indicator).setVisibility(View.GONE);
        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        swaggerAPI= retrofit.create(SwaggerAPI.class);
        user = findViewById(R.id.Name);
    }
    public void buttonClick(View v){
        findViewById(R.id.indicator).setVisibility(View.VISIBLE);

        Call<Git> call = swaggerAPI.getInfoUser(user.getText().toString());
        call.enqueue(new Callback<Git>() {
            @Override
            public void onResponse(Call<Git> call, Response<Git> response) {
                if(!response.isSuccessful()){
                    Log.d("MYAPP", "Error "+response.code());
                    findViewById(R.id.indicator).setVisibility(View.GONE);
                    return;
                }
                Git git = response.body();
                Log.d("MYAPP", git.getId());
                Intent intent = new Intent(Inicial.this,Followers.class);
                intent.putExtra("repos",git.getPublic_repos());
                intent.putExtra("following",git.getFollowing());
                intent.putExtra("user",git.getLogin());
                intent.putExtra("image",git.getAvatar_url());
                startActivity(intent);
                findViewById(R.id.indicator).setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Git> call, Throwable t) {
                Log.d("MYAPP", "Error conexion servidor:"+t.getMessage());
            }
        });
    }
}