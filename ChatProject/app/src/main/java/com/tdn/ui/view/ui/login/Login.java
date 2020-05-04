package com.tdn.ui.view.ui.login;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import com.tdn.ui.R;
import com.tdn.ui.databinding.ActivityLoginBinding;


public class Login extends AppCompatActivity {
    private ActivityLoginBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        binding.setIsLoading(false);

    }
}
