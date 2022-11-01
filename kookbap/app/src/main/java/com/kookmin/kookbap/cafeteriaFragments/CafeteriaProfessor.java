package com.kookmin.kookbap.cafeteriaFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kookmin.kookbap.R;
import com.kookmin.kookbap.ReviewData;
import com.kookmin.kookbap.ReviewDataAdapter;

import org.json.JSONObject;

import java.util.ArrayList;

public class CafeteriaProfessor extends Fragment {
    ArrayList<ReviewData> reviewData;  // recyclerView 에 넘겨줄 ReviewData 객체를 가지고 있는 리스트
    ReviewDataAdapter reviewDataAdapter;
    private JSONObject jsonObject;
    private RecyclerView recyclerView;

    public CafeteriaProfessor(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cafeteria_hanul, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewHanul);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        reviewData = new ArrayList<>();

        reviewDataAdapter = new ReviewDataAdapter(reviewData, getActivity().getApplicationContext());

        reviewData.add(new ReviewData("qwe", "asd", "zxc", "delicious", R.drawable.ic_setting, (float) (Math.random()*5), 0));
        reviewData.add(new ReviewData("asd", "asd", "zxc", "delicious", R.drawable.ic_setting, (float) (Math.random()*5), 0));
        reviewData.add(new ReviewData("zxc", "asd", "zxc", "delicious", R.drawable.ic_setting, (float) (Math.random()*5), 0));

        recyclerView.setAdapter(reviewDataAdapter);

        return view;
    }
}