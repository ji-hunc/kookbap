package com.kookmin.kookbap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class FoodDetail extends AppCompatActivity {

    TextView foodDetailName, foodDetailNameSide, foodDetailPrice;
    ImageView foodDetailImage, foodDetailHeart;
    RatingBar foodDetailRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        foodDetailName = findViewById(R.id.foodDetailName);
        foodDetailNameSide = findViewById(R.id.foodDetailNameSide);
        foodDetailPrice = findViewById(R.id.foodDetailPrice);
        foodDetailImage = findViewById(R.id.foodDetailImage);
        foodDetailHeart = findViewById(R.id.foodDetailHeart);
        foodDetailRating = findViewById(R.id.foodDetailRatingBar);

        foodDetailName.setText(getIntent().getStringExtra("foodName"));
        foodDetailNameSide.setText(getIntent().getStringExtra(("foodNameSide")));
        foodDetailPrice.setText(getIntent().getStringExtra(("price")));
        foodDetailImage.setImageResource(getIntent().getIntExtra("image", 0));
        // TODO
        // 하트 빈거 찬거 구별하기
//        foodDetailHeart.setImageResource(getIntent().getIntExtra("heart", 0));
        foodDetailRating.setRating(getIntent().getFloatExtra("rating", 0));

    }
}