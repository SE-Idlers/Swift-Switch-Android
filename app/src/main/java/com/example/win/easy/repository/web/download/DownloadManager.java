package com.example.win.easy.repository.web.download;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class DownloadManager {

    private final BlockingQueue<Runnable> queue;
    private final ThreadPoolExecutor poolExecutor;
    private final Executor mainExecutor;
    private static final int CORE_POOL_SIZE=5;
    private static final int MAX_POOL_SIZE=5;
    private static final int KEEP_ALIVE_TIME=50;

    protected DownloadManager(){
        queue=new LinkedBlockingQueue<>();
        poolExecutor=new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.MILLISECONDS,
                queue);
        mainExecutor=new MainThreadExecutor();
    }

    public void download(Runnable downloadTask){
        poolExecutor.execute(downloadTask);
    }

    public void executeInMain(Runnable runnable){
        mainExecutor.execute(runnable);
    }

    private class MainThreadExecutor implements Executor{
        private Handler mainThreadHandler=new Handler(Looper.getMainLooper());
        @Override
        public void execute(Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
