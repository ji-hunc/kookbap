package com.kookmin.kookbap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class ReviewTabActivity extends AppCompatActivity {
    TextView textView;
    RecyclerView recyclerView;
    ImageView showMoreClose;

    String targetData; // intent로 어떤것의 더보기인가 받아올 정보

    ArrayList<BestReviewerData> bestReviewerData;
    BestReviewerDataAdapter bestReviewerDataAdapter;
    ArrayList<MenuData> starRankReviewData;
    MenuDataAdapter reviewDataAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_tab);
        targetData=getIntent().getStringExtra("targetData"); //intent값 받아오기

        //상단 제목 지정
        textView = findViewById(R.id.textView);
        textView.setText(targetData);

        //testdata 생성, 리사이클러뷰 지정
        createTestData();
        recyclerView = findViewById(R.id.showMoreRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //intent에서 보낸대로 데이터받아 화면생성.
        switch (targetData){
            case "bestReviewer":
                bestReviewerDataAdapter = new BestReviewerDataAdapter();
                recyclerView.setAdapter(bestReviewerDataAdapter);
                bestReviewerDataAdapter.setBestReviewerData(bestReviewerData);
                break;

            default:
                reviewDataAdapter = new MenuDataAdapter(starRankReviewData, this.getApplicationContext());
                recyclerView.setAdapter(reviewDataAdapter);

        }
        // 뒤로가기 버튼 시 창 닫기
        showMoreClose = findViewById(R.id.showMoreClose);
        showMoreClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {finish();}
        }); // activity 닫기
    }

    //test를 위한 함수
    public void createTestData(){
        bestReviewerData = new ArrayList<>();
        starRankReviewData = new ArrayList<>();
        for( int i =0; i<10; i++){
            String name = i +"등";
            int rank = i;
            starRankReviewData.add(new MenuData( name, "아직 작성된 리뷰가 없습니다.", "unknown", "delicious", R.drawable.test_bread_picture, (float) (Math.random()*5), 0));
            bestReviewerData.add(new BestReviewerData( rank,"uzznknown",(10-i)*100));
        }
    }
}