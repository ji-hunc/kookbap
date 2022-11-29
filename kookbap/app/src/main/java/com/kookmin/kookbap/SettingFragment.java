package com.kookmin.kookbap;

import static android.app.Activity.RESULT_OK;
import static android.app.PendingIntent.FLAG_IMMUTABLE;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SettingFragment extends Fragment {
    ImageView settingProfileImage;
    TextView settingName, settingCollegeNumber, textViewUserEmail, textViewAppVersion;
    LinearLayout settingMyReviews, settingLogout;
    Switch settingNotice;
    Button settingBtnNotice, testBtn;
    Boolean noticeOn = false;
    int cameraPermission, galleryPermission, noticePermission;
    private static final int SINGLE_PERMISSION = 1004;
    Bitmap imageBitmap, noticeBitmap;
    Uri uri;
    AlarmManager alarmManager;
    String from = "2022-11-27 21:13:00"; //임의로 날짜와 시간을 지정
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        settingProfileImage = view.findViewById(R.id.settingProfileImage);
        settingName = view.findViewById(R.id.settingName);
        settingCollegeNumber = view.findViewById(R.id.settingCollegeNumber);
        settingMyReviews = view.findViewById(R.id.settingMyReviews);
        settingNotice = view.findViewById(R.id.settingNotice);
//        settingBtnNotice = view.findViewById(R.id.settingBtnNotice);
        textViewUserEmail = view.findViewById(R.id.textViewUserEmail);
        textViewAppVersion = view.findViewById(R.id.textViewAppVersion);
        settingLogout = view.findViewById(R.id.settingLogout);

        // 로그인한 유저 정보를 받아와 프로필 사진, 이름, 학번 등 설정, 유저 관리 구현하면 구현해야 함.
        settingProfileImage.setImageResource(R.drawable.ic_basic_profile);
        settingName.setText("김민제");
        settingCollegeNumber.setText("학번: 20191557");
        textViewUserEmail.setText("kevinmj12@kookmin.ac.kr");

        //

        textViewAppVersion.setText("1.1.0");



        // 프로필 사진을 눌렀을 때 사진 변경
        settingProfileImage.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view){
               // 카메라 및 앨범 권한 확인
               cameraPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
               galleryPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
               // 카메라와 앨범 중 원하는 방법을 고르기 위한 dialog 출력
               AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
               dlg.setTitle("프로필 사진 설정하기");
               final String[] selectProfileImages = new String[] {"카메라로 사진 찍기", "앨범에서 사진 가져오기"};
               dlg.setItems(selectProfileImages, new DialogInterface.OnClickListener(){
                   @Override
                   public void onClick(DialogInterface dialog, int which){
                       // 카메라로 사진 찍기
                       if (which == 0){
                           // 카메라 권한이 있다면 카메라 실행
                           if (cameraPermission == PackageManager.PERMISSION_GRANTED){
                               Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                               activityResultCamera.launch(cameraIntent);
                           }
                           // 카메라 권한이 없다면 권한 요청
                           else{
                               requestPermissions(new String[]{Manifest.permission.CAMERA}, SINGLE_PERMISSION);
                           }
                       }
                       // 앨범에서 사진 가져오기
                       else if (which == 1){
                           // 파일 접근 권한이 있다면 앨범 실행
                           if (galleryPermission == PackageManager.PERMISSION_GRANTED){
                               Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                               galleryIntent.setType("image/*");
                               activityResultGallery.launch(galleryIntent);
                           }
                           // 파일 접근 권한이 없다면 권한 요청
                           else{
                               requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, SINGLE_PERMISSION);
                           }

                       }
                   }
               });
               AlertDialog dialog = dlg.create();
               dialog.show();
           }
        });

        // 내가 쓴 리뷰를 눌렀을 때 화면 출력
        settingMyReviews.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), UserReviewsActivity.class);
                startActivity(intent);
            }
        });

        // 알림 설정
