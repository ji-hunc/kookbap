package com.kookmin.kookbap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FoodDetail extends AppCompatActivity {

    TextView foodDetailName, foodDetailNameSide, foodDetailPrice;
    ImageView foodDetailImage, foodDetailHeart;
    RatingBar foodDetailRating;
    FloatingActionButton addReviewButton;
    Spinner reviewSortSpinner;

    RecyclerView reviewRecyclerView;
    ReviewDataAdapter reviewDataAdapter;
    //임시
    ArrayList<ReviewData> currentReviewData;

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

        reviewSortSpinner = (Spinner) findViewById(R.id.reviewSortSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.reviewSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reviewSortSpinner.setAdapter(adapter);

        //디버깅
        currentReviewData = new ArrayList<ReviewData>();
        for(int i =0;i<5;i++){
            currentReviewData.add(new ReviewData("너무 맛있어요!",foodDetailName.getText().toString(),"학생식당","조현민",new ArrayList<String>(),new ArrayList<String>(),R.drawable.test_bread_picture,4.5f,i));
        }
        reviewRecyclerView = (RecyclerView) findViewById(R.id.reviewRecyclerView);
//        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        reviewDataAdapter = new ReviewDataAdapter(currentReviewData);
        reviewRecyclerView.setAdapter(reviewDataAdapter);



        foodDetailName.setText(getIntent().getStringExtra("foodName"));
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