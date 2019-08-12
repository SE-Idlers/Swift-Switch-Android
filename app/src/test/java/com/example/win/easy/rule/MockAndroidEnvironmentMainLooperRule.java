package com.example.win.easy.rule;

import com.example.win.easy.viewmodel.SongViewModel;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.internal.schedulers.ExecutorScheduler;

/**
 * <p>人为构造一个安卓环境下的主线程</p>
 * <p>单元测试是运行在测试主机（笔记本、台式机甚至服务器）的JVM上的，而在JVM中并没有所谓安卓UI主线程这样的概念</p>
 * <p>因此在单元测试这种没有安卓环境下，主线程就无法获取到，而对某些测试比如{@link SongViewModel}</p>
 * <p>主线程的获取又是必不可少的（比如LiveData的observe方法，源码第一句就是assertMainThread，要求必须在主线程中注册），</p>
 * <p>综上，mock一个名义上的mainThread有时是必须的</p>
 */
public class MockAndroidEnvironmentMainLooperRule implements TestRule {

    //构造一个简单的Scheduler
    private Scheduler scheduler=new Scheduler() {
        @Override
        public Worker createWorker() {
            return new ExecutorScheduler.ExecutorWorker(Runnable::run,true);
        }
    };

    @Override
    public Statement apply(Statement base, Description description) {

        return new Statement() {
            @Override
            public void evaluate() {

                //设置给主线程
                RxAndroidPlugins.setMainThreadSchedulerHandler(handler->scheduler);

                try {
                    base.evaluate();
                } catch (Throwable throwable) {
                    RxAndroidPlugins.reset();
                }
            }
        };

    }
}
