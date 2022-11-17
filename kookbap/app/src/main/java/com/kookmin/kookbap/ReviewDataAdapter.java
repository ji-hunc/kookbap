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
        holder.webView.getSettings().setUseWideViewPort(true);
        holder.webView.getSettings().setLoadWithOverviewMode(true);
        Log.e("url", reviewDataArray.get(position).getImage());
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
                            // TODO 수정하기 누르면 글쓰기 액티비티창 띄우고 쓴거 그대로 복붙하고 수정하기 누르면 업데이트
                            case R.id.editMenuDoEditReview:
                                Intent intent = new Intent(view.getContext(),WriteReview.class);
                                view.getContext().startActivity(intent);
                                break;

                            case R.id.editMenuDoDeleteReview:
                                AlertDialog.Builder adBuilder = new AlertDialog.Builder(view.getContext());
                                adBuilder.setMessage("삭제하시겠습니까?")
                                         .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // TODO 삭제하기 누르면 해당 글이 삭제가 되도록 함
                                        //사라져라 얍!
                                        Toast.makeText(view.getContext(),holder.reviewContext.getText().toString()+"가 삭제됨.",Toast.LENGTH_SHORT).show();
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
        ImageView editMenuImageButton, likeImage;
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
