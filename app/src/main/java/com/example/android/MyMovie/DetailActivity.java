package com.example.android.MyMovie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(MainActivity.MOVIE_TAG,
                    getIntent().getParcelableExtra(MainActivity.MOVIE_TAG));
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movieDetail, fragment)
                    .commit();
        }
    }

}
