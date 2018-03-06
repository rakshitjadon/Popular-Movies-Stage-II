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

    public static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("http://api.themoviedb.org/")
            .addConverterFactory(GsonConverterFactory.create());

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static <Services> Services createService(Class<Services> Class) {
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                HttpUrl uri = chain.request().url().newBuilder().addQueryParameter("api_key", BuildConfig.API_KEY).build();
                Request request = chain.request().newBuilder().url(uri).build();
                return chain.proceed(request);
            }
        });
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(Class);
    }
}
