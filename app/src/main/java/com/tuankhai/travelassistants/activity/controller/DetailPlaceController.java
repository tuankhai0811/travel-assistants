package com.tuankhai.travelassistants.activity.controller;

import com.google.firebase.auth.FirebaseUser;
import com.tuankhai.travelassistants.activity.DetailPlaceActivity;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.webservice.DTO.CheckerDTO;
import com.tuankhai.travelassistants.webservice.DTO.FavoriteDTO;
import com.tuankhai.travelassistants.webservice.DTO.PlaceGoogleDTO;
import com.tuankhai.travelassistants.webservice.DTO.PlaceNearDTO;
import com.tuankhai.travelassistants.webservice.DTO.ReviewDTO;
import com.tuankhai.travelassistants.webservice.main.MyCallback;
import com.tuankhai.travelassistants.webservice.main.RequestService;
import com.tuankhai.travelassistants.webservice.request.AddFavoriteRequest;
import com.tuankhai.travelassistants.webservice.request.AddReviewRequest;
import com.tuankhai.travelassistants.webservice.request.CheckFavoriteRequest;
import com.tuankhai.travelassistants.webservice.request.EditReviewRequest;
import com.tuankhai.travelassistants.webservice.request.GetReviewRequest;
import com.tuankhai.travelassistants.webservice.request.RemoveFavoriteRequest;
import com.tuankhai.travelassistants.webservice.request.RemoveReviewRequest;

/**
 * Created by tuank on 02/10/2017.
 */

public class DetailPlaceController {
    DetailPlaceActivity mActivity;

    public DetailPlaceController(DetailPlaceActivity activity) {
        mActivity = activity;
    }

