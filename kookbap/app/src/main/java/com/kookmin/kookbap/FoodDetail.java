package com.kookmin.kookbap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodDetail extends AppCompatActivity {

    TextView foodDetailName, foodDetailNameSide, foodDetailPrice;
    ImageView foodDetailImage, foodDetailHeart;
    RatingBar foodDetailRating;
    FloatingActionButton addReviewButton;
    Spinner reviewSortSpinner;

    RecyclerView reviewRecyclerView;
    ReviewDataAdapter reviewDataAdapter;

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

        addReviewButton = findViewById(R.id.reviewAddReviewButton);
        addReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),WriteReview.class);
                intent.putExtra("menuName",foodDetailName.getText());
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


        Call<ArrayList<ReviewData>> call; // 원래 Retrofit 은 받아올 데이터 클래스를 정의해야 하지만, 완전 통으로 가져올 때는 따로 정의 없이 Object로 가져올 수 있음
        call = RetrofitClient.getApiService().getReviewData();
        call.enqueue(new Callback<ArrayList<ReviewData>>() {
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


        foodDetailName.setText(getIntent().getStringExtra(("foodName")));
        foodDetailNameSide.setText(getIntent().getStringExtra(("foodNameSide")));
        foodDetailPrice.setText(getIntent().getStringExtra(("price")));
        //foodDetailImage.setImageResource(getIntent().getIntExtra("image", 0));
        foodDetailRating.setRating(getIntent().getFloatExtra("rating", 0));


        foodDetailHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodDetailHeart.setSelected(!foodDetailHeart.isSelected());
            }
        });

    }
}