package com.chuangdu.pad.module.splash.contract;

import com.chuangdu.pad.common.i.IBasePresenter;
import com.chuangdu.pad.common.i.IBaseView;
import com.chuangdu.pad.models.VersionModel;

/**
 * @author sc
 * @since 2020-04-20
 */
public interface SplashContract {
    interface Model {
    }

    interface View extends IBaseView<Presenter> {


        void versionSuccess(VersionModel model);
        void versionFail(String msg);
    }

    interface Presenter extends IBasePresenter {
        void appVersion();
    }
}
