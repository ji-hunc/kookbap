package com.kookmin.kookbap;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class ReviewFragment extends Fragment {
    MainActivity mainActivity;
    ImageView searchImage;
    FragmentTransaction transaction;
    SearchFragment searchFragment;


    //화면에 구성될 RecyclerView 용 메서드.
    RecyclerView bestReviewerRecycler;
    ArrayList<BestReviewerData> bestReviewerData;
    RecyclerView firstReviewRecycler;
    RecyclerView startRankRecycler;
    ArrayList<MenuData> starRankReviewData;
    RecyclerView lotOfReviewRecycler;
    ArrayList<MenuData> lotOfReviewData;

    MenuDataAdapter reviewDataAdapter;
    BestReviewerDataAdapter bestReviewerDataAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_review, container, false);

        mainActivity = (MainActivity) getActivity();

        //search버튼 눌렀을때 searchFragment로 이동, 트랜잭션으로 관리.
        searchImage = view.findViewById(R.id.searchImageButton);
        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchFragment = new SearchFragment();
                transaction = mainActivity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, searchFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        // 데이터 형식 정하면 바꿀 곳, test를 위해 임시작업.
        bestReviewerRecycler = view.findViewById(R.id.bestReviewerRecycler);
        bestReviewerRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));

        firstReviewRecycler = view.findViewById(R.id.firstReviewRecycler);
        firstReviewRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));

        startRankRecycler = view.findViewById(R.id.starRankRecycler);
        startRankRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));

        lotOfReviewRecycler = view.findViewById(R.id.lotOfReviewRecycler);
        lotOfReviewRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));



        bestReviewerData = new ArrayList<>();
        starRankReviewData = new ArrayList<>();
        // 임시데이터 생성
        for( int i =0; i<5; i++){
            String name = i +"등";
            int rank = i;
            starRankReviewData.add(new MenuData( name, "아직 작성된 리뷰가 없습니다.", "unknown", "delicious", R.drawable.test_bread_picture, (float) (Math.random()*5), 0));
            bestReviewerData.add(new BestReviewerData( rank,"uzznknown",(10-i)*100));
        }

        bestReviewerDataAdapter = new BestReviewerDataAdapter();
        bestReviewerRecycler.setAdapter(bestReviewerDataAdapter);
        bestReviewerDataAdapter.setBestReviewerData(bestReviewerData);

        reviewDataAdapter = new MenuDataAdapter(starRankReviewData, getActivity().getApplicationContext());

        firstReviewRecycler.setAdapter(reviewDataAdapter);
        startRankRecycler.setAdapter(reviewDataAdapter);
        lotOfReviewRecycler.setAdapter(reviewDataAdapter);

        //테스트를 위해 같은 데이터로 구현 => 추후 수정예정.

        return view;
    }
}