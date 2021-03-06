package com.tencent.liteav.trtcaudiocalldemo.ui.audiolayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.component.gatherimage.SynthesizedImageView;
import com.wang.avi.AVLoadingIndicatorView;


/**
 *
 */
public class TRTCAudioLayout extends RelativeLayout {
    private SynthesizedImageView mHeadImg;
    private TextView               mNameTv;
    private ProgressBar            mAudioPb;
    private String                 mUserId;
    private AVLoadingIndicatorView mViewLoading;
    private FrameLayout            mShadeFl;

    public TRTCAudioLayout(Context context) {
        this(context, null);
    }

    public TRTCAudioLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.audiocall_item_user_layout, this);
        initView();
    }

    private void initView() {
        mHeadImg = (SynthesizedImageView) findViewById(R.id.img_head);
        mHeadImg.setRadius(999);
        mNameTv = (TextView) findViewById(R.id.tv_name);
        mAudioPb = (ProgressBar) findViewById(R.id.pb_audio);
        mViewLoading = (AVLoadingIndicatorView) findViewById(R.id.loading_view);
        mShadeFl = (FrameLayout) findViewById(R.id.fl_shade);
    }

    public void setAudioVolume(int vol) {
        mAudioPb.setProgress(vol);
    }

    public void setUserId(String userId) {
        mUserId = userId;
        mNameTv.setText(mUserId);
    }

    public void setBitmap(Bitmap bitmap) {
        mHeadImg.setImageBitmap(bitmap);
    }

    public SynthesizedImageView getImageView() {
        return mHeadImg;
    }

    public void startLoading() {
        mShadeFl.setVisibility(VISIBLE);
        mViewLoading.show();
    }


    public void stopLoading() {
        mShadeFl.setVisibility(GONE);
        mViewLoading.hide();
    }
}
