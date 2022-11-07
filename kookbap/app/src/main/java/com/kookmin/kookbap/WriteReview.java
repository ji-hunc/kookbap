package com.kookmin.kookbap;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.kookmin.kookbap.cafeteriaFragments.DatePickerFragment;

import java.io.FileNotFoundException;
import java.io.IOException;

public class WriteReview extends AppCompatActivity {
    TextView mDate_Text;
    ImageView mFood;
    EditText mReview, mAddTag;

    Button mKorfood,mChinfood,mJapfood,mVeryspicy,mSave_btn,mDate_btn;

    String tag = "";


    String[] items = {"메뉴1","메뉴2","메뉴3"};


    int cameraPermission, galleryPermission;
    private static final int SINGLE_PERMISSION = 1004;
    Switch settingNotice;
    Bitmap imageBitmap, noticeBitmap;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        mDate_Text = (TextView) findViewById(R.id.write_review_dateText);

        mFood = (ImageView) findViewById(R.id.myFood);

        mReview = (EditText) findViewById(R.id.myReview);

        mKorfood = (Button) findViewById(R.id.korean_food);
        mChinfood = (Button) findViewById(R.id.chinese_food);
        mJapfood = (Button) findViewById(R.id.japanese_food);
        mVeryspicy = (Button) findViewById(R.id.very_spicy);
        mSave_btn = (Button) findViewById(R.id.save_Review);
        mDate_btn = (Button) findViewById(R.id.write_review_datebtn);

        //String myreview = mReview.getText().toString();

        mAddTag = (EditText) findViewById(R.id.addTag);

        mDate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });


        Spinner spinner = findViewById(R.id.write_review_toDayMenu);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                items);

        spinner.setAdapter(adapter);
        spinner.setSelection(0);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        mFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 카메라 및 앨범 권한 확인
                cameraPermission = ContextCompat.checkSelfPermission(WriteReview.this, Manifest.permission.CAMERA);
                galleryPermission = ContextCompat.checkSelfPermission(WriteReview.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                // 카메라와 앨범 중 원하는 방법을 고르기 위한 dialog 출력
                AlertDialog.Builder dlg = new AlertDialog.Builder(WriteReview.this);
                dlg.setTitle("프로필 사진 설정하기");
                final String[] selectProfileImages = new String[]{"카메라로 사진 찍기", "앨범에서 사진 가져오기"};
                dlg.setItems(selectProfileImages, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 카메라로 사진 찍기
                        if (which == 0) {
                            // 카메라 권한이 있다면 카메라 실행
                            if (cameraPermission == PackageManager.PERMISSION_GRANTED) {
                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                activityResultCamera.launch(cameraIntent);
                            }

                        }
                        // 앨범에서 사진 가져오기
                        else if (which == 1) {
                            // 파일 접근 권한이 있다면 앨범 실행
                            if (galleryPermission == PackageManager.PERMISSION_GRANTED) {
                                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                                galleryIntent.setType("image/*");
                                activityResultGallery.launch(galleryIntent);
                            }
                            // 파일 접근 권한이 없다면 권한 요청

                        }
                    }
                });
                AlertDialog dialog = dlg.create();
                dialog.show();
            }
        });


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
                finish();
            }
        });
    }
    // 권한 요청 이후 권한 결과
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case SINGLE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 카메라
                    if (permissions[0].equals("android.permission.CAMERA")) {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        activityResultCamera.launch(cameraIntent);
                    }
                    // 갤러리
                    else if (permissions[0].equals("android.permission.READ_EXTERNAL_STORAGE")) {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                        galleryIntent.setType("image/*");
                        activityResultGallery.launch(galleryIntent);
                    }
                }
                // 권한 요청 필요 토스트 출력
                else {
                    Toast.makeText(this.getApplicationContext(), "권한이 필요합니다", Toast.LENGTH_SHORT).show();
                }
        }
    }

    // 카메라 사진 찍기 구현
    ActivityResultLauncher<Intent> activityResultCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null){
                        Bundle extras = result.getData().getExtras();
                        imageBitmap = (Bitmap) extras.get("data");
                        mFood.setImageBitmap(imageBitmap);
                    }
                }
            }
    );
    // 앨범에서 사진 가져오기 구현
    ActivityResultLauncher<Intent> activityResultGallery = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null){
                        uri = result.getData().getData();
                        try{
                            imageBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
                            mFood.setImageBitmap(imageBitmap);
                        }
                        catch(FileNotFoundException e){
                            e.printStackTrace();
                        }
                        catch(IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    public void processDatePickerResult(int year, int month, int day){
        String month_string = Integer.toString(month+1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = (month_string + "/" + day_string + "/" + year_string);

        mDate_Text.setText("Date: "+dateMessage);
    }

}
//....