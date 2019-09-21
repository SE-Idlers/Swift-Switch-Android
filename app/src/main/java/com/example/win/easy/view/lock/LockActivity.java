package com.example.win.easy.view.lock;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentManager;

import com.example.win.easy.R;
import com.example.win.easy.dagger.SwiftSwitchApplication;

import javax.inject.Inject;

public class LockActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    private DisplayFragment displayFragment;
    private SearchFragment searchFragment;
    private HandWritingFragment handWritingFragment;

    @Inject FragmentFactory fragmentFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwiftSwitchApplication.application.getDashboardComponent().inject(this);
        fragmentManager=getSupportFragmentManager();
        fragmentManager.setFragmentFactory(fragmentFactory);
        setContentView(R.layout.activity_lock);
        displayFragment=(DisplayFragment)fragmentManager.findFragmentById(R.id.lockDisplay);
        searchFragment= (SearchFragment) fragmentManager.findFragmentById(R.id.searchFragment);
        handWritingFragment=(HandWritingFragment)fragmentManager.findFragmentById(R.id.handWritingFragment);
        handWritingFragment.getObserved(this,recognitionResult->{
            System.out.println(recognitionResult);
            searchFragment.search(recognitionResult);
        });

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwiftSwitchApplication.application.clearDashboardComponent();
    }

}
