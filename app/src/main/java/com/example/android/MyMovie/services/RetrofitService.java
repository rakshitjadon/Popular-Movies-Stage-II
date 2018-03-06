package com.example.android.MyMovie.services;

import com.example.android.MyMovie.models.MovieResponse;
import com.example.android.MyMovie.models.ReviewResponse;
import com.example.android.MyMovie.models.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {

    @GET("3/discover/movie?")
    Call<MovieResponse>
            getMovies(@Query("sort_by") String sortBy);

    @GET("3/movie/{id}/reviews?")
    Call<ReviewResponse>
            getReviews(@Path("id") String id);

    @GET("3/movie/{id}/videos?")
    Call<TrailerResponse>
            getTrailers(@Path("id") String id);
}
