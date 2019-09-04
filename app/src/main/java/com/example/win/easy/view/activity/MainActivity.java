package com.example.win.easy.view.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentFactory;

import com.example.win.easy.ActivityHolder;
import com.example.win.easy.R;
import com.example.win.easy.dagger.SwiftSwitchApplication;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity{

    @Inject FragmentFactory fragmentFactory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //对这个activity依赖注入（虽然只注入了一个fragmentFactory..）
        SwiftSwitchApplication.application.getMainActivityComponent().inject(this);

        //设置，使得创建fragment的时候使用自定义的fragment工厂而不是默认构造函数，这样就可以注入依赖了
        getSupportFragmentManager().setFragmentFactory(fragmentFactory);

        //更新一下全局对这个对象的引用
        ActivityHolder.update(this);

        //把xml视图文件绑定到这个对象
        setContentView(R.layout.activity_main);
    }

}
