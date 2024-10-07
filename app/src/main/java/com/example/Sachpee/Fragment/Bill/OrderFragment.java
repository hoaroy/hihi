package com.example.Sachpee.Fragment.Bill;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.Sachpee.Adapter.OderViewPagerAdapter;
import com.example.Sachpee.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class OrderFragment extends Fragment {


    TabLayout tabLayout_Order;
    ViewPager2 viewPager_Order;
    OderViewPagerAdapter orderViewPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order, container, false);

        tabLayout_Order = view.findViewById(R.id.tabLayout_Order);
        viewPager_Order = view.findViewById(R.id.viewPager_Order);

        orderViewPagerAdapter = new OderViewPagerAdapter(this);
        viewPager_Order.setAdapter(orderViewPagerAdapter);

        new TabLayoutMediator(tabLayout_Order, viewPager_Order, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0: tab.setText("Đơn hàng hiện tại");
                        break;
                    case 1: tab.setText("Lịch sử");
                        break;

                }
            }
        }).attach();


        return view;
    }
}