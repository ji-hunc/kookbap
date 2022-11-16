package com.kookmin.kookbap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReviewDataAdapter extends RecyclerView.Adapter<ReviewDataAdapter.ReviewDataViewHolder>{
    ArrayList<ReviewData> reviewDataArray;
    Context context;

    public ReviewDataAdapter(ArrayList<ReviewData> reviewDataArray) {
        this.reviewDataArray = reviewDataArray;
        // reviewDataArray 에는 ReviewData 객체가 여러개 들어있음
    }

    @NonNull
    @Override
    public ReviewDataAdapter.ReviewDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //LayoutInflater inflater = LayoutInflater.from(context);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_card,parent,false);
        return new ReviewDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewDataAdapter.ReviewDataViewHolder holder, int position) {
        holder.reviewContext.setText(reviewDataArray.get(position).getDescription());
        // TODO image는 임시로 heart
//        holder.reviewImage.setImageResource(R.drawable.ic_filled_heart);
        String url = "https://kookbap.run.goorm.io/images/" + reviewDataArray.get(position).getImage();
        holder.webView.loadUrl(url);
        holder.webView.setFocusable(false);
        Log.e("url", reviewDataArray.get(position).getImage());
        holder.reviewReviewerName.setText(reviewDataArray.get(position).getReview_user_id());
        holder.reviewRating.setRating(reviewDataArray.get(position).getStar());



//        holder.likeImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                holder.likeImage.setSelected(!holder.likeImage.isSelected());
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return reviewDataArray.size();
    }

    public class ReviewDataViewHolder extends RecyclerView.ViewHolder {
        TextView reviewContext, reviewReviewerName;
        ImageView reviewImage, likeImage;
        RatingBar reviewRating;
        WebView webView;
        //LinearLayout reviewCardLayout;
        public ReviewDataViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewContext = itemView.findViewById(R.id.reviewContextTextView);
            reviewReviewerName = itemView.findViewById(R.id.reviewReviewerID);
//            reviewImage = itemView.findViewById((R.id.reviewFoodImage));
            webView = itemView.findViewById(R.id.ID_IMG);
            //likeImage = itemView.findViewById(R.id.)
            reviewRating = itemView.findViewById(R.id.reviewRatingBar);
            //reviewCardLayout = itemView.findViewById(R.id.reviewCardView);
        }
    }
}
