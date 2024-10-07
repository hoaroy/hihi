package com.example.Sachpee.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.Sachpee.Fragment.BottomNav.HomePageFragment;
import com.example.Sachpee.Fragment.Bill.OrderFragment;
import com.example.Sachpee.Fragment.BottomNav.PersonalFragment;
import com.example.Sachpee.Fragment.BottomNav.ChatFragment;

public class ViewPagerHomeAdapter extends FragmentStatePagerAdapter {
    public ViewPagerHomeAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1:
                return new ChatFragment();
            case 2:
                return new OrderFragment();
            default:
                return new PersonalFragment();
            case 3:
                return new PersonalFragment();
            case 0:
                return new HomePageFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
