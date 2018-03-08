package com.example.android.MyMovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.MyMovie.models.Movie;

public class MainActivity extends AppCompatActivity implements MainFragment.MoiveCall {

    private static final String DTAG = "DTAG";
    public final static String MTAG = "MOVIE";

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.movieDetail) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movieDetail, new DetailFragment(), DTAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }

    @Override
    public void on(Movie movie) {
        if (mTwoPane) {
            Bundle argument = new Bundle();
            argument.putParcelable(MTAG, movie);
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(argument);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movieDetail, fragment, DTAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .putExtra(MTAG, movie);
            startActivity(intent);
        }
    }

}
