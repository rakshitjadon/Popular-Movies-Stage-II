package com.example.android.MyMovie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.MyMovie.adapters.ReviewAdapter;
import com.example.android.MyMovie.adapters.TrailerAdapter;
import com.example.android.MyMovie.models.Movie;
import com.example.android.MyMovie.models.Review;
import com.example.android.MyMovie.models.ReviewResponse;
import com.example.android.MyMovie.models.Trailer;
import com.example.android.MyMovie.models.TrailerResponse;
import com.example.android.MyMovie.services.RetrofitClient;
import com.example.android.MyMovie.services.RetrofitService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailFragment extends Fragment {

    @Bind(R.id.movie_title)
    TextView movieTitle;
    @Bind(R.id.movie_overview)
    TextView movieOverview;
    @Bind(R.id.movie_rating)
    TextView movieRating;
    @Bind(R.id.movie_release)
    TextView movieRelease;
    @Bind(R.id.image_poster)
    ImageView moviePoster;
    @Bind(R.id.fav_button)
    Button favouriteButton;
    public Movie mMovie;
    public ReviewAdapter rAdapter;
    public TrailerAdapter tAdapter;
    public RetrofitService retrofitService;
    public DetailFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);
        setDetail();
        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Clicked",Toast.LENGTH_LONG).show();
            }
        });
        final List<Review> reviews = new ArrayList<>();
        rAdapter = new ReviewAdapter(getActivity(), reviews);
        ListView reviewList = (ListView) rootView.findViewById(R.id.mReview);
        reviewList.setAdapter(rAdapter);
        final List<Trailer> tList = new ArrayList<>();
        tAdapter = new TrailerAdapter(getActivity(), tList);
        ListView trailerList = (ListView) rootView.findViewById(R.id.mTrailer);
        trailerList.setAdapter(tAdapter);
        trailerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent youTubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + tList.get(position).getKey()));
                startActivity(youTubeIntent);
            }
        });
        retrofitService = RetrofitClient.createService(RetrofitService.class);
        Reviews();
        Trailers();
        return rootView;
    }
    private void setDetail(){
        Bundle arguments = getArguments();
        mMovie = arguments.getParcelable(MainActivity.MOVIE_TAG);
        if (mMovie!= null) {
            movieTitle.setText(mMovie.getOriginalTitle());
            movieOverview.setText(mMovie.getOverview());
            movieRating.setText( mMovie.getRating());
            movieRelease.setText(mMovie.getMovieReleaseDate());
            Picasso.with(getContext()).load(mMovie.getImage()).into(moviePoster);
        }
    }
    private void Reviews() {
        Call<ReviewResponse> getReview = retrofitService.getReviews(mMovie.getId());
        getReview.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Response<ReviewResponse> response) {
                List<Review> movieReviews = response.body().getResults();
                rAdapter.clear();
                for (Review review : movieReviews) {
                    rAdapter.add(review);
                }
            }
            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getContext(),"Error Fetching Data",Toast.LENGTH_SHORT);

            }
        });
    }
    private void Trailers() {
        Call<TrailerResponse> getTrailer = retrofitService.getTrailers(mMovie.getId());
        getTrailer.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Response<TrailerResponse> response) {
                List<Trailer> movieTrailers = response.body().getResults();
                tAdapter.clear();
                for (Trailer trailer : movieTrailers) {
                    tAdapter.add(trailer);
                }
            }
            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getContext(),"Error Fetching Data",Toast.LENGTH_SHORT);

            }
        });
    }
}
