package com.chuangdu.pad.module.web.presenter;

import com.chuangdu.pad.common.BasePresenter;
import com.chuangdu.pad.common.retrofit.RxUtil;
import com.chuangdu.pad.data.user.UserRepository;
import com.chuangdu.pad.module.web.contract.MainWebContract;

import java.io.File;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author sc
 * @since 2020-10-13
 */
public class MainWebPresenter extends BasePresenter implements MainWebContract.Presenter {
    private UserRepository mUserRepository;
    private MainWebContract.View mView;
    @Inject
    public MainWebPresenter(MainWebContract.View view, UserRepository userRepository) {
        mView = view;
        mUserRepository = userRepository;
    }

    @Override
    public void logout() {

    }

    @Override
    public void uploadFile(String path) {

        File file = new File(path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);


        mUserRepository.uploadFile(part).compose(RxUtil.applySchedulers())
                .doOnSubscribe(subscription -> {
//                    mView.showLoadingView(true);
                })
                .doAfterTerminate(() -> {
                    //mView.hideLoadingView();
                })
                .subscribe(requestBean -> {
                    mView.uploadFileSuccess(requestBean.getData());
                }, e -> {
                    mView.uploadFileFail(errorMessage(e));
                });
    }

    public void uploadFile(byte[] path) {

//        File file = new File(path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), path);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", "header.png", requestBody);


        mUserRepository.uploadFile(part).compose(RxUtil.applySchedulers())
                .doOnSubscribe(subscription -> {
//                    mView.showLoadingView(true);
                })
                .doAfterTerminate(() -> {
                    //mView.hideLoadingView();
                })
                .subscribe(requestBean -> {
                    mView.uploadFileSuccess(requestBean.getData());
                }, e -> {
                    mView.uploadFileFail(errorMessage(e));
                });
    }
}
