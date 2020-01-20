package com.example.safero.reachout;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static final String BASE_URL = "https://twenty-five-bow.000webhostapp.com/api/";
    public static Retrofit retrofit = null;

    public static Retrofit getApiClient() {
        if (retrofit==null){
            //Gson gson = new GsonBuilder().setLenient().create();
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        }
        return retrofit;
    }
}
