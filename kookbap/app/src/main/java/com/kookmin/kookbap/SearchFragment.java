package com.kookmin.kookbap;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;


public class SearchFragment extends Fragment {
    MainActivity mainActivity;
    ImageView seachClose;
    EditText searchTextInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        mainActivity = (MainActivity) getActivity();

        //검색들어가자마자 검색 창으로 이동, 자동으로 키보드 띄우기
        searchTextInput = view.findViewById(R.id.searchTextInput);
        searchTextInput.requestFocus();
        InputMethodManager imm = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


        //뒤로가기 눌렀을때 reviewFragment로 이동. 트랜잭션으로 관리
        seachClose = view.findViewById(R.id.seachClose);
        seachClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.getSupportFragmentManager().popBackStack();
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // 키보드 닫기
            }
        });


        // Inflate the layout for this fragment
        return view;
    }
}