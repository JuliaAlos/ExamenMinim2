package edu.upc.dsa.recyclerview;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

        user = findViewById(R.id.Name);

        SharedPreferences sharedPref = getSharedPreferences("user", Context.MODE_PRIVATE);
        user.setText(sharedPref.getString("userName",null));

        findViewById(R.id.indicator).setVisibility(View.GONE);
        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        swaggerAPI= retrofit.create(SwaggerAPI.class);

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
                    userNotFound();
                    return;
                }
                Git git = response.body();
                Log.d("MYAPP", git.getId());
                Intent intent = new Intent(Inicial.this, Repositoris.class);
                intent.putExtra("following",git.getFollowing());
                intent.putExtra("followers",git.getFollowers());
                intent.putExtra("user",git.getLogin());
                intent.putExtra("image",git.getAvatar_url());
                startActivity(intent);
                findViewById(R.id.indicator).setVisibility(View.GONE);
                saveSharedPreferences(git.getLogin());
            }

            @Override
            public void onFailure(Call<Git> call, Throwable t) {
                Log.d("MYAPP", "Error conexion servidor:"+t.getMessage());
                errorServer();
            }
        });
    }
    public void saveSharedPreferences(String userName){
        SharedPreferences sharedPref = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("userName",userName);
        editor.commit();
    }

    public void userNotFound(){
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setMessage("User does not exist on GitHub")
                .setTitle("USER NOT FOUND")
                .setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setCancelable(true);

        alerta.show();
    }

    public void errorServer(){
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setMessage("Error getting response from the server")
                .setTitle("CONNECTION FAIL")
                .setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setCancelable(true);

        alerta.show();
    }
}