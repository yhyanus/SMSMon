package com.sample.jerry.smsmon;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

//import com.javen.util.InterceptKeyKeeper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
    private static  String TAG="SmsReceiver";
    private Context mContext;
    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    public static final String SMS_DELIVER_ACTION = "android.provider.Telephony.SMS_DELIVER";

    @Override
    public void onReceive(Context context, Intent intent) {
        this.mContext=context;
        Toast.makeText(context, "接收短信执行了.....", Toast.LENGTH_LONG).show();
        Log.e(TAG, "SMSReceiver, isOrderedBroadcast()=" );
        Log.e(TAG,"SmsReceiver onReceive...");
        String action = intent.getAction();
        if (SMS_RECEIVED_ACTION.equals(action) || SMS_DELIVER_ACTION.equals(action)) {
            //Toast.makeText(context, "开始接收短信.....", Toast.LENGTH_LONG).show();
            Log.e(TAG,"SmsReceiver onReceive..." );

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[])bundle.get("pdus");
                if (pdus != null && pdus.length > 0) {
                    SmsMessage[] messages = new SmsMessage[pdus.length];
                    for (int i = 0; i < pdus.length; i++) {
                        byte[] pdu = (byte[]) pdus[i];
                        messages[i] = SmsMessage.createFromPdu(pdu);
                    }
                    for (SmsMessage message : messages) {
                        String content = message.getMessageBody();// 得到短信内容
                        String sender = message.getOriginatingAddress();// 得到发信息的号码
                        if (true) {//content.contains(InterceptKeyKeeper.getInterceptKey(mContext))) {
                            Toast.makeText(mContext, "内容为："+content, Toast.LENGTH_LONG).show();
                            //setResultData(null);
                            this.abortBroadcast();// 中止
                        }else if (sender.equals("10010") || sender.equals("10086")) {
                            Toast.makeText(mContext, "内容为："+content, Toast.LENGTH_LONG).show();
                            this.abortBroadcast();// 中止
                        }
                        Date date = new Date(message.getTimestampMillis());
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        format.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
                        String sendContent = format.format(date) + ":" + sender + "--" + content;
                        Log.e("SmsReceicer onReceive ",sendContent +" ");

                        Toast.makeText(mContext, "sender："+sender+ ";" + "content:" + content, Toast.LENGTH_LONG).show();

                        if( content.contains("SLN:"))
                            startAlarm();
                    }
                }
            }
        }
    }
    MediaPlayer mMediaPlayer;
    private void startAlarm() {
        mMediaPlayer = MediaPlayer.create(mContext, getSystemDefultRingtoneUri());
        mMediaPlayer.setLooping(true);
        try {
            mMediaPlayer.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();
    }
    private Uri getSystemDefultRingtoneUri() {
        return RingtoneManager.getActualDefaultRingtoneUri(mContext,
                RingtoneManager.TYPE_RINGTONE);
    }
}
