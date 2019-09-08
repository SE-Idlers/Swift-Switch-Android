package com.example.win.easy.dagger;

import android.app.Application;

import com.example.win.easy.dagger.component.AppComponent;
import com.example.win.easy.dagger.component.DaggerAppComponent;
import com.example.win.easy.dagger.component.DaggerDashboardComponent;
import com.example.win.easy.dagger.component.DaggerFragmentComponent;
import com.example.win.easy.dagger.component.DaggerMainActivityComponent;
import com.example.win.easy.dagger.component.DaggerViewModelComponent;
import com.example.win.easy.dagger.component.DashboardComponent;
import com.example.win.easy.dagger.component.FragmentComponent;
import com.example.win.easy.dagger.component.MainActivityComponent;
import com.example.win.easy.dagger.component.ViewModelComponent;
import com.example.win.easy.dagger.module.ListenerModule;
import com.example.win.easy.dagger.module.RepositoryModule;
import com.example.win.easy.view.activity.LockActivity;

/**
 * <p>帮助dagger初始化component的静态工具类</p>
 * <p>在依赖注入的源头（比如说我们的mainActivity），我们需要写{@code component.inject(this);}来注入这个activity的依赖，但是这个component从哪里来呢？</p>
 * <p>如果看看这个类的那几个get方法就会发现，好像这些功能随便找个类写都可以啊，或者说，需要component的时候单独builer.build一下也可以啊，虽然说代码丑了点儿</p>
 * <p>这种做法有啥问题呢？</p>
 * <p>从设计的角度来说，最大的问题是单例。如果每个activity（当然了，虽然我们只有两个activity，但是这不影响“普适性”~）需要component的时候都自己创建个新的，
 * 就会出现问题。因为许多时候，两个activity依赖的模块是由重叠的，每个人自己创建新的component，就会导致他们依赖交叠的这些模块每个人用的不一样。这种问题就无需多说明了~自己体会
 * </p>
 * <p>从代码管理的角度来说，这会让activity看起来更丑一点，虽然也没丑多少，但是吧，本身activity就要搞一堆初始化，配置，加载等等，这种builder.build的代码又是比较丑的，
 * 而且每个component的初始化也都差不多，而且会发现component跟component之间也是有依赖的，需要稍微管理下，因此抽离出来可能会好一点</p>
 * <p>最后但是其实是最重要的-.-||，就是生命周期的问题。有些component是需要context对象的，也就是说，它的初始化需要安卓上下文（关于为什么产生这种依赖，我也觉得值得推敲，
 * 但是目前没时间去考虑这个，先放放），那么就要确保初始化的时候这个app是启动了起来的。而extend {@link Application}这个类显然是一个好的选择（他的类文档上也说了，这个类可以在需要的时候用来提供
 * 安卓context）</p>
 * <p>综上，就有了这个类</p>
 * <p>当然，这个方式是在网上看到的，而且在我找到的参考博客中占比还挺高的，可能大家认为这是一种最佳实践吧</p>
 * <p>（虽然这个类的名字起得不咋地）</p>
 */
public class SwiftSwitchApplication extends Application {

    private AppComponent appComponent;
    private ViewModelComponent viewModelComponent;
    private DashboardComponent dashboardComponent;
    private FragmentComponent fragmentComponent;
    private MainActivityComponent mainActivityComponent;

    public static SwiftSwitchApplication application;
    @Override
    public void onCreate(){
        super.onCreate();
        application=this;


    }

    private AppComponent getAppComponent() {
        if (appComponent==null){
            appComponent= DaggerAppComponent.builder()
                    .repositoryModule(new RepositoryModule(this))
                    .build();
        }
        return appComponent;
    }

    private ViewModelComponent getViewModelComponent(){
        if (viewModelComponent==null){
            viewModelComponent= DaggerViewModelComponent.builder()
                    .appComponent(getAppComponent())
                    .build();
        }
        return viewModelComponent;
    }

    private FragmentComponent getFragmentComponent(){
        if (fragmentComponent==null){
            fragmentComponent= DaggerFragmentComponent.builder()
                    .viewModelComponent(getViewModelComponent())
                    .build();
        }
        return fragmentComponent;
    }

    public MainActivityComponent getMainActivityComponent(){
        if (mainActivityComponent==null){
            mainActivityComponent= DaggerMainActivityComponent.builder()
                    .fragmentComponent(getFragmentComponent())
                    .build();
        }
        return mainActivityComponent;
    }

    public DashboardComponent getDashboardComponent(LockActivity lockActivity){
        if(dashboardComponent==null){
            dashboardComponent= DaggerDashboardComponent.builder()
                    .viewModelComponent(getViewModelComponent())
                    .listenerModule(new ListenerModule(lockActivity))
                    .build();
        }
        return dashboardComponent;
    }

    public void clearDashboardComponent(){
        dashboardComponent=null;
    }
}
