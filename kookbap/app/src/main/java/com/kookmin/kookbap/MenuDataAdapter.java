package com.kookmin.kookbap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kookmin.kookbap.LoginAndSignup.UserData;
import com.kookmin.kookbap.Retrofits.Result;
import com.kookmin.kookbap.Retrofits.RetrofitClient;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuDataAdapter extends RecyclerView.Adapter<MenuDataAdapter.MenuDataViewHolder> {

    ArrayList<MenuData> MenuDataArray;
    Context context;
    String userID;

    public MenuDataAdapter(ArrayList<MenuData> MenuDataArray, Context context) {
        this.MenuDataArray = MenuDataArray;
        this.context = context;
        this.userID = UserData.getUserData(context).getUserId();
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
            String url = URLConnector.URL + "images/" + MenuDataArray.get(position).getImage();
            new DownloadFilesTask().execute(url); // 이미지뷰에 외부 이미지 적용
        }
        holder.foodName.setText(MenuDataArray.get(position).getMenu_name());
        holder.foodPrice.setText("₩ " + MenuDataArray.get(position).getPrice());
        holder.foodRating.setRating(MenuDataArray.get(position).getStar_avg());
        //반올림해서 소수점 한자리까지 화면에 보여줌
        holder.foodRatingNum.setText(String.format("%.1f",MenuDataArray.get(position).getStar_avg()));
        holder.foodLikeCount.setText(Integer.toString(MenuDataArray.get(position).getTotal_like()));
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
                intent.putExtra("heartOn", holder.foodHeart.isSelected());
                intent.putExtra("menu_like_id",  MenuDataArray.get(position).getMenu_like_id());

                context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        // 하트 클릭시 하트 채우고 비우기
        holder.foodHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.foodHeart.isSelected()) {
                    holder.foodLikeCount.setText(Integer.toString(Integer.parseInt(holder.foodLikeCount.getText().toString())-1));
                    holder.foodHeart.setSelected(false);
                }
                else {
                    holder.foodLikeCount.setText(Integer.toString(Integer.parseInt(holder.foodLikeCount.getText().toString())+1));
                    holder.foodHeart.setSelected(true);
                }
//                holder.foodHeart.setSelected(!holder.foodHeart.isSelected());

//                holder.foodHeart.setClickable(false);
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        holder.foodHeart.setClickable(true);
                    }
                }, 1000);

                // card_id는 리뷰넘버, 메뉴넘버 둘다 포함함. 일단 서버로 보내면 거기서 type을 조건으로 분류함.
                int card_id = MenuDataArray.get(position).getMenu_id();
                boolean pushOrNot = holder.foodHeart.isSelected();
                String type = "menu";
                int menu_like_id = MenuDataArray.get(position).getMenu_like_id();

                // 좋아요 레트로핏 통신
                Call<Result> call = RetrofitClient.getApiService().postLikeInfo(userID, card_id, pushOrNot, type, menu_like_id, 0);
                call.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(@NotNull Call<Result> call, @NotNull Response<Result> response) {

                        // 서버에서 응답을 받아옴
                        if (response.isSuccessful() && response.body() != null) {

                            // 응답을 받아오지 못했을경우
                        } else {
                            assert response.body() != null;
                        }
                    }

                    // 통신실패시
                    @Override
                    public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {

                    }
                });
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
