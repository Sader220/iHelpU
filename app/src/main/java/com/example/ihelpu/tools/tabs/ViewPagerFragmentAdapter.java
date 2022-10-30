package com.example.ihelpu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ihelpu.tools.tabs.HomeFragment;
import com.example.ihelpu.tools.tabs.InfoFragment;
import com.google.android.material.tabs.TabLayout;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter{
    private static int howManyPages = 2;

    public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return  new InfoFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return howManyPages;
    }
}
