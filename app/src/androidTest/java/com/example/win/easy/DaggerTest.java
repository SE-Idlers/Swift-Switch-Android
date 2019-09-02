package com.example.win.easy;

import android.content.Context;

import androidx.test.InstrumentationRegistry;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.example.win.easy.dagger.component.AppComponent;
import com.example.win.easy.dagger.component.DaggerAppComponent;
import com.example.win.easy.dagger.module.RepositoryModule;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.Executor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4ClassRunner.class)
public class DaggerTest {

    /**
     * 测试@Named注解对@Component的接口也是有效的
     */
    @Test
    public void TestNamedAnnotationForComponentInterface(){
        AppComponent appComponent=constructAppComponent();

        Executor diskIO1=appComponent.diskIO();//第一次获取diskIO
        Executor diskIO2=appComponent.diskIO();//第二次获取diskIO
        Executor mainThread=appComponent.mainThread();//第一次获取mainThread

        assertEquals(diskIO1,diskIO2);//appComponent两次提供的diskIO应当相同
        assertNotEquals(diskIO1,mainThread);//diskIO与mainThread应当不同
    }

    private AppComponent constructAppComponent(){
        Context applicationContext= InstrumentationRegistry.getTargetContext();
        return DaggerAppComponent.builder()
                .repositoryModule(new RepositoryModule(applicationContext))
                .build();
    }

}