    public void removeReviews(final FirebaseUser mUser, PlaceGoogleDTO.Result.Review review) {
        if (mUser == null || review == null) return;
        new RequestService().load(
                new RemoveReviewRequest("", mUser.getEmail(), review.id_place),
                false,
                new MyCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        super.onSuccess(response);
                        CheckerDTO checker = (CheckerDTO) response;
                        if (checker.isSuccess()) {
                            mActivity.removeReviewSuccess();
                        } else {
                            onFailure("Action Fail!");
                        }
                    }

                    @Override
                    public void onFailure(Object error) {
                        super.onFailure(error);
                        mActivity.logError(error.toString());
                    }
                }, CheckerDTO.class);
    }

    public void editReviews(FirebaseUser mUser, PlaceGoogleDTO.Result.Review review) {
        if (mUser == null || review == null) return;
        new RequestService().load(
                new EditReviewRequest("", mUser.getEmail(), review.id_place, review.rating, review.text, review.time),
                false,
                new MyCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        super.onSuccess(response);
                        CheckerDTO checker = (CheckerDTO) response;
                        if (checker.isSuccess()) {
                            mActivity.editReviewSuccess();
                        } else {
                            onFailure("Action Fail!");
                        }
                    }

                    @Override
                    public void onFailure(Object error) {
                        super.onFailure(error);
                        mActivity.logError(error.toString());
                    }
                }, CheckerDTO.class);
    }

    public void getLocalReviews(String id) {
        new RequestService().load(new GetReviewRequest("", id), false, new MyCallback() {
            @Override
            public void onSuccess(Object response) {
                super.onSuccess(response);
                ReviewDTO checker = (ReviewDTO) response;
                if (checker.isSuccess()) {
                    mActivity.getLocalReviewSuccess((ReviewDTO) response);
                } else {
                    onFailure("Get Review Error!");
                }
            }

            @Override
            public void onFailure(Object error) {
                super.onFailure(error);
                mActivity.logError(error.toString());
            }
        }, ReviewDTO.class);
    }

    public void getGooglePlaceDetail(String place_id) {
        new RequestService().getPlace(place_id, new MyCallback() {
            @Override
            public void onSuccess(Object response) {
                super.onSuccess(response);
                PlaceGoogleDTO checker = (PlaceGoogleDTO) response;
                if (checker.isSuccess()) {
                    mActivity.getGooglePlaceDetailSuccess((PlaceGoogleDTO) response);
                } else {
                    onFailure("Get Google Detail Fail!");
                }
            }

            @Override
            public void onFailure(Object error) {
                super.onFailure(error);
                mActivity.logError(error.toString());
            }
        });
    }

    public void getNearFoodPlace(String lat, String lng) {

        new RequestService().nearPlace(AppContansts.INTENT_TYPE_FOOD, lat, lng, new MyCallback() {
            @Override
            public void onSuccess(Object response) {
                super.onSuccess(response);
                PlaceNearDTO checker = (PlaceNearDTO) response;
                if (checker.isSuccess()) {
                    mActivity.getNearFoodPlaceSuccess((PlaceNearDTO) response);
                } else {
                    onFailure("Get Near Food Place Fail!");
                }
            }

            @Override
            public void onFailure(Object error) {
                super.onFailure(error);
                mActivity.logError(error.toString());
            }
        });
    }

    public void getNearHotelPlace(String lat, String lng) {
        new RequestService().nearPlace(AppContansts.INTENT_TYPE_HOTEL, lat, lng, new MyCallback() {
            @Override
            public void onSuccess(Object response) {
                super.onSuccess(response);
                PlaceNearDTO checker = (PlaceNearDTO) response;
                if (checker.isSuccess()) {
                    mActivity.getNearHotelPlaceSuccess((PlaceNearDTO) response);
                } else {
                    onFailure("Get Near Hotel Place Fail!");
                }
            }

            @Override
            public void onFailure(Object error) {
                super.onFailure(error);
                mActivity.logError(error.toString());
            }
        });
    }

    public void checkIsFavorite(String id, String email) {
        new RequestService().load(new CheckFavoriteRequest("", id, email), false, new MyCallback() {
            @Override
            public void onSuccess(Object response) {
                super.onSuccess(response);
                CheckerDTO result = (CheckerDTO) response;
                if (result.result) {
                    mActivity.setLiked(true);
                } else {
                    mActivity.setLiked(false);
                }
            }
        }, CheckerDTO.class);
    }

    public void addFavorite(String id, String email) {
        new RequestService().load(new AddFavoriteRequest("", id, email), false, new MyCallback() {
            @Override
            public void onSuccess(Object response) {
                super.onSuccess(response);
                FavoriteDTO checker = (FavoriteDTO) response;
                if (checker.isSuccess()) {
                    mActivity.addFavoriteSuccess();
                } else {
                    onFailure("Action Fail!");
                }
            }

            @Override
            public void onFailure(Object error) {
                super.onFailure(error);
                mActivity.logError(error.toString());
            }
        }, FavoriteDTO.class);
    }

    public void removeFavorite(String id, String email) {
        new RequestService().load(new RemoveFavoriteRequest("", id, email), false, new MyCallback() {
            @Override
            public void onSuccess(Object response) {
                super.onSuccess(response);
                FavoriteDTO checker = (FavoriteDTO) response;
                if (checker.isSuccess()) {
                    mActivity.removeFavoriteSuccess();
                } else {
                    onFailure("Action Fail!");
                }
            }

            @Override
            public void onFailure(Object error) {
                super.onFailure(error);
                mActivity.logError(error.toString());
            }
        }, FavoriteDTO.class);
    }

    public void sendDataReview(String name, String email, String urlPhoto, String id, String rating, String text, String time) {
        new RequestService().load(
                new AddReviewRequest("", name, email, urlPhoto, id, rating, text, time),
                false,
                new MyCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        super.onSuccess(response);
                        CheckerDTO checker = (CheckerDTO) response;
                        if (checker.isSuccess()) {
                            mActivity.addReviewSuccess();
                        } else {
                            onFailure("Action Fail!");
                        }
                    }

                    @Override
                    public void onFailure(Object error) {
                        super.onFailure(error);
                        mActivity.logError(error.toString());
                    }
                }, CheckerDTO.class);
    }
}
