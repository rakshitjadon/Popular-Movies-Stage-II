package com.example.android.MyMovie;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.MyMovie.Database.MovieContract;
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
    public static final String S_P = "popularity.desc";
    public static final String S_R = "vote_average.desc";
    public static final String S_F = "favourites";
    public MovieAdapter mAdapter;
    private RetrofitService retrofitService;
    private List<Movie> movies;
    private String mSort = S_P;
    private MenuItem mP;
    private MenuItem mR;
    private MenuItem mF;

    public MainFragment() {
        setHasOptionsMenu(true);
    }

    public interface MoiveCall{
        void on(Movie movie);
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
        mP = menu.findItem(R.id.sort_popular);
        mR = menu.findItem(R.id.sort_rating);
        mF = menu.findItem(R.id.sort_fav);
        if (mSort.contentEquals(S_P)) {
            if (!mP.isChecked()) {
                mP.setChecked(true);
            }
        } else if (mSort.contentEquals(S_R)) {
            if (!mR.isChecked()) {
                mR.setChecked(true);
            }
        } else if (mSort.contentEquals(S_F)) {
            if (!mF.isChecked()) {
                mF.setChecked(true);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.sort_popular:
                mSort = S_P;
                getMovies(mSort);
                    if (!mP.isChecked()) {
                        mP.setChecked(true);
                    }
                return true;
            case R.id.sort_rating:
                mSort = S_R;
                getMovies(mSort);
                    if (!mR.isChecked()) {
                        mR.setChecked(true);
                    }
                return true;
            case R.id.sort_fav:
                mSort = S_F;
                if (!mF.isChecked()) {
                    mF.setChecked(true);
                }
                new FetchFavorites(getContext(), mAdapter, movies).execute();
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
        GridView mGrid = (GridView) rootView.findViewById(R.id.movie_grid);
        mGrid.setAdapter(mAdapter);
        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MoiveCall) getActivity()).on(movies.get(position));
            }
        });
        retrofitService = RetrofitClient.createService(RetrofitService.class);
        getMovies(mSort);
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
            @SuppressLint("ShowToast")
            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getContext(),"Error Fetching Data",Toast.LENGTH_SHORT);
            }
        });
    }
}
class FetchFavorites extends AsyncTask<Void, Void, List<Movie>> {
    private MovieAdapter mAdapter;@SuppressLint("StaticFieldLeak")
    private Context mContext;
    private List<Movie> mList;
    private static final String[] STRINGS = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_POSTER_IMAGE,
            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE
    };
    private static final int ID = 0;
    private static final int TITLE = 1;
    private static final int OVERVIEW = 2;
    private static final int IMAGE = 3;
    private static final int VOTE = 4;
    private static final int DATE = 5;
    FetchFavorites(Context context, MovieAdapter movieAdapter, List<Movie> list) {
        mContext = context;
        mAdapter = movieAdapter;
        mList = list;
    }
    private List<Movie> getMovies(Cursor cursor) {
        List<Movie> movieList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Movie movie = new Movie(cursor.getString(ID),
                        cursor.getString(TITLE),
                        cursor.getString(OVERVIEW),
                        cursor.getString(IMAGE),
                        cursor.getFloat(VOTE),
                        cursor.getString(DATE));
                movieList.add(movie);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return movieList;
    }
    @Override
    protected List<Movie> doInBackground(Void... params) {
        Cursor cursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                STRINGS,
                null,
                null,
                null
        );
        return getMovies(cursor);
    }

    @Override
    protected void onPostExecute(List<Movie> lllllll) {
        if (lllllll != null) {
            if (mAdapter != null) {
                mAdapter.clear();
                mList = new ArrayList<>();
                mList.addAll(lllllll);
                for (Movie movie : lllllll) {
                    mAdapter.add(movie);
                }
                //mAdapter.notifyDataSetChanged();
            }
        }
    }
}
