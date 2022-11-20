package com.kookmin.kookbap.ReviewRank;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.kookmin.kookbap.MainActivity;
import com.kookmin.kookbap.MenuData;
import com.kookmin.kookbap.MenuDataAdapter;
import com.kookmin.kookbap.R;
import com.kookmin.kookbap.Retrofits.RankData;
import com.kookmin.kookbap.Retrofits.RetrofitClient;
import com.kookmin.kookbap.cafeteriaFragments.CafeteriaViewPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewFragment extends Fragment {
    MainActivity mainActivity;
    LinearLayout searchLayout;
    FragmentTransaction transaction;
    SearchFragment searchFragment;

    // 더보기 버튼
    LinearLayout bestReviewerShowMore, firstReviewShowMore, starRankShowMore, countRankShowMore;

    //화면에 구성될 RecyclerView 용 메서드.
    RecyclerView bestReviewerRecycler;
    RecyclerView firstReviewRecycler;
    RecyclerView startRankRecycler;
    ArrayList<MenuData> starRankReviewData;
    RecyclerView lotOfReviewRecycler;

    ArrayList<MenuData> lotOfReviewData;  //todo 차례로 데이터 불러오게 만들면 굳이 여러개의 arraylist를 필요가 있을까?

    BestReviewerDataAdapter bestReviewerDataAdapter;
    MenuDataAdapter menuDataAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_review, container, false);

        mainActivity = (MainActivity) getActivity();

        //search버튼 눌렀을때 searchFragment로 이동, 트랜잭션으로 관리.
        searchLayout = view.findViewById(R.id.searchLayout);
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchFragment = new SearchFragment();
                transaction = mainActivity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, searchFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        //더보기 구현
        bestReviewerShowMore = view.findViewById(R.id.bestReviewerShowMore);
        firstReviewShowMore = view.findViewById(R.id.firstReviewShowMore);
        starRankShowMore = view.findViewById(R.id.starRankShowMore);
        countRankShowMore= view.findViewById(R.id.countRankShowMore);

        bestReviewerShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadShowMore("bestReviewer");
            }
        });
        firstReviewShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadShowMore("firstReviewer");
            }
        });
        starRankShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadShowMore("starRank");
            }
        });
        countRankShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadShowMore("countRank");
            }
        });



        // 데이터 형식 정하면 바꿀 곳, test를 위해 임시작업.
        bestReviewerRecycler = view.findViewById(R.id.bestReviewerRecycler);


        firstReviewRecycler = view.findViewById(R.id.firstReviewRecycler);
        firstReviewRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()){
            @Override
            public boolean canScrollVertically(){
                return false;
            }
        });
        startRankRecycler = view.findViewById(R.id.starRankRecycler);
        startRankRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()){
            @Override
            public boolean canScrollVertically(){
                return false;
            }
        });
        lotOfReviewRecycler = view.findViewById(R.id.countRankRecycler);
        lotOfReviewRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()){
            @Override
            public boolean canScrollVertically(){
                return false;
            }
        });



        starRankReviewData = new ArrayList<>();
        // 임시데이터 생성
        for( int i =0; i<3; i++){
            String name = i +"등";
            int rank = i;
            starRankReviewData.add(new MenuData( name, "아직 작성된 리뷰가 없습니다.", "unknown", "delicious", R.drawable.ic_setting, (float) (Math.random()*5), 0, "식당이름 알 수 없음"));
        }

        Call<ArrayList<RankData>> call;
        call = RetrofitClient.getApiService().getUserReviewRankData();
        call.enqueue(new Callback<ArrayList<RankData>>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.code()==200){
                    ArrayList<RankData> bestReviewerData=(ArrayList<RankData>) response.body();
                    bestReviewerDataAdapter = new BestReviewerDataAdapter(bestReviewerData, getActivity().getApplicationContext());
                    bestReviewerData.add(new RankData("1","1","2"));
                }else{
                }
            }
            @Override
            public void onFailure(Call<ArrayList<RankData>> call, Throwable t) {
                Log.d("tag","fail");
            }
        });

        bestReviewerRecycler.setAdapter(bestReviewerDataAdapter);
        bestReviewerRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()){
            @Override
            public boolean canScrollVertically(){
                return false;
            }
        });




        menuDataAdapter = new MenuDataAdapter(starRankReviewData, getActivity().getApplicationContext());
        firstReviewRecycler.setAdapter(menuDataAdapter);
        startRankRecycler.setAdapter(menuDataAdapter);
        lotOfReviewRecycler.setAdapter(menuDataAdapter);

        //테스트를 위해 같은 데이터로 구현 => 추후 수정예정.
        return view;
    }

    //더보기 버튼 눌렀을때 activity 생성 refactoring
    public void loadShowMore(String data){
        Intent intent = new Intent(mainActivity.getApplicationContext(),ReviewTabActivity.class);
        intent.putExtra("targetData",data);
        startActivity(intent);
    }
}