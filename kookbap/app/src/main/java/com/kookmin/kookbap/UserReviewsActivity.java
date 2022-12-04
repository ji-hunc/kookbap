package com.kookmin.kookbap;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.kookmin.kookbap.Retrofits.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserReviewsActivity extends AppCompatActivity {

    ArrayList<ReviewData> reviewsThatIWrote;
    RecyclerView userReviewsRecyclerView;
    ReviewDataAdapter userReviewsDataAdapter;
    TextView totalReviewCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reviews);

        userReviewsRecyclerView = findViewById(R.id.userReviewsRecyclerView);
        totalReviewCount = findViewById(R.id.totalReviewCount);

        // 유저가 작성한 리뷰 서버에서 받아옴
        Call<ArrayList<ReviewData>> call;
        // TODO userName은 변수로 받아야 함. 우선 상수로 "jihun"
        call = RetrofitClient.getApiService().getUserReviewData("jihun");
        call.enqueue(new Callback<ArrayList<ReviewData>>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.code() == 200) { // 서버로부터 OK 사인을 받았을 때
                    ArrayList<ReviewData> reviewsThatIWrote = (ArrayList<ReviewData>) response.body();
                    userReviewsDataAdapter = new ReviewDataAdapter(reviewsThatIWrote);
                    userReviewsRecyclerView.setAdapter(userReviewsDataAdapter);
                    totalReviewCount.setText("작성한 리뷰: " + Integer.toString(reviewsThatIWrote.size()) + "개");
                } else {
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        userReviewsRecyclerView.setLayoutManager(linearLayoutManager);
        userReviewsRecyclerView.setAdapter(userReviewsDataAdapter);
    }
}

