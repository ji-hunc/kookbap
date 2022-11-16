package com.kookmin.kookbap;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
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

import com.google.gson.Gson;
import com.kookmin.kookbap.cafeteriaFragments.CafeteriaHanul;
import com.kookmin.kookbap.cafeteriaFragments.CafeteriaStudent;
import com.kookmin.kookbap.cafeteriaFragments.CafeteriaViewPagerAdapter;
import com.kookmin.kookbap.cafeteriaFragments.MenuDataParser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;


public class WriteReview extends AppCompatActivity {
    TextView dateTextView;
    ImageView foodImage, calendarImageButton;
    EditText editTextReview;
    Button postButton;
    RatingBar ratingBar;

    int cameraPermission, galleryPermission;
    Bitmap imageBitmap, noticeBitmap;
    Uri uri;
    String chosenDate;
    String date, nowYear, nowMonth, nowDate;


    boolean isFilledImage;
    JSONObject jsonObject;
    String[] cafeteriaNames = {"식당", "한울식당", "학생식당", "교직원식당", "K-Bob", "청향 한식당", "청향 양식당", "생활관식당"};
    String[] menus;
    ArrayList<ArrayList<String>> menuArrayListsss = new ArrayList<>();

    Spinner menuSpinner, cafeteriaSpinner;


    MenuDataParser menuDataParser;
    private static final int SINGLE_PERMISSION = 1004;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        menuSpinner = findViewById(R.id.menuSpinner);
        cafeteriaSpinner = findViewById(R.id.cafeteriaSpinner);

        Calendar calendar = Calendar.getInstance();
        nowYear = Integer.toString(calendar.get(Calendar.YEAR));
        nowMonth = Integer.toString(calendar.get(Calendar.MONTH) + 1);
        nowDate = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        date = nowYear + "-" + nowMonth + "-" + nowDate; // 오늘 날짜가 url에 포함됨. 일단 하드 코딩

        Call<Object> call; // 원래 Retrofit 은 받아올 데이터 클래스를 정의해야 하지만, 완전 통으로 가져올 때는 따로 정의 없이 Object로 가져올 수 있음
        call = RetrofitClient.getApiService().getMenuData();
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.code() == 200) { // 서버로부터 OK 사인을 받았을 때
                    try {
                        jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        menuDataParser = new MenuDataParser(jsonObject, chosenDate);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });

        isFilledImage = false;

        dateTextView = findViewById(R.id.write_review_dateText);
        foodImage = findViewById(R.id.myFood);
        editTextReview = findViewById(R.id.myReview);
        postButton = findViewById(R.id.save_Review);
        calendarImageButton = findViewById(R.id.write_review_datebtn);
        ratingBar = findViewById(R.id.myRating);

        //지훈님과 날짜 표시 방식 통일
//        Calendar calendar = Calendar.getInstance();
        printDateResult(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

        // 달력 그림 눌렀을 때 달력 다이얼로그 띄우기
//        calendarImageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DialogFragment newFragment = new DatePickerFragment();
//                newFragment.show(getSupportFragmentManager(), "datePicker");
//            }
//        });
        // TODO 날짜 선택시 이벤트가 있는 아래 함수로 바꿔야함
        calendarImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(WriteReview.this, new DatePickerDialog.OnDateSetListener() {
                    // 날자를 선택했을 때
                    @Override
                    public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {
                        nowYear = Integer.toString(yy);
                        nowMonth = mm + 1 < 10 ? "0" + (mm + 1) : "" + (mm + 1);
                        nowDate = dd < 10 ? "0" + dd : "" + dd;
                        date = nowYear + "-" + nowMonth + "-" + nowDate;
                        dateTextView.setText(date);

                        menuDataParser = new MenuDataParser(jsonObject, date);
                        Log.e("list", menuDataParser.getHanulMenuData().toString());
                        cafeteriaSpinner.setSelection(0);
                        menuSpinner.setSelection(0);

                    }
                }, Integer.parseInt(nowYear), Integer.parseInt(nowMonth) - 1, Integer.parseInt(nowDate)); // 처음 DatePicker가 켜졌을 때 최초로 선택되어 있는 날짜
                datePickerDialog.show();
            }
        });


        ArrayAdapter<String> adapterMenu = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, new String[] {"메뉴"});
        menuSpinner.setAdapter(adapterMenu);
        menuSpinner.setSelection(0);
        menuSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> adapterCafeteria = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cafeteriaNames);
        cafeteriaSpinner.setAdapter(adapterCafeteria);
        cafeteriaSpinner.setSelection(0);
        cafeteriaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                menus = null;
