package com.kookmin.kookbap;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;


public class UserReviewsActivity extends AppCompatActivity {

    ArrayList<MenuData> userReviewsData;
    RecyclerView userReviewsRecyclerView;
    MenuDataAdapter userReviewsDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reviews);

        userReviewsRecyclerView = (RecyclerView) findViewById(R.id.userReviewsRecyclerView);
        userReviewsData = new ArrayList<>();
        userReviewsDataAdapter = new MenuDataAdapter(userReviewsData, this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        userReviewsRecyclerView.setLayoutManager(linearLayoutManager);
        userReviewsRecyclerView.setAdapter(userReviewsDataAdapter);

        loadUserReviewsData();
    }

    // 서버에서 유저의 데이터 받아오는 것 구현해야 함.
    public void loadUserReviewsData(){
        userReviewsData.add(new MenuData("chicken", "subChicken", "17,000", "delicious", R.drawable.ic_review, 4.5f, 0));
        userReviewsData.add(new MenuData("hamburger", "subHamburger", "7,000", "good", R.drawable.ic_home, 3.5f, 0));
        userReviewsData.add(new MenuData("suntofu", "subsuntofu", "6,000", "good", R.drawable.ic_home, 3.5f, 0));

        userReviewsDataAdapter.notifyDataSetChanged();
    }

}

