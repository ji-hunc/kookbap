package com.kookmin.kookbap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MenuDataAdapter2 extends RecyclerView.Adapter<MenuDataAdapter2.MenuDataViewHolder> {

    ArrayList<MenuData2> MenuDataArray;
    Context context;

    public MenuDataAdapter2(ArrayList<MenuData2> MenuDataArray, Context context) {
        this.MenuDataArray = MenuDataArray;
        this.context = context;
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

        holder.foodNameSide.setText(MenuDataArray.get(position).getSubMenu());
        if (MenuDataArray.get(position).getImage() == null) {
            holder.foodImage.setImageResource(R.drawable.ic_spoon);
        } else {
            // 외부이미지 이미지뷰에 적용해주는 클래스
            class DownloadFilesTask extends AsyncTask<String,Void, Bitmap> {
                @Override
                protected Bitmap doInBackground(String... strings) {
                    Bitmap bmp = null;
                    try {
                        String img_url = strings[0]; //url of the image
                        URL url = new URL(img_url);
                        bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return bmp;
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }


                @Override
                protected void onPostExecute(Bitmap result) {
                    // doInBackground 에서 받아온 total 값 사용 장소
                    holder.foodImage.setImageBitmap(result);
                }
            }
            String url = "https://kookbap.run.goorm.io/images/" + MenuDataArray.get(position).getImage();
            new DownloadFilesTask().execute(url); // 이미지뷰에 외부 이미지 적용
        }
        holder.foodName.setText(MenuDataArray.get(position).getMenu_name());
        holder.foodPrice.setText("₩ " + MenuDataArray.get(position).getPrice());
        holder.foodRating.setRating(MenuDataArray.get(position).getStar_avg());
        //반올림해서 소수점 한자리까지 화면에 보여줌
        holder.foodRatingNum.setText(String.format("%.1f",MenuDataArray.get(position).getStar_avg()));
        holder.foodLikeCount.setText("좋아요 : " + Integer.toString(MenuDataArray.get(position).getTotal_like()));
        if (MenuDataArray.get(position).isUserLikeTrueFalse() == 1) {
            holder.foodHeart.setSelected(!holder.foodHeart.isSelected());
        }

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

        TextView foodName, foodNameSide, foodPrice, foodRatingNum, foodLikeCount;
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
            foodLikeCount = itemView.findViewById(R.id.menuLikeCount);
        }
    }
}
