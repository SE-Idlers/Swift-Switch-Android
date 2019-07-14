package com.example.win.easy.thread;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppExecutors {

    private Executor diskIO;
    private Executor mainThread;

    @Inject public AppExecutors(){
        this.diskIO= Executors.newSingleThreadExecutor();
        this.mainThread=new MainThreadExecutor();
    }

    public Executor diskIO(){return diskIO;}
    public Executor mainThread(){return mainThread;}

    private class MainThreadExecutor implements Executor{
        private Handler mainThreadHandler=new Handler(Looper.getMainLooper());
        @Override
        public void execute(Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
