package com.eatda.ui.review;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.eatda.R;
import com.eatda.data.form.review.ReviewResponseDTO;
import com.eatda.data.form.review.ReviewResponseParcel;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ReviewContext extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_review_context, container, false);
        LinearLayout reviewContainer = v.findViewById(R.id.review_container);

        List<ReviewResponseDTO> reviews = (List<ReviewResponseDTO>) getArguments().getSerializable("reviews");
        if(reviews != null){
            for(ReviewResponseDTO review : reviews){
                View reviewView = inflater.inflate(R.layout.restaurant_item_detail_review, reviewContainer, false);

                CardView cardView = reviewView.findViewById(R.id.review_card_view);
                TextView reviewBody = reviewView.findViewById(R.id.review_body);
                TextView createdAt = reviewView.findViewById(R.id.created_at);

                // 별점 표시 로직
                setStarRating(reviewView, review.getReview_star());
                reviewBody.setText(review.getReview_body());
                createdAt.setText(review.getCreatedAt().toString());
                Log.e("ReviewContext", "리뷰 바디 :" + review.getReview_body());

                reviewContainer.addView(reviewView);
            }
        }else{
            Log.e("ReviewContext", "리뷰 널이다");
        }
        return v;
    }


    private void setStarRating(View reviewView, int rating) {
        int[] starIds = {R.id.star1, R.id.star2, R.id.star3, R.id.star4, R.id.star5};

        for (int i = 0; i < 5; i++) {
            if (i < rating) {
                reviewView.findViewById(starIds[i]).setBackgroundResource(R.drawable.star_filled);
            } else {
                reviewView.findViewById(starIds[i]).setBackgroundResource(R.drawable.star_empty);
            }
        }
    }

}

