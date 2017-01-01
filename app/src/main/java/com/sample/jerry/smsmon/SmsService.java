package com.sample.jerry.smsmon;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.Process;
import android.widget.Toast;

/**
 * @author Javen
 * 开启一个服务开监听数据库
 */
public class SmsService extends Service {

    private SmsObserver mObserver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "SmsService 服务器启动了....", Toast.LENGTH_LONG).show();
        // 在这里启动
        ContentResolver resolver = getContentResolver();
        mObserver = new SmsObserver(resolver, new SmsHandler(this));
        resolver.registerContentObserver(Uri.parse("content://sms"), true,mObserver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.getContentResolver().unregisterContentObserver(mObserver);
        Process.killProcess(Process.myPid());
    }
}
