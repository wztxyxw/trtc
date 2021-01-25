package com.tencent.liteav.window;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.tencent.liteav.model.FlyAudioTimePushModel;
import com.tencent.liteav.trtcaudiocalldemo.ui.TRTCAudioCallActivity;
import com.tencent.qcloud.tim.uikit.event.FlyAudioPushEvent;
import com.tencent.qcloud.tim.uikit.event.PubliclyEvent;
import com.tencent.qcloud.tim.uikit.utils.ImConstant;
import com.tencent.qcloud.tim.uikit.utils.ImDataUtils;
import com.tencent.qcloud.tim.uikit.utils.ImPathUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author sc
 */
public class FloatWindowService extends Service {

    public static final String TAG = "FloatWindowService";

    public static final String ACTION_FOLLOW_TOUCH = "action_follow_touch";
    public static final String ACTION_KILL = "action_kill";

    private FollowTouchView mFollowTouchView;

    public FloatWindowService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        EventBus.getDefault().register(this);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();

        mTimeCount = intent.getIntExtra(ImPathUtil.DATA_TYPE_TIME_1, 0);
        mFlyTime = intent.getIntExtra(ImPathUtil.DATA_TYPE_TIME_2, 0);
        mSourceId = intent.getIntExtra(ImPathUtil.DATA_TYPE_SOURCE_ID, 0);
        isShow = intent.getBooleanExtra(ImPathUtil.DATA_TYPE_SHOW, true);

        if (ACTION_FOLLOW_TOUCH.equals(action)) {
            showFollowTouch();
        } else if (ACTION_KILL.equals(action)) {
            stopSelf();
            stopTimeCount();
        }
        return START_STICKY;
    }

    private void showFollowTouch() {
        dismissFollowTouch();
        mFollowTouchView = new FollowTouchView(FloatWindowService.this);
        mFollowTouchView.show();
        mFollowTouchView.setTimeVisibility(isShow);
        mFollowTouchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dismissFollowTouch();
//                Intent intent = new Intent(getApplication(), FlyAudioCallActivity.class);
//
//                Application.getApplication().startActivity(intent);
//                EventBus.getDefault().post(new FlyAudioReStartEvent(true));

                if (mSourceId > 0) {
                    ARouter.getInstance().build(ImPathUtil.PATH_FLY_AUDIO_CALL).navigation();
                } else {
                    ARouter.getInstance().build(ImPathUtil.PATH_IM_AUDIO_CALL).navigation();
                }
            }
        });
        showTimeCount();
    }

    private void dismissFollowTouch() {
        if (mFollowTouchView != null) {
            mFollowTouchView.remove();
            mFollowTouchView = null;
        }
    }

    @Override
    public void onDestroy() {

        EventBus.getDefault().unregister(this);
        dismissFollowTouch();

        super.onDestroy();
    }

    private Runnable mTimeRunnable;
    private int mTimeCount;
    private Handler mTimeHandler = new Handler();

    private int mFlyTime = 300;

    private int mSourceId;

    private boolean isShow;

    private void showTimeCount() {
        if (mTimeRunnable != null) {
            return;
        }
        if (null != mFollowTouchView) {
            if (mSourceId > 0) {
                mFollowTouchView.setTime(ImDataUtils.getShowTime(mFlyTime - mTimeCount));
            } else {
                mFollowTouchView.setTime(ImDataUtils.getShowTime(mTimeCount));
            }
            if (mTimeRunnable == null) {
                mTimeRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if (null != mFollowTouchView) {
                            mTimeCount++;

                            if (mSourceId > 0) {

                                int time = mFlyTime - mTimeCount;

                                if (time < 0) {
                                    mFollowTouchView.setTime(ImDataUtils.getShowTime(0));
                                } else {
                                    mFollowTouchView.setTime(ImDataUtils.getShowTime(time));
                                }
                            } else {
                                mFollowTouchView.setTime(ImDataUtils.getShowTime(mTimeCount));
                            }

                            mTimeHandler.postDelayed(mTimeRunnable, 1000);
                        }
                    }
                };
            }
            mTimeHandler.postDelayed(mTimeRunnable, 1000);
        }
    }

    private void stopTimeCount() {
        if (mTimeHandler != null) {
            mTimeHandler.removeCallbacks(mTimeRunnable);
        }
        mTimeRunnable = null;
        mFlyTime = 0;
        mTimeCount = 0;
        mSourceId = 0;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void flyAudioPushEvent(FlyAudioPushEvent event) {
        if (null != event && !TextUtils.isEmpty(event.params)) {
            FlyAudioTimePushModel model = JSON.parseObject(event.params, FlyAudioTimePushModel.class);
            if (event.openType == ImConstant.OpenType.FLY_AUDIO_TIME) {
                if (null != model && mSourceId == model.sourceId) {
                    mFlyTime = mFlyTime + 600;
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void publiclyEvent(PubliclyEvent event) {
        if (null != mFollowTouchView) {
            mFollowTouchView.setTimeVisibility(event.status);
        }
    }
}
