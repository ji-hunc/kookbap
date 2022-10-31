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
    ArrayList<ReviewData> reviewData;
    ArrayList<String> cafeteriaBoothStudent;
    String todayDate;
    ReviewDataAdapter reviewDataAdapter;

    ArrayList<String> cafeteriaNames;
    ArrayList<String>[] cafeteriaMenuData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        cafeteriaNames = new ArrayList<>(8);
        cafeteriaMenuData = new ArrayList[8];
        for (int i=0; i<8; i++) {
            cafeteriaMenuData[i] = new ArrayList<String>();
        }

        todayDate = "2022-10-31";

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        reviewData = new ArrayList<>();

        loadData();

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
                    JSONObject jsonObject = new JSONObject(result);

                    Iterator<String> iter = jsonObject.keys();

                    while (iter.hasNext()) {
                        String cafeteria = iter.next().toString();
                        Log.e("cafeteria", cafeteria);
                        cafeteriaNames.add(cafeteria); // 식당 이름 8개 들어있음. 한울, 복지관 등
                    }
                    for (int i=0; i<cafeteriaNames.size(); i++) {
                        JSONObject eachCafeteriaPerDate = jsonObject.getJSONObject(cafeteriaNames.get(i)).getJSONObject(todayDate); // 각각 식당 코너가 나옴
                        Iterator<String> eachCornerIter = eachCafeteriaPerDate.keys(); // 각각 식당 코너 이름 keys
//                        Log.e("keys", )
                        ArrayList<String> eachCorner = new ArrayList<>();
                        while (eachCornerIter.hasNext()) {
                            String item = eachCornerIter.next().toString();
                            Log.e("item", item);
                            cafeteriaMenuData[i].add(item);
                        }
//                        cafeteriaMenuData[i] = eachCorner;
                    }

                    for (int i=0; i<cafeteriaMenuData.length; i++) {
                        for (int j=0; j<cafeteriaMenuData[i].size(); j++) {
                            Log.e("each", cafeteriaMenuData[i].get(j));
                        }
                    }






                    JSONObject cafeteriaHanul = jsonObject.getJSONObject("한울식당(법학관 지하1층)").getJSONObject(todayDate);
                    ArrayList<String> booths = new ArrayList<>();
                    Iterator j = cafeteriaHanul.keys();
                    while(j.hasNext())
                    {
                        String b = j.next().toString();
                        booths.add(b); // 키 값 저장
                        Log.e("key", b);
                    }

                    JSONObject cafeteriaStudent = jsonObject.getJSONObject("학생식당(복지관 1층)");
                    JSONObject cafeteriaProfessor = jsonObject.getJSONObject("교직원식당(복지관 1층)");

                    for (int i=0; i<cafeteriaBoothStudent.size(); i++) {
                        String menus = cafeteriaStudent.getJSONObject(todayDate).getJSONObject(cafeteriaBoothStudent.get(i)).getString("메뉴");
                        String price = cafeteriaStudent.getJSONObject(todayDate).getJSONObject(cafeteriaBoothStudent.get(i)).getString("가격");
                        String[] array = menus.split("\r", 2);
                        if (!(menus.equals(""))) {
                            String mainMenu = array[0];
                            String subMenu = array[1];
                            subMenu = subMenu.replace("\n", " ");
                            reviewData.add(new ReviewData(mainMenu, subMenu, price, "delicious", R.drawable.ic_setting, (float) (Math.random()*5), 0));
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