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

        SharedPreferences getNotice = getActivity().getSharedPreferences("isNoticeOn", MODE_PRIVATE); // ????????? ????????? ????????????????????? ??? ?????? ?????????
        noticeOn = getNotice.getBoolean("isNoticeOn", false);
        settingNotice.setChecked(noticeOn);
        settingContext = getActivity().getApplicationContext();


        // ???????????? ?????? ????????? ????????? ????????? ??????, ??????, ?????? ??? ??????
        userNickName = UserData.getUserData(settingContext).getNickname();
        userEmail = UserData.getUserData(settingContext).getUserE_mail();
        settingProfileImage.setImageResource(R.drawable.ic_basic_profile);
        settingName.setText(userNickName);
        settingCollegeNumber.setText(userEmail);
        textViewUserEmail.setText(userEmail);


        // ????????? ????????? ????????? ??? ?????? ?????? (?????? ??? DB ?????? ?????? ?????? ????????? ??????)
//        settingProfileImage.setOnClickListener(new View.OnClickListener(){
//           @Override
//           public void onClick(View view){
//               // ????????? ??? ?????? ?????? ??????
//               cameraPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
//               galleryPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
//               // ???????????? ?????? ??? ????????? ????????? ????????? ?????? dialog ??????
//               AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
//               dlg.setTitle("????????? ?????? ????????????");
//               final String[] selectProfileImages = new String[] {"???????????? ?????? ??????", "???????????? ?????? ????????????"};
//               dlg.setItems(selectProfileImages, new DialogInterface.OnClickListener(){
//                   @Override
//                   public void onClick(DialogInterface dialog, int which){
//                       // ???????????? ?????? ??????
//                       if (which == 0){
//                           // ????????? ????????? ????????? ????????? ??????
//                           if (cameraPermission == PackageManager.PERMISSION_GRANTED){
//                               Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                               activityResultCamera.launch(cameraIntent);
//                           }
//                           // ????????? ????????? ????????? ?????? ??????
//                           else{
//                               requestPermissions(new String[]{Manifest.permission.CAMERA}, SINGLE_PERMISSION);
//                           }
//                       }
//                       // ???????????? ?????? ????????????
//                       else if (which == 1){
//                           // ?????? ?????? ????????? ????????? ?????? ??????
//                           if (galleryPermission == PackageManager.PERMISSION_GRANTED){
//                               Intent galleryIntent = new Intent(Intent.ACTION_PICK);
//                               galleryIntent.setType("image/*");
//                               activityResultGallery.launch(galleryIntent);
//                           }
//                           // ?????? ?????? ????????? ????????? ?????? ??????
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

        // ?????? ??? ????????? ????????? ??? ?????? ??????
        settingMyReviews.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), UserReviewsActivity.class);
                startActivity(intent);
            }
        });


        //?????? ?????? (?????? ????????? MyFirebaseMessagingService.java??? ??????)
        settingNotice.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                noticeOn = settingNotice.isChecked();
                prfNotification = getActivity().getSharedPreferences("isNoticeOn", MODE_PRIVATE);
                prfNotificationEditor = prfNotification.edit();
                prfNotificationEditor.putBoolean("isNoticeOn", noticeOn); // ?????? ?????? ????????? preference??? ??????
                prfNotificationEditor.commit();
            }
        });


        // ??? ?????? ??????

        textViewAppVersion.setText("1.1.0");


        // ????????????
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


//    // ?????? ?????? ?????? ?????? ??????
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
//        switch(requestCode){
//            case SINGLE_PERMISSION:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    // ?????????
//                    if (permissions[0].equals("android.permission.CAMERA")){
//                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        activityResultCamera.launch(cameraIntent);
//                    }
//                    // ?????????
//                    else if (permissions[0].equals("android.permission.READ_EXTERNAL_STORAGE")){
//                        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
//                        galleryIntent.setType("image/*");
//                        activityResultGallery.launch(galleryIntent);
//                    }
//                }
//                // ?????? ?????? ?????? ????????? ??????
//                else{
//                    Toast.makeText(getActivity().getApplicationContext(), "????????? ???????????????", Toast.LENGTH_SHORT).show();
//                }
//        }
//    }
//
//    // ????????? ?????? ?????? ??????
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
//    // ???????????? ?????? ???????????? ??????
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