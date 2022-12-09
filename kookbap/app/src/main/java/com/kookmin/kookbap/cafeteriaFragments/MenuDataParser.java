package com.kookmin.kookbap.cafeteriaFragments;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MenuDataParser {
    JSONObject jsonObject;
    String date;


//    public void joinArrayList() {
//        result.addAll(menus);
//        result.addAll(prices);
//    }

    public MenuDataParser(JSONObject jsonObject, String date) {
        this.jsonObject = jsonObject;
        this.date = date;
    }

    public ArrayList<String> getHanulMenuData() {
        ArrayList<String> menus = new ArrayList<>();
        ArrayList<String> prices = new ArrayList<>();
        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> boothNames = new ArrayList<>();
        try {
            JSONObject jsonObjectBoothNames = jsonObject.getJSONObject("한울식당(법학관 지하1층)").getJSONObject(date);
            Iterator<String> iter = jsonObjectBoothNames.keys();

            while (iter.hasNext()) {
                String boothName = iter.next().toString();
                boothNames.add(boothName);
            }

            try {
                for (int i = 0; i < boothNames.size(); i++) {
                    String menu = jsonObjectBoothNames.getJSONObject(boothNames.get(i)).getString("메뉴");
                    String price = jsonObjectBoothNames.getJSONObject(boothNames.get(i)).getString("가격");
                    price = price.replaceAll("[^0-9]", "").replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
                    //                Log.e("menu", menu);
                    //                Log.e("price", price);
                    String[] array = menu.split("\r\n");

                    if (menu.equals("") || price.equals("")) {
                        // 특수한 경우: 메뉴와 가격 둘 중 하나가 비어있음
                        // ex
                        // "메뉴":"운영시간\r\n11시~18시30분","가격":""
                        // "메뉴":"","가격":""
                        // "메뉴":"[중식]\r\n불닭치즈라이스\r\n￦4500\r\n[석식]\r\n더진국\r\n얼큰국밥\r\n￦5500","가격":""
                        if (!(menu.equals("") && price.equals(""))) {
                            // 둘 다 비어있는 경우는 아예 취급 안함.
                            if (!(array[0].equals("운영시간") || array[0].equals("주말운영") || array.length == 1)) {
                                // 운영시간인 경우는 버림
                                // 결과적으로 남은 예외는
                                // "메뉴":"[중식]\r\n불닭치즈라이스\r\n￦4500\r\n[석식]\r\n더진국\r\n얼큰국밥\r\n￦5500","가격":""
                                // "메뉴":"[중식]\r\n더진국\r\n수육국밥\r\n￦5000\r\n[석식]\r\n더진국\r\n얼큰국밥\r\n￦5500", "가격": ""

                                if (array[1].equals("더진국")) {
                                    menu = array[2];
                                    price = array[3].replaceAll("[^0-9]", "").replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
                                } else {
                                    menu = array[1];
                                    price = array[2].replaceAll("[^0-9]", "").replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
                                }
                                menus.add(menu);
                                prices.add(price);

                                if (array[5].equals("더진국")) {
                                    menu = array[6];
                                    price = array[7].replaceAll("[^0-9]", "").replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
                                } else {
                                    menu = array[5];
                                    price = array[6].replaceAll("[^0-9]", "").replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
                                }
                                // 하드코딩으로 처리
                                menus.add(menu);
                                prices.add(price);
                            }

                        }

                    } else {
                        // 일반적인 경우: 메뉴와 가격 둘다 들어 있음
                        // ex
                        // "메뉴":"[중식]\r\n소고기칼국수\r\n&배추겉절이\r\n&공기밥","가격":"3700"
                        // "메뉴":"[중식]\r\n차돌된장비빔밥","가격":"4500"
                        if (array[0].equals("[중식]") || array[0].equals("[중석식]") || array[0].equals("[석식]")) {
                            // 메뉴이름 맨앞에 [중식], [중석식], [석식] 필터링
                            menu = array[1];
                        } else {
                            menu = array[0];
                        }
                        menus.add(menu);
                        prices.add(price);
                    }

                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 하나의 리스트로 반환하기 위해 조인
        result.addAll(menus);
        result.addAll(prices);

        return result;
    }

    public ArrayList<String> getStudentMenuData() {
        ArrayList<String> menus = new ArrayList<>();
        ArrayList<String> prices = new ArrayList<>();
        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> boothNames = new ArrayList<>();
        try {
            JSONObject jsonObjectBoothNames = jsonObject.getJSONObject("학생식당(복지관 1층)").getJSONObject(date);
            Iterator<String> iter = jsonObjectBoothNames.keys();

            while (iter.hasNext()) {
                String boothName = iter.next().toString();
                boothNames.add(boothName);
            }

            try {
                for (int i = 0; i < boothNames.size(); i++) {
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
                            for (int j = 0; j < array.length; j++) {
                                String[] arr = array[j].split(" ");
                                menu = arr[0];
                                price = arr[1].replaceAll("[^0-9]", "").replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
                                menus.add(menu);
                                prices.add(price);
                            }
                        }

                    } else {
                        // 일반적으로 메뉴, 가격 하나씩 나오는 경우
                        // "메뉴": "북어채무국\r\n쌀밥/계란후라이\r\n호박느타리볶음\r\n양념깻잎지\r\n",
                        // "가격": "3300"
                        String[] array = menu.split("\r\n");
                        if (!menu.equals("") && !price.equals("")) {
                            menu = array[0];
                            if (array[0].contains("[")) {
                                // [금주의 추천메뉴], [오믈렛☆시리즈] 와 같이 [] 있을 때
                                // "메뉴": "[금주의 추천메뉴]\r\n직화)꼬치어묵우동\r\n&참치콘주먹밥\r\n와사비타코야끼\r\n단무지\r\n", "가격": "4800"
                                // "메뉴": "[오믈렛☆시리즈]\r\n포크볼★오므라이스\r\n후르츠샐러드\r\n오이&할라피뇨피클\r\n", "가격": "4500"
                                menu = array[1];
                            }
                            menus.add(menu);
                            prices.add(price);
                        }
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        result.addAll(menus);
        result.addAll(prices);

        return result;
    }

    public ArrayList<String> getProfessorMenuData() {
        ArrayList<String> menus = new ArrayList<>();
        ArrayList<String> prices = new ArrayList<>();
        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> boothNames = new ArrayList<>();
        try {
            JSONObject jsonObjectBoothNames = jsonObject.getJSONObject("교직원식당(복지관 1층)").getJSONObject(date);
            Iterator<String> iter = jsonObjectBoothNames.keys();

            while (iter.hasNext()) {
                String boothName = iter.next().toString();
                boothNames.add(boothName);
            }

            try {
                for (int i=0; i<boothNames.size(); i++) {
                    String menu = jsonObjectBoothNames.getJSONObject(boothNames.get(i)).getString("메뉴");
                    String price = jsonObjectBoothNames.getJSONObject(boothNames.get(i)).getString("가격");
                    price = price.replaceAll("[^0-9]", "").replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
//                Log.e("menu", menu);
//                Log.e("price", price);

                    String[] array = menu.split("\r\n");
                    if (!(menu.equals("") || price.equals(""))) {
                        // 전부 일반적인 경우로 처리 됨
                        menu = array[0];
                        menus.add(menu);
                        prices.add(price);
                    }

                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        result.addAll(menus);
        result.addAll(prices);

        return result;
    }

    public ArrayList<String> getKBobMenuData() {
        ArrayList<String> menus = new ArrayList<>();
        ArrayList<String> prices = new ArrayList<>();
        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> boothNames = new ArrayList<>();
        try {
            JSONObject jsonObjectBoothNames = jsonObject.getJSONObject("K-Bob<sup>+</sup>").getJSONObject(date);
            Iterator<String> iter = jsonObjectBoothNames.keys();

            while (iter.hasNext()) {
                String boothName = iter.next().toString();
                boothNames.add(boothName);
            }

            try {
                for (int i=0; i<boothNames.size(); i++) {
                    String menu = jsonObjectBoothNames.getJSONObject(boothNames.get(i)).getString("메뉴");
                    String price = jsonObjectBoothNames.getJSONObject(boothNames.get(i)).getString("가격");
                    price = price.replaceAll("[^0-9]", "").replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
//                Log.e("menu", menu);
//                Log.e("price", price);

                    String[] array = menu.split("\r\n");
                    if (!((menu.equals("")) && (price.equals("")))) {
                        if (!((array[0].equals("운영시간")) || (array[0]).equals("＊ 회의 및 행사용 도시락의 경우 3일전 주문 필수") || array[0].equals("주말 및 공휴일 휴 점"))) {
                            for (int j=0; j<array.length/3 + 1; j++) {
                                menu = array[3*j];
                                price = array[3*j+1].replaceAll("[^0-9]", "").replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");;
                                menus.add(menu);
                                prices.add(price);
                            }
                        }
                    }

                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        result.addAll(menus);
        result.addAll(prices);

        return result;
    }

    public ArrayList<String> getChungHyangKoreanMenuData() {
        ArrayList<String> menus = new ArrayList<>();
        ArrayList<String> prices = new ArrayList<>();
        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> boothNames = new ArrayList<>();
        try {
            JSONObject jsonObjectBoothNames = jsonObject.getJSONObject("청향 한식당(법학관 5층)").getJSONObject(date);
            Iterator<String> iter = jsonObjectBoothNames.keys();

            while (iter.hasNext()) {
                String boothName = iter.next().toString();
                boothNames.add(boothName);
            }

            try {
                for (int i=0; i<boothNames.size(); i++) {
                    String menu = jsonObjectBoothNames.getJSONObject(boothNames.get(i)).getString("메뉴");
                    String price = jsonObjectBoothNames.getJSONObject(boothNames.get(i)).getString("가격");
                    price = price.replaceAll("[^0-9]", "").replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
//                Log.e("menu", menu);
//                Log.e("price", price);

                    String[] array = menu.split("\r\n");
                    if (!((menu.equals("")) && (price.equals("")))) {
                        menu = array[0];
                        menus.add(menu);
                        prices.add(price);
                    }

                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        result.addAll(menus);
        result.addAll(prices);

        return result;
    }

    public ArrayList<String> getChungHyangWesternMenuData() {
        ArrayList<String> menus = new ArrayList<>();
        ArrayList<String> prices = new ArrayList<>();
        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> boothNames = new ArrayList<>();
        try {
            JSONObject jsonObjectBoothNames = jsonObject.getJSONObject("청향 양식당(법학관 5층)").getJSONObject(date);
            Log.e("json1", jsonObjectBoothNames.toString());
            Iterator<String> iter = jsonObjectBoothNames.keys();
            while (iter.hasNext()) {
                String boothName = iter.next().toString();
                boothNames.add(boothName);
            }

            try {
                for (int i=0; i<boothNames.size(); i++) {
                    String menu = jsonObjectBoothNames.getJSONObject(boothNames.get(i)).getString("메뉴");
                    String price = jsonObjectBoothNames.getJSONObject(boothNames.get(i)).getString("가격");
                    price = price.replaceAll("[^0-9]", "").replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
//                Log.e("menu", menu);
//                Log.e("price", price);

                    String[] array = menu.split("\r\n");
                    if (!((menu.equals("")) && (price.equals("")))) {
                        for (int j=0; j<array.length/2; j++) {
                            menu = array[2*j];
                            price = array[2*j+1].replaceAll("[^0-9]", "").replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");;
                            if (j == 3) {
                                // 남들은 이런데           뽈보오일파스타 \r\n26,000원
                                // 너만 사탄들려서 \r\n없니  해산물토마토파스타 23,000원
                                String[] arr = menu.split(" ");
                                menu = arr[0];
                                price = arr[1].replaceAll("[^0-9]", "").replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");;
                            }
                            if (j >= 4) {
                                menu = array[2*j-1];
                                price = array[2*j].replaceAll("[^0-9]", "").replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");;
                            }
                            menus.add(menu);
                            prices.add(price);
                        }

                    }

                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("json2", jsonObject.toString());
        }
        result.addAll(menus);
        result.addAll(prices);

        return result;
    }

    public ArrayList<String> getDormitoryMenuData() {
        ArrayList<String> menus = new ArrayList<>();
        ArrayList<String> prices = new ArrayList<>();
        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> boothNames = new ArrayList<>();
        try {
            JSONObject jsonObject2 = jsonObject.getJSONObject("생활관식당 정기식(생활관 A동 1층)").getJSONObject(date);
            Log.e("json1", jsonObject2.toString());
            Iterator<String> iter = jsonObject2.keys();
            while (iter.hasNext()) {
                String boothName = iter.next().toString();
                boothNames.add(boothName);
            }

            try {
                for (int i=0; i<boothNames.size(); i++) {
                    String menu = jsonObject2.getJSONObject(boothNames.get(i)).getString("메뉴");
                    String price = jsonObject2.getJSONObject(boothNames.get(i)).getString("가격");
//                Log.e("menu", menu);
//                Log.e("price", price);

                    String[] array = menu.split("\r\n");
                    if (!menu.equals("") && !price.equals("")) {
                        menu = array[0];
                        menus.add(menu);
                        prices.add(price);
                    }

                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        result.addAll(menus);
        result.addAll(prices);

        return result;
    }
}
