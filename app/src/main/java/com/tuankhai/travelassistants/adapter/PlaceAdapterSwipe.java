package com.tuankhai.travelassistants.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseUser;
import com.tuankhai.likebutton.LikeButton;
import com.tuankhai.likebutton.OnLikeListener;
import com.tuankhai.ratingbar.MaterialRatingBar;
import com.tuankhai.swipelayout.SwipeLayout;
import com.tuankhai.swipelayout.adapter.RecyclerSwipeAdapter;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.activity.ListPlaceActivity;
import com.tuankhai.travelassistants.activity.LoginActivity;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.webservice.DTO.CheckDTO;
import com.tuankhai.travelassistants.webservice.DTO.FavoriteDTO;
import com.tuankhai.travelassistants.webservice.DTO.PlaceDTO;
import com.tuankhai.travelassistants.webservice.main.MyCallback;
import com.tuankhai.travelassistants.webservice.main.RequestService;
import com.tuankhai.travelassistants.webservice.request.AddFavoriteRequest;
import com.tuankhai.travelassistants.webservice.request.CheckFavoriteRequest;
import com.tuankhai.travelassistants.webservice.request.RemoveFavoriteRequest;

import java.util.ArrayList;

/**
 * Created by Khai on 14/09/2017.
 */

public class PlaceAdapterSwipe extends RecyclerSwipeAdapter<PlaceAdapterSwipe.PlaceViewHolder> {

    public class PlaceViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, OnLikeListener {
        SwipeLayout swipeLayout;
        RelativeLayout layout;
        TextView txtName;
        ImageView imageView;
        MaterialRatingBar ratingBar;
        LikeButton likeButton;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            swipeLayout = itemView.findViewById(R.id.swipe);
            layout = itemView.findViewById(R.id.layout_item_place);
            txtName = itemView.findViewById(R.id.txt_item_name_place);
            imageView = itemView.findViewById(R.id.img_item_place);
            imageView.setOnClickListener(this);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            ratingBar.invalidate();
            ratingBar.setMax(5);
            ratingBar.setNumStars(5);
            ratingBar.setStepSize(0.1f);
            likeButton = itemView.findViewById(R.id.heart_button);
            likeButton.setOnLikeListener(this);
            itemView.setOnClickListener(this);
//            swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
//                @Override
//                public void onDoubleClick(SwipeLayout layout, boolean surface) {
//                    itemListener.onItemPlaceClick(layout, arrPlace.get(getAdapterPosition()));
//                }
//            });
        }

        private void refreshFavorite() {
            if (currentUser == null) {
                setLiked(false);
            } else {
                new RequestService().load(new CheckFavoriteRequest("", arrPlace.get(getAdapterPosition()).id, currentUser.getEmail()), false, new MyCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        super.onSuccess(response);
                        CheckDTO result = (CheckDTO) response;
                        if (result.result) {
                            setLiked(true);
                        } else {
                            setLiked(false);
                        }
                    }
                }, CheckDTO.class);
            }
        }

        @Override
        public void onClick(View view) {
            itemListener.onItemPlaceClick(view, arrPlace.get(getAdapterPosition()));
        }

        @Override
        public void liked(final LikeButton likeButton) {
            if (currentUser == null) {
                Intent intent = new Intent(context, LoginActivity.class);
                intent.putExtra(AppContansts.INTENT_DATA, AppContansts.REQUEST_LOGIN);
                context.startActivityForResult(intent, AppContansts.REQUEST_LOGIN);
                setLiked(false);
            } else {
                likeButton.setEnabled(false);
                new RequestService().load(
                        new AddFavoriteRequest("", arrPlace.get(getAdapterPosition()).id, currentUser.getEmail()),
                        false,
                        new MyCallback() {
                            @Override
                            public void onSuccess(Object response) {
                                super.onSuccess(response);
                                likeButton.setEnabled(true);
//                                Toast.makeText(context, context.getString(R.string.add_favorite), Toast.LENGTH_SHORT).show();
                            }
                        }, FavoriteDTO.class);
            }
        }

        @Override
        public void unLiked(final LikeButton likeButton) {
            likeButton.setEnabled(false);
            if (currentUser == null) {
                Intent intent = new Intent(context, LoginActivity.class);
                intent.putExtra(AppContansts.INTENT_DATA, AppContansts.REQUEST_LOGIN);
                context.startActivityForResult(intent, AppContansts.REQUEST_LOGIN);
            } else {
                new RequestService().load(
                        new RemoveFavoriteRequest("", arrPlace.get(getAdapterPosition()).id, currentUser.getEmail()),
                        false,
                        new MyCallback() {
                            @Override
                            public void onSuccess(Object response) {
                                super.onSuccess(response);
                                likeButton.setEnabled(true);
                                if (context.type == AppContansts.INTENT_TYPE_FAVORITE) {
                                    arrPlace.remove(getAdapterPosition());
                                    notifyItemRemoved(getAdapterPosition());
                                    if (arrPlace.size() == 0){
                                        context.showNotifyNullList();
                                    }
                                }
//                                Toast.makeText(context, context.getString(R.string.remove_favorite), Toast.LENGTH_SHORT).show();
                            }
                        }, FavoriteDTO.class);
            }
        }

        public void setLiked(boolean flag) {
            if (flag) {
                likeButton.setOnLikeListener(null);
                likeButton.setLiked(true);
                likeButton.setOnLikeListener(this);
            } else {
                likeButton.setOnLikeListener(null);
                likeButton.setLiked(false);
                likeButton.setOnLikeListener(this);
            }
        }
    }


    ListPlaceActivity context;
    ArrayList<PlaceDTO.Place> arrPlace;
    FirebaseUser currentUser;


    private static int type;

    PlaceAdapter.LayoutListPlaceItemListener itemListener;

    public PlaceAdapterSwipe(
            ListPlaceActivity context,
            ArrayList<PlaceDTO.Place> arrPlace,
            PlaceAdapter.LayoutListPlaceItemListener listener) {
        this.context = context;
        this.arrPlace = arrPlace;
        this.itemListener = listener;
        this.currentUser = context.currentUser;
        type = R.layout.item_place_line_swipe;
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        return new PlaceViewHolder(inflater.inflate(type, viewGroup, false));
//        return new PlaceViewHolder(
//                MaterialRippleLayout.on(inflater.inflate(type, viewGroup, false))
//                        .rippleOverlay(true)
//                        .rippleAlpha(0.2f)
//                        .rippleColor(R.integer.rippleColor)
//                        .rippleHover(true)
//                        .create()
//        );
    }

    @Override
    public void onBindViewHolder(final PlaceViewHolder viewHolder, final int position) {
        PlaceDTO.Place item = arrPlace.get(position);
        String url = AppContansts.URL_IMAGE + item.id + AppContansts.IMAGE_RATIO_16_9;
        viewHolder.txtName.setText(item.long_name);
        viewHolder.ratingBar.setRating(Float.parseFloat(item.rating));
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.imageView);
        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        viewHolder.refreshFavorite();
        mItemManger.bind(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return arrPlace.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

}