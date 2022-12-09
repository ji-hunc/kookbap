package com.kookmin.kookbap.Setting;

import static android.content.Context.MODE_PRIVATE;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.kookmin.kookbap.LoginAndSignup.UserData;
import com.kookmin.kookbap.R;

public class SettingFragment extends Fragment {
    public static Context settingContext;
    ImageView settingProfileImage;
    TextView settingName, settingCollegeNumber, textViewUserEmail, textViewAppVersion;
    LinearLayout settingMyReviews, settingLogout;
    Switch settingNotice;
    Boolean noticeOn = false;
    SharedPreferences prfNotification;
    SharedPreferences.Editor prfNotificationEditor;
    int cameraPermission, galleryPermission, noticePermission;
    private static final int SINGLE_PERMISSION = 1004;
    String userNickName, userEmail;

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

        SharedPreferences getNotice = getActivity().getSharedPreferences("isNoticeOn", MODE_PRIVATE); // 이전에 알림을 설정해두었다면 그 값을 불러옴
        noticeOn = getNotice.getBoolean("isNoticeOn", false);
        settingNotice.setChecked(noticeOn);
        settingContext = getActivity().getApplicationContext();


        // 로그인한 유저 정보를 받아와 프로필 사진, 이름, 학번 등 설정
        userNickName = UserData.getUserData(settingContext).getNickname();
        userEmail = UserData.getUserData(settingContext).getUserE_mail();
        settingProfileImage.setImageResource(R.drawable.ic_basic_profile);
        settingName.setText(userNickName);
        settingCollegeNumber.setText(userEmail);
        textViewUserEmail.setText(userEmail);


        // 프로필 사진을 눌렀을 때 사진 변경 (서버 및 DB 저장 용량 부족 문제로 보류)
//        settingProfileImage.setOnClickListener(new View.OnClickListener(){
//           @Override
//           public void onClick(View view){
//               // 카메라 및 앨범 권한 확인
//               cameraPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
//               galleryPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
//               // 카메라와 앨범 중 원하는 방법을 고르기 위한 dialog 출력
//               AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
//               dlg.setTitle("프로필 사진 설정하기");
//               final String[] selectProfileImages = new String[] {"카메라로 사진 찍기", "앨범에서 사진 가져오기"};
//               dlg.setItems(selectProfileImages, new DialogInterface.OnClickListener(){
//                   @Override
//                   public void onClick(DialogInterface dialog, int which){
//                       // 카메라로 사진 찍기
//                       if (which == 0){
//                           // 카메라 권한이 있다면 카메라 실행
//                           if (cameraPermission == PackageManager.PERMISSION_GRANTED){
//                               Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                               activityResultCamera.launch(cameraIntent);
//                           }
//                           // 카메라 권한이 없다면 권한 요청
//                           else{
//                               requestPermissions(new String[]{Manifest.permission.CAMERA}, SINGLE_PERMISSION);
//                           }
//                       }
//                       // 앨범에서 사진 가져오기
//                       else if (which == 1){
//                           // 파일 접근 권한이 있다면 앨범 실행
//                           if (galleryPermission == PackageManager.PERMISSION_GRANTED){
//                               Intent galleryIntent = new Intent(Intent.ACTION_PICK);
//                               galleryIntent.setType("image/*");
//                               activityResultGallery.launch(galleryIntent);
//                           }
//                           // 파일 접근 권한이 없다면 권한 요청
//                           else{
//                               requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, SINGLE_PERMISSION);
//                           }
//
//                       }
//                   }
//               });
//               AlertDialog dialog = dlg.create();
//               dialog.show();
//           }
//        });

        // 내가 쓴 리뷰를 눌렀을 때 화면 출력
        settingMyReviews.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), UserReviewsActivity.class);
                startActivity(intent);
            }
        });


        //알림 설정 (알림 구현은 MyFirebaseMessagingService.java에 있음)
        settingNotice.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                noticeOn = settingNotice.isChecked();
                prfNotification = getActivity().getSharedPreferences("isNoticeOn", MODE_PRIVATE);
                prfNotificationEditor = prfNotification.edit();
                prfNotificationEditor.putBoolean("isNoticeOn", noticeOn); // 알림 설정 여부를 preference에 저장
                prfNotificationEditor.commit();
            }
        });


        // 앱 버전 정보

        textViewAppVersion.setText("1.1.0");


        // 로그아웃
       settingLogout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                SharedPreferences prf = getActivity().getSharedPreferences("userData", MODE_PRIVATE);
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


//    // 권한 요청 이후 권한 결과
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
//        switch(requestCode){
//            case SINGLE_PERMISSION:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    // 카메라
//                    if (permissions[0].equals("android.permission.CAMERA")){
//                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        activityResultCamera.launch(cameraIntent);
//                    }
//                    // 갤러리
//                    else if (permissions[0].equals("android.permission.READ_EXTERNAL_STORAGE")){
//                        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
//                        galleryIntent.setType("image/*");
//                        activityResultGallery.launch(galleryIntent);
//                    }
//                }
//                // 권한 요청 필요 토스트 출력
//                else{
//                    Toast.makeText(getActivity().getApplicationContext(), "권한이 필요합니다", Toast.LENGTH_SHORT).show();
//                }
//        }
//    }
//
//    // 카메라 사진 찍기 구현
//    ActivityResultLauncher<Intent> activityResultCamera = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    if (result.getResultCode() == RESULT_OK && result.getData() != null){
//                        Bundle extras = result.getData().getExtras();
//                        imageBitmap = (Bitmap) extras.get("data");
//                        settingProfileImage.setImageBitmap(imageBitmap);
//                    }
//                }
//            }
//    );
//    // 앨범에서 사진 가져오기 구현
//    ActivityResultLauncher<Intent> activityResultGallery = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    if (result.getResultCode() == RESULT_OK && result.getData() != null){
//                        uri = result.getData().getData();
//                        try{
//                            imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), uri);
//                            settingProfileImage.setImageBitmap(imageBitmap);
//                        }
//                        catch(FileNotFoundException e){
//                            e.printStackTrace();
//                        }
//                        catch(IOException e){
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//    );

}