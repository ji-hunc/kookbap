package com.kookmin.kookbap.cafeteriaFragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.kookmin.kookbap.MenuData;

import java.util.ArrayList;

public class CafeteriaViewPagerAdapter extends FragmentStateAdapter {
    private final ArrayList<MenuData> todayMenus;

    private final ArrayList<MenuData> menuHanul = new ArrayList<>();
    private final ArrayList<MenuData> menuStudent = new ArrayList<>();
    private final ArrayList<MenuData> menuProfessor = new ArrayList<>();
    private final ArrayList<MenuData> menuKbob = new ArrayList<>();
    private final ArrayList<MenuData> menuChungHyangKorean = new ArrayList<>();
    private final ArrayList<MenuData> menuChungHyangWestern = new ArrayList<>();
    String date;

    public CafeteriaViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<MenuData> todayMenus, String date) {
        super(fragmentActivity);
        this.todayMenus = todayMenus;
        this.date = date;

        String cafeteriaName;
        for (int i=0; i<todayMenus.size(); i++) {
            cafeteriaName = todayMenus.get(i).getRestaurant_name();
            switch (cafeteriaName) {
                case "한울식당":
                    menuHanul.add(todayMenus.get(i));
                    break;
                case "학생식당":
                    menuStudent.add(todayMenus.get(i));
                    break;
                case "교직원식당":
                    menuProfessor.add(todayMenus.get(i));
                    break;
                case "K-BOB+":
                    menuKbob.add(todayMenus.get(i));
                    break;
                case "청향 한식당":
                    menuChungHyangKorean.add(todayMenus.get(i));
                    break;
                case "청향 양식당":
                    menuChungHyangWestern.add(todayMenus.get(i));
                    break;
            }
        }
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new CafeteriaHanul(menuHanul, date);
            case 1:
                return new CafeteriaStudent(menuStudent, date);
            case 2:
                return new CafeteriaProfessor(menuProfessor, date);
            case 3:
                return new CafeteriaKBob(menuKbob, date);
            case 4:
                return new CafeteriaChungHyangKorean(menuChungHyangKorean, date);
            case 5:
                return new CafeteriaChungHyangWestern(menuChungHyangWestern, date);
            default:
                return new CafeteriaHanul(todayMenus, date);
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
