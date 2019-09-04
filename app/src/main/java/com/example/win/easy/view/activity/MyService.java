package com.example.win.easy.view.activity;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private BroadcastReceiver mScreenOffReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent mLockIntent=new Intent(context,LockActivity.class);
            mLockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            startActivity(mLockIntent);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("ServiceSuccess");
        IntentFilter mScreenOffFilter=new IntentFilter();
        mScreenOffFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mScreenOffReceiver,mScreenOffFilter);

        return super.onStartCommand(intent, flags, startId);
    }
}
