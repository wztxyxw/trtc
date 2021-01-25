package com.chuangdu.pad.models;

/**
 *
 * @author SC
 * @date 2018/2/4
 */

public class UserModel {
    public String authorization;
    public String userId;

    public int accountId;

    public long timeout;
    public String loginExpTime;

    public String openid;

    /**是否新用户 0 老用户  1是新用户*/
    public int registerFlag;
}
