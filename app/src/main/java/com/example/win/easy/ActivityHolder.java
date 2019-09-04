package com.example.win.easy;

import com.example.win.easy.view.activity.LockActivity;
import com.example.win.easy.view.activity.MainActivity;

import java.lang.ref.WeakReference;

public class ActivityHolder {

    private static WeakReference<LockActivity> lockActivityWeakReference;
    private static WeakReference<MainActivity> mainActivityWeakReference;

    public static WeakReference<LockActivity> getLockActivity(){
        return lockActivityWeakReference;
    }

    public static WeakReference<MainActivity> getMainActivity() {
        return mainActivityWeakReference;
    }

    public static void update(MainActivity mainActivity){
        mainActivityWeakReference=new WeakReference<>(mainActivity);
    }

    public static void update(LockActivity lockActivity){
        lockActivityWeakReference=new WeakReference<>(lockActivity);
    }
}
