package com.kookmin.kookbap;

import android.app.DownloadManager;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<ReviewData> reviewData;
    ArrayList<String> cafeteriaBooth;
    String todayDate;
    ReviewDataAdapter reviewDataAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        cafeteriaBooth = new ArrayList<>();
        cafeteriaBooth.add("착한아침");
        cafeteriaBooth.add("가마<br>중식");
        cafeteriaBooth.add("누들송(면)<br>중식");
        cafeteriaBooth.add("누들송<br>(카페테리아)<br>중식");
        cafeteriaBooth.add("인터쉐프<br>중식");
        cafeteriaBooth.add("데일리밥<br>중식");
        cafeteriaBooth.add("가마<br>석식");
        cafeteriaBooth.add("인터쉐프<br>석식");
        cafeteriaBooth.add("데일리밥<br>석식");
        cafeteriaBooth.add("차이웨이<br>상시");
        cafeteriaBooth.add("차이웨이<br>특화");

        todayDate = "2022-10-31";

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        reviewData = new ArrayList<>();

        loadData();
//        test
//        reviewData.add(new ReviewData("chicken", "17,000", "delicious", R.drawable.ic_review, 4.5f, 0));
//        reviewData.add(new ReviewData("hamburger", "7,000", "good", R.drawable.ic_home, 3.5f, 0));
//        reviewData.add(new ReviewData("Ronaldo", "7,000", "good", R.drawable.ic_empty_heart, 2.5f, 0));
//        reviewData.add(new ReviewData("Messi", "7,000", "good", R.drawable.ic_filled_heart, 1.5f, 0));
//        reviewData.add(new ReviewData("hamburger", "7,000", "good", R.drawable.ic_home, 3.5f, 0));
//        reviewData.add(new ReviewData("hamburger", "7,000", "good", R.drawable.ic_home, 3.5f, 0));
//        reviewData.add(new ReviewData("hamburger", "7,000", "good", R.drawable.ic_home, 3.5f, 0));
//        reviewData.add(new ReviewData("hamburger", "7,000", "good", R.drawable.ic_home, 3.5f, 0));

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
                    URL url = new URL("https://kmucoop.kookmin.ac.kr/menu/menujson.php?sdate=2022-10-31&edate=2022-10-31&today=2022-10-31");
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
                    Log.e("qwe", result);

                    JSONObject jsonObject = new JSONObject(result);

                    JSONObject cafeteriaHanul = jsonObject.getJSONObject("한울식당(법학관 지하1층)");
                    JSONObject cafeteriaStudent = jsonObject.getJSONObject("학생식당(복지관 1층)");
                    JSONObject cafeteriaProfessor = jsonObject.getJSONObject("교직원식당(복지관 1층)");

                    Log.e("한울식당", cafeteriaHanul.toString());
                    Log.e("학생식당", cafeteriaStudent.toString());
                    Log.e("교직원식당", cafeteriaProfessor.toString());

                    for (int i=0; i<cafeteriaBooth.size(); i++) {
//                        Log.e("booth", cafeteriaBooth.get(i));
//                        Log.e("booth", cafeteriaStudent.getJSONObject("2022-10-31").getJSONObject(cafeteriaBooth.get(i)).getString("메뉴"));
//                        Log.e("booth", cafeteriaStudent.getJSONObject("2022-10-31").getJSONObject(cafeteriaBooth.get(i)).getString("가격"));
                        String menuName = cafeteriaStudent.getJSONObject("2022-10-31").getJSONObject(cafeteriaBooth.get(i)).getString("메뉴");
                        String price = cafeteriaStudent.getJSONObject("2022-10-31").getJSONObject(cafeteriaBooth.get(i)).getString("가격");
                        reviewData.add(new ReviewData(menuName, price, "delicious", R.drawable.ic_home, 3, 0));
                    }
                    reviewDataAdapter = new ReviewDataAdapter(reviewData, getActivity().getApplicationContext());
                    Message msg = handler.obtainMessage();
                    handler.sendMessage(msg);
//                    sick = sick.getJSONObject("2022-10-31");
//                    sick = sick.getJSONObject("2코너<BR>NOODLE");
//                    String menu = sick.getString("메뉴");
//                    Log.e("zxc", jsonObject.toString());
//                    Log.e("zxc", sick.toString());
//                    Log.e("rty", menu);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}