package com.huantansheng.easyphotos.ui;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.huantansheng.easyphotos.R;

/**
 * @author sc
 */
public class EasyPhotosDialog {
    private final static String EXTRA_SHOW_BOTTOM = "show_bottom";
    private final static String EXTRA_MENUS = "menus";
    private final static String CHOSE = "chose";

    TextView tvSubmitBtn;
    ImageView ivCloseBtn;

    TextView tvTel;
    TextView tvMoneyTitle;
    TextView tvMoney;
    TextView tvMsgCodeBtn;

//    PayPwdEditText ptMsgCode;

    private String mTel;
    private double mMoney;
    private String mTitle;

    private Activity mActivity;

    private int mSendTimes = 0;

    //private MyCountDownTimer myCountDownTimer;

    private int mExpiration = 60;
    private Listener mListener;
    public interface Listener {
        void onSuccess(String code);
        void onGetMsgCode();
        void close();
    }

    public static EasyPhotosDialog newInstance(Activity activity, String title, double money, String tel, Listener listener) {
        EasyPhotosDialog f = new EasyPhotosDialog();
        f.mListener = listener;
        f.mTel = tel;
        f.mActivity = activity;
        f.mMoney = money;
        f.mTitle = title;
        f.onCreateView();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        activity.addContentView(f.mView, params);
        f.mView.setVisibility(View.GONE);

        return f;
    }
    View mView;

    public View onCreateView() {
        mView = LayoutInflater.from(mActivity).inflate(R.layout.easy_photos_view, null, false);
//
//        ptMsgCode = mView.findViewById(R.id.msg_code);
//        tvTel = mView.findViewById(R.id.tel);
//        tvMoneyTitle = mView.findViewById(R.id.money_title);
//        tvMoney = mView.findViewById(R.id.money);
//        tvMsgCodeBtn = mView.findViewById(R.id.get_code_btn);
//
//        tvSubmitBtn = mView.findViewById(R.id.submit_btn);
//        ivCloseBtn = mView.findViewById(R.id.close_btn);
//
//        tvMoneyTitle.setText(mTitle);
//        tvMoney.setText("¥ " + DataUtils.doubleFormat(mMoney, 2));
//
//        ptMsgCode.initStyle(R.drawable.pay_pwd_edit_num_bg, 6, 1f, R.color.c1, R.color.c6, 25);
//
//        ptMsgCode.setOnTextFinishListener(new PayPwdEditText.OnTextFinishListener() {
//            @Override
//            public void onFinish(String str) {//密码输入完后的回调
//                //手动收起软键盘
//                InputMethodManager imm = (InputMethodManager)  mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(ptMsgCode.getWindowToken(), 0);
//                //支付密码输入框消失
//            }
//        });
//
//        tvMsgCodeBtn.setOnClickListener(mOnClickListener);
//        tvSubmitBtn.setOnClickListener(mOnClickListener);
//        ivCloseBtn.setOnClickListener(mOnClickListener);

        return mView;
    }

//    View.OnClickListener mOnClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            if (v.getId() == R.id.submit_btn) {
//                submit();
//            }
//            if (v.getId() == R.id.close_btn) {
//                dismiss();
//                if (null != mListener) {
//                    mListener.close();
//                }
//            }
//            if (v.getId() == R.id.get_code_btn) {
//                if (null != mListener) {
//                    mListener.onGetMsgCode();
//                }
//            }
//        }
//    };

//    private void submit(){
//        if (TextUtils.isEmpty(ptMsgCode.getPwdText())) {
//            ToastUtil.getInstance().toast("验证码不能为空");
//            return;
//        }
//
//        if (null != mListener) {
//            mListener.onSuccess(ptMsgCode.getPwdText());
//        }
//    }


    public void dismiss() {
        mView.setVisibility(View.GONE);
    }

    public void show(){
        mView.setVisibility(View.VISIBLE);
    }

    public void showNew(){
    }
}
