package com.tuankhai.travelassistants.adapter;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tuankhai.ratingbar.MaterialRatingBar;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.webservice.DTO.PlaceGoogleDTO;

import java.util.ArrayList;

/**
 * Created by Khai on 12/09/2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewHolder> {

    Activity context;
    ArrayList<PlaceGoogleDTO.Result.Review> arrReviews;

    public ReviewsAdapter(Activity context, ArrayList<PlaceGoogleDTO.Result.Review> arrReviews) {
        this.arrReviews = arrReviews;
        this.context = context;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ReviewHolder(context.getLayoutInflater().inflate(R.layout.item_reviews, viewGroup));
    }

    @Override
    public void onBindViewHolder(ReviewHolder reviewHolder, int i) {
        PlaceGoogleDTO.Result.Review item = arrReviews.get(i);
        reviewHolder.txtName.setText(item.author_name);
        reviewHolder.txtTime.setText(item.relative_time_description);
        reviewHolder.txtContent.setText(item.text);
        Glide.with(context).load(Uri.parse(item.profile_photo_url)).asBitmap().into(reviewHolder.imgView);
    }

    @Override
    public int getItemCount() {
        return arrReviews == null ? 0 : arrReviews.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtTime, txtContent;
        MaterialRatingBar ratingBar;
        ImageView imgView;

        public ReviewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtTime = itemView.findViewById(R.id.txt_time_review);
            txtContent = itemView.findViewById(R.id.txt_comment);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            imgView = itemView.findViewById(R.id.img_user);
        }
    }
}
