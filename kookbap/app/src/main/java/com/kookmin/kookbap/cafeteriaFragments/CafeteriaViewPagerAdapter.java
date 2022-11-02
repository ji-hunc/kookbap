package com.kookmin.kookbap.cafeteriaFragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.json.JSONObject;

public class CafeteriaViewPagerAdapter extends FragmentStateAdapter {
    private final JSONObject jsonObject;
    String date;

    public CafeteriaViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, JSONObject jsonObject, String date) {
        super(fragmentActivity);
        this.jsonObject = jsonObject;
        this.date = date;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new CafeteriaHanul(jsonObject, date);
            case 1:
                return new CafeteriaStudent(jsonObject, date);
            case 2:
                return new CafeteriaProfessor(jsonObject, date);
            case 3:
                return new CafeteriaKBob(jsonObject, date);
            case 4:
                return new CafeteriaChungHyangKorean(jsonObject, date);
            case 5:
                return new CafeteriaChungHyangWestern(jsonObject, date);
            case 6:
                return new CafeteriaDormitory(jsonObject, date);
            default:
                return new CafeteriaHanul(jsonObject, date);
        }
    }

    @Override
    public int getItemCount() {
        return 7;
    }
}
