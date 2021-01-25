package com.chuangdu.library.widget;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.chuangdu.library.R;

public class YesNoDialog extends DialogFragment {

    private final static String EXTRA_TITLE = "title";
    private final static String EXTRA_MESSAGE = "message";
    private final static String EXTRA_POSITIVE_LABEL = "positive_label";
    private final static String EXTRA_NEGATIVE_LABEL = "negative_label";
    private final static String EXTRA_SHOW_BOTTOM = "show_bottom";

    private final static String EXTRA_CONTRARY = "contrary";

    int mMessageColor;

    private Listener mListener;
    public interface Listener {

        boolean onYes(YesNoDialog dialog);

        boolean onNo(YesNoDialog dialog);

    }

    /**
     * @param title    Dialog标题
     * @param msg      Dialog 内容
     * @param no       取消按钮文本
     * @param yes      确认按钮文本
     * @param listener 取消/确认按钮事件回调
     * @return
     */
    public static YesNoDialog newInstance(String title, CharSequence msg,
                                          String yes, String no,
                                          Listener listener) {
        YesNoDialog f = new YesNoDialog();
        Bundle b = new Bundle();
        b.putString(EXTRA_TITLE, title);
        b.putCharSequence(EXTRA_MESSAGE, msg);
        b.putString(EXTRA_POSITIVE_LABEL, yes);
        b.putString(EXTRA_NEGATIVE_LABEL, no);
        f.setArguments(b);
        f.mListener = listener;
        return f;
    }
    /**
     * @param title    Dialog标题
     * @param msg      Dialog 内容
     * @param no       取消按钮文本
     * @param yes      确认按钮文本
     * @param listener 取消/确认按钮事件回调
     * @return
     */
    public static YesNoDialog newInstance(String title, CharSequence msg,
                                          String yes,
                                          Listener listener) {
        YesNoDialog f = new YesNoDialog();
        Bundle b = new Bundle();
        b.putString(EXTRA_TITLE, title);
        b.putCharSequence(EXTRA_MESSAGE, msg);
        b.putString(EXTRA_POSITIVE_LABEL, yes);
        f.setArguments(b);
        f.mListener = listener;
        return f;
    }

    /**
     * 设置dialog的显示位置
     *
     * @param showBottom true 在底部显示 false 在中间显示
     */
    public YesNoDialog setShowBottom(boolean showBottom) {
        getArguments().putBoolean(EXTRA_SHOW_BOTTOM, showBottom);
        return this;
    }

    /**
     * 设置按钮颜色对调
     *
     * @param status true 对调 false 对调不对调
     */
    public YesNoDialog setContrary(boolean status) {
        getArguments().putBoolean(EXTRA_CONTRARY, status);
        return this;
    }

    public void setMessageTextColor(int color){
        this.mMessageColor = color;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        boolean showBottom = getArguments().getBoolean(EXTRA_SHOW_BOTTOM, false);
        Window window = getDialog().getWindow();
        if (showBottom) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            layoutParams.dimAmount = 0.5f;
            window.setGravity(Gravity.BOTTOM);
            window.setAttributes(layoutParams);
        }

        window.requestFeature(Window.FEATURE_NO_TITLE);
        int layoutId = showBottom ? R.layout.dialog_bottom_custom_layout : R.layout.dialog_custom_layout;
        View v = inflater.inflate(layoutId, ((ViewGroup) window.findViewById(android.R.id.content)), false);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        if (showBottom) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        getDialog().setCanceledOnTouchOutside(true);

        TextView title = (TextView) v.findViewById(R.id.dialog_custom_title);

        if (getArguments().getString(EXTRA_TITLE) == null) {
            title.setVisibility(View.GONE);
        } else {
            title.setText(getArguments().getString(EXTRA_TITLE));
        }

        TextView messageView = (TextView) v.findViewById(R.id.dialog_custom_content);
        if (mMessageColor > 0) {
            messageView.setTextColor(getResources().getColor(mMessageColor));
        }
        if (getArguments().getCharSequence(EXTRA_MESSAGE) == null) {
            messageView.setVisibility(View.GONE);
        } else {
            messageView.setText(getArguments().getCharSequence(EXTRA_MESSAGE));
        }
        TextView no = (TextView) v.findViewById(R.id.dialog_custom_no);
        TextView yes = (TextView) v.findViewById(R.id.dialog_custom_yes);

        boolean contrary = getArguments().getBoolean(EXTRA_CONTRARY, false);

        if (contrary) {
            no.setTextColor(getResources().getColor(R.color.confirm_text_color));
            yes.setTextColor(getResources().getColor(R.color.cancel_text_color));
        } else {
            no.setTextColor(getResources().getColor(R.color.cancel_text_color));
            yes.setTextColor(getResources().getColor(R.color.confirm_text_color));
        }

        if (getArguments().getString(EXTRA_NEGATIVE_LABEL) == null) {
            no.setVisibility(View.GONE);
        } else {
            no.setVisibility(View.VISIBLE);
            no.setText(getArguments().getString(EXTRA_NEGATIVE_LABEL));
        }
        if (getArguments().getString(EXTRA_POSITIVE_LABEL) == null) {
            yes.setText(getActivity().getString(android.R.string.yes));
        } else {
            yes.setText(getArguments().getString(EXTRA_POSITIVE_LABEL));
        }
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean dismiss = mListener.onNo(YesNoDialog.this);
                if (!dismiss) {
                    YesNoDialog.this.dismiss();
                }
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean dismiss = mListener.onYes(YesNoDialog.this);
                if (!dismiss) {
                    YesNoDialog.this.dismiss();
                }
            }
        });
        getDialog().setCanceledOnTouchOutside(false);

        return v;
    }
}
