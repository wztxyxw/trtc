package com.chuangdu.pad.module.web.contract;

import com.chuangdu.pad.common.i.IBasePresenter;
import com.chuangdu.pad.common.i.IBaseView;
import com.chuangdu.pad.models.UploadFileModel;

import java.util.List;

/**
 * @author sc
 * @since 2020-10-13
 */
public interface MainWebContract {
    interface Model {
    }

    interface View extends IBaseView<Presenter> {
        void uploadFileSuccess(List<UploadFileModel> models);
        void uploadFileFail(String msg);
    }

    interface Presenter extends IBasePresenter {
        void logout();
        void uploadFile(String path);
    }
}
