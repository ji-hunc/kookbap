package com.kookmin.kookbap;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
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

public class ReviewDataAdapter extends RecyclerView.Adapter<ReviewDataAdapter.ReviewDataViewHolder> {

    ArrayList<ReviewData> reviewData;
    Context context;

    public ReviewDataAdapter(ArrayList<ReviewData> reviewData, Context context) {
        this.reviewData = reviewData;
        this.context = context;
//        Log.e("size", Integer.toString(reviewData.size()));
    }

    @NonNull
    @Override
    public ReviewDataAdapter.ReviewDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.review_card, parent, false);
        return new ReviewDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewDataAdapter.ReviewDataViewHolder holder, final int position) {
        holder.foodName.setText(reviewData.get(position).getMenuName());
        holder.foodNameSide.setText(reviewData.get(position).getSubMenuName());
        holder.foodPrice.setText(reviewData.get(position).getPrice());
        holder.foodImage.setImageResource(reviewData.get(position).getImage());
        holder.foodRating.setRating(reviewData.get(position).getStars());

        holder.cardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FoodDetail.class);
                intent.putExtra("foodName", reviewData.get(position).getMenuName());
                intent.putExtra("foodNameSide", reviewData.get(position).getSubMenuName());
                intent.putExtra("price", reviewData.get(position).getPrice());
                intent.putExtra("image", reviewData.get(position).getImage());
                intent.putExtra("heart", reviewData.get(position).getHeart());
                intent.putExtra("rating", reviewData.get(position).getStars());
                context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        // 하트 클릭시 하트 채우고 비우기
        holder.foodHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.foodHeart.setSelected(!holder.foodHeart.isSelected());

            }
        });
    }

    @Override
    public int getItemCount() {
        return reviewData.size();
    }

    public class ReviewDataViewHolder extends RecyclerView.ViewHolder {

        TextView foodName, foodNameSide, foodPrice;
        ImageView foodImage, foodHeart;
        RatingBar foodRating;
        LinearLayout cardLayout;

        public ReviewDataViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
            foodNameSide = itemView.findViewById(R.id.foodNameSide);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            foodImage = itemView.findViewById(R.id.foodImage);
            foodHeart = itemView.findViewById(R.id.foodHeart);
            foodRating = itemView.findViewById(R.id.foodRatingBar);
            cardLayout = itemView.findViewById(R.id.cardLayout);
        }
    }
}
