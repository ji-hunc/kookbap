package com.kookmin.kookbap;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class RecommendMenuFragment extends Fragment {
    Spinner timeSpinner;
    Spinner cafeteriaSpinner;
    String[] times = {"전체", "아침", "점심", "저녁"};
    String selectTime = times[0];
    String[] cafeterias = {"전체", "한울식당", "학생식당", "교직원식당", "K-BOB",
            "청향 한식당", "청향 양식당", "생활관식당 정기식"};
    String selectCafeteria = cafeterias[0];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend_menu, container, false);
        timeSpinner = view.findViewById(R.id.recommendMenuTimeSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, times
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        timeSpinner.setAdapter(adapter);
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectTime = times[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return view;
    }
}