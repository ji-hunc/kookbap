package com.kookmin.kookbap;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<ReviewData> reviewData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        reviewData = new ArrayList<>();

        reviewData.add(new ReviewData("chicken", "17,000", "delicious", R.drawable.ic_review, 4.5f, 0));
        reviewData.add(new ReviewData("hamburger", "7,000", "good", R.drawable.ic_home, 3.5f, 0));
        reviewData.add(new ReviewData("Ronaldo", "7,000", "good", R.drawable.ic_empty_heart, 2.5f, 0));
        reviewData.add(new ReviewData("Messi", "7,000", "good", R.drawable.ic_filled_heart, 1.5f, 0));
        reviewData.add(new ReviewData("hamburger", "7,000", "good", R.drawable.ic_home, 3.5f, 0));
        reviewData.add(new ReviewData("hamburger", "7,000", "good", R.drawable.ic_home, 3.5f, 0));
        reviewData.add(new ReviewData("hamburger", "7,000", "good", R.drawable.ic_home, 3.5f, 0));
        reviewData.add(new ReviewData("hamburger", "7,000", "good", R.drawable.ic_home, 3.5f, 0));

        ReviewDataAdapter reviewDataAdapter = new ReviewDataAdapter(reviewData, view.getContext());
        recyclerView.setAdapter(reviewDataAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        return view;
    }
}