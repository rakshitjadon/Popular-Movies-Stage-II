package com.example.android.MyMovie.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.MyMovie.R;
import com.example.android.MyMovie.models.Review;

import java.util.List;

public class ReviewAdapter extends ArrayAdapter<Review> {

    public ReviewAdapter(Context context, List<Review> reviews) {
        super(context, 0, reviews);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Review review = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_item, parent, false);
        }

        TextView authorText = (TextView)convertView.findViewById(R.id.author);
        authorText.setText(review.getAuthor());
        TextView reviewText = (TextView)convertView.findViewById(R.id.review);
        reviewText.setText(review.getContent());
        return convertView;
    }
}
