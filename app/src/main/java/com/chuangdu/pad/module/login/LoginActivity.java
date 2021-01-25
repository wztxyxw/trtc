package com.chuangdu.pad.module.login;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chuangdu.library.app.BaseSplashActivity;
import com.chuangdu.library.util.FileUtils;
import com.chuangdu.library.util.KeyBoardUtils;
import com.chuangdu.library.util.SP;
import com.chuangdu.library.util.ScreenUtils;
import com.chuangdu.library.widget.YesNoDialog;
import com.chuangdu.suyangpad.R;
import com.chuangdu.pad.common.Application;
import com.chuangdu.pad.common.Constant;
import com.chuangdu.pad.common.PathUtil;
import com.chuangdu.pad.im.IMManager;
import com.chuangdu.pad.models.UserInfoModel;
import com.chuangdu.pad.models.UserModel;
import com.chuangdu.pad.models.req.LoginRequest;
import com.chuangdu.pad.module.login.contract.DaggerLoginComponent;
import com.chuangdu.pad.module.login.contract.LoginContract;
import com.chuangdu.pad.module.login.contract.LoginModule;
import com.chuangdu.pad.module.login.presenter.LoginPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author sc
 */
@Route(path = PathUtil.PATH_LOGIN)
public class LoginActivity extends BaseSplashActivity implements LoginContract.View {

    @BindView(R.id.account)
    EditText etAccount;
    @BindView(R.id.password)
    EditText etPassword;

    @BindView(R.id.login_layout)
    LinearLayout llLoginLayout;
    @BindView(R.id.wait_layout)
    LinearLayout llWaitLayout;

    UserInfoModel mUserInfoModel;

    @Inject
    SP mSP;
    @Inject
    LoginPresenter mPresenter;

    @Override
    public int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {

        DaggerLoginComponent
                .builder()
                .appComponent(Application.getAppComponent())
                .loginModule(new LoginModule(this))
                .build()
                .inject(this);

        String acount = mSP.getString(Constant.ACCOUNT);
        String password = mSP.getString(Constant.PASSWORD);

        if (!TextUtils.isEmpty(acount)) {
            etAccount.setText(acount);

        }

    }

    @Override
    public void initClick() {

    }

    @OnClick({R.id.login_btn, R.id.login_out_btn, R.id.login_btn1, R.id.login_btn2})
    public void onClick(View v) {
        if (v.getId() == R.id.login_btn) {
            login();
        }
        if (v.getId() == R.id.login_out_btn) {
            logout();
        }
        if (v.getId() == R.id.login_btn1) {
//            String imAccount = "59023518c5614311a4e29af8aae539d3";
//            String imUserSig = "eJw1jksPgkAMhP-LXjXY3VICJB7ExwUuBg96XN2FNEazAgGN8b-LQ9vTTOeb9C0OWe7Zp*PKilgSkQKA*ei2thKxUB6ISdfmqp1j0*d8AAyGnS5s7L3hgkeAIlBIMrxQIH2UUvtWRboItbaEkcF-G5d9GHVZWHJrtUXVPLrXUaUn3Lt216UbzuvkDG6WrcwiTLrlD2z4NrwaAPYDvvx8AQU9Nw8_";
////            String imAccount = "31054923b77d45bbb281570b4d3c9220";
////            String imUserSig = "eJw9TksKwjAUvEu2SnnJS5qk4EZFhBaLnwuYJtogSoml2Ip3tx*RWc2XeZNTdozcq-LBkYQKIRgAzEe1cYEkhEVAJv60t3NVedvnOADGAybHW-eo-cWPBaQguGZopLRcGGOYokKC4RYLzdh-zV-7cNqudabbbpfjpuDNUoWKHiSg5V2qyny2XXlUgRtV7he-Yu3vw9UYEAUTlH6*0Ug2Cg__";
//            String nickName = "屁民";
//            IMManager.initIM(imAccount, imUserSig, nickName, "");
            toast("test");
        }
        if (v.getId() == R.id.login_btn2) {

            FileUtils.saveBGBitmapAsPic(new FileUtils.SaveListener() {
                @Override
                public void callBack(boolean status, String path) {
                    IMManager.startVideoCall("31054923b77d45bbb281570b4d3c9220", "局长贵人", path);
                }
            }, ScreenUtils.snapShotWithoutStatusBar(this));


        }
    }

    @Override
    public void initData() {

    }

    private void login(){

        if (TextUtils.isEmpty(etAccount.getText())) {
            toast("请输入账号");
            return;
        }
        if (TextUtils.isEmpty(etPassword.getText())) {
            toast("请输入密码");
            return;
        }
        LoginRequest param = new LoginRequest();
        param.account = etAccount.getText().toString();
        param.pwd = etPassword.getText().toString();


        KeyBoardUtils.closeKeybord(etAccount, this);
        KeyBoardUtils.closeKeybord(etPassword, this);

        mPresenter.login(param);
    }

    private void logout(){
        YesNoDialog dialog = YesNoDialog.newInstance("提示", "确定退出登录？", "退出", "取消", new YesNoDialog.Listener() {
            @Override
            public boolean onYes(YesNoDialog dialog) {
                llLoginLayout.setVisibility(View.VISIBLE);
                llWaitLayout.setVisibility(View.GONE);
                mPresenter.logout();
                IMManager.logout();
                return false;
            }

            @Override
            public boolean onNo(YesNoDialog dialog) {
                return false;
            }
        });

        dialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void loginSuccess(UserModel model) {
        mPresenter.userInfo();
    }

    @Override
    public void loginFail(String msg) {
        toast(msg);
    }

    @Override
    public void userInfoSuccess(UserInfoModel model) {
        mUserInfoModel = model;
        next();
    }

    @Override
    public void userInfoFail(String msg) {

    }

    private void next(){
        IMManager.initIM(mUserInfoModel.imAccount, mUserInfoModel.imUserSig, mUserInfoModel.nickName, mUserInfoModel.headPicUrl);
        llLoginLayout.setVisibility(View.GONE);
        llWaitLayout.setVisibility(View.VISIBLE);
    }
}
