package com.chuangdu.pad.widget;

import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.chuangdu.library.util.ScreenUtils;
import com.chuangdu.suyangpad.R;

/**
 * @author sc
 */
public class UpdateViewUtils {


    private FragmentActivity mActivity;

    private Listener mListener;

    private String mMsg;
    private boolean mFlag;

    public interface Listener {
        void close();
        void update();
    }

    public static UpdateViewUtils newInstance(FragmentActivity activity, String msg, boolean flag, Listener listener) {
        UpdateViewUtils f = new UpdateViewUtils();
        f.mListener = listener;
        f.mActivity = activity;
        f.mMsg = msg;
        f.mFlag = flag;
        f.onCreateView();

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        activity.addContentView(f.mView, params);
        return f;
    }

    View mView;


    private LayoutInflater mInflater;

    private int mW ;
    private int mH ;

    TextView tvMessage;

    private ProgressBar pbProgress;
    private TextView tvProgressNumber;
    View vNo;
    View vYes;

    View vMainLayout;
    View vProgressLayout;

    public View onCreateView() {
        mView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_update_layout, null, false);

        tvMessage = mView.findViewById(R.id.message);
        if (TextUtils.isEmpty(mMsg)) {
            tvMessage.setVisibility(View.GONE);
        } else {
            tvMessage.setText(mMsg);
        }

        pbProgress = mView.findViewById(R.id.progress);
        tvProgressNumber = mView.findViewById(R.id.progress_number);

        vNo = mView.findViewById(R.id.close_btn);
        vYes = mView.findViewById(R.id.dialog_custom_yes);

        vMainLayout = mView.findViewById(R.id.main_layout);
        vProgressLayout = mView.findViewById(R.id.progress_layout);

        if (mFlag) {
            vNo.setVisibility(View.GONE);
        }

        initListener();
        return mView;
    }

    private void initListener(){
        vNo.setOnClickListener(mOnClickListener);
        vYes.setOnClickListener(mOnClickListener);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isFastClick()){
                return;
            }
            if (v.getId() == R.id.close_btn) {
                hintView();
                if (null != mListener) {
                    mListener.close();
                }
            } else if (v.getId() == R.id.dialog_custom_yes) {
                if (null != mListener) {
                    vMainLayout.setVisibility(View.GONE);
                    vProgressLayout.setVisibility(View.VISIBLE);
                    mListener.update();
                }
            }
        }
    };

    public void initBarHeight(View view){

        //4.4以下 无法沉浸 状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            view.setPadding(0, ScreenUtils.getStatusHeight(mActivity), 0, 0);
        }
    }

    private void showView(){
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mActivity.addContentView(mView, params);
    }
    public void hintView(){
        ViewGroup vg = (ViewGroup) mView.getParent();
        if (null != vg) {
            vg.removeView(mView);
        }
    }

    public boolean isShow(){
        ViewGroup vg = (ViewGroup) mView.getParent();
        if (null != vg) {
            return true;
        } else {
            return false;
        }
    }

    private long mClickTime = System.currentTimeMillis() + 500;
    private static final long INTERVAL_TIME = 500;

    public boolean isFastClick(){

        boolean fast = false;

        if (System.currentTimeMillis() - mClickTime < INTERVAL_TIME) {
            fast = true;
        }
        mClickTime = System.currentTimeMillis();
        return fast;
    }

    public void setProgress(int value) {
        pbProgress.setProgress(value);
        tvProgressNumber.setText(value+"%");
    }

}
