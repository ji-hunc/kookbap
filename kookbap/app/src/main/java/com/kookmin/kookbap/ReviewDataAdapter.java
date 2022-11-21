package com.kookmin.kookbap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kookmin.kookbap.Retrofits.Result;
import com.kookmin.kookbap.Retrofits.RetrofitClient;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
//        String url = "https://kookbap.run.goorm.io/images/" + reviewDataArray.get(position).getImage();
        String url = "http://10.0.2.2:3000/images/" + reviewDataArray.get(position).getImage();
        holder.webView.loadUrl(url);
        holder.webView.setFocusable(false);
        holder.webView.getSettings().setUseWideViewPort(true);
        holder.webView.getSettings().setLoadWithOverviewMode(true);
        holder.reviewReviewerName.setText(reviewDataArray.get(position).getReview_user_id());
        holder.reviewRating.setRating(reviewDataArray.get(position).getStar());
        holder.reviewDate.setText(reviewDataArray.get(position).getWrite_date().toString().substring(0, 10));


        // TODO 메뉴 버튼을 눌렀을 때, 본인이면 신고/수정/삭제하기<->아니면 신고하기만, 현재는 다 가능
        holder.editMenuImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                popupMenu.getMenuInflater().inflate(R.menu.review_own_edit_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.editMenuDoEditReview:
                                // 수정하기모드로 리뷰작성페이지 들어갈시에 초기화에 필요한 데이터들과, DB에서 key로 쓰일 review_number를 넘겨줌
                                Intent intent = new Intent(view.getContext(),WriteReview.class);
                                intent.putExtra("signal", 3); // signal: 3 수정하기 모드
                                intent.putExtra("review_number", reviewDataArray.get(position).getReview_number());
                                intent.putExtra("foodName", reviewDataArray.get(position).getMenu_name());
                                intent.putExtra("star", reviewDataArray.get(position).getStar());
                                intent.putExtra("description", reviewDataArray.get(position).getDescription());
                                intent.putExtra("imageUrl", reviewDataArray.get(position).getImage());
                                view.getContext().startActivity(intent);
                                break;

                            case R.id.editMenuDoDeleteReview:
                                AlertDialog.Builder adBuilder = new AlertDialog.Builder(view.getContext());
                                adBuilder.setMessage("삭제하시겠습니까?")
                                         .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        // DB에서 reivew 테이블의 키로 쓰일 review_number를 인텐트로 받아옴
                                        int reviewNumber = reviewDataArray.get(position).getReview_number();

                                        // 레트로핏 수정하는 함수 deleteReview(int reviewNumber)를 부름
                                        Call<Result> call = RetrofitClient.getApiService().deleteReview(reviewNumber);
                                        call.enqueue(new Callback<Result>() {
                                            @Override
                                            public void onResponse(@NotNull Call<Result> call, @NotNull Response<Result> response) {

                                                // 서버에서 응답을 받아옴
                                                if (response.isSuccessful() && response.body() != null) {
                                                    Boolean success = response.body().getSuccess();
                                                    String message = response.body().getMessage();
                                                    // 입력성공시 서버에서 메시지를 받아와서 테스트용으로 토스트로 출력
                                                    // TODO 서버에서 상황에 따라 다른 결과를 전달해줘야함. 일단 GOOD만 보내도록 되어있음
                                                    if (success) {
                                                        Log.e("LOGLOG", "success1");
                                                        Toast.makeText(view.getContext(),"정상적으로 삭제되었습니다.",Toast.LENGTH_SHORT).show();
                                                        Log.e("서버에서 받아온내용", message);
                                                    } else {
                                                        Log.e("LOGLOG", "success2");
                                                    }
                                                    // 응답을 받아오지 못했을경우
                                                } else {
                                                    assert response.body() != null;
                                                    Log.e("LOGLOG", "success3");
                                                }
                                            }

                                            // 통신실패시
                                            @Override
                                            public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {
                                                Log.e("LOGLOG", "success4");
                                            }
                                        });
                                        //사라져라 얍!
                                        dialogInterface.dismiss();
                                    }
                                })
                                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                        .create().show();
                                break;
                            case R.id.editMenuDoReportReview:
                                String[] array = view.getContext().getResources().getStringArray(R.array.reportArray);
                                AlertDialog.Builder adB = new AlertDialog.Builder(view.getContext());
                                adB.setTitle("신고 사유 선택")
                                        .setItems(array, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                //TODO 신고 구현 마지막에?
                                                Toast.makeText(view.getContext(),array[i]+"로 신고가 접수되었습니다.",Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .create().show();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviewDataArray.size();
    }

    public class ReviewDataViewHolder extends RecyclerView.ViewHolder {
        TextView reviewContext, reviewReviewerName, reviewDate;
        ImageView reviewImage, likeImage, editMenuImageButton;
        RatingBar reviewRating;
        WebView webView;
        //LinearLayout reviewCardLayout;
        public ReviewDataViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewContext = itemView.findViewById(R.id.reviewContextTextView);
            reviewReviewerName = itemView.findViewById(R.id.reviewReviewerID);
            reviewDate = itemView.findViewById(R.id.reviewDate);
//            reviewImage = itemView.findViewById((R.id.reviewFoodImage));
            webView = itemView.findViewById(R.id.ID_IMG);
            reviewRating = itemView.findViewById(R.id.reviewRatingBar);
            //reviewCardLayout = itemView.findViewById(R.id.reviewCardView);
            editMenuImageButton = itemView.findViewById(R.id.reviewEditMenuImageView);
        }
    }
}
