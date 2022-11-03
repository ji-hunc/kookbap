package com.kookmin.kookbap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class WriteReview extends AppCompatActivity {
    TextView mMenu , mPrice ;
    ImageView mFood;
    EditText mReview, mAddTag;

    Button mKorfood,mChinfood,mJapfood,mVeryspicy,mSave_btn;

    String tag;

    ArrayList<ReviewData> mReviewData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        mMenu = (TextView) findViewById(R.id.myMenu);
        mPrice = (TextView) findViewById(R.id.myPrice);
        mFood = (ImageView) findViewById(R.id.myFood);

        mReview = (EditText) findViewById(R.id.myReview);

        mKorfood = (Button) findViewById(R.id.korean_food);
        mChinfood = (Button) findViewById(R.id.chinese_food);
        mJapfood = (Button) findViewById(R.id.japanese_food);
        mVeryspicy = (Button) findViewById(R.id.very_spicy);
        mSave_btn = (Button) findViewById(R.id.save_Review);

        String myreview = mReview.getText().toString();

        mAddTag = (EditText) findViewById(R.id.addTag);

        mKorfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tag = tag + "#한식";
                mAddTag.setText(tag);
            }
        });
        mChinfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tag = tag + "#중식";
                mAddTag.setText(tag);
            }
        });
        mJapfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tag = tag + "#일식";
                mAddTag.setText(tag);
            }
        });
        mVeryspicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tag = tag + "#아주매움";
                mAddTag.setText(tag);
            }
        });
        mSave_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Save_Data();
                finish();
            }
        });

    }
    public void Save_Data(){
        mReviewData = new ArrayList<ReviewData>();
        mReviewData.add(new ReviewData(mReview.getText().toString(),"",mPrice.getText().toString(),tag,0,0,0));
        return;
    }
}