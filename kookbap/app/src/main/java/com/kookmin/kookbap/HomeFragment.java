package com.kookmin.kookbap;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    ReviewDataAdapter reviewDataAdapter;
    ArrayList<ReviewData> reviewData;  // recyclerView 에 넘겨줄 ReviewData 객체를 가지고 있는 리스트

    String todayDate;
    ArrayList<String> cafeteriaNames; // 식당 이름 8개 리스트 ["한울식당(법학관 지하1층)", "학생식당(복지관 1층)" ... ]
    ArrayList<String>[] eachCafeteriaBoothNames; // 각 식당마다 있는 부스 이름 있는 2차원 배열 [["1코너<br>SNACK1", "1코너<br>SNACK2", ...], ["착한아침", "가마<br>중식", ...] ...]

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        todayDate = "2022-10-31"; // 오늘 날짜가 url에 포함됨. 일단 하드 코딩

        // 총 식당 개수인 8개로 초기화
        cafeteriaNames = new ArrayList<>(8);

        // 각각 식당마다 있는 부스들을 담을 배열 8개 초기화(2차원)
        eachCafeteriaBoothNames = new ArrayList[8];
        for (int i=0; i<8; i++) {
            eachCafeteriaBoothNames[i] = new ArrayList<String>();
        }

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        // 리스트 초기화
        reviewData = new ArrayList<>();

        loadData(); // 스레드 함수 호출

        return view;
    }

    public void loadData() {
        final Handler handler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message msg) {
                recyclerView.setAdapter(reviewDataAdapter);
            }
        };
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://kmucoop.kookmin.ac.kr/menu/menujson.php?" +
                            "sdate=" + todayDate + "&edate=" +todayDate + "&today=" + todayDate);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);

                    InputStream is = connection.getInputStream();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String result;
                    while ((result = br.readLine()) != null) {
                        sb.append(result += "\n");
                        String line = br.readLine();
                    }
                    result = sb.toString();
                    // 위의 마법의 코드로 url 에 있는 Json 객체 result에 얻어오고 초기화
                    JSONObject jsonObject = new JSONObject(result);

                    // jsonObject 에 있는 최상위 Key 값(식당 이름)들 리턴
                    Iterator<String> iter = jsonObject.keys();
                    while (iter.hasNext()) {
                        String cafeteria = iter.next().toString();
//                        Log.e("cafeteria", cafeteria);
                        cafeteriaNames.add(cafeteria); // 식당 이름 8개 들어있음. 한울, 복지관 등
                    }
                    // cafeteriaNames : ["한울식당(법학관 지하1층)", "학생식당(복지관 1층)", "교직원식당(복지관 1층)", "청향 한식당(법학관 5층)", "청향 양식당(법학관 5층)", "생활관식당 일반식(생활관 A동 1층)", "생활관식당 정기식(생활관 A동 1층)", "K-Bob<sup>+</sup>"]

                    // 각각 식당마다의 부스 이름을 eachCafeteriaBoothNames 에 저장
                    for (int i=0; i<cafeteriaNames.size(); i++) {
                        try { // 생활관식당 일반식(생활관 A동 1층)이 내부 데이터가 없어서 예외가 발생함(미운영)
                            JSONObject eachCafeteriaPerDate = jsonObject.getJSONObject(cafeteriaNames.get(i)).getJSONObject(todayDate); // 각각 식당 코너가 있는 jsonObject 리턴
                            Iterator<String> eachCornerIter = eachCafeteriaPerDate.keys(); // 각각 식당 코너 이름 keys("착한아침", "가마<br>중식", "누들송(면)<br>중식", ...)
                            ArrayList<String> eachCorner = new ArrayList<>();
                            while (eachCornerIter.hasNext()) {
                                String item = eachCornerIter.next().toString();
//                                Log.e("booth", item);
                                eachCafeteriaBoothNames[i].add(item);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Log.e("exception", "exception 식당 내부에 부스가 없음 exception");
                        }
                    }

                    for (int i=0; i<cafeteriaNames.size(); i++) {
                        String menus;
                        String price;
                        for (int j=0; j<eachCafeteriaBoothNames[i].size(); j++) {
                            // ex) menus, price = 최상위 오브젝트.getJSONObject("학생식당(복지관 1층)").getJSONObject("2022-10-31").getJSONObject("누들송(면)<br>중식").getString("메뉴");
                            menus = jsonObject.getJSONObject(cafeteriaNames.get(i)).getJSONObject(todayDate).getJSONObject(eachCafeteriaBoothNames[i].get(j)).getString("메뉴");
                            price = jsonObject.getJSONObject(cafeteriaNames.get(i)).getJSONObject(todayDate).getJSONObject(eachCafeteriaBoothNames[i].get(j)).getString("가격");

//                            Log.e("menu", menus);
//                            Log.e("price", price);

                            String mainMenu = "";
                            String subMenu = "";

                            // array 에 메뉴에서 가장 \r 을 기준으로 앞에서부터 3등분
                            String[] array = menus.split("\r", 3);

                            // 매뉴 이름과 가격이 ""이 아니어야만 정상이라고 판단. 그러나 예외 있음 -> else
                            if (!(menus.equals("") || price.equals(""))) {

                                if (array[0].equals("[중식]") || array[0].equals("[중석식]") || array[0].equals("[석식]") || array[0].equals("[금주의 추천메뉴]")) { // 한울식당 메뉴이름 맨앞에 [중식] 필터링
                                    mainMenu = array[1];
                                } else {
                                    mainMenu = array[0];
                                }
                                subMenu = "아직 작성된 리뷰가 없습니다.";

                                reviewData.add(new ReviewData(mainMenu, subMenu, price, "delicious", R.drawable.ic_setting, (float) (Math.random()*5), 0));
                            }
                            else {

                                final String REGEX = "[0-9]+";
                                if (menus.matches(REGEX)) {

                                } else {
//                                    Log.e("numbers", menus);
                                    array = menus.split("\r\n");
                                    String str = "";
                                    for (String item : array) {
                                        str += item + ",";
                                    }
                                    Log.e("split", str);
                                    for (int k=0; k<array.length/2; k++) {
                                        mainMenu = array[2*k];
                                        price = array[2*k+1];
                                        Log.e("menus", mainMenu);
                                        Log.e("price", price);
                                        reviewData.add(new ReviewData(mainMenu, subMenu, price, "delicious", R.drawable.ic_setting, (float) (Math.random()*5), 0));
                                    }
                                }
                                // TODO 이 경우도 하나씩 메뉴, 가격 분리해서 reviewData 에 add 해야함
                                // 싹다 가격 항목은 ""으로 비어있는 예외. 메뉴 항목에 연달아서 몇개의 메뉴가 있음
                                // "[중식]\r\n불닭치즈라이스\r\n￦4500\r\n[석식]\r\n더진국\r\n얼큰국밥\r\n￦5500"
                                // "찹쌀탕수육 4000\t\t\t\t\t\t\r\n직화간짜장 4500\t\t\t\t\t\t\r\n직화짬뽕 5000\t\t\t\t\t\t\r\n짬짜면 6500"
                                // "포모도로\r\n17,000원\r\n감바스알아히오 \r\n17,900원\r\n까르보나라\r\n18,900원\r\n해산물토마토파스타 23,000원\r\n뽈보오일파스타 \r\n26,000원\r\n랍스터비스크로제파스타\r\n35,000원"
                                // "트러플베이컨풍기리조또\r\n19,000원"
                                // "채끝스테이크\r\n35,000원\r\n안심스테이크\r\n42,000원\r\n콜키지\r\n(1인)5,000원"
                                // "칼집삼겹도시락\r\n￦8,500\r\n\r\n항정살도시락\r\n￦9,500\r\n\r\n돈가스마요덮밥+국\r\n￦6,500\r\n\r\n핵불닭덮밥\r\n￦7,000\r\n\r\n육회비빔밥\r\n￦7,000\r\n\r\n참치비빔밥\r\n￦6,500\r\n\r\n흑돼지김치찌개\r\n￦6,500\r\n\r\n흑돼지참치김치찌개\r\n￦7,000\r\n\r\n흑돼지스팸김치찌개\r\n￦7,000"
                                // "국민김밥\r\n￦3,100\r\n\r\n돈가스김밥\r\n￦4,500"
                            }
                        }
                    }

                    reviewDataAdapter = new ReviewDataAdapter(reviewData, getActivity().getApplicationContext());
                    Message msg = handler.obtainMessage();
                    handler.sendMessage(msg);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}