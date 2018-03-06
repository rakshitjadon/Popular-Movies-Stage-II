package com.example.android.MyMovie;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.MyMovie.adapters.MovieAdapter;
import com.example.android.MyMovie.models.MovieResponse;
import com.example.android.MyMovie.models.Movie;
import com.example.android.MyMovie.services.RetrofitClient;
import com.example.android.MyMovie.services.RetrofitService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment {
    public static final String SORT_POPULARITY = "popularity.desc";
    public static final String SORT_RATING = "vote_average.desc";
    public static final String SORT_FAV = "favourites";
    public MovieAdapter mAdapter;
    private RetrofitService retrofitService;
    private List<Movie> movies;
    private String mSort = SORT_POPULARITY;
    private MenuItem mPopular;
    private MenuItem mRating;
    private MenuItem mFav;

    public MainFragment() {
        setHasOptionsMenu(true);
    }

    public interface BundleCallback {
        void onItemSelected(Movie movie);
    }

    @Override
    public void onPause() {
        super.onPause();
        ProgressDialog dialog;
        dialog = null;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);

        mPopular = menu.findItem(R.id.sort_popular);
        mRating = menu.findItem(R.id.sort_rating);
        mFav = menu.findItem(R.id.sort_fav);

        if (mSort.contentEquals(SORT_POPULARITY)) {
            if (!mPopular.isChecked()) {
                mPopular.setChecked(true);
            }
        } else if (mSort.contentEquals(SORT_RATING)) {
            if (!mRating.isChecked()) {
                mRating.setChecked(true);
            }
        } else if (mSort.contentEquals(SORT_FAV)) {
            if (!mFav.isChecked()) {
                mFav.setChecked(true);
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.sort_popular:
                mSort = SORT_POPULARITY;
                getMovies(mSort);
                    if (!mPopular.isChecked()) {
                        mPopular.setChecked(true);
                    }
                return true;
            case R.id.sort_rating:
                mSort = SORT_RATING;
                getMovies(mSort);
                    if (!mRating.isChecked()) {
                        mRating.setChecked(true);
                    }
                return true;
            case R.id.sort_fav:
                mSort = SORT_FAV;
                if (!mFav.isChecked()) {
                    mFav.setChecked(true);
                }
                return true;
            default:
                return true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        movies = new ArrayList<>();
        mAdapter = new MovieAdapter(getActivity(), movies);
        GridView movieGrid = (GridView) rootView.findViewById(R.id.movie_grid);
        movieGrid.setAdapter(mAdapter);
        retrofitService = RetrofitClient.createService(RetrofitService.class);
        movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((BundleCallback) getActivity()).onItemSelected(movies.get(position));
            }
        });
        return rootView;
    }

    public void getMovies(String sort) {
        Call<MovieResponse> getMovies = retrofitService.getMovies(sort);
        getMovies.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Response<MovieResponse> response) {
                List<Movie> mList = response.body().getResults();
                mAdapter.clear();
                for (Movie movie : mList) {
                    mAdapter.add(movie);
                }
            }
            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getContext(),"Error Fetching Data",Toast.LENGTH_SHORT);
            }
        });
    }


}
