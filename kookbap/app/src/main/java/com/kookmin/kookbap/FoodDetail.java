package com.kookmin.kookbap;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kookmin.kookbap.Retrofits.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodDetail extends AppCompatActivity {

    TextView foodDetailName, foodDetailNameSide, foodDetailPrice, foodDetailRatingNum, foodDetailRestaurant;
    ImageView foodDetailImage, foodDetailHeart;
    RatingBar foodDetailRating;
    FloatingActionButton addReviewButton;
    Spinner reviewSortSpinner;

    RecyclerView reviewRecyclerView;
    ReviewDataAdapter reviewDataAdapter;


    @Override
    protected void onRestart() {
        String menuName = getIntent().getStringExtra("foodName");

        super.onRestart();
        Call<ArrayList<ReviewData>> call;
        // selectedWay를 함수 파라미터로 전달해서 db에서 정렬 후 받아옴.
        call = RetrofitClient.getApiService().getReviewData(menuName,"최신순", "jihun");
        call.enqueue(new Callback<ArrayList<ReviewData>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.code() == 200) { // 서버로부터 OK 사인을 받았을 때
                    ArrayList<ReviewData> reviewDataArrayList = (ArrayList<ReviewData>) response.body();
                    reviewDataAdapter = new ReviewDataAdapter(reviewDataArrayList);
                    reviewRecyclerView.setAdapter(reviewDataAdapter);
                } else {
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        foodDetailName = findViewById(R.id.foodDetailName);
        foodDetailNameSide = findViewById(R.id.foodDetailNameSide);
        foodDetailPrice = findViewById(R.id.foodDetailPrice);
        //foodDetailImage = findViewById(R.id.foodDetailImage);
        foodDetailHeart = findViewById(R.id.foodDetailHeart);
        foodDetailRating = findViewById(R.id.foodDetailRatingBar);
        foodDetailRatingNum = findViewById(R.id.foodDetailRatingNum);
        foodDetailRestaurant = findViewById(R.id.foodDetailRestaurant);

        addReviewButton = findViewById(R.id.reviewAddReviewButton);
        addReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // FoodDetail에서 받은 메뉴정보들 리뷰작성 페이지로 전달
                Intent intent = new Intent(getApplicationContext(),WriteReview.class);
                intent.putExtra("menuId", getIntent().getIntExtra("menuId", 0));
                Log.e("menuId FoodDetail", Integer.toString(getIntent().getIntExtra("menuId", 0)));
                intent.putExtra("signal", 2); // INFORMED_WRITE. 메뉴정보를 가지고 리뷰작성 페이지로 이동하는 경우
                intent.putExtra("restaurantName", getIntent().getStringExtra("restaurantName"));
                intent.putExtra("foodName", getIntent().getStringExtra("foodName"));
                startActivity(intent);
            }
        });

        reviewSortSpinner = findViewById(R.id.reviewSortSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.reviewSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reviewSortSpinner.setAdapter(adapter);




        reviewRecyclerView = findViewById(R.id.reviewRecyclerView);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
//        reviewDataAdapter = new ReviewDataAdapter(currentReviewData);
        reviewRecyclerView.setAdapter(reviewDataAdapter);


        // 리퀘스트 파라미터에 넣을 메뉴 이름 스트링 정의
        String menuName = getIntent().getStringExtra("foodName");
        // 서버에 메뉴 이름에 해당하는 리뷰 요청
        // 메뉴에 해당하는 리뷰들을 ArrayList 형식으로 가져옴. 각각의 리뷰객체들이 들어있음
         // 원래 Retrofit 은 받아올 데이터 클래스를 정의해야 하지만, 완전 통으로 가져올 때는 따로 정의 없이 Object로 가져올 수 있음

        LottieAnimationView animationView = findViewById(R.id.lottie);
        animationView.setVisibility(View.VISIBLE);
        animationView.setAnimation("loading.json");
        animationView.setRepeatCount(LottieDrawable.INFINITE);
        animationView.playAnimation();

        reviewSortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedWay = adapterView.getSelectedItem().toString();
                Call<ArrayList<ReviewData>> call;
                // selectedWay를 함수 파라미터로 전달해서 db에서 정렬 후 받아옴.
                //todo : 'jongbin'부분 userid 받아오게 변경
                call = RetrofitClient.getApiService().getReviewData(menuName,selectedWay,"jihun");
                call.enqueue(new Callback<ArrayList<ReviewData>>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) {
                        if (response.code() == 200) { // 서버로부터 OK 사인을 받았을 때
                            ArrayList<ReviewData> reviewDataArrayList = (ArrayList<ReviewData>) response.body();
                            reviewDataAdapter = new ReviewDataAdapter(reviewDataArrayList);
                            reviewRecyclerView.setAdapter(reviewDataAdapter);
                        } else {
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                        Log.e("Error", t.getMessage());
                    }
                });
                animationView.cancelAnimation();
                animationView.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // 리뷰 페이지 최상단 정보 내용 초기화 부분
        foodDetailName.setText(menuName);
        foodDetailNameSide.setText(getIntent().getStringExtra(("foodNameSide")));
        foodDetailPrice.setText("₩ " + getIntent().getIntExtra("price", 0));
        //foodDetailImage.setImageResource(getIntent().getIntExtra("image", 0));
        foodDetailRating.setRating(getIntent().getFloatExtra("rating", 0));
        foodDetailRatingNum.setText(getIntent().getStringExtra("ratingNum"));
        foodDetailRestaurant.setText(getIntent().getStringExtra("restaurantName"));


        foodDetailHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodDetailHeart.setSelected(!foodDetailHeart.isSelected());
            }
        });

    }
}