package com.kookmin.kookbap;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import androidx.core.content.ContextCompat;

import com.kookmin.kookbap.LoginAndSignup.UserData;

import com.airbnb.lottie.LottieAnimationView;
import com.kookmin.kookbap.Retrofits.Result;
import com.kookmin.kookbap.Retrofits.RetrofitClient;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
    ArrayList<MenuData> todayMenus;
    String[] cafeteriaNames = {"??????", "????????????", "????????????", "???????????????", "K-BOB+", "?????? ?????????", "?????? ?????????", "???????????????"};
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


    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        userID = UserData.getUserData(this).getUserId(); // ????????????????????? Id ?????????

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
        * ?????? ?????????????????? signal??? ?????? ?????? ????????? ?????????
        * signal: 1 -> GENERAL_WRITE   Bottom Navigation ?????? ????????? ??????: ???????????? ????????? ???????????? ??????
        * signal: 2 -> INFORMED_WRITE  ?????? ????????????????????? ????????? ??????: ??????????????? ???????????????, ??????, ????????? ??????
        * signal: 3 -> MODIFY_WRITE    ??????????????? ??????????????? ????????? ????????? ??????: ????????????, ?????????, ??????, ??????????????? ??????????????? ??????, ????????? ??????
        ***************************************************************************************************************************/
        int reviewWriteSignal = getIntent().getIntExtra("signal", 0);
        AlertDialog.Builder builder = new AlertDialog.Builder(WriteReview.this);
        View dialogView = LayoutInflater.from(WriteReview.this).inflate(
                R.layout.loading_dialog,
                null);
        builder.setView(dialogView);
        LottieAnimationView lottieAnimationView = (LottieAnimationView)dialogView.findViewById(R.id.loadingAnimationView);
        lottieAnimationView.setAnimation("loading.json");
        AlertDialog alertDialog =  builder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //?????? ??????
        foodImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ????????? ??? ?????? ?????? ??????
                cameraPermission = ContextCompat.checkSelfPermission(WriteReview.this, Manifest.permission.CAMERA);
                galleryPermission = ContextCompat.checkSelfPermission(WriteReview.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                // ???????????? ?????? ??? ????????? ????????? ????????? ?????? dialog ??????
                AlertDialog.Builder dlg = new AlertDialog.Builder(WriteReview.this);
                dlg.setTitle("?????? ?????? ????????????");
                final String[] selectProfileImages = new String[]{"???????????? ?????? ??????", "???????????? ?????? ????????????"};
                dlg.setItems(selectProfileImages, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // ???????????? ?????? ??????
                        if (which == 0) {
                            // ????????? ????????? ????????? ????????? ??????
                            if (cameraPermission == PackageManager.PERMISSION_GRANTED) {
                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                activityResultCamera.launch(cameraIntent);
                            }
                            // ????????? ?????? ????????? ?????? ??? ??????
                            else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(new String[]{Manifest.permission.CAMERA}, SINGLE_PERMISSION);
                                }
                            }
                        }
                        // ???????????? ?????? ????????????
                        else if (which == 1) {
                            // ?????? ?????? ????????? ????????? ?????? ??????
                            if (galleryPermission == PackageManager.PERMISSION_GRANTED) {
                                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                                galleryIntent.setType("image/*");
                                activityResultGallery.launch(galleryIntent);
                            }
                            // ?????? ?????? ????????? ????????? ?????? ??????
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


        // TODO ???????????? ????????? ???????????? ?????? ???????????? ??????
        if (reviewWriteSignal == GENERAL_WRITE) {
            selectAbleLayout.setVisibility(View.VISIBLE);
            fixedLayout.setVisibility(View.GONE);
            Calendar calendar = Calendar.getInstance();
            nowYear = Integer.toString(calendar.get(Calendar.YEAR));
            nowMonth = Integer.toString(calendar.get(Calendar.MONTH) + 1);
            nowDate = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
            date = nowYear + "-" + nowMonth + "-" + nowDate; // ?????? ????????? url??? ?????????. ?????? ?????? ??????
            isFilledImage = false;

            Call<ArrayList<MenuData>> call;
            call = RetrofitClient.getApiService().getMenuDataEachDate(date, userID);
            call.enqueue(new Callback<ArrayList<MenuData>>() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    if (response.code() == 200) { // ??????????????? OK ????????? ????????? ???
                        todayMenus = (ArrayList<MenuData>) response.body();// ?????? ????????? ???????????? ?????? ????????? ??? ??????
                        String cafeteriaName;
                        for (int j=0; j<todayMenus.size(); j++) {
                            cafeteriaName = todayMenus.get(j).getRestaurant_name();
                            switch (cafeteriaName) {
                                case "????????????":
                                    menuHanul.add(todayMenus.get(j).getMenu_name());
                                    break;
                                case "????????????":
                                    menuStudent.add(todayMenus.get(j).getMenu_name());
                                    break;
                                case "???????????????":
                                    menuProfessor.add(todayMenus.get(j).getMenu_name());
                                    break;
                                case "K-BOB+":
                                    menuKbob.add(todayMenus.get(j).getMenu_name());
                                    break;
                                case "?????? ?????????":
                                    menuChungHyangKorean.add(todayMenus.get(j).getMenu_name());
                                    break;
                                case "?????? ?????????":
                                    menuChungHyangWestern.add(todayMenus.get(j).getMenu_name());
                                    break;
                                case "??????????????? ?????????":
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


            // ?????? ??????????????? ??????????????? ??????
            printDateResult(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
            calendarImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(WriteReview.this, new DatePickerDialog.OnDateSetListener() {
                        // ????????? ???????????? ???
                        @Override
                        public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {
                            nowYear = Integer.toString(yy);
                            nowMonth = mm + 1 < 10 ? "0" + (mm + 1) : "" + (mm + 1);
                            nowDate = dd < 10 ? "0" + dd : "" + dd;
                            date = nowYear + "-" + nowMonth + "-" + nowDate;
                            dateTextView.setText(date);
                            ArrayAdapter<String> adapterMenu = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, new String[] {"??????"});
                            menuSpinner.setAdapter(adapterMenu);
                            menuSpinner.setSelection(0);

                            Call<ArrayList<MenuData>> call;
                            call = RetrofitClient.getApiService().getMenuDataEachDate(date, userID);
                            call.enqueue(new Callback<ArrayList<MenuData>>() {
                                @Override
                                public void onResponse(@NonNull Call call, @NonNull Response response) {
                                    if (response.code() == 200) { // ??????????????? OK ????????? ????????? ???
                                        menuHanul.clear();
                                        menuStudent.clear();
                                        menuProfessor.clear();
                                        menuKbob.clear();
                                        menuChungHyangKorean.clear();
                                        menuChungHyangWestern.clear();
                                        menuDormitory.clear();
                                        todayMenus = (ArrayList<MenuData>) response.body();String cafeteriaName;
                                        for (int j=0; j<todayMenus.size(); j++) {
                                            cafeteriaName = todayMenus.get(j).getRestaurant_name();
                                            switch (cafeteriaName) {
                                                case "????????????":
                                                    menuHanul.add(todayMenus.get(j).getMenu_name());
                                                    break;
                                                case "????????????":
                                                    menuStudent.add(todayMenus.get(j).getMenu_name());
                                                    break;
                                                case "???????????????":
                                                    menuProfessor.add(todayMenus.get(j).getMenu_name());
                                                    break;
                                                case "K-BOB+":
                                                    menuKbob.add(todayMenus.get(j).getMenu_name());
                                                    break;
                                                case "?????? ?????????":
                                                    menuChungHyangKorean.add(todayMenus.get(j).getMenu_name());
                                                    break;
                                                case "?????? ?????????":
                                                    menuChungHyangWestern.add(todayMenus.get(j).getMenu_name());
                                                    break;
                                                case "??????????????? ?????????":
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
                    }, Integer.parseInt(nowYear), Integer.parseInt(nowMonth) - 1, Integer.parseInt(nowDate)); // ?????? DatePicker??? ????????? ??? ????????? ???????????? ?????? ??????
                    datePickerDialog.show();
                }
            });


            // ?????? ????????? ?????????
            ArrayAdapter<String> adapterMenu = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, new String[] {"??????"});
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

            // ?????? ????????? ?????????
            ArrayAdapter<String> adapterCafeteria = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cafeteriaNames);
            cafeteriaSpinner.setAdapter(adapterCafeteria);
            cafeteriaSpinner.setSelection(0);
            cafeteriaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    menus = null;
                    if ("????????????".equals(cafeteriaSpinner.getSelectedItem().toString())) {
                        int sizeOfMenu = menuHanul.size();
                        menus = new String[sizeOfMenu];

                        for (int k = 0; k < sizeOfMenu; k++) {
                            menus[k] = menuHanul.get(k);
                        }
                        ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, menus);
                        menuSpinner.setAdapter(menuAdapter);
                    } else if ("????????????".equals(cafeteriaSpinner.getSelectedItem().toString())) {
                        int sizeOfMenu = menuStudent.size();
                        menus = new String[sizeOfMenu];

                        for (int k = 0; k < sizeOfMenu; k++) {
                            menus[k] = menuStudent.get(k);
                        }
                        ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, menus);
                        menuSpinner.setAdapter(menuAdapter);
                    } else if ("???????????????".equals(cafeteriaSpinner.getSelectedItem().toString())) {
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
                    } else if ("?????? ?????????".equals(cafeteriaSpinner.getSelectedItem().toString())) {
                        int sizeOfMenu = menuChungHyangKorean.size();
                        menus = new String[sizeOfMenu];

                        for (int k = 0; k < sizeOfMenu; k++) {
                            menus[k] = menuChungHyangKorean.get(k);
                        }
                        ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, menus);
                        menuSpinner.setAdapter(menuAdapter);
                    } else if ("?????? ?????????".equals(cafeteriaSpinner.getSelectedItem().toString())) {
                        int sizeOfMenu = menuChungHyangWestern.size();
                        menus = new String[sizeOfMenu];

                        for (int k = 0; k < sizeOfMenu; k++) {
                            menus[k] = menuChungHyangWestern.get(k);
                        }
                        ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, menus);
                        menuSpinner.setAdapter(menuAdapter);
                    } else if ("???????????????".equals(cafeteriaSpinner.getSelectedItem().toString())){
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


            // ?????? ?????? ????????? ??? ?????? DB??? ?????? ??????
            postButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!(editTextReview.getText().toString().equals("") || !isFilledImage)) {
                        // ????????? ?????? ????????? ?????????, ???????????? ?????? ??????
                        if (!(cafeteriaSpinner.getSelectedItem().toString().equals("??????") || menuSpinner.getSelectedItem().toString().equals("??????"))) {

                            lottieAnimationView.playAnimation();
                            ((TextView)dialogView.findViewById(R.id.loadingDialogText)).setText("?????? ?????? ???...");
                            alertDialog.show();

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
                            RequestBody reviewUserId = RequestBody.create(MultipartBody.FORM, userID);
                            RequestBody menuName = RequestBody.create(MultipartBody.FORM, menuSpinner.getSelectedItem().toString());
                            RequestBody writeDate = RequestBody.create(MultipartBody.FORM, userID);
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

                            Call<Result> call = RetrofitClient.getApiService().uploadFileWithPartMap(map, body);
                            call.enqueue(new Callback<Result>() {
                                @Override
                                public void onResponse(@NotNull Call<Result> call, @NotNull Response<Result> response) {
                                    alertDialog.dismiss();

                                    // ???????????? ????????? ?????????
                                    if (response.isSuccessful() && response.body() != null) {
                                        Boolean success = response.body().getSuccess();
                                        String message = response.body().getMessage();
                                        // ??????????????? ???????????? ???????????? ???????????? ?????????????????? ???????????? ??????
                                        // TODO ???????????? ????????? ?????? ?????? ????????? ??????????????????. ?????? GOOD??? ???????????? ????????????
                                        if (success) {
                                            Log.e("LOGLOG", "success1");
//                                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                                            Log.e("???????????? ???????????????", message);
                                        } else {
                                            Log.e("LOGLOG", "success2");
//                                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                        }
                                        // ????????? ???????????? ???????????????
                                    } else {
                                        assert response.body() != null;
//                                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.e("LOGLOG", "success3");
                                    }
                                }

                                // ???????????????
                                @Override
                                public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {
                                    alertDialog.dismiss();
//                                    Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    Log.e("LOGLOG", "success4");

                                }
                            });
                            finish();
                        } else {
                            Toast.makeText(WriteReview.this, "????????? ?????????, ????????? ??????????????????", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(WriteReview.this, "????????? ???????????? ???????????? ??????????????????", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        else if (reviewWriteSignal == INFORMED_WRITE) {
            selectAbleLayout.setVisibility(View.GONE);
            fixedLayout.setVisibility(View.VISIBLE);
//            Toast.makeText(getApplicationContext(), "Informed Mode", Toast.LENGTH_SHORT).show();
            menuNameTextview.setText(getIntent().getStringExtra("foodName"));

            // ?????? ?????? ????????? ??? ?????? DB??? ?????? ??????(Informed Write)
            postButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!(editTextReview.getText().toString().equals("") || !isFilledImage)) {

                        lottieAnimationView.playAnimation();
                        ((TextView)dialogView.findViewById(R.id.loadingDialogText)).setText("?????? ?????? ???...");
                        alertDialog.show();

                        // ????????? ?????? ????????? ?????????, ???????????? ?????? ??????
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
                        RequestBody reviewUserId = RequestBody.create(MultipartBody.FORM, userID);
                        RequestBody menuName = RequestBody.create(MultipartBody.FORM, menuNameTextview.getText().toString());
                        RequestBody writeDate = RequestBody.create(MultipartBody.FORM, userID);
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

                        Call<Result> call = RetrofitClient.getApiService().uploadFileWithPartMap(map, body);
                        call.enqueue(new Callback<Result>() {
                            @Override
                            public void onResponse(@NotNull Call<Result> call, @NotNull Response<Result> response) {
                                alertDialog.dismiss();
                                // ???????????? ????????? ?????????
                                if (response.isSuccessful() && response.body() != null) {
                                    Boolean success = response.body().getSuccess();
                                    String message = response.body().getMessage();
                                    // ??????????????? ???????????? ???????????? ???????????? ?????????????????? ???????????? ??????
                                    // TODO ???????????? ????????? ?????? ?????? ????????? ??????????????????. ?????? GOOD??? ???????????? ????????????
                                    if (success) {
                                        Log.e("LOGLOG", "success1");
//                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                        finish();

                                        Log.e("???????????? ???????????????", message);
                                    } else {
                                        Log.e("LOGLOG", "success2");
//                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    }
                                    // ????????? ???????????? ???????????????
                                } else {
                                    assert response.body() != null;
//                                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.e("LOGLOG", "success3");
                                }

                            }

                            // ???????????????
                            @Override
                            public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {
                                alertDialog.dismiss();
//                                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("LOGLOG", "success4");

                            }
                        });
                    } else {
                        Toast.makeText(WriteReview.this, "????????? ??????????????? ????????? ????????? ????????????", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        } else if (reviewWriteSignal == MODIFY_WRITE) {
            selectAbleLayout.setVisibility(View.GONE);
            fixedLayout.setVisibility(View.VISIBLE);
//            Toast.makeText(getApplicationContext(), "modify mode", Toast.LENGTH_SHORT).show();
            menuNameTextview.setText(getIntent().getStringExtra("foodName"));

            int reviewNumberOrigin = getIntent().getIntExtra("review_number", 0);
            float oldStar = getIntent().getFloatExtra("star", 0);
            String oldDescription = getIntent().getStringExtra("description");
            String imageUrl = URLConnector.URL + "images/" + getIntent().getStringExtra("imageUrl");


            // ??????????????? ??????????????? ??????????????? ?????????
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
                    // doInBackground ?????? ????????? total ??? ?????? ??????
                    foodImage.setImageBitmap(result);
                }
            }
            new DownloadFilesTask().execute(imageUrl); // ??????????????? ?????? ????????? ??????
            ratingBar.setRating(oldStar);
            editTextReview.setText(oldDescription);
            postButton.setText("??????");


            // ?????? ?????? ????????? ??? ?????? DB??? ?????? ??????(Informed Write)
            postButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!(editTextReview.getText().toString().equals(""))) {

                        // ?????? ????????? ????????? ???
                        if (isFilledImage || !(editTextReview.getText().toString().equals(oldDescription)) || ratingBar.getRating() != oldStar) {
                            lottieAnimationView.playAnimation();
                            ((TextView)dialogView.findViewById(R.id.loadingDialogText)).setText("?????? ?????? ???...");
                            alertDialog.show();
                            // ??????????????? ?????? ???
                            File newFile = new File(getApplicationContext().getFilesDir(), "test.png");
                            if (isFilledImage) { // ???????????? ??????????????? ???
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
                            RequestBody reviewUserId = RequestBody.create(MultipartBody.FORM, userID);
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


                            Call<Result> call = RetrofitClient.getApiService().modifyReview(map, body);
                            call.enqueue(new Callback<Result>() {
                                @Override
                                public void onResponse(@NotNull Call<Result> call, @NotNull Response<Result> response) {
                                    alertDialog.dismiss();

                                    // ???????????? ????????? ?????????
                                    if (response.isSuccessful() && response.body() != null) {
                                        Boolean success = response.body().getSuccess();
                                        String message = response.body().getMessage();
                                        // ??????????????? ???????????? ???????????? ???????????? ?????????????????? ???????????? ??????
                                        // TODO ???????????? ????????? ?????? ?????? ????????? ??????????????????. ?????? GOOD??? ???????????? ????????????
                                        if (success) {
                                            Log.e("LOGLOG", "success1");
//                                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                            finish();

                                            Log.e("???????????? ???????????????", message);
                                        } else {
                                            Log.e("LOGLOG", "success2");
//                                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                        }
                                        // ????????? ???????????? ???????????????
                                    } else {
                                        assert response.body() != null;
//                                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.e("LOGLOG", "success3");
                                    }
                                }

                                // ???????????????
                                @Override
                                public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {
                                    alertDialog.dismiss();
//                                    Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    Log.e("LOGLOG", "success4");

                                }
                            });
                        } else {
                            Toast.makeText(WriteReview.this, "??????????????? ????????????.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(WriteReview.this, "????????? ??????????????????", Toast.LENGTH_SHORT).show();
                    }
                }
            });

//            Toast.makeText(getApplicationContext(), Integer.toString(getIntent().getIntExtra("review_number", 0)), Toast.LENGTH_SHORT).show();
        }


    }

    // ?????? ?????? ?????? ?????? ??????
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case SINGLE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // ?????????
                    if (permissions[0].equals("android.permission.CAMERA")) {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        activityResultCamera.launch(cameraIntent);
                    }
                    // ?????????
                    else if (permissions[0].equals("android.permission.READ_EXTERNAL_STORAGE")) {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                        galleryIntent.setType("image/*");
                        activityResultGallery.launch(galleryIntent);
                    }
                }
                // ?????? ?????? ?????? ????????? ??????
                else {
                    Toast.makeText(this.getApplicationContext(), "????????? ???????????????", Toast.LENGTH_SHORT).show();
                }
        }
    }

    // ????????? ?????? ?????? ??????
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

    // ???????????? ?????? ???????????? ??????
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

    //?????? ??????
    public void printDateResult(int year, int month, int date){
        String today_Year = Integer.toString(year);
        // TODO ??? -1????????? ????????????
        String today_Month = month+1 < 10 ? "0" + (month+1) : "" + (month+1);
        String today_Date = Integer.toString(date).length() < 2 ? "0" + date : Integer.toString(date);
        chosenDate = today_Year +"-"+today_Month + "-" + today_Date;
        dateTextView.setText(chosenDate);
    }
}