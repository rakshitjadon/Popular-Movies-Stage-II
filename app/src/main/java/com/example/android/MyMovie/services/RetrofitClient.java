package com.example.android.MyMovie.services;

import com.example.android.MyMovie.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;


public class RetrofitClient {

    public static Retrofit.Builder makes = new Retrofit.Builder()
            .baseUrl("http://api.themoviedb.org/")
            .addConverterFactory(GsonConverterFactory.create());
    private static OkHttpClient.Builder C = new OkHttpClient.Builder();
    public static <ryan> ryan make(Class<ryan> Class) {
        C.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain rope) throws IOException {
                HttpUrl www = rope.request().url().newBuilder().addQueryParameter("api_key", BuildConfig.API_KEY).build();
                Request please = rope.request().newBuilder().url(www).build();
                return rope.proceed(please);
            }
        });
        Retrofit retrofit = makes.client(C.build()).build();
        return retrofit.create(Class);
    }
}
