package com.tencent.liteav.trtcvideocalldemo.ui;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import com.chuangdu.library.util.StatusBarUtil;
import com.facebook.imagepipeline.drawable.DrawableFactory;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.liteav.login.UserModel;
import com.tencent.liteav.model.ITRTCAVCall;
import com.tencent.liteav.model.IntentParams;
import com.tencent.liteav.model.TRTCAVCallImpl;
import com.tencent.liteav.model.TRTCAVCallListener;
import com.tencent.liteav.trtcaudiocalldemo.ui.TRTCAudioCallActivity;
import com.tencent.liteav.trtcvideocalldemo.ui.videolayout.TRTCVideoLayout;
import com.tencent.liteav.trtcvideocalldemo.ui.videolayout.TRTCVideoLayoutManager;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.component.picture.imageEngine.impl.GlideEngine;
import com.tencent.qcloud.tim.uikit.event.PutMsgEvent;
import com.tencent.qcloud.tim.uikit.utils.PermissionUtils;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

import static com.chuangdu.library.util.StatusBarUtil.FlymeSetStatusBarLightMode;
import static com.chuangdu.library.util.StatusBarUtil.MIUISetStatusBarLightMode;

/**
 * 用于展示视频通话的主界面，通话的接听和拒绝就是在这个界面中完成的。
 *
 * @author guanyifeng
 */
public class TRTCVideoCallActivity extends AppCompatActivity {

    private static final String TAG = TRTCVideoCallActivity.class.getSimpleName();

    public static final int TYPE_BEING_CALLED = 1;
    public static final int TYPE_CALL         = 2;

    public static final String PARAM_GROUP_ID = "group_id";
    public static final  String PARAM_TYPE                = "type";
    public static final  String PARAM_USER                = "user_model";
    public static final  String PARAM_BG                = "bg";
    public static final  String PARAM_BEINGCALL_USER      = "beingcall_user_model";
    public static final  String PARAM_OTHER_INVITING_USER = "other_inviting_user_model";
    private static final int    MAX_SHOW_INVITING_USER    = 4;

    private static final int RADIUS = 30;

    /**
     * 界面元素相关
     */
    private ImageView              mMuteImg;
    private LinearLayout           mMuteLl;
    private ImageView              mHangupImg;
    private LinearLayout           mHangupLl;
    private ImageView              mHandsfreeImg;
    private LinearLayout           mHandsfreeLl;
    private ImageView              mDialingImg;
    private LinearLayout           mDialingLl;
    private TRTCVideoLayoutManager mLayoutManagerTrtc;
    private Group mInvitingGroup;
    private LinearLayout           mImgContainerLl;
    private TextView               mTimeTv;
    private Runnable               mTimeRunnable;
    private int                    mTimeCount;
    private Handler                mTimeHandler;
    private HandlerThread          mTimeHandlerThread;

    private View vWaitLayout;
    private View vMainLayout;
    private View vShowLayout;

    private TextView tvWaitTitle;
    private TextView tvWaitName;
    private TextView tvWaitInfo;
    private View vWaitDialing;
    private View vWaitHangup;
    private View vWaitMsg;
    private TextView tvWaitDialingText;

    /**
     * 拨号相关成员变量
     */
    private UserModel              mSelfModel;
    private List<UserModel>        mCallUserModelList = new ArrayList<>(); // 呼叫方
    private Map<String, UserModel> mCallUserModelMap  = new HashMap<>();
    private UserModel              mSponsorUserModel; // 被叫方
    private List<UserModel>        mOtherInvitingUserModelList;
    private int                    mCallType;
    private ITRTCAVCall mITRTCAVCall;
    private boolean                isHandsFree        = true;
    private boolean                isMuteMic          = false;
    private String mGroupId;

    private Vibrator mVibrator;
    private Ringtone mRingtone;

