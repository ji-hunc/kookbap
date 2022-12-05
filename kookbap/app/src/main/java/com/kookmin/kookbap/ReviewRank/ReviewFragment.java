package com.kookmin.kookbap.ReviewRank;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.kookmin.kookbap.LoginAndSignup.UserData;
import com.kookmin.kookbap.MainActivity;
import com.kookmin.kookbap.MenuData2;
import com.kookmin.kookbap.MenuDataAdapter2;
import com.kookmin.kookbap.R;
import com.kookmin.kookbap.Retrofits.RetrofitClient;
import com.kookmin.kookbap.WriteReview;

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
    LinearLayout bestReviewerShowMore, mostLikeMenuShowMore, starRankShowMore, countRankShowMore;

    //화면에 구성될 RecyclerView 용 메서드.
    RecyclerView bestReviewerRecycler; //베스트리뷰어
    BestReviewerDataAdapter bestReviewerDataAdapter;

    RecyclerView mostLikeMenuRecycler; //좋아요많은 메뉴 순
    MenuDataAdapter2 mostLikeMenuCallAdapter;

    RecyclerView startRankRecycler; //별점높은순
    MenuDataAdapter2 menuStarRankAdapter;

    RecyclerView lotOfReviewRecycler;
    MenuDataAdapter2 lotOfReviewAdapter;

    String userID;

    boolean a , b, c, d;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = LayoutInflater.from(getActivity()).inflate(
                R.layout.loading_dialog,
                null);
        builder.setView(dialogView);
        LottieAnimationView lottieAnimationView = (LottieAnimationView)dialogView.findViewById(R.id.loadingAnimationView);
        lottieAnimationView.setAnimation("loading.json");
        AlertDialog alertDialog =  builder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        lottieAnimationView.playAnimation();
        ((TextView)dialogView.findViewById(R.id.loadingDialogText)).setText("불러오는 중...");
        alertDialog.show();


        a = b = c = d = false;

        View view =inflater.inflate(R.layout.fragment_review, container, false);

        mainActivity = (MainActivity) getActivity();
        userID = UserData.getUserData(mainActivity).getUserId();

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
        mostLikeMenuShowMore = view.findViewById(R.id.mostLikeMenuShowMore);
        starRankShowMore = view.findViewById(R.id.starRankShowMore);
        countRankShowMore= view.findViewById(R.id.countRankShowMore);


        //더보기 버튼 눌렀을때 동작하는 함수바꾸기
        setShowMoreCreateView(bestReviewerShowMore,"bestReviewer");
        setShowMoreCreateView(mostLikeMenuShowMore,"mostLikeMenu");
        setShowMoreCreateView(starRankShowMore,"starRank");
        setShowMoreCreateView(countRankShowMore,"countRank");


        //항목별 리사이클러뷰
        bestReviewerRecycler = view.findViewById(R.id.bestReviewerRecycler); //베스트 리뷰어 항목
        mostLikeMenuRecycler = view.findViewById(R.id.mostLikeMenuRecycler); //좋아요 많은순 항목
        startRankRecycler = view.findViewById(R.id.starRankRecycler); //별점높은순 항목
        lotOfReviewRecycler = view.findViewById(R.id.countRankRecycler); //리뷰 많은 순 항목

        //베트스 리뷰어 항목
        Call<ArrayList<UserRankData>> rankDataCall;
        rankDataCall = RetrofitClient.getApiService().getUserReviewRankData(4);
        rankDataCall.enqueue(new Callback<ArrayList<UserRankData>>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                a = true;
                if (response.code()==200){
                    ArrayList<UserRankData> bestReviewerData=(ArrayList<UserRankData>) response.body();
                    bestReviewerDataAdapter = new BestReviewerDataAdapter(bestReviewerData, getActivity().getApplicationContext());
                    bestReviewerRecycler.setAdapter(bestReviewerDataAdapter);
                    bestReviewerRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()){
                        @Override
                        public boolean canScrollVertically(){
                            return false;
                        } //스크롤 방지
                    });
                }else{}
                if(a&&b&&c&&d)
                    alertDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<UserRankData>> call, Throwable t) {
                a = true;
                if(a&&b&&c&&d)
                    alertDialog.dismiss();
                Log.d("tag","fail");
            }
        });

        //좋아요 많은 순 항목
        Call<ArrayList<MenuData2>> mostLikeMenuCall;
        mostLikeMenuCall = RetrofitClient.getApiService().getMenuReviewRankData("total_like",3,userID);
        mostLikeMenuCall.enqueue(new Callback<ArrayList<MenuData2>>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                b = true;
                if (response.code()==200){
                    ArrayList<MenuData2> mostLikeCallData=(ArrayList<MenuData2>) response.body();
                    mostLikeMenuCallAdapter = new MenuDataAdapter2(mostLikeCallData, getActivity().getApplicationContext());
                    mostLikeMenuRecycler.setAdapter(mostLikeMenuCallAdapter);
                    mostLikeMenuRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()){
                        @Override
                        public boolean canScrollVertically(){
                            return false;
                        } //스크롤 방지
                    });
                }else{
                }
                if(a&&b&&c&&d)
                    alertDialog.dismiss();
            }
            @Override
            public void onFailure(Call<ArrayList<MenuData2>> call, Throwable t) {
                b = true;
                if(a&&b&&c&&d)
                    alertDialog.dismiss();
            }
        });

        //별점높은 순 항목
        Call<ArrayList<MenuData2>> starRankCall;
        starRankCall = RetrofitClient.getApiService().getMenuReviewRankData("star_avg",3,userID);
        starRankCall.enqueue(new Callback<ArrayList<MenuData2>>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                c = true;
                if (response.code()==200){
                    ArrayList<MenuData2> starRankCallData=(ArrayList<MenuData2>) response.body();
                    menuStarRankAdapter = new MenuDataAdapter2(starRankCallData, getActivity().getApplicationContext());
                    startRankRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()){
                        @Override
                        public boolean canScrollVertically(){
                            return false;
                        } //스크롤 방지

                    });

                    startRankRecycler.setAdapter(menuStarRankAdapter);

                }else{
                }
                if(a&&b&&c&&d)
                    alertDialog.dismiss();
            }
            @Override
            public void onFailure(Call<ArrayList<MenuData2>> call, Throwable t) {
                c = true;
            }
        });

        //리뷰 많은 순 항목 (항목 다른걸로 바까야될듯)
        Call<ArrayList<MenuData2>> lotOfReviewCall;
        lotOfReviewCall = RetrofitClient.getApiService().getMenuReviewRankData("count_review",3,userID);
        lotOfReviewCall.enqueue(new Callback<ArrayList<MenuData2>>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                d = true;
                if (response.code()==200){
                    ArrayList<MenuData2> lotOfReviewCallData=(ArrayList<MenuData2>) response.body();
                    lotOfReviewAdapter = new MenuDataAdapter2(lotOfReviewCallData, getActivity().getApplicationContext());
                    lotOfReviewRecycler.setAdapter(lotOfReviewAdapter);
                    lotOfReviewRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()){
                        @Override
                        public boolean canScrollVertically(){
                            return false;
                        }
                    });
                }else{
                }
                if(a&&b&&c&&d)
                    alertDialog.dismiss();
            }
            @Override
            public void onFailure(Call<ArrayList<MenuData2>> call, Throwable t) {
                d = true;
            }
        });

        return view;
    }


    public void setShowMoreCreateView(View v, String data){
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity.getApplicationContext(), ShowMoreRankActivity.class);
                intent.putExtra("targetData",data);
                startActivity(intent);;
            }
        });
    }

}