package com.eatda.data.form.review;

import android.os.Parcel;
import android.os.Parcelable;

public class ReviewResponseParcel implements Parcelable {
    private int id;
    private int review_star;
    private String review_body;
    private String createdAt;
    private int childId;
    private int restaurantId;

    // 생성자
    public ReviewResponseParcel(int id, int review_star, String review_body, String createdAt, int childId, int restaurantId) {
        this.id = id;
        this.review_star = review_star;
        this.review_body = review_body;
        this.createdAt = createdAt;
        this.childId = childId;
        this.restaurantId = restaurantId;
    }

    // Parcelable 구현
    protected ReviewResponseParcel(Parcel in) {
        id = in.readInt();
        review_star = in.readInt();
        review_body = in.readString();
        createdAt = in.readString();
        childId = in.readInt();
        restaurantId = in.readInt();
    }

    public static final Parcelable.Creator<ReviewResponseParcel> CREATOR = new Creator<ReviewResponseParcel>() {
        @Override
        public ReviewResponseParcel createFromParcel(Parcel in) {
            return new ReviewResponseParcel(in);
        }

        @Override
        public ReviewResponseParcel[] newArray(int size) {
            return new ReviewResponseParcel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(review_star);
        dest.writeString(review_body);
        dest.writeString(createdAt);
        dest.writeInt(childId);
        dest.writeInt(restaurantId);
    }

    public int getId() {
        return id;
    }

    public int getReview_star() {
        return review_star;
    }

    public String getReview_body() {
        return review_body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getChildId() {
        return childId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }
}
