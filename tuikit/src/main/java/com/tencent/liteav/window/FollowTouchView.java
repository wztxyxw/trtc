package com.tencent.liteav.window;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.chuangdu.library.util.SizeUtils;
import com.tencent.qcloud.tim.uikit.R;

/**
 * Create by sun on 2020/7/31 5:16 PM
 * @author sc
 */
public class FollowTouchView extends AbsFloatBase {

    private TextView tvTime;

    public FollowTouchView(Context context) {
        super(context);

        mViewMode = WRAP_CONTENT_TOUCHABLE;

        mGravity = Gravity.START | Gravity.TOP;

        mAddX = SizeUtils.dp2px(100);
        mAddY = SizeUtils.dp2px(100);

        inflate(R.layout.view_audio_show_layout);

        tvTime = findView(R.id.time);

        mInflate.setOnTouchListener(new View.OnTouchListener() {

            private float mLastY;
            private float mLastX;
            private float mDownY;
            private float mDownX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getRawX();
                float y = event.getRawY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mDownX = x;
                        mDownY = y;
                        mLastX = x;
                        mLastY = y;
                        break;
                    case MotionEvent.ACTION_MOVE:

                        float moveX = x - mLastX;
                        float moveY = y - mLastY;

                        Log.e("TAG", moveX + " " + moveY);

                        mLayoutParams.x += moveX;
                        mLayoutParams.y += moveY;

                        mWindowManager.updateViewLayout(mInflate, mLayoutParams);

                        mLastX = x;
                        mLastY = y;
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }

                return false;
            }
        });

    }

    public void setOnClickListener(View.OnClickListener listener){
        mInflate.setOnClickListener(listener);
    }


    @Override
    protected void onAddWindowFailed(Exception e) {

    }

    public void setTime(String time){
        tvTime.setText(time);
    }
    public void setTimeVisibility(boolean show){
        tvTime.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
