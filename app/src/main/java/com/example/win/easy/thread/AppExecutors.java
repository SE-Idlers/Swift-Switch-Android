package com.example.win.easy.thread;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class AppExecutors {

    private Executor diskIO;
    private Executor mainThread;
    private ThreadPoolExecutor songDownloadExecutor;

    public AppExecutors(){
        this.diskIO= Executors.newSingleThreadExecutor();
        this.mainThread=new MainThreadExecutor();
    }
    public Executor diskIO(){return diskIO;}
    public Executor mainThread(){return mainThread;}
    public ThreadPoolExecutor songDownloadExecutor(){return songDownloadExecutor;}


    private class MainThreadExecutor implements Executor{
        private Handler mainThreadHandler=new Handler(Looper.getMainLooper());
        @Override
        public void execute(Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
