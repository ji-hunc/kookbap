package com.kookmin.kookbap.ReviewRank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kookmin.kookbap.LoginAndSignup.UserData;
import com.kookmin.kookbap.MenuData;
import com.kookmin.kookbap.MenuDataAdapter;
import com.kookmin.kookbap.R;
import com.kookmin.kookbap.Retrofits.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowMoreRankActivity extends AppCompatActivity {
    TextView textView;
    RecyclerView recyclerView;
    ImageView showMoreClose;
    final int SHOW_NUM=100;

    String menuDataParameter;

    String targetData; // intent로 어떤것의 더보기인가 받아올 정보

    BestReviewerDataAdapter bestReviewerDataAdapter;
    Context context = this;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_tab);

        userID = UserData.getUserData(this).getUserId();
        targetData=getIntent().getStringExtra("targetData"); //intent값 받아오기

        //상단 제목 지정
        textView = findViewById(R.id.textView);
        textView.setText(targetData);

        recyclerView = findViewById(R.id.showMoreRecycler);
        //intent에서 보낸대로 데이터받아 화면생성.

        switch (targetData){
            case "bestReviewer":
                getPasteDataAboutReview(SHOW_NUM,recyclerView,context);
                break;
            case "mostLikeMenu":
                menuDataParameter = "total_like";
                getPasteDataAboutMenu(menuDataParameter,SHOW_NUM,recyclerView,context);
                break;
            case "starRank":
                menuDataParameter = "star_avg";
                getPasteDataAboutMenu(menuDataParameter,SHOW_NUM,recyclerView,context);
                break;
            case "countRank":
                menuDataParameter = "count_review";
                getPasteDataAboutMenu(menuDataParameter,SHOW_NUM,recyclerView,context);
            default:

        }

        // 뒤로가기 버튼 시 창 닫기
        showMoreClose = findViewById(R.id.showMoreClose);
        showMoreClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {finish();}
        }); // activity 닫기
    }

    //todo:제네릭타입으로 함수 리팩토링 할 수 있을듯

    // menu에 관련된 Rank 데이터를 가져오고, recyclerview에 붙여주는 함수.
    public void getPasteDataAboutMenu(String menuDataParameter, int SHOW_NUM, RecyclerView recyclerView, Context context){
        Call<ArrayList<MenuData>> mostLikeMenuCall;
        mostLikeMenuCall = RetrofitClient.getApiService().getMenuReviewRankData(menuDataParameter,SHOW_NUM,userID);
        mostLikeMenuCall.enqueue(new Callback<ArrayList<MenuData>>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.code()==200){
                    ArrayList<MenuData> starRankCallData=(ArrayList<MenuData>) response.body();
                    MenuDataAdapter menuDataAdapter = new MenuDataAdapter(starRankCallData, context);
                    recyclerView.setAdapter(menuDataAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                }else{
                }
            }
            @Override
            public void onFailure(Call<ArrayList<MenuData>> call, Throwable t) {
            }
        });
    }

    // review 관련된 Rank 데이터를 가져오고, recyclerview에 붙여주는 함수.
    public void getPasteDataAboutReview(int SHOW_NUM, RecyclerView recyclerView, Context context){
        Call<ArrayList<UserRankData>> rankDataCall;
        rankDataCall = RetrofitClient.getApiService().getUserReviewRankData(SHOW_NUM);
        rankDataCall.enqueue(new Callback<ArrayList<UserRankData>>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.code()==200){
                    ArrayList<UserRankData> bestReviewerData=(ArrayList<UserRankData>) response.body();
                    BestReviewerDataAdapter bestReviewerDataAdapter = new BestReviewerDataAdapter(bestReviewerData, context);
                    recyclerView.setAdapter(bestReviewerDataAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                }else{
                }
            }
            @Override
            public void onFailure(Call<ArrayList<UserRankData>> call, Throwable t) {
                Log.d("tag","fail");
            }
        });
    }
}