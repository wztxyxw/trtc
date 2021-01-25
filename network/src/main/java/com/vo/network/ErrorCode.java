package com.vo.network;

import com.chuangdu.library.event.ServerLogoutEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * @author sc
 */
public class ErrorCode {
    /** 登录过期 */
    public static final int LOGIN_OVERTIME = 10011;
    /** 司机被封号 */
    public static final int ACCOUNT_SUSPEND = 10012;
    /** 司机被加入黑名单 */
    public static final int ACCOUNT_BLACK_LIST = 10013;
    /** 账号被冻结 */
    public static final int ACCOUNT_FROZEN = 10019;
    /** 刷新订单状态 */
    public static final int REFRESH_ORDER_STATUS = 20003;

    /** 要实名认证 */
    public static final int NO_PERSONAL_VERIFIED = 3001;
    /** 还要买vip */
    public static final int NO_VIP = 3002;
    /** 要实名认证 */
    public static final int NO_STUDENT_VERIFIED = 3003;
    /** 要实名认证 */
    public static final int NO_VERIFIED = 3004;
    /** 还要买vip */
    public static final int NO_VIP2 = 3027;
    /** 还要买vip */
    public static final int NO_VIP3 = 3034;

    public static final int LOGOUT_CODE1 = 2011;
    public static final int LOGOUT_CODE2 = 2021;
    public static final int LOGOUT_CODE3 = 2010;
    public static final int LOGOUT_CODE4 = 2020;
    public static final int LOGOUT_CODE5 = 2024;
    public static final int LOGOUT_CODE6 = 2022;


    public static int SUCCESS = 200 ;

    public static boolean isSuccess(int code){

        if (code == LOGOUT_CODE1 || code == LOGOUT_CODE2 || code == LOGOUT_CODE3
                || code == LOGOUT_CODE4 || code == LOGOUT_CODE5 || code == LOGOUT_CODE6) {
            EventBus.getDefault().post(new ServerLogoutEvent());
        }


        return SUCCESS == code || 0 == code;
    }
}
