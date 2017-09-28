package com.tuankhai.travelassistants.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tuankhai.ratingbar.MaterialRatingBar;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.utils.MyCache;
import com.tuankhai.travelassistants.utils.Utils;
import com.tuankhai.travelassistants.webservice.DTO.PlaceGoogleDTO;

import java.util.ArrayList;

import static com.tuankhai.travelassistants.utils.MyCache.icon_user_logo_review;

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
        return new ReviewHolder(context.getLayoutInflater().inflate(R.layout.item_reviews, null));
    }

    @Override
    public void onBindViewHolder(ReviewHolder reviewHolder, int i) {
        PlaceGoogleDTO.Result.Review item = arrReviews.get(i);
        reviewHolder.txtName.setText(item.author_name);
        if (Utils.isEmptyString(item.relative_time_description)) {
            String time = Utils.getDescriptionTime(item.time);
            reviewHolder.txtTime.setText(time);
        } else {
            reviewHolder.txtTime.setText(item.relative_time_description);
        }
        reviewHolder.txtContent.setText(item.text);
        reviewHolder.ratingBar.setRating(item.getRating());
        Log.e("status",item.profile_photo_url+"");
        if (!Utils.isEmptyString(item.profile_photo_url)){
            Glide.with(context)
                    .load(Uri.parse(item.profile_photo_url))
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(reviewHolder.imgView);
        }else{
            if (MyCache.getInstance().getBitmapFromMemCache(icon_user_logo_review) == null) {
                Bitmap image = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_user_logo);
                MyCache.getInstance().addBitmapToMemoryCache(icon_user_logo_review, image);
                reviewHolder.imgView.setImageBitmap(image);
            } else {
                reviewHolder.imgView.setImageBitmap(MyCache.getInstance().getBitmapFromMemCache(icon_user_logo_review));
            }
        }

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

            ratingBar.invalidate();
            ratingBar.setMax(5);
            ratingBar.setNumStars(5);
            ratingBar.setStepSize(0.1f);
        }
    }
}
