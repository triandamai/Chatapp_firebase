package com.tdn.ui.adapter;

import android.view.View;

import androidx.databinding.BindingAdapter;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.todkars.shimmer.ShimmerRecyclerView;

public class CustomBindingAdapter {
    @BindingAdapter("visibleGone")
    public static void  showHide(View view, boolean show){
        view.setVisibility(show ? View.GONE : View.VISIBLE);
    }
    @BindingAdapter("showshimmer")
    public static void  showHide(ShimmerRecyclerView shimmerRecyclerView, boolean show){
        if (show) {
            shimmerRecyclerView.showShimmer();
        } else {
            shimmerRecyclerView.hideShimmer();
        }
    }
    @BindingAdapter("onItemSelected")
    public static void onNavSelected(BottomNavigationView view,BottomNavigationView.OnNavigationItemSelectedListener listener){
        view.setOnNavigationItemSelectedListener(listener);

    }
    @BindingAdapter("onItemReselected")
    public static void onNavReselected(BottomNavigationView view,BottomNavigationView.OnNavigationItemReselectedListener listener){
        view.setOnNavigationItemReselectedListener(listener);
    }
}
