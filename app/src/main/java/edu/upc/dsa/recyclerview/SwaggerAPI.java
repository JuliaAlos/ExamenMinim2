package edu.upc.dsa.recyclerview;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SwaggerAPI {

    @GET("users/{id}")
    Call<Git> getInfoUser(@Path("id") String userid);

    @GET("users/{id}/repos")
    Call<List<GitRepos>> getRepos(@Path("id") String userid);
}
