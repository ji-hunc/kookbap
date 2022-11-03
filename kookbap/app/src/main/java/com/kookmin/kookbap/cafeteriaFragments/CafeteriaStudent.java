package com.kookmin.kookbap.cafeteriaFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kookmin.kookbap.R;
import com.kookmin.kookbap.ReviewData;
import com.kookmin.kookbap.ReviewDataAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class CafeteriaStudent extends Fragment {
    ArrayList<ReviewData> reviewData;  // recyclerView 에 넘겨줄 ReviewData 객체를 가지고 있는 리스트
    ReviewDataAdapter reviewDataAdapter;
    private final JSONObject jsonObject;
    String date;

    public CafeteriaStudent(JSONObject jsonObject, String date) {
        this.jsonObject = jsonObject;
        this.date = date;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cafeteria_hanul, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewHanul);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        reviewData = new ArrayList<>();
        reviewDataAdapter = new ReviewDataAdapter(reviewData, requireActivity().getApplicationContext());
        ArrayList<String> boothNames = new ArrayList<>(); // 식당의 각각 부스 이름이 들어갈 리스트

        try {
            JSONObject jsonObjectBoothNames = jsonObject.getJSONObject("학생식당(복지관 1층)").getJSONObject(date);
            Iterator<String> iter = jsonObjectBoothNames.keys();

            while (iter.hasNext()) {
                String boothName = iter.next().toString();
                boothNames.add(boothName);
            }

            for (int i=0; i<boothNames.size(); i++) {
                String menu = jsonObjectBoothNames.getJSONObject(boothNames.get(i)).getString("메뉴");
                String price = jsonObjectBoothNames.getJSONObject(boothNames.get(i)).getString("가격");
                price = price.replaceAll("[^0-9]", "").replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
//                Log.e("menu", menu);
//                Log.e("price", price);

                if (boothNames.get(i).equals("차이웨이<br>상시")) {
                    // 차이웨이는 \t\t\t\t\t\t\r\n 때문에 따로 예외처리
                    // "메뉴": "찹쌀탕수육 4000\t\t\t\t\t\t\r\n직화간짜장 4500\t\t\t\t\t\t\r\n직화짬뽕 5000\t\t\t\t\t\t\r\n짬짜면 6500",
                    // "가격": ""
                    if (!(menu.equals("") && price.equals(""))) { // 주말이 아닐 때
                        String[] array = menu.split("\t\t\t\t\t\t\r\n");
                        for (int j=0; j<array.length; j++) {
                            String[] arr = array[j].split(" ");
                            menu = arr[0];
                            price = arr[1].replaceAll("[^0-9]", "").replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");;
                            reviewData.add(new ReviewData(menu, "아직 작성된 리뷰가 없습니다.", price, "delicious", R.drawable.ic_setting, (float) (Math.random()*5), 0));
                        }
                    }

                } else {
                    // 일반적으로 메뉴, 가격 하나씩 나오는 경우
                    // "메뉴": "북어채무국\r\n쌀밥/계란후라이\r\n호박느타리볶음\r\n양념깻잎지\r\n",
                    // "가격": "3300"
                    String[] array = menu.split("\r\n");
                    if ( !menu.equals("") && !price.equals("")) {
                        menu = array[0];
                        if (array[0].contains("[")) {
                            // [금주의 추천메뉴], [오믈렛☆시리즈] 와 같이 [] 있을 때
                            // "메뉴": "[금주의 추천메뉴]\r\n직화)꼬치어묵우동\r\n&참치콘주먹밥\r\n와사비타코야끼\r\n단무지\r\n", "가격": "4800"
                            // "메뉴": "[오믈렛☆시리즈]\r\n포크볼★오므라이스\r\n후르츠샐러드\r\n오이&할라피뇨피클\r\n", "가격": "4500"
                            menu = array[1];
                        }
                        reviewData.add(new ReviewData(menu, "아직 작성된 리뷰가 없습니다.", price, "delicious", R.drawable.ic_setting, (float) (Math.random()*5), 0));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
//            Log.e("json2", jsonObject.toString());
        }
        recyclerView.setAdapter(reviewDataAdapter);

        return view;
    }
}