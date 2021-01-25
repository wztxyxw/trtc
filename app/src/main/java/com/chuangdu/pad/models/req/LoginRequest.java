package com.chuangdu.pad.models.req;

/**
 *
 * @author SC
 * @date 2018/2/4
 */

public class LoginRequest extends BaseRequest {
    public String account;

    /**
     * 登录模式 1一键登录 2验证码登录 3微信登陆
     */

    public int loginType;

    public String authCode;

    public String pwd;


}
