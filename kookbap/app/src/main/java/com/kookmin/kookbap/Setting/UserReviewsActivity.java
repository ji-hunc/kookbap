package com.kookmin.kookbap.Setting;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.kookmin.kookbap.LoginAndSignup.UserData;
import com.kookmin.kookbap.R;
import com.kookmin.kookbap.Retrofits.RetrofitClient;
import com.kookmin.kookbap.ReviewData;
import com.kookmin.kookbap.ReviewDataAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserReviewsActivity extends AppCompatActivity {

    ArrayList<ReviewData> reviewsThatIWrote;
    RecyclerView userReviewsRecyclerView;
    ReviewDataAdapter userReviewsDataAdapter;
    TextView totalReviewCount;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reviews);

        userID = UserData.getUserData(this).getUserId(); // 프리퍼런스에서 Id 받아옴
        userReviewsRecyclerView = findViewById(R.id.userReviewsRecyclerView);
        totalReviewCount = findViewById(R.id.totalReviewCount);

        // 유저가 작성한 리뷰 서버에서 받아옴
        Call<ArrayList<ReviewData>> call;
        call = RetrofitClient.getApiService().getUserReviewData(userID);
        call.enqueue(new Callback<ArrayList<ReviewData>>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.code() == 200) { // 서버로부터 OK 사인을 받았을 때
                    ArrayList<ReviewData> reviewsThatIWrote = (ArrayList<ReviewData>) response.body();
                    userReviewsDataAdapter = new ReviewDataAdapter(reviewsThatIWrote,userID);
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

