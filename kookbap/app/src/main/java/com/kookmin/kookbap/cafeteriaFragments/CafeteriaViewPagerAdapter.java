package com.kookmin.kookbap.cafeteriaFragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.json.JSONObject;

public class CafeteriaViewPagerAdapter extends FragmentStateAdapter {
    private JSONObject jsonObject;

    public CafeteriaViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, JSONObject jsonObject) {
        super(fragmentActivity);
        this.jsonObject = jsonObject;
    }

    public CafeteriaViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public CafeteriaViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new CafeteriaHanul(jsonObject);
            case 1:
                return new CafeteriaStudent(jsonObject);
            case 2:
                return new CafeteriaProfessor(jsonObject);
            default:
                return new CafeteriaHanul(jsonObject);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
