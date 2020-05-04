package com.tdn.ui.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tdn.data.Constants;
import com.tdn.ui.R;
import com.tdn.ui.databinding.ActivityHomeScreenBinding;
import com.tdn.ui.view.ui.ChatList.ChatList;

public class HomeScreen extends AppCompatActivity {
    private ActivityHomeScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home_screen);
        setFragment(new ChatList(),null);
        binding.setReselected(onNavigationItemReselectedListener);
        binding.setSelected(onNavigationItemSelectedListener);
        binding.setIsLoading(false);

    }

    public void setFragment(Fragment fragment, String TAG) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction
                    .replace(R.id.container,fragment,TAG)
                    .addToBackStack(TAG)
                    .commit();
    }
    public void setBack(){
        super.onBackPressed();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return false;
        }
    };
    private BottomNavigationView.OnNavigationItemReselectedListener onNavigationItemReselectedListener = new BottomNavigationView.OnNavigationItemReselectedListener() {
        @Override
        public void onNavigationItemReselected(@NonNull MenuItem item) {

        }
    };
}
