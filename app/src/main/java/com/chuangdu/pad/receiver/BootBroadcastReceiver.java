package com.chuangdu.pad.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.chuangdu.library.util.LogUtil;


/**
 * 开机自启动的广播
 * Created by yj on 2017/5/31.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {

    static final String ACTION = Intent.ACTION_BOOT_COMPLETED;

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.e("BootBroadcastReceiver", ACTION);
        if (intent.getAction().equals(ACTION)) {
            LogUtil.e("BootBroadcastReceiver", ACTION);
//            Intent mainActivityIntent = new Intent(context, SplashActivity.class);  // 要启动的Activity
//            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(mainActivityIntent);
            Intent service = new Intent(context, ScPadService.class);
            context.startService(service);
//
//            Intent dialogIntent = new Intent(context, SplashActivity.class);
////            Intent dialogIntent = new Intent(context, BrowserActivity.class);
//            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(dialogIntent);
//            ARouter.getInstance().build(PathUtil.PATH_SPLASH).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).navigation();
        }
    }
}
