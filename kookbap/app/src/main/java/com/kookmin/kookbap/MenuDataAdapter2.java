package com.kookmin.kookbap;

import android.content.Context;
import android.content.Intent;
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

public class MenuDataAdapter2 extends RecyclerView.Adapter<MenuDataAdapter2.MenuDataViewHolder> {

    ArrayList<MenuData2> MenuDataArray;
    Context context;

    public MenuDataAdapter2(ArrayList<MenuData2> MenuDataArray, Context context) {
        this.MenuDataArray = MenuDataArray;
        this.context = context;
//        Log.e("size", Integer.toString(MenuDataArray.size()));
    }

    @NonNull
    @Override
    public MenuDataAdapter2.MenuDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.menu_card, parent, false);
        return new MenuDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuDataAdapter2.MenuDataViewHolder holder, final int position) {

//        holder.foodNameSide.setText(MenuDataArray.get(position).getSubMenuName());
//        holder.foodImage.setImageResource(MenuDataArray.get(position).getImage());
        holder.foodImage.setImageResource(R.drawable.ic_spoon);
        holder.foodName.setText(MenuDataArray.get(position).getMenu_name());
        holder.foodPrice.setText("₩ " + MenuDataArray.get(position).getPrice());
        holder.foodRating.setRating(MenuDataArray.get(position).getStar_avg());
        //반올림해서 소수점 한자리까지 화면에 보여줌
        holder.foodRatingNum.setText(String.format("%.1f",MenuDataArray.get(position).getStar_avg()));

        holder.cardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // FoodDetail 페이지(메뉴 클릭했을 때 나오는 리뷰 리스트 페이지)로 클릭한 메뉴 데이터 정보 intent로 전달
                Intent intent = new Intent(context, FoodDetail.class);
//                intent.putExtra("image", MenuDataArray.get(position).getImage());
//                intent.putExtra("heart", MenuDataArray.get(position).getHeart());
//                intent.putExtra("foodNameSide", MenuDataArray.get(position).getSubMenuName());
                intent.putExtra("menuId", MenuDataArray.get(position).getMenu_id());
                intent.putExtra("foodName", MenuDataArray.get(position).getMenu_name());
                intent.putExtra("price", MenuDataArray.get(position).getPrice());
                intent.putExtra("rating", MenuDataArray.get(position).getStar_avg());
                intent.putExtra("ratingNum", String.format("%.1f",MenuDataArray.get(position).getStar_avg()));
                intent.putExtra("restaurantName", MenuDataArray.get(position).getRestaurant_name());

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

        TextView foodName, foodNameSide, foodPrice, foodRatingNum;
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
            foodRatingNum = itemView.findViewById(R.id.foodRatingNum);
        }
    }
}