//                menuSpinner.setAdapter(null);
                if ("한울식당".equals(cafeteriaSpinner.getSelectedItem().toString())) {
                    ArrayList<String> menuArrayList = menuDataParser.getHanulMenuData();
                    int sizeOfMenu = menuArrayList.size() / 2;
                    menus = new String[sizeOfMenu];

                    for (int k = 0; k < sizeOfMenu; k++) {
                        menus[k] = menuArrayList.get(k);
                    }
                    ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, menus);
                    menuSpinner.setAdapter(menuAdapter);
                } else if ("학생식당".equals(cafeteriaSpinner.getSelectedItem().toString())) {
                    ArrayList<String> menuArrayList = menuDataParser.getStudentMenuData();
                    int sizeOfMenu = menuArrayList.size() / 2;
                    menus = new String[sizeOfMenu];

                    for (int k = 0; k < sizeOfMenu; k++) {
                        menus[k] = menuArrayList.get(k);
                    }
                    ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, menus);
                    menuSpinner.setAdapter(menuAdapter);
                } else if ("교직원식당".equals(cafeteriaSpinner.getSelectedItem().toString())) {
                    ArrayList<String> menuArrayList = menuDataParser.getProfessorMenuData();
                    int sizeOfMenu = menuArrayList.size() / 2;
                    menus = new String[sizeOfMenu];

                    for (int k = 0; k < sizeOfMenu; k++) {
                        menus[k] = menuArrayList.get(k);
                    }
                    ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, menus);
                    menuSpinner.setAdapter(menuAdapter);
                } else if ("K-Bob".equals(cafeteriaSpinner.getSelectedItem().toString())) {
                    ArrayList<String> menuArrayList = menuDataParser.getKBobMenuData();
                    int sizeOfMenu = menuArrayList.size() / 2;
                    menus = new String[sizeOfMenu];

                    for (int k = 0; k < sizeOfMenu; k++) {
                        menus[k] = menuArrayList.get(k);
                    }
                    ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, menus);
                    menuSpinner.setAdapter(menuAdapter);
                } else if ("청향 한식당".equals(cafeteriaSpinner.getSelectedItem().toString())) {
                    ArrayList<String> menuArrayList = menuDataParser.getChungHyangKoreanMenuData();
                    int sizeOfMenu = menuArrayList.size() / 2;
                    menus = new String[sizeOfMenu];

                    for (int k = 0; k < sizeOfMenu; k++) {
                        menus[k] = menuArrayList.get(k);
                    }
                    ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, menus);
                    menuSpinner.setAdapter(menuAdapter);
                } else if ("청향 양식당".equals(cafeteriaSpinner.getSelectedItem().toString())) {
                    ArrayList<String> menuArrayList = menuDataParser.getChungHyangWesternMenuData();
                    int sizeOfMenu = menuArrayList.size() / 2;
                    menus = new String[sizeOfMenu];

                    for (int k = 0; k < sizeOfMenu; k++) {
                        menus[k] = menuArrayList.get(k);
                    }
                    ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, menus);
                    menuSpinner.setAdapter(menuAdapter);
                } else if ("생활관식당".equals(cafeteriaSpinner.getSelectedItem().toString())){
                    ArrayList<String> menuArrayList = menuDataParser.getDormitoryMenuData();
                    int sizeOfMenu = menuArrayList.size() / 2;
                    menus = new String[sizeOfMenu];

                    for (int k = 0; k < sizeOfMenu; k++) {
                        menus[k] = menuArrayList.get(k);
                    }
                    ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, menus);
                    menuSpinner.setAdapter(menuAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //사진 등록
        foodImage.setOnClickListener(new View.OnClickListener() {
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
                            // 카메라 접근 권한이 없을 때 요청
                            else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(new String[]{Manifest.permission.CAMERA}, SINGLE_PERMISSION);
                                }
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
                            else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, SINGLE_PERMISSION);
                                }
                            }
                        }
                    }
                });
                AlertDialog dialog = dlg.create();
                dialog.show();
            }
        });


        // 저장 버튼 눌렀을 때 서버 DB에 저장 요청
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(editTextReview.getText().toString().equals("") || !isFilledImage)) {
                    // 채우지 않은 항목이 있거나, 이미지가 없을 경우
                    if (!(cafeteriaSpinner.getSelectedItem().toString().equals("식당") || menuSpinner.getSelectedItem().toString().equals("메뉴"))) {

                        File newFile = new File(getApplicationContext().getFilesDir(), "test.png");
                        FileOutputStream fileOutputStream = null;
                        try {
                            fileOutputStream = new FileOutputStream(newFile);
                            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        try {
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), newFile);
                        MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image_from_client.png", requestFile);


                        RequestBody reviewUserId = RequestBody.create(MultipartBody.FORM, "jihun");
                        RequestBody menuName = RequestBody.create(MultipartBody.FORM, menuSpinner.getSelectedItem().toString());
                        RequestBody writeDate = RequestBody.create(MultipartBody.FORM, "jihun");
                        RequestBody star = RequestBody.create(MultipartBody.FORM, String.valueOf(ratingBar.getRating()));
                        RequestBody reviewLike = RequestBody.create(MultipartBody.FORM, String.valueOf(0));
                        RequestBody description = RequestBody.create(MultipartBody.FORM, editTextReview.getText().toString());
                        RequestBody restaurantName = RequestBody.create(MultipartBody.FORM, cafeteriaSpinner.getSelectedItem().toString());

                        HashMap<String, RequestBody> map = new HashMap<>();
                        map.put("reviewUserId", reviewUserId);
                        map.put("menuName", menuName);
                        map.put("writeDate", writeDate);
                        map.put("star", star);
                        map.put("reviewLike", reviewLike);
                        map.put("description", description);
                        map.put("restaurantName", restaurantName);

                        // TODO 유저 아이디("jihun" 아직 상수) 유저관리로 변수로 받아야함
                        Call<Result> call = RetrofitClient.getApiService().uploadFileWithPartMap(map, body);
                        call.enqueue(new Callback<Result>() {
                            @Override
                            public void onResponse(@NotNull Call<Result> call, @NotNull Response<Result> response) {

                                // 서버에서 응답을 받아옴
                                if (response.isSuccessful() && response.body() != null) {
                                    Boolean success = response.body().getSuccess();
                                    String message = response.body().getMessage();
                                    // 입력성공시 서버에서 메시지를 받아와서 테스트용으로 토스트로 출력
                                    // TODO 서버에서 상황에 따라 다른 결과를 전달해줘야함. 일단 GOOD만 보내도록 되어있음
                                    if (success) {
                                        Log.e("LOGLOG", "success1");
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                                        Log.e("서버에서 받아온내용", message);
                                    } else {
                                        Log.e("LOGLOG", "success2");
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    }
                                    // 응답을 받아오지 못했을경우
                                } else {
                                    assert response.body() != null;
                                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.e("LOGLOG", "success3");
                                }
                            }

                            // 통신실패시
                            @Override
                            public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {
                                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("LOGLOG", "success4");

                            }
                        });
                        finish();
                    } else {
                        Toast.makeText(WriteReview.this, "식당을 고르고, 메뉴를 선택하십시오", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(WriteReview.this, "내용을 채우거나 이미지를 등록하십시오", Toast.LENGTH_SHORT).show();
                }
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
                        foodImage.setImageBitmap(imageBitmap);
                        isFilledImage = true;
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
                            foodImage.setImageBitmap(imageBitmap);
                            isFilledImage = true;
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

    //날짜 출력
    public void printDateResult(int year, int month, int date){
        String today_Year = Integer.toString(year);
        // TODO 월 -1이라서 고쳐야함
        String today_Month = month+1 < 10 ? "0" + (month+1) : "" + (month+1);
        String today_Date = Integer.toString(date).length() < 2 ? "0" + date : Integer.toString(date);
        chosenDate = today_Year +"-"+today_Month + "-" + today_Date;
        dateTextView.setText(chosenDate);
    }
}