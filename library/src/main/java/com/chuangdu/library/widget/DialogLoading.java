package com.chuangdu.library.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.chuangdu.library.R;
import com.chuangdu.library.util.ScreenUtils;


public class DialogLoading {

    private Context mContext;
    private Dialog dialog;
    private RotateLoading rotateLoading;

    public DialogLoading(Context mContext) {
        this.mContext = mContext;
    }

    public DialogLoading show() {
        rotateLoading.showLoading();
        dialog.show();
        return this;
    }

    public DialogLoading dismiss() {
        if (rotateLoading != null) {
            rotateLoading.hideLoading();
        }
        if (dialog != null) {
            dialog.dismiss();
        }
        return this;
    }

    public DialogLoading builder() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_loading, null);// 得到加载view
        // 创建自定义样式dialog
        dialog = new Dialog(mContext, R.style.LoadingDialog);
        dialog.setContentView(view);// 设置布局
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        int w = ScreenUtils.getScreenWidth(mContext);
        int h = ScreenUtils.getScreenHeight(mContext);
        dialog.getWindow().setLayout(w, h);
        rotateLoading = (RotateLoading) view.findViewById(R.id.rotateloading);
        return this;
    }
}
