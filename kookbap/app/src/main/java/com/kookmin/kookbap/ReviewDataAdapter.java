package com.kookmin.kookbap;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReviewDataAdapter extends RecyclerView.Adapter<ReviewDataAdapter.ReviewDataViewHolder>{
    ArrayList<ReviewData> ReviewDataArray;
    Context context;

    public ReviewDataAdapter(ArrayList<ReviewData> reviewDataArray) {
        ReviewDataArray = reviewDataArray;
        //this.context = context;
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
        holder.reviewContext.setText(ReviewDataArray.get(position).getReviewContext());
        holder.reviewTags.setText(ReviewDataArray.get(position).getReviewTags().toString());
        holder.reviewImage.setImageResource(ReviewDataArray.get(position).getReviewImage());
        holder.reviewReviewerName.setText(ReviewDataArray.get(position).getReviewReviewerName());
        holder.reviewRating.setRating(ReviewDataArray.get(position).getReviewStars());



//        holder.likeImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                holder.likeImage.setSelected(!holder.likeImage.isSelected());
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return ReviewDataArray.size();
    }

    public class ReviewDataViewHolder extends RecyclerView.ViewHolder {
        TextView reviewContext, reviewTags, reviewReviewerName;
        ImageView reviewImage, likeImage;
        RatingBar reviewRating;
        //LinearLayout reviewCardLayout;
        public ReviewDataViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewContext = itemView.findViewById(R.id.reviewContextTextView);
            reviewReviewerName = itemView.findViewById(R.id.reviewReviewerID);
            reviewTags = itemView.findViewById(R.id.reviewTag);
            reviewImage = itemView.findViewById((R.id.reviewFoodImage));
            //likeImage = itemView.findViewById(R.id.)
            reviewRating = itemView.findViewById(R.id.reviewRatingBar);
            //reviewCardLayout = itemView.findViewById(R.id.reviewCardView);
        }
    }
}