    /**
     * 拨号的回调
     */
    private TRTCAVCallListener mTRTCAVCallListener = new TRTCAVCallListener() {
        @Override
        public void onError(int code, String msg) {
            //发生了错误，报错并退出该页面
            ToastUtil.toastLongMessage("发送错误[" + code + "]:" + msg);
            finishActivity();
        }

        @Override
        public void onInvited(String sponsor, List<String> userIdList, boolean isFromGroup, int callType) {
        }

        @Override
        public void onGroupCallInviteeListUpdate(List<String> userIdList) {
        }

        @Override
        public void onUserEnter(final String userId) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showCallingView();
                    //1.先造一个虚拟的用户添加到屏幕上
                    UserModel model = new UserModel();
                    model.userId = userId;
                    model.phone = "";
                    model.userName = userId;
                    model.userAvatar = "";
                    mCallUserModelList.add(model);
                    mCallUserModelMap.put(model.userId, model);
                    TRTCVideoLayout videoLayout = addUserToManager(model);
                    if (videoLayout == null) {
                        return;
                    }
                    videoLayout.setVideoAvailable(false);
                    //2. 再获取用户资料，如果搜索到了该用户，更新用户的信息
                    ProfileManager.getInstance().getUserInfoByUserId(userId, new ProfileManager.GetUserInfoCallback() {
                        @Override
                        public void onSuccess(UserModel model) {
                            UserModel oldModel = mCallUserModelMap.get(model.userId);
                            if (oldModel != null) {
                                oldModel.userName = model.userName;
                                oldModel.userAvatar = model.userAvatar;
                                oldModel.phone = model.phone;
                                TRTCVideoLayout videoLayout = mLayoutManagerTrtc.findCloudViewView(model.userId);
                                if (videoLayout != null) {
                                    GlideEngine.loadCornerImage(videoLayout.getHeadImg(), oldModel.userAvatar, null, RADIUS);
                                    videoLayout.getUserNameTv().setText(oldModel.userName);
                                }
                            }
                        }

                        @Override
                        public void onFailed(int code, String msg) {
                            ToastUtil.toastLongMessage("获取用户" + userId + "的资料失败");
                        }
                    });
                }
            });
        }

        @Override
        public void onUserLeave(final String userId) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //1. 回收界面元素
                    mLayoutManagerTrtc.recyclerCloudViewView(userId);
                    //2. 删除用户model
                    UserModel userModel = mCallUserModelMap.remove(userId);
                    if (userModel != null) {
                        mCallUserModelList.remove(userModel);
                    }
                }
            });
        }

        @Override
        public void onReject(final String userId) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mCallUserModelMap.containsKey(userId)) {
//                        // 进入拒绝环节
//                        //1. 回收界面元素
//                        mLayoutManagerTrtc.recyclerCloudViewView(userId);
//                        //2. 删除用户model
//                        UserModel userModel = mCallUserModelMap.remove(userId);
//                        if (userModel != null) {
//                            mCallUserModelList.remove(userModel);
//                            ToastUtil.toastLongMessage(userModel.userName + "拒绝通话");
//                        }
                        callWaitTimeOut();
                    }
                }
            });
        }

        @Override
        public void onNoResp(final String userId) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mCallUserModelMap.containsKey(userId)) {
                        // 进入无响应环节
                        //1. 回收界面元素
                        mLayoutManagerTrtc.recyclerCloudViewView(userId);
                        //2. 删除用户model
                        UserModel userModel = mCallUserModelMap.remove(userId);
                        if (userModel != null) {
                            mCallUserModelList.remove(userModel);
                            ToastUtil.toastLongMessage(userModel.userName + "繁忙");
                        }
                    }

                    finishActivity2();
                }
            });
        }

        @Override
        public void onLineBusy(String userId) {
            if (mCallUserModelMap.containsKey(userId)) {
                // 进入无响应环节
                //1. 回收界面元素
                mLayoutManagerTrtc.recyclerCloudViewView(userId);
                //2. 删除用户model
                UserModel userModel = mCallUserModelMap.remove(userId);
                if (userModel != null) {
                    mCallUserModelList.remove(userModel);
                    ToastUtil.toastLongMessage(userModel.userName + "忙线");
                }
            }
        }

        @Override
        public void onCallingCancel() {
            if (mSponsorUserModel != null) {
                ToastUtil.toastLongMessage(mSponsorUserModel.userName + " 取消了通话");
            }
            finishActivity();
        }

        @Override
        public void onCallingTimeout() {
            if (mSponsorUserModel != null) {
                ToastUtil.toastLongMessage(mSponsorUserModel.userName + " 繁忙");
            }
            finishActivity();
        }

        @Override
        public void onCallEnd() {
            finishActivity();
        }

        @Override
        public void onUserVideoAvailable(final String userId, final boolean isVideoAvailable) {
            //有用户的视频开启了
            TRTCVideoLayout layout = mLayoutManagerTrtc.findCloudViewView(userId);
            if (layout != null) {
                layout.setVideoAvailable(isVideoAvailable);
                if (isVideoAvailable) {
                    mITRTCAVCall.startRemoteView(userId, layout.getVideoView());
                } else {
                    mITRTCAVCall.stopRemoteView(userId);
                }
            } else {

            }
        }

        @Override
        public void onUserAudioAvailable(String userId, boolean isVideoAvailable) {

        }

        @Override
        public void onUserVoiceVolume(Map<String, Integer> volumeMap) {
            for (Map.Entry<String, Integer> entry : volumeMap.entrySet()) {
                String          userId = entry.getKey();
                TRTCVideoLayout layout = mLayoutManagerTrtc.findCloudViewView(userId);
                if (layout != null) {
                    layout.setAudioVolumeProgress(entry.getValue());
                }
            }
        }
    };
    private ImageView             mSponsorAvatarImg;
    private TextView              mSponsorUserNameTv;
    private Group                 mSponsorGroup;

    /**
     * 主动拨打给某个用户
     *
     * @param context
     * @param models
     */
    public static void startCallSomeone(Context context, List<UserModel> models) {
        TUIKitLog.i(TAG, "startCallSomeone");
        ((TRTCAVCallImpl)TRTCAVCallImpl.sharedInstance(context)).setWaitingLastActivityFinished(false);
        Intent starter = new Intent(context, TRTCVideoCallActivity.class);
        starter.putExtra(PARAM_TYPE, TYPE_CALL);
        starter.putExtra(PARAM_USER, new IntentParams(models));
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }

    /**
     * 主动拨打给某个用户
     *
     * @param context
     * @param models
     */
    public static void startCallSomeone(Context context, List<UserModel> models, String path) {
        TUIKitLog.i(TAG, "startCallSomeone");
        ((TRTCAVCallImpl)TRTCAVCallImpl.sharedInstance(context)).setWaitingLastActivityFinished(false);
        Intent starter = new Intent(context, TRTCVideoCallActivity.class);
        starter.putExtra(PARAM_TYPE, TYPE_CALL);
        starter.putExtra(PARAM_USER, new IntentParams(models));
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        starter.putExtra(PARAM_BG, path);
        context.startActivity(starter);
    }

    /**
     * 主动拨打给某些用户
     *
     * @param context
     * @param models
     */
    public static void startCallSomePeople(Context context, List<UserModel> models, String groupId) {
        TUIKitLog.i(TAG, "startCallSomePeople");
        ((TRTCAVCallImpl)TRTCAVCallImpl.sharedInstance(context)).setWaitingLastActivityFinished(false);
        Intent starter = new Intent(context, TRTCVideoCallActivity.class);
        starter.putExtra(PARAM_GROUP_ID, groupId);
        starter.putExtra(PARAM_TYPE, TYPE_CALL);
        starter.putExtra(PARAM_USER, new IntentParams(models));
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }

    /**
     * 作为用户被叫
     *
     * @param context
     * @param beingCallUserModel
     */
    public static void startBeingCall(Context context, UserModel beingCallUserModel, List<UserModel> otherInvitingUserModel) {
        TUIKitLog.i(TAG, "startBeingCall");
        ((TRTCAVCallImpl)TRTCAVCallImpl.sharedInstance(context)).setWaitingLastActivityFinished(false);
        Intent starter = new Intent(context, TRTCVideoCallActivity.class);
        starter.putExtra(PARAM_TYPE, TYPE_BEING_CALLED);
        starter.putExtra(PARAM_BEINGCALL_USER, beingCallUserModel);
        starter.putExtra(PARAM_OTHER_INVITING_USER, new IntentParams(otherInvitingUserModel));
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TUIKitLog.i(TAG, "onCreate");

        mCallType = getIntent().getIntExtra(PARAM_TYPE, TYPE_BEING_CALLED);
        TUIKitLog.i(TAG, "mCallType: " + mCallType);
        if (mCallType == TYPE_BEING_CALLED && ((TRTCAVCallImpl)TRTCAVCallImpl.sharedInstance(this)).isWaitingLastActivityFinished()) {
            // 有些手机禁止后台启动Activity，但是有bug，比如一种场景：对方反复拨打并取消，三次以上极容易从后台启动成功通话界面，
            // 此时对方再挂断时，此通话Activity调用finish后，上一个从后台启动的Activity就会弹出。此时这个Activity就不能再启动。
            TUIKitLog.w(TAG, "ignore activity launch");
            finishActivity();
            return;
        }

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.cancelAll();
        }

        // 应用运行时，保持不锁屏、全屏化
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.videocall_activity_online_call);

        PermissionUtils.checkPermission(this, Manifest.permission.CAMERA);
        PermissionUtils.checkPermission(this, Manifest.permission.RECORD_AUDIO);

        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        mRingtone = RingtoneManager.getRingtone(this,
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));

        setTransparentForWindow();
        unStatusBarLightMode();

        initView();
        initData();
        initListener();
    }

    /**
     * 设置透明
     */
    public void setTransparentForWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public void unStatusBarLightMode() {
        int type = StatusBarUtil.StatusBarLightMode(this);
        if (type == 1) {
            MIUISetStatusBarLightMode(this, false);
        } else if (type == 2) {
            FlymeSetStatusBarLightMode(getWindow(), false);
        } else if (type == 3) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

    }

    private void finishActivity2() {
        if (vWaitLayout.getVisibility() == View.VISIBLE) {
            callWaitTimeOut();
        } else {
            ((TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(this)).setWaitingLastActivityFinished(true);
            finish();
        }
    }
    private void finishActivity() {
        ((TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(this)).setWaitingLastActivityFinished(true);
        finish();
    }

    @Override
    public void onBackPressed() {
        mITRTCAVCall.hangup();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVibrator != null) {
            mVibrator.cancel();
        }
        if (mRingtone != null) {
            mRingtone.stop();
        }
        if (mITRTCAVCall != null) {
            mITRTCAVCall.closeCamera();
            mITRTCAVCall.removeListener(mTRTCAVCallListener);
        }
        stopTimeCount();
        if (mTimeHandlerThread != null) {
            mTimeHandlerThread.quit();
        }
    }

    private void initListener() {
        mMuteLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMuteMic = !isMuteMic;
                mITRTCAVCall.setMicMute(isMuteMic);
                mMuteImg.setActivated(isMuteMic);
                ToastUtil.toastLongMessage(isMuteMic ? "开启静音" : "关闭静音");
            }
        });
        mHandsfreeLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isHandsFree = !isHandsFree;
                mITRTCAVCall.setHandsFree(isHandsFree);
                mHandsfreeImg.setActivated(isHandsFree);
                ToastUtil.toastLongMessage(isHandsFree ? "使用扬声器" : "使用听筒");
            }
        });

        vWaitLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mMuteImg.setActivated(isMuteMic);
        mHandsfreeImg.setActivated(isHandsFree);
    }

    private void initData() {
        // 初始化成员变量
        mITRTCAVCall = TRTCAVCallImpl.sharedInstance(this);
        mITRTCAVCall.addListener(mTRTCAVCallListener);
        mTimeHandlerThread = new HandlerThread("time-count-thread");
        mTimeHandlerThread.start();
        mTimeHandler = new Handler(mTimeHandlerThread.getLooper());
        // 初始化从外界获取的数据
        Intent intent = getIntent();
        //自己的资料
        mSelfModel = ProfileManager.getInstance().getUserModel();
        mCallType = intent.getIntExtra(PARAM_TYPE, TYPE_BEING_CALLED);
        mGroupId = intent.getStringExtra(PARAM_GROUP_ID);

        v();

//        if (mCallType == TYPE_BEING_CALLED) {
//            // 作为被叫
//            mSponsorUserModel = (UserModel) intent.getSerializableExtra(PARAM_BEINGCALL_USER);
//            IntentParams params = (IntentParams) intent.getSerializableExtra(PARAM_OTHER_INVITING_USER);
//            if (params != null) {
//                mOtherInvitingUserModelList = params.mUserModels;
//            }
//            dialingWait();
//            showWaitingResponseView();
//            mVibrator.vibrate(new long[] { 0, 1000, 1000 }, 0);
//            mRingtone.play();
//        } else {
//            // 主叫方
//            IntentParams params = (IntentParams) intent.getSerializableExtra(PARAM_USER);
//            if (params != null) {
//                mCallUserModelList.clear();
//                mCallUserModelList.addAll(params.mUserModels);
//                for (UserModel userModel : mCallUserModelList) {
//                    mCallUserModelMap.put(userModel.userId, userModel);
//                }
//                callWait();
//                startInviting();
//                showInvitingView();
//            }
//        }

    }

    private void v(){

        Intent intent = getIntent();

        if (mCallType == TYPE_BEING_CALLED) {
            // 作为被叫
            mSponsorUserModel = (UserModel) intent.getSerializableExtra(PARAM_BEINGCALL_USER);
            IntentParams params = (IntentParams) intent.getSerializableExtra(PARAM_OTHER_INVITING_USER);
            if (params != null) {
                mOtherInvitingUserModelList = params.mUserModels;
            }
            dialingWait();
            showWaitingResponseView();
            mVibrator.vibrate(new long[] { 0, 1000, 1000 }, 0);
            mRingtone.play();
        } else {
            // 主叫方
            IntentParams params = (IntentParams) intent.getSerializableExtra(PARAM_USER);
            if (params != null) {
                mCallUserModelList.clear();
                mCallUserModelList.addAll(params.mUserModels);
                for (UserModel userModel : mCallUserModelList) {
                    mCallUserModelMap.put(userModel.userId, userModel);
                }
                callWait();
                startInviting();
                showInvitingView();
            }
        }
    }

    private void startInviting() {
        List<String> list = new ArrayList<>();
        for (UserModel userModel : mCallUserModelList) {
            list.add(userModel.userId);
        }
        mITRTCAVCall.groupCall(list, ITRTCAVCall.TYPE_VIDEO_CALL, mGroupId);
    }

    private void initView() {
        mMuteImg = (ImageView) findViewById(R.id.img_mute);
        mMuteLl = (LinearLayout) findViewById(R.id.ll_mute);
        mHangupImg = (ImageView) findViewById(R.id.img_hangup);
        mHangupLl = (LinearLayout) findViewById(R.id.ll_hangup);
        mHandsfreeImg = (ImageView) findViewById(R.id.img_handsfree);
        mHandsfreeLl = (LinearLayout) findViewById(R.id.ll_handsfree);
        mDialingImg = (ImageView) findViewById(R.id.img_dialing);
        mDialingLl = (LinearLayout) findViewById(R.id.ll_dialing);
        mLayoutManagerTrtc = (TRTCVideoLayoutManager) findViewById(R.id.trtc_layout_manager);
        mInvitingGroup = (Group) findViewById(R.id.group_inviting);
        mImgContainerLl = (LinearLayout) findViewById(R.id.ll_img_container);
        mTimeTv = (TextView) findViewById(R.id.tv_time);
        mSponsorAvatarImg = (ImageView) findViewById(R.id.img_sponsor_avatar);
        mSponsorUserNameTv = (TextView) findViewById(R.id.tv_sponsor_user_name);
        mSponsorGroup = (Group) findViewById(R.id.group_sponsor);

        vMainLayout = findViewById(R.id.main_layout);
        vWaitLayout = findViewById(R.id.wait_layout);
        vShowLayout = findViewById(R.id.show_layout);

        tvWaitTitle = findViewById(R.id.wait_title);
        tvWaitName = findViewById(R.id.wait_name);
        tvWaitInfo = findViewById(R.id.wait_info);
        vWaitDialing = findViewById(R.id.wait_dialing);
        vWaitHangup = findViewById(R.id.wait_hangup);
        vWaitMsg = findViewById(R.id.wait_msg);

        tvWaitDialingText = findViewById(R.id.wait_dialing_text);

        String path = getIntent().getStringExtra(PARAM_BG);
        if (!TextUtils.isEmpty(path)) {
            Drawable drawable = new BitmapDrawable(path);

            vShowLayout.setBackground(drawable);
            vWaitLayout.setBackground(drawable);
        }

    }

    private void callWait(){


        vMainLayout.setVisibility(View.INVISIBLE);
        vWaitLayout.setVisibility(View.VISIBLE);

        tvWaitTitle.setText("正在呼叫中…");
        if (null != mCallUserModelList && mCallUserModelList.size() > 0) {
            tvWaitName.setText(mCallUserModelList.get(0).userName);
        }

        tvWaitInfo.setVisibility(View.GONE);

        vWaitDialing.setVisibility(View.GONE);
        vWaitHangup.setVisibility(View.VISIBLE);
        vWaitMsg.setVisibility(View.GONE);
        vWaitDialing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                mVibrator.cancel();
//                mRingtone.stop();
//                mITRTCAVCall.accept();
//                showCallingView();
                callWait();
                startInviting();
            }
        });

        vWaitHangup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mITRTCAVCall.hangup();

                ((TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(TRTCVideoCallActivity.this)).setWaitingLastActivityFinished(true);
                finish();
            }
        });
