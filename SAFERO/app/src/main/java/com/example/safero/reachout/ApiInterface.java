package com.example.safero.reachout;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("getLastData.php")
    Call<F_lvl> RetriveData();
}
