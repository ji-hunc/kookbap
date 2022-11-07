package com.kookmin.kookbap;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;


public class UserReviewsActivity extends AppCompatActivity {

    ArrayList<ReviewData> userReviewsData;
    RecyclerView userReviewsRecyclerView;
    ReviewDataAdapter userReviewsDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reviews);

        userReviewsRecyclerView = (RecyclerView) findViewById(R.id.userReviewsRecyclerView);
        userReviewsData = new ArrayList<ReviewData>();
        userReviewsDataAdapter = new ReviewDataAdapter(userReviewsData);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        userReviewsRecyclerView.setLayoutManager(linearLayoutManager);
        userReviewsRecyclerView.setAdapter(userReviewsDataAdapter);

        loadUserReviewsData();
    }

    // 서버에서 유저의 데이터 받아오는 것 구현해야 함.
    public void loadUserReviewsData(){
//        userReviewsData.add(new MenuData("chicken", "subChicken", "17,000", "delicious", R.drawable.ic_review, 4.5f, 0));
        ArrayList<String> exReviewComment = new ArrayList<String>();
        ArrayList<String> exTag = new ArrayList<String>();
        exReviewComment.add("맛있겠다"); exReviewComment.add("와");
        exTag.add("빵"); exTag.add("부드러워요");

        userReviewsData.add(new ReviewData("맛있어요", "빵", "복지관", "kevinmj12", exReviewComment, exTag, R.drawable.test_bread_picture, 4, 2));

        userReviewsDataAdapter.notifyDataSetChanged();
    }

}

