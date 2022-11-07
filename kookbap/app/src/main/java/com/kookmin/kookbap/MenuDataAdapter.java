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

public class MenuDataAdapter extends RecyclerView.Adapter<MenuDataAdapter.MenuDataViewHolder> {

    ArrayList<MenuData> MenuDataArray;
    Context context;

    public MenuDataAdapter(ArrayList<MenuData> MenuDataArray, Context context) {
        this.MenuDataArray = MenuDataArray;
        this.context = context;
//        Log.e("size", Integer.toString(MenuDataArray.size()));
    }

    @NonNull
    @Override
    public MenuDataAdapter.MenuDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.menu_card, parent, false);
        return new MenuDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuDataAdapter.MenuDataViewHolder holder, final int position) {
        holder.foodName.setText(MenuDataArray.get(position).getMenuName());
        holder.foodNameSide.setText(MenuDataArray.get(position).getSubMenuName());
        holder.foodPrice.setText(MenuDataArray.get(position).getPrice());
        holder.foodImage.setImageResource(MenuDataArray.get(position).getImage());
        holder.foodRating.setRating(MenuDataArray.get(position).getStars());

        holder.cardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FoodDetail.class);
                intent.putExtra("foodName", MenuDataArray.get(position).getMenuName());
                intent.putExtra("foodNameSide", MenuDataArray.get(position).getSubMenuName());
                intent.putExtra("price", MenuDataArray.get(position).getPrice());
                intent.putExtra("image", MenuDataArray.get(position).getImage());
                intent.putExtra("heart", MenuDataArray.get(position).getHeart());
                intent.putExtra("rating", MenuDataArray.get(position).getStars());
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
        return MenuDataArray.size();
    }

    public static class MenuDataViewHolder extends RecyclerView.ViewHolder {

        TextView foodName, foodNameSide, foodPrice;
        ImageView foodImage, foodHeart;
        RatingBar foodRating;
        LinearLayout cardLayout;

        public MenuDataViewHolder(@NonNull View itemView) {
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