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
    private JSONObject jsonObject;
    private RecyclerView recyclerView;

    public CafeteriaStudent(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cafeteria_hanul, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewHanul);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        reviewData = new ArrayList<>();
        reviewDataAdapter = new ReviewDataAdapter(reviewData, getActivity().getApplicationContext());
        Log.e("json3", jsonObject.toString());

        ArrayList<String> boothNames = new ArrayList<>();
        try {
            JSONObject jsonObject2 = jsonObject.getJSONObject("학생식당(복지관 1층)").getJSONObject("2022-10-31");
            // {"착한아침":{"메뉴":"북어채무국\r\n쌀밥\/계란후라이\r\n호박느타리볶음\r\n양념깻잎지\r\n","가격":"3300"},"가마<br>중식":{"메뉴":"[금주의 추천메뉴]\r\n직화)꼬치어묵우동\r\n&참치콘주먹밥\r\n와사비타코야끼\r\n단무지\r\n","가격":"4800"},"누들송(면)<br>중식":{"메뉴":"강된장덮밥*계란후라이\r\n\r\n단무지\r\n","가격":"3500"},"누들송<br>(카페테리아)<br>중식":{"메뉴":"","가격":""},"인터쉐프<br>중식":{"메뉴":"매시드포테이토돈까스*데미S\r\n\r\n삼색푸질리파스타\r\n오이피클\/후리가케밥\r\n","가격":"4500"},"데일리밥<br>중식":{"메뉴":"황태콩나물해장국\r\n기장밥\r\n제육두루치기\r\n어묵야채볶음\r\n얼갈이겉절이\r\n깍두기\r\n양상추샐러드\/누룽지샐러드\r\n소금건빵튀김\/수정과\r\n","가격":"6500"},"가마<br>석식":{"메뉴":"사리곰탕\r\n쌀밥\r\n동그랑땡전\r\n마늘쫑무침\r\n","가격":"4000"},"인터쉐프<br>석식":{"메뉴":"삼색도리야끼덮밥\r\n(닭고기,청경채,스크램블)\r\n\r\n카레사모사*스윗칠리S\r\n마늘쫑무침\r\n","가격":"4500"},"데일리밥<br>석식":{"메뉴":"","가격":""},"차이웨이<br>상시":{"메뉴":"찹쌀탕수육 4000\t\t\t\t\t\t\r\n직화간짜장 4500\t\t\t\t\t\t\r\n직화짬뽕 5000\t\t\t\t\t\t\r\n짬짜면 6500","가격":""},"차이웨이<br>특화":{"메뉴":"","가격":""}}
            Log.e("json1", jsonObject2.toString());
            Iterator<String> iter = jsonObject2.keys();
            while (iter.hasNext()) {
                String boothName = iter.next().toString();
                boothNames.add(boothName);
            }

            for (int i=0; i<boothNames.size(); i++) {
                String menu = jsonObject2.getJSONObject(boothNames.get(i)).getString("메뉴");
                String price = jsonObject2.getJSONObject(boothNames.get(i)).getString("가격");
                Log.e("menu", menu);
                Log.e("price", price);

                if (boothNames.get(i).equals("차이웨이<br>상시")) {
                    String[] array = menu.split("\t\t\t\t\t\t\r\n");
                    for (int j=0; j<array.length; j++) {
                        String[] arr = array[j].split(" ");
                        menu = arr[0];
                        price = arr[1];
                        reviewData.add(new ReviewData(menu, "아직 작성된 리뷰가 없습니다.", price, "delicious", R.drawable.ic_setting, (float) (Math.random()*5), 0));
                    }
                } else {
                    String[] array = menu.split("\r\n");
                    if (!(menu.equals("") && price.equals(""))) {
                        menu = array[0];
                        if (array[0].equals("[금주의 추천메뉴]")) {
                            menu = array[1];
                        }
                        reviewData.add(new ReviewData(menu, "아직 작성된 리뷰가 없습니다.", price, "delicious", R.drawable.ic_setting, (float) (Math.random()*5), 0));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("json2", jsonObject.toString());
        }

        recyclerView.setAdapter(reviewDataAdapter);

        return view;
    }
}