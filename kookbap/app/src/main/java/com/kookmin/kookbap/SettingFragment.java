package com.kookmin.kookbap;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

public class SettingFragment extends Fragment {
    ImageView settingProfileImage;
    TextView settingName;
    int cameraPermission, galleryPermission;
    private static final int SINGLE_PERMISSION = 1004;
    Bitmap bitmap;
    Uri uri;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        settingProfileImage = (ImageView) view.findViewById(R.id.settingProfileImage);
        settingName = (TextView) view.findViewById(R.id.settingName);

        // 프로필 사진을 눌렀을 때 사진 변경
        settingProfileImage.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view){
               // 카메라 및 앨범 권한 확인
               cameraPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
               galleryPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
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

        return view;
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
                        bitmap = (Bitmap) extras.get("data");
                        settingProfileImage.setImageBitmap(bitmap);
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
                            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), uri);
                            settingProfileImage.setImageBitmap(bitmap);
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