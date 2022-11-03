package com.kookmin.kookbap;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.CompoundButton;
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

public class SettingFragment extends Fragment {
    ImageView settingProfileImage;
    TextView settingName, settingCollegeNumber, nodeTest;
    LinearLayout settingMyReviews;
    Switch settingNotice;
    Button settingBtnNotice;
    Boolean noticeOn = false;
    int cameraPermission, galleryPermission, noticePermission;
    private static final int SINGLE_PERMISSION = 1004;
    Bitmap imageBitmap, noticeBitmap;
    Uri uri;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        settingProfileImage = (ImageView) view.findViewById(R.id.settingProfileImage);
        settingName = (TextView) view.findViewById(R.id.settingName);
        settingCollegeNumber = (TextView) view.findViewById(R.id.settingCollegeNumber);
        settingMyReviews = (LinearLayout) view.findViewById(R.id.settingMyReviews);
        settingNotice = (Switch) view.findViewById(R.id.settingNotice);
        settingBtnNotice = (Button) view.findViewById(R.id.settingBtnNotice);

        // 로그인한 유저 정보를 받아와 프로필 사진, 이름, 학번 설정
        settingProfileImage.setImageResource(R.drawable.ic_basic_profile);
        settingName.setText("김민제");
        settingCollegeNumber.setText("학번: 20191557");


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
        settingBtnNotice.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.S)
            @Override
            public void onClick(View view){
                if (settingNotice.isChecked()){
                    noticeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_basic_profile);

                    Intent intent = new Intent(getActivity(), MainActivity.class);
//

                    // 알림 발생시키는 다른 방식인데 SDK버전 확인 필요
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel("channel1", "hello", NotificationManager.IMPORTANCE_HIGH);
                        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.createNotificationChannel(channel);
                        NotificationCompat.Builder notification = new NotificationCompat.Builder(requireContext(), "channel1");
                        notification.setContentTitle("테스트");
                        notification.setContentText("테스트 알림입니다");
                        notification.setDefaults(Notification.DEFAULT_SOUND);
                        notification.setSmallIcon(R.drawable.ic_basic_profile);

                        // PendingIntent를 통해 알림 터치 시 MainActivity로 이동할 수 있는데, 아직 모르겠음.
                        PendingIntent pendingIntent = PendingIntent.getActivity(view.getContext(), 0, intent, PendingIntent.FLAG_MUTABLE);
//                        notification.setContentIntent(pendingIntent);
                        notificationManager.notify(0, notification.build());
                    }
                }
            }


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


//                    NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
//                    notificationManager.notify(0, notification.build());


        });
        Button btnNodeTest = (Button) view.findViewById(R.id.nodeTest);
        nodeTest = view.findViewById(R.id.nodeTextView);
        btnNodeTest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                new JSONTask().execute("http://192.168.25.16:3000/post");//AsyncTask
            }
        });

        return view;
    }
    public class JSONTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("user_id", "androidTest");
                jsonObject.accumulate("name", "yun");

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);
                    //연결을 함
                    con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");//POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();

                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();
                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }

                    return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임

                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        if(reader != null){
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            nodeTest.setText(result);//서버로 부터 받은 값을 출력해주는 부
        }
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