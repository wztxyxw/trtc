package com.chuangdu.pad.common.i;

import androidx.annotation.StringRes;

/**
 * @author sc
 * @since 2018/9/22
 */
public interface IBaseView<T> {

    /**
     * 显示加载视图
     * @param fullScreen true=全屏
     */
    void showLoadingView(boolean fullScreen);

    /**
     * 隐藏加载视图
     */
    void hideLoadingView();

    /**
     * 弹出Toast
     * @param msg 提示的文字
     */
    void toast(String msg);

    /**
     * 弹出Toast
     * @param msgId 提示文字的id
     */
    void toast(@StringRes int msgId);

}