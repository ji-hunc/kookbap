package com.kookmin.kookbap;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import com.kookmin.kookbap.cafeteriaFragments.CafeteriaViewPagerAdapter;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.google.gson.Gson;
import com.kookmin.kookbap.Retrofits.Result;
import com.kookmin.kookbap.Retrofits.RetrofitClient;
import com.kookmin.kookbap.cafeteriaFragments.MenuDataParser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WriteReview extends AppCompatActivity {
    TextView dateTextView, menuNameTextview;
    ImageView foodImage, calendarImageButton;
    EditText editTextReview;
    Button postButton;
    RatingBar ratingBar;
    LinearLayout selectAbleLayout, fixedLayout;

    int cameraPermission, galleryPermission;
    Bitmap imageBitmap, noticeBitmap;
    Uri uri;
    String chosenDate;
    String date, nowYear, nowMonth, nowDate;


    boolean isFilledImage;
    ArrayList<MenuData2> todayMenus;
    String[] cafeteriaNames = {"식당", "한울식당", "학생식당", "교직원식당", "K-BOB+", "청향 한식당", "청향 양식당", "생활관식당"};
    String[] menus;
    ArrayList<ArrayList<String>> menuArrayListsss = new ArrayList<>();

    Spinner menuSpinner, cafeteriaSpinner;

    final int GENERAL_WRITE = 1;
    final int INFORMED_WRITE = 2;
    final int MODIFY_WRITE = 3;

    ArrayList<String> menuHanul = new ArrayList<>();
    ArrayList<String> menuStudent = new ArrayList<>();
    ArrayList<String> menuProfessor = new ArrayList<>();
    ArrayList<String> menuKbob = new ArrayList<>();
    ArrayList<String> menuChungHyangKorean = new ArrayList<>();
    ArrayList<String> menuChungHyangWestern = new ArrayList<>();
    ArrayList<String> menuDormitory = new ArrayList<>();

    private static final int SINGLE_PERMISSION = 1004;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        menuSpinner = findViewById(R.id.menuSpinner);
        cafeteriaSpinner = findViewById(R.id.cafeteriaSpinner);
        dateTextView = findViewById(R.id.write_review_dateText);
        foodImage = findViewById(R.id.myFood);
        editTextReview = findViewById(R.id.myReview);
        postButton = findViewById(R.id.save_Review);
        calendarImageButton = findViewById(R.id.write_review_datebtn);
        ratingBar = findViewById(R.id.myRating);
        selectAbleLayout = findViewById(R.id.selectAbleLayout);
        fixedLayout = findViewById(R.id.fixedLayout);
        menuNameTextview = findViewById(R.id.menuNameTextview);

        /**************************************************************************************************************************
        * 리뷰 작성페이지를 signal에 따라 다른 경우로 진입함
        * signal: 1 -> GENERAL_WRITE   Bottom Navigation 에서 진입한 경우: 초기화할 아무런 데이터가 없음
        * signal: 2 -> INFORMED_WRITE  메뉴 상세페이지에서 진입한 경우: 메뉴이름을 초기화하고, 날짜, 식당을 없앰
        * signal: 3 -> MODIFY_WRITE    리뷰카드의 수정하기를 눌러서 진입한 경우: 메뉴이름, 이미지, 별점, 리뷰내용을 초기화하고 날짜, 식당을 없앰
        ***************************************************************************************************************************/
        int reviewWriteSignal = getIntent().getIntExtra("signal", 0);

        //사진 등록
        foodImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 카메라 및 앨범 권한 확인
                cameraPermission = ContextCompat.checkSelfPermission(WriteReview.this, Manifest.permission.CAMERA);
                galleryPermission = ContextCompat.checkSelfPermission(WriteReview.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                // 카메라와 앨범 중 원하는 방법을 고르기 위한 dialog 출력
                AlertDialog.Builder dlg = new AlertDialog.Builder(WriteReview.this);
                dlg.setTitle("리뷰 사진 등록하기");
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


        // TODO 공통으로 필요한 코드들은 조건 바깥으로 빼기
        if (reviewWriteSignal == GENERAL_WRITE) {
            selectAbleLayout.setVisibility(View.VISIBLE);
            fixedLayout.setVisibility(View.GONE);
            Calendar calendar = Calendar.getInstance();
            nowYear = Integer.toString(calendar.get(Calendar.YEAR));
            nowMonth = Integer.toString(calendar.get(Calendar.MONTH) + 1);
            nowDate = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
            date = nowYear + "-" + nowMonth + "-" + nowDate; // 오늘 날짜가 url에 포함됨. 일단 하드 코딩
            isFilledImage = false;

            Call<ArrayList<MenuData2>> call;
            // TODO userId 프리퍼런스에서 가져와야함 현재는 상수
            call = RetrofitClient.getApiService().getMenuDataEachDate(date, "jihun");
            call.enqueue(new Callback<ArrayList<MenuData2>>() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    if (response.code() == 200) { // 서버로부터 OK 사인을 받았을 때
                        todayMenus = (ArrayList<MenuData2>) response.body();// 식당 스피너 조건별로 메뉴 스피너 값 변경
                        String cafeteriaName;
                        for (int j=0; j<todayMenus.size(); j++) {
                            cafeteriaName = todayMenus.get(j).getRestaurant_name();
                            switch (cafeteriaName) {
                                case "한울식당":
                                    menuHanul.add(todayMenus.get(j).getMenu_name());
                                    break;
                                case "학생식당":
                                    menuStudent.add(todayMenus.get(j).getMenu_name());
                                    break;
                                case "교직원식당":
                                    menuProfessor.add(todayMenus.get(j).getMenu_name());
                                    break;
                                case "K-BOB+":
                                    menuKbob.add(todayMenus.get(j).getMenu_name());
                                    break;
                                case "청향 한식당":
                                    menuChungHyangKorean.add(todayMenus.get(j).getMenu_name());
                                    break;
                                case "청향 양식당":
                                    menuChungHyangWestern.add(todayMenus.get(j).getMenu_name());
                                    break;
                                case "생활관식당 정기식":
                                    menuDormitory.add(todayMenus.get(j).getMenu_name());
                                    break;
                            }
                        }
                    } else {
                    }
                }

                @Override
                public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                    Log.e("Error", t.getMessage());
                }
            });


            // 날짜 텍스트뷰에 출력해주는 함수
            printDateResult(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
            calendarImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(WriteReview.this, new DatePickerDialog.OnDateSetListener() {
                        // 날짜를 선택했을 때
                        @Override
                        public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {
                            nowYear = Integer.toString(yy);
                            nowMonth = mm + 1 < 10 ? "0" + (mm + 1) : "" + (mm + 1);
                            nowDate = dd < 10 ? "0" + dd : "" + dd;
                            date = nowYear + "-" + nowMonth + "-" + nowDate;
                            dateTextView.setText(date);
                            ArrayAdapter<String> adapterMenu = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, new String[] {"메뉴"});
                            menuSpinner.setAdapter(adapterMenu);
                            menuSpinner.setSelection(0);

                            Call<ArrayList<MenuData2>> call;
                            // TODO userId 프리퍼런스에서 가져와야함 현재는 상수
                            call = RetrofitClient.getApiService().getMenuDataEachDate(date, "jihun");
                            call.enqueue(new Callback<ArrayList<MenuData2>>() {
                                @Override
                                public void onResponse(@NonNull Call call, @NonNull Response response) {
                                    if (response.code() == 200) { // 서버로부터 OK 사인을 받았을 때
                                        menuHanul.clear();
                                        menuStudent.clear();
                                        menuProfessor.clear();
                                        menuKbob.clear();
                                        menuChungHyangKorean.clear();
                                        menuChungHyangWestern.clear();
                                        menuDormitory.clear();
                                        todayMenus = (ArrayList<MenuData2>) response.body();String cafeteriaName;
                                        for (int j=0; j<todayMenus.size(); j++) {
                                            cafeteriaName = todayMenus.get(j).getRestaurant_name();
                                            switch (cafeteriaName) {
                                                case "한울식당":
                                                    menuHanul.add(todayMenus.get(j).getMenu_name());
                                                    break;
                                                case "학생식당":
                                                    menuStudent.add(todayMenus.get(j).getMenu_name());
                                                    break;
                                                case "교직원식당":
                                                    menuProfessor.add(todayMenus.get(j).getMenu_name());
                                                    break;
                                                case "K-BOB+":
                                                    menuKbob.add(todayMenus.get(j).getMenu_name());
                                                    break;
                                                case "청향 한식당":
                                                    menuChungHyangKorean.add(todayMenus.get(j).getMenu_name());
                                                    break;
                                                case "청향 양식당":
                                                    menuChungHyangWestern.add(todayMenus.get(j).getMenu_name());
                                                    break;
                                                case "생활관식당 정기식":
                                                    menuDormitory.add(todayMenus.get(j).getMenu_name());
                                                    break;
                                            }
                                        }
                                    } else {
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                                    Log.e("Error", t.getMessage());
                                }
                            });

                            cafeteriaSpinner.setSelection(0);
                            menuSpinner.setSelection(0);

                        }
                    }, Integer.parseInt(nowYear), Integer.parseInt(nowMonth) - 1, Integer.parseInt(nowDate)); // 처음 DatePicker가 켜졌을 때 최초로 선택되어 있는 날짜
                    datePickerDialog.show();
                }
            });


            // 메뉴 스피너 초기화
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

            // 식당 스피너 초기화
            ArrayAdapter<String> adapterCafeteria = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cafeteriaNames);
            cafeteriaSpinner.setAdapter(adapterCafeteria);
            cafeteriaSpinner.setSelection(0);
            cafeteriaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    menus = null;
                    if ("한울식당".equals(cafeteriaSpinner.getSelectedItem().toString())) {
                        int sizeOfMenu = menuHanul.size();
                        menus = new String[sizeOfMenu];

                        for (int k = 0; k < sizeOfMenu; k++) {
                            menus[k] = menuHanul.get(k);
                        }
                        ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, menus);
                        menuSpinner.setAdapter(menuAdapter);
                    } else if ("학생식당".equals(cafeteriaSpinner.getSelectedItem().toString())) {
                        int sizeOfMenu = menuStudent.size();
                        menus = new String[sizeOfMenu];

                        for (int k = 0; k < sizeOfMenu; k++) {
                            menus[k] = menuStudent.get(k);
                        }
                        ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, menus);
                        menuSpinner.setAdapter(menuAdapter);
                    } else if ("교직원식당".equals(cafeteriaSpinner.getSelectedItem().toString())) {
                        int sizeOfMenu = menuProfessor.size();
                        menus = new String[sizeOfMenu];

                        for (int k = 0; k < sizeOfMenu; k++) {
                            menus[k] = menuProfessor.get(k);
                        }
                        ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, menus);
                        menuSpinner.setAdapter(menuAdapter);
                    } else if ("K-BOB+".equals(cafeteriaSpinner.getSelectedItem().toString())) {
                        int sizeOfMenu = menuKbob.size();
                        menus = new String[sizeOfMenu];

                        for (int k = 0; k < sizeOfMenu; k++) {
                            menus[k] = menuKbob.get(k);
                        }
                        ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, menus);
                        menuSpinner.setAdapter(menuAdapter);
                    } else if ("청향 한식당".equals(cafeteriaSpinner.getSelectedItem().toString())) {
                        int sizeOfMenu = menuChungHyangKorean.size();
                        menus = new String[sizeOfMenu];

                        for (int k = 0; k < sizeOfMenu; k++) {
                            menus[k] = menuChungHyangKorean.get(k);
                        }
                        ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, menus);
                        menuSpinner.setAdapter(menuAdapter);
                    } else if ("청향 양식당".equals(cafeteriaSpinner.getSelectedItem().toString())) {
                        int sizeOfMenu = menuChungHyangWestern.size();
                        menus = new String[sizeOfMenu];

                        for (int k = 0; k < sizeOfMenu; k++) {
                            menus[k] = menuChungHyangWestern.get(k);
                        }
                        ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, menus);
                        menuSpinner.setAdapter(menuAdapter);
                    } else if ("생활관식당".equals(cafeteriaSpinner.getSelectedItem().toString())){
                        int sizeOfMenu = menuDormitory.size();
                        menus = new String[sizeOfMenu];

                        for (int k = 0; k < sizeOfMenu; k++) {
                            menus[k] = menuDormitory.get(k);
                        }
                        ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, menus);
                        menuSpinner.setAdapter(menuAdapter);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

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


                            RequestBody menuId = RequestBody.create(MultipartBody.FORM, String.valueOf(getIntent().getIntExtra("menuId", 0)));
                            RequestBody reviewUserId = RequestBody.create(MultipartBody.FORM, "jihun");
                            RequestBody menuName = RequestBody.create(MultipartBody.FORM, menuSpinner.getSelectedItem().toString());
                            RequestBody writeDate = RequestBody.create(MultipartBody.FORM, "jihun");
                            RequestBody star = RequestBody.create(MultipartBody.FORM, String.valueOf(ratingBar.getRating()));
                            RequestBody reviewLike = RequestBody.create(MultipartBody.FORM, String.valueOf(0));
                            RequestBody description = RequestBody.create(MultipartBody.FORM, editTextReview.getText().toString());
                            RequestBody restaurantName = RequestBody.create(MultipartBody.FORM, cafeteriaSpinner.getSelectedItem().toString());

                            HashMap<String, RequestBody> map = new HashMap<>();
                            map.put("menuId", menuId);
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

        else if (reviewWriteSignal == INFORMED_WRITE) {
            selectAbleLayout.setVisibility(View.GONE);
            fixedLayout.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), "Informed Mode", Toast.LENGTH_SHORT).show();
            menuNameTextview.setText(getIntent().getStringExtra("foodName"));

            AlertDialog.Builder builder = new AlertDialog.Builder(WriteReview.this);
            View dialogView = LayoutInflater.from(WriteReview.this).inflate(
                    R.layout.loading_dialog,
                    (ConstraintLayout)findViewById(R.id.layoutLoadingDialog));
            builder.setView(dialogView);
            LottieAnimationView lottieAnimationView = (LottieAnimationView)dialogView.findViewById(R.id.loadingAnimationView);
            lottieAnimationView.setAnimation("loading.json");

            // 저장 버튼 눌렀을 때 서버 DB에 저장 요청(Informed Write)
            postButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lottieAnimationView.setRepeatCount(LottieDrawable.INFINITE);
                    lottieAnimationView.playAnimation();

                    AlertDialog alertDialog =  builder.create();
                    alertDialog.setCancelable(false);
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alertDialog.show();

                    if (!(editTextReview.getText().toString().equals("") || !isFilledImage)) {
                        // 채우지 않은 항목이 있거나, 이미지가 없을 경우
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


                        RequestBody menuId = RequestBody.create(MultipartBody.FORM, String.valueOf(getIntent().getIntExtra("menuId", 0)));
                        RequestBody reviewUserId = RequestBody.create(MultipartBody.FORM, "jihun");
                        RequestBody menuName = RequestBody.create(MultipartBody.FORM, menuNameTextview.getText().toString());
                        RequestBody writeDate = RequestBody.create(MultipartBody.FORM, "jihun");
                        RequestBody star = RequestBody.create(MultipartBody.FORM, String.valueOf(ratingBar.getRating()));
                        RequestBody reviewLike = RequestBody.create(MultipartBody.FORM, String.valueOf(0));
                        RequestBody description = RequestBody.create(MultipartBody.FORM, editTextReview.getText().toString());
                        RequestBody restaurantName = RequestBody.create(MultipartBody.FORM, getIntent().getStringExtra("restaurantName"));

                        HashMap<String, RequestBody> map = new HashMap<>();
                        map.put("menuId", menuId);
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
                                alertDialog.dismiss();
                                // 서버에서 응답을 받아옴
                                if (response.isSuccessful() && response.body() != null) {
                                    Boolean success = response.body().getSuccess();
                                    String message = response.body().getMessage();
                                    // 입력성공시 서버에서 메시지를 받아와서 테스트용으로 토스트로 출력
                                    // TODO 서버에서 상황에 따라 다른 결과를 전달해줘야함. 일단 GOOD만 보내도록 되어있음
                                    if (success) {
                                        Log.e("LOGLOG", "success1");
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                        finish();

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
                    } else {
                        Toast.makeText(WriteReview.this, "사진을 업로드하고 내용을 입력해 주십시오", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        } else if (reviewWriteSignal == MODIFY_WRITE) {
            selectAbleLayout.setVisibility(View.GONE);
            fixedLayout.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), "modify mode", Toast.LENGTH_SHORT).show();
            menuNameTextview.setText(getIntent().getStringExtra("foodName"));

            int reviewNumberOrigin = getIntent().getIntExtra("review_number", 0);
            float oldStar = getIntent().getFloatExtra("star", 0);
            String oldDescription = getIntent().getStringExtra("description");
//            String imageUrl = "http://10.0.2.2:3000/images/" + getIntent().getStringExtra("imageUrl");
            String imageUrl = "https://kookbap.run.goorm.io/images/" + getIntent().getStringExtra("imageUrl");


            // 외부이미지 이미지뷰에 적용해주는 클래스
            class DownloadFilesTask extends AsyncTask<String,Void, Bitmap> {
                @Override
                protected Bitmap doInBackground(String... strings) {
                    Bitmap bmp = null;
                    try {
                        String img_url = strings[0]; //url of the image
                        URL url = new URL(img_url);
                        bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return bmp;
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }


                @Override
                protected void onPostExecute(Bitmap result) {
                    // doInBackground 에서 받아온 total 값 사용 장소
                    foodImage.setImageBitmap(result);
                }
            }
            new DownloadFilesTask().execute(imageUrl); // 이미지뷰에 외부 이미지 적용
            ratingBar.setRating(oldStar);
            editTextReview.setText(oldDescription);
            postButton.setText("수정");


            // 수정 버튼 눌렀을 때 서버 DB에 저장 요청(Informed Write)
            postButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!(editTextReview.getText().toString().equals(""))) {
                        // 리뷰 내용을 채웠을 때
                        if (isFilledImage || !(editTextReview.getText().toString().equals(oldDescription)) || ratingBar.getRating() != oldStar) {
                            // 변경사항이 있을 때
                            File newFile = new File(getApplicationContext().getFilesDir(), "test.png");
                            if (isFilledImage) { // 이미지를 변경하였을 때
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
                            }
                            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), newFile);
                            MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image_from_client.png", requestFile);

                            RequestBody menuId = RequestBody.create(MultipartBody.FORM, String.valueOf(getIntent().getIntExtra("menuId", 0)));
                            RequestBody reviewUserId = RequestBody.create(MultipartBody.FORM, "jihun");
                            RequestBody menuName = RequestBody.create(MultipartBody.FORM, menuNameTextview.getText().toString());
                            RequestBody reviewNumber = RequestBody.create(MultipartBody.FORM, String.valueOf(reviewNumberOrigin));
                            RequestBody star = RequestBody.create(MultipartBody.FORM, String.valueOf(ratingBar.getRating()));
                            RequestBody description = RequestBody.create(MultipartBody.FORM, editTextReview.getText().toString());
                            RequestBody isUploadNewImage = RequestBody.create(MultipartBody.FORM, String.valueOf(isFilledImage));

                            HashMap<String, RequestBody> map = new HashMap<>();
                            map.put("menuId", menuId);
                            map.put("reviewUserId", reviewUserId);
                            map.put("menuName", menuName);
                            map.put("reviewNumber", reviewNumber);
                            map.put("star", star);
                            map.put("description", description);
                            map.put("isUploadNewImage", isUploadNewImage);

                            // TODO 유저 아이디("jihun" 아직 상수) 유저관리로 변수로 받아야함
                            Call<Result> call = RetrofitClient.getApiService().modifyReview(map, body);
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
                            Toast.makeText(WriteReview.this, "변경사항이 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(WriteReview.this, "내용을 채워주십시오", Toast.LENGTH_SHORT).show();
                    }
                }
            });

//            Toast.makeText(getApplicationContext(), Integer.toString(getIntent().getIntExtra("review_number", 0)), Toast.LENGTH_SHORT).show();
        }


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