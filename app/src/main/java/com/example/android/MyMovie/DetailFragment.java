package com.example.android.MyMovie;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
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

import com.example.android.MyMovie.models.Importances;
import com.example.android.MyMovie.Database.MovieContract;
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
    @Bind(R.id.movie_release)
    TextView movieRelease;
    @Bind(R.id.movie_rating)
    TextView movieRating;
    @Bind(R.id.image_poster)
    ImageView moviePoster;
    @Bind(R.id.movie_overview)
    TextView movieOverview;
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
        new CatchFav(getActivity(), mMovie, false, favouriteButton).execute();
        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CatchFav(getActivity(), mMovie, true, favouriteButton).execute();
            }
        });
        retrofitService = RetrofitClient.make(RetrofitService.class);
        Reviews();
        Trailers();
        return rootView;
    }
    private void setDetail(){
        Bundle arguments = getArguments();
        mMovie = arguments.getParcelable(MainActivity.MTAG);
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
            @SuppressLint("ShowToast")
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
            @SuppressLint("ShowToast")
            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getContext(),"Error Fetching Data",Toast.LENGTH_SHORT);

            }
        });
    }
    public static boolean aBoolean(Context context, String id) {
        Cursor roror = context.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                MovieContract.MovieEntry._ID + " = ?",
                new String[]{id},
                null
        );
        if (roror!= null) {
            roror.close();
            return (roror.getCount() > 0);
        }
        return false;
    }
}
class CatchFav extends AsyncTask<Void, Void, Boolean> {
    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private Movie movie;
    private Boolean aBoolean;
    @SuppressLint("StaticFieldLeak")
    private Button button;

    CatchFav(Context mContext, Movie movie, Boolean aBoolean, Button button) {
        this.mContext = mContext;
        this.movie = movie;
        this.aBoolean = aBoolean;
        this.button = button;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return DetailFragment.aBoolean(mContext, movie.getId());
    }

    @Override
    protected void onPostExecute(Boolean bBoolean) {
        if (aBoolean) {
            new BeFav(mContext, movie, bBoolean, button).execute();
        } else {
            if (bBoolean) {
                button.setText(mContext.getString(R.string.unstar_it));
            } else {
                button.setText(mContext.getString(R.string.star_it));
            }
        }
    }
}
class BeFav extends AsyncTask<Void, Void, Void> {
    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private Movie movie;
    private Boolean aBoolean;
    @SuppressLint("StaticFieldLeak")
    private Button button;

    BeFav(Context mContext, Movie movie, Boolean aBoolean, Button button) {
        this.mContext = mContext;
        this.movie = movie;
        this.aBoolean = aBoolean;
        this.button = button;
    }

    @Override
    protected Void doInBackground(Void... params) {
        if (!aBoolean) {
            ContentValues contentValues = Importances.toContentValue(movie);
            mContext.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

        } else {
            mContext.getContentResolver().delete(
                    MovieContract.MovieEntry.CONTENT_URI,
                    MovieContract.MovieEntry._ID + " = ?",
                    new String[]{movie.getId()}
            );
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aid) {
        if (!aBoolean) {
            button.setText(mContext.getString(R.string.unstar_it));
        } else {
            button.setText(mContext.getString(R.string.star_it));
        }
    }
}




