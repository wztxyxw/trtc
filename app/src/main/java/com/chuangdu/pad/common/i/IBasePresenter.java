package com.chuangdu.pad.common.i;

import io.reactivex.disposables.Disposable;

/**
 * @author sc
 * @since 2018/9/22
 */
public interface IBasePresenter {

    /**
     * 订阅
     */
    void subscribe();

    /**
     * 取消订阅
     */
    void unsubscribe();

    /**
     * 取消订阅某个监听
     */
    void unsubscribe(Disposable observable);

}