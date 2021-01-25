package com.chuangdu.pad.receiver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.chuangdu.library.util.LogUtil;
import com.chuangdu.pad.module.splash.SplashActivity;


public class ScPadService extends Service {
    public ScPadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        start();
    }
    private void start(){
        LogUtil.e("ScPadService", "start");
        Intent dialogIntent = new Intent(getBaseContext(), SplashActivity.class);

//        Intent dialogIntent = new Intent(getApplicationContext(), BrowserActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(dialogIntent);
//        ARouter.getInstance().build(PathUtil.PATH_SPLASH).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).navigation();



//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("cdpad://splash/open"));
//
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        getApplicationContext().startActivity(intent);
    }
}
