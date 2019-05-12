package com.example.win.easy.thread;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {

    private Executor diskIO;
    private Executor mainThread;

    private static AppExecutors instance=new AppExecutors();
    public static AppExecutors getInstance(){return instance;}
    private AppExecutors(){
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