//        Observable.timer(30000, TimeUnit.MILLISECONDS).subscribe((v) -> {
//            callWaitTimeOut();
//        });
    }

    private void callWaitTimeOut(){

        tvWaitInfo.setVisibility(View.VISIBLE);
        tvWaitInfo.setText("暂无人接听，请稍后再拨！");


        tvWaitDialingText.setText("继续呼叫");
        vWaitDialing.setVisibility(View.VISIBLE);
        vWaitHangup.setVisibility(View.VISIBLE);
        vWaitMsg.setVisibility(View.VISIBLE);

        vWaitMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mCallUserModelList && mCallUserModelList.size() > 0) {
                    EventBus.getDefault().post(new PutMsgEvent(mCallUserModelList.get(0).userId));
                }

                finishActivity();
            }
        });
    }

    private void dialingWait(){
        vMainLayout.setVisibility(View.INVISIBLE);
        vWaitLayout.setVisibility(View.VISIBLE);

        tvWaitTitle.setText("邀请通话...");
        tvWaitName.setText(mSponsorUserModel.userName);

        tvWaitInfo.setVisibility(View.GONE);

        tvWaitDialingText.setText("接听");
        vWaitDialing.setVisibility(View.VISIBLE);
        vWaitHangup.setVisibility(View.GONE);
        vWaitMsg.setVisibility(View.GONE);

        vWaitDialing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mVibrator.cancel();
                mRingtone.stop();
                mITRTCAVCall.accept();
                showCallingView();
            }
        });
        vWaitHangup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mVibrator.cancel();
                mRingtone.stop();
                mITRTCAVCall.reject();
                finishActivity();
            }
        });
    }

    private void stopWait(){

//        ViewGroup.LayoutParams params = vMainLayout.getLayoutParams();
//        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
//
//        vMainLayout.setLayoutParams(params);

        vMainLayout.setVisibility(View.VISIBLE);
        vWaitLayout.setVisibility(View.GONE);

    }

    /**
     * 等待接听界面
     */
    public void showWaitingResponseView() {
        //1. 展示自己的画面
        mLayoutManagerTrtc.setMySelfUserId(mSelfModel.userId);
        TRTCVideoLayout videoLayout = addUserToManager(mSelfModel);
        if (videoLayout == null) {
            return;
        }
        videoLayout.setVideoAvailable(true);
        mITRTCAVCall.openCamera(true, videoLayout.getVideoView());

        //2. 展示对方的头像和蒙层
        mSponsorGroup.setVisibility(View.VISIBLE);
        GlideEngine.loadCornerImage(mSponsorAvatarImg, mSponsorUserModel.userAvatar, null, RADIUS);
        mSponsorUserNameTv.setText(mSponsorUserModel.userName);

        //3. 展示电话对应界面
        mHangupLl.setVisibility(View.VISIBLE);
        mDialingLl.setVisibility(View.VISIBLE);
        mHandsfreeLl.setVisibility(View.GONE);
        mMuteLl.setVisibility(View.GONE);
        //3. 设置对应的listener
        mHangupLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVibrator.cancel();
                mRingtone.stop();
                mITRTCAVCall.reject();
                finishActivity();
            }
        });
        mDialingLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVibrator.cancel();
                mRingtone.stop();
                mITRTCAVCall.accept();
                showCallingView();
            }
        });
        //4. 展示其他用户界面
        showOtherInvitingUserView();
    }

    /**
     * 展示邀请列表
     */
    public void showInvitingView() {
        //1. 展示自己的界面
        mLayoutManagerTrtc.setMySelfUserId(mSelfModel.userId);
        TRTCVideoLayout videoLayout = addUserToManager(mSelfModel);
        if (videoLayout == null) {
            return;
        }
        videoLayout.setVideoAvailable(true);
        mITRTCAVCall.openCamera(true, videoLayout.getVideoView());
        //        for (UserModel userModel : mCallUserModelList) {
        //            TRTCVideoLayout layout = addUserToManager(userModel);
        //            layout.getShadeImg().setVisibility(View.VISIBLE);
        //        }
        //2. 设置底部栏
        mHangupLl.setVisibility(View.VISIBLE);
        mHangupLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mITRTCAVCall.hangup();
                finishActivity();
            }
        });
        mDialingLl.setVisibility(View.GONE);
        mHandsfreeLl.setVisibility(View.GONE);
        mMuteLl.setVisibility(View.GONE);
        //3. 隐藏中间他们也在界面
        hideOtherInvitingUserView();
        //4. sponsor画面也隐藏
        mSponsorGroup.setVisibility(View.GONE);
    }

    /**
     * 展示通话中的界面
     */
    public void showCallingView() {

        //1. 蒙版消失
        mSponsorGroup.setVisibility(View.GONE);
        //2. 底部状态栏
        mHangupLl.setVisibility(View.VISIBLE);
        mDialingLl.setVisibility(View.GONE);
        mHandsfreeLl.setVisibility(View.VISIBLE);
        mMuteLl.setVisibility(View.VISIBLE);

        mHangupLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mITRTCAVCall.hangup();
                finishActivity();
            }
        });
        stopWait();
        showTimeCount();
        hideOtherInvitingUserView();
    }

    private void showTimeCount() {
        if (mTimeRunnable != null) {
            return;
        }
        mTimeCount = 0;
        mTimeTv.setText(getShowTime(mTimeCount));
        if (mTimeRunnable == null) {
            mTimeRunnable = new Runnable() {
                @Override
                public void run() {
                    mTimeCount++;
                    if (mTimeTv != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTimeTv.setText(getShowTime(mTimeCount));
                            }
                        });
                    }
                    mTimeHandler.postDelayed(mTimeRunnable, 1000);
                }
            };
        }
        mTimeHandler.postDelayed(mTimeRunnable, 1000);
    }

    private void stopTimeCount() {
        if (mTimeHandler != null) {
            mTimeHandler.removeCallbacks(mTimeRunnable);
        }
        mTimeRunnable = null;
    }

    private String getShowTime(int count) {
        return String.format("%02d:%02d", count / 60, count % 60);
    }

    private void showOtherInvitingUserView() {
        if (mOtherInvitingUserModelList == null || mOtherInvitingUserModelList.size() == 0) {
            return;
        }
        mInvitingGroup.setVisibility(View.VISIBLE);
        int squareWidth = getResources().getDimensionPixelOffset(R.dimen.small_image_size);
        int leftMargin  = getResources().getDimensionPixelOffset(R.dimen.small_image_left_margin);
        for (int index = 0; index < mOtherInvitingUserModelList.size() && index < MAX_SHOW_INVITING_USER; index++) {
            UserModel                 userModel    = mOtherInvitingUserModelList.get(index);
            ImageView                 imageView    = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(squareWidth, squareWidth);
            if (index != 0) {
                layoutParams.leftMargin = leftMargin;
            }
            imageView.setLayoutParams(layoutParams);
            GlideEngine.loadCornerImage(imageView, userModel.userAvatar, null, RADIUS);

            mImgContainerLl.addView(imageView);
        }
    }

    private void hideOtherInvitingUserView() {
        mInvitingGroup.setVisibility(View.GONE);
    }

    private TRTCVideoLayout addUserToManager(UserModel userModel) {
        TRTCVideoLayout layout = mLayoutManagerTrtc.allocCloudVideoView(userModel.userId);
        if (layout == null) {
            return null;
        }
        layout.getUserNameTv().setText(userModel.userName);
        if (!TextUtils.isEmpty(userModel.userAvatar)) {
            GlideEngine.loadCornerImage(layout.getHeadImg(), userModel.userAvatar, null, RADIUS);
        }
        return layout;
    }

}
