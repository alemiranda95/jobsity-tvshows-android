package com.jobsity.tvshows.ui.presentation.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.jobsity.tvshows.R;
import com.jobsity.tvshows.databinding.ActivityMainBinding;
import com.jobsity.tvshows.ui.presentation.BaseActivity;
import com.jobsity.tvshows.ui.presentation.main.MainViewModel;
import com.jobsity.tvshows.ui.presentation.pin.PinActivity;

public class MainActivity extends BaseActivity {

    //Binding
    private ActivityMainBinding mBinding;

    //ViewModel
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(mBinding.navView, navController);

        setViewModel();
        setObservers();
    }

    private void setViewModel() {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    private void setObservers() {
        mainViewModel.getMainViewState().observe(this, mainViewState -> {
            if (mainViewState.isUsePin()) {
                Intent intent = new Intent(this, PinActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}