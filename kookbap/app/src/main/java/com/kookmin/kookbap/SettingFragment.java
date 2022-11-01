package com.kookmin.kookbap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingFragment extends Fragment {
    ImageView settingProfileImage;
    TextView settingName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        settingProfileImage = (ImageView) view.findViewById(R.id.settingProfileImage);
        settingName = (TextView) view.findViewById(R.id.settingName);

        settingProfileImage.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view){
               AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
               dlg.setTitle("프로필 사진 설정하기");
               final String[] selectProfileImages = new String[] {"카메라로 사진 찍기", "앨범에서 사진 가져오기"};
               dlg.setItems(selectProfileImages, new DialogInterface.OnClickListener(){
                   @Override
                   public void onClick(DialogInterface dialog, int which){
                       // 사진 설정
                       settingName.setText(selectProfileImages[which]);
                   }
               });
               AlertDialog dialog = dlg.create();
               dialog.show();
           }
        });

        return view;
    }
}