//        settingBtnNotice.setOnClickListener(new View.OnClickListener(){
//            @RequiresApi(api = Build.VERSION_CODES.S)
//            @Override
//            public void onClick(View view){
//                if (settingNotice.isChecked()){
//                    noticeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_basic_profile);
//
//                    Intent intent = new Intent(getActivity(), MainActivity.class);
////
//
//                    // 알림 발생시키는 다른 방식인데 SDK버전 확인 필요
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                        NotificationChannel channel = new NotificationChannel("channel1", "hello", NotificationManager.IMPORTANCE_HIGH);
//                        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
//                        notificationManager.createNotificationChannel(channel);
//                        NotificationCompat.Builder notification = new NotificationCompat.Builder(requireContext(), "channel1");
//                        notification.setContentTitle("KookBob");
//                        notification.setContentText("jihun님, 오늘의 추천 메뉴 확인하세요!");
//                        notification.setDefaults(Notification.DEFAULT_SOUND);
//                        notification.setSmallIcon(R.drawable.ic_basic_profile);
//
//                        PendingIntent pendingIntent = PendingIntent.getActivity(view.getContext(), 0, intent, PendingIntent.FLAG_MUTABLE);
//
//                        notification.setContentIntent(pendingIntent);
//                        notification.setAutoCancel(true);
//
//                        notificationManager.notify(0, notification.build());
//                    }
//                }
//            }
//
//
//                    알림 발생시키는 다른 방식(7.0 이상에서는 작동 안됨)
//                    NotificationCompat.Builder notification = new NotificationCompat.Builder(requireContext(), "channel1");
//                    notification.setContentTitle("테스트");
//                    notification.setContentText("테스트 알림입니다");
//                    notification.setSmallIcon(R.drawable.ic_basic_profile);
//                    notification.setDefaults(Notification.DEFAULT_SOUND); // 소리, 진동은 DEFAULT_VIBRATE
//                    notification.setAutoCancel(true); // 알림 터치 시 자동으로 사라짐
//                    notification.setContentIntent(pendingIntent);
//                    PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                    PendingIntent를 통해 알림 터치 시 MainActivity로 이동할 수 있는데, 아직 모르겠음.
//
//
//                    NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
//                    notificationManager.notify(0, notification.build());
//
//
//        });
        NotificationManager notificationManager = (NotificationManager)getActivity().getSystemService(NOTIFICATION_SERVICE);

        alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);

        Calendar mCalendar = new GregorianCalendar();
//        setAlarm();



        settingLogout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                SharedPreferences prf = getActivity().getSharedPreferences("outo_login_id", MODE_PRIVATE);
                SharedPreferences.Editor editor = prf.edit();
                editor.clear();
                editor.apply();
                PackageManager packageManager = getActivity().getPackageManager();
                Intent intent = packageManager.getLaunchIntentForPackage(getActivity().getPackageName());
                ComponentName componentName = intent.getComponent();
                Intent mainIntent = Intent.makeRestartActivityTask(componentName);
                startActivity(mainIntent);
            }
        });

        return view;
    }

    private void setAlarm() {
        //AlarmReceiver에 값 전달
        Intent receiverIntent = new Intent(getActivity().getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 0, receiverIntent, FLAG_IMMUTABLE);



        //날짜 포맷을 바꿔주는 소스코드
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date datetime = null;
        try {
            datetime = dateFormat.parse(from);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datetime);


        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(),pendingIntent);
    }


    // 권한 요청 이후 권한 결과
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch(requestCode){
            case SINGLE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // 카메라
                    if (permissions[0].equals("android.permission.CAMERA")){
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        activityResultCamera.launch(cameraIntent);
                    }
                    // 갤러리
                    else if (permissions[0].equals("android.permission.READ_EXTERNAL_STORAGE")){
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                        galleryIntent.setType("image/*");
                        activityResultGallery.launch(galleryIntent);
                    }
                }
                // 권한 요청 필요 토스트 출력
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "권한이 필요합니다", Toast.LENGTH_SHORT).show();
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
                        settingProfileImage.setImageBitmap(imageBitmap);
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
                            imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), uri);
                            settingProfileImage.setImageBitmap(imageBitmap);
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

}