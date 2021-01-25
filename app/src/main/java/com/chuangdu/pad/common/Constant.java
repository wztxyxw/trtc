package com.chuangdu.pad.common;

import com.chuangdu.suyangpad.BuildConfig;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;

/**
 *
 * @author Tnno Wu
 * @date 2018/01/30
 */

public class Constant {

    public static final String MAIN_URL = "main_url";

    public static final String RELEASE = "release";

    public static final String IS_AGREE = "is_agree";
    public static final String NOTIFICATION_OPENED = "com.chuangdu.vo.intent.NOTIFICATION_OPENED";

    // 存储
    public static final String USERINFO = "userInfo";
    public static final String ACCOUNT = "account";
    public static final String PASSWORD = "password";
    public static final String ROOM = "room";
    public static final String AUTO_LOGIN = "auto_login";
    public static final String LOGOUT = "logout";
    public static final String ICON_URL = "icon_url";
    public static final String LAST_ACCOUNT = "last_account";
    public static final String LAST_PHONE = "last_phone";

    public static final String TODAY_ORDER_COURSE = "TODAY_ORDER_COURSE";
    public static final String NEW_ORDER_COURSE = "NEW_ORDER_COURSE";

    public static final String LAST_APP_SHOW = "app_show";
    public static final String LAST_APP_SHOW_ONE_KEY = "app_show_one_key";
//    public static final String VERIFIED_CHANNEL = "xiaomi";
//    public static final String ONE_KEY_VERIFIED_CHANNEL = "xiaomi";
    public static final String VERIFIED_CHANNEL = "bast";
    public static final String ONE_KEY_VERIFIED_CHANNEL = "bast";

    public static final String SP_SHOW_BALANCE = "show_balance";

    public static final String SP_SEARCH_CAR_GO_NAME_HISTORY = "search_car_go_name_history";

    public static final String LAST_BULLET_TIME = "last_bullet_time";

    public static final String LAST_MAIN_PUT_LAYOUT = "last_main_put_layout";
    public static final String LAST_RUN_PUT_LAYOUT = "last_run_put_layout";
    public static final String LAST_MINE_PUT_LAYOUT = "last_mine_put_layout";

    public static final String BOARD_RULE_SHOW = "board_rule_show";

    public static final String BROWSER_URL = "browser_url";

    public static int APP_TYPE = BuildConfig.app_type;
    public static int IM_ID = BuildConfig.im_id;

    public static String APP_ID = BuildConfig.wechat_app_id;
    public static String MOB_APP_KEY = BuildConfig.mob_app_key;
    public static String MOB_APP_SECRET = BuildConfig.mob_app_secret;
    public static String UMENG_KEY = BuildConfig.umeng_key;
    public static String BUGLY_KEY = BuildConfig.bugly_key;

    public static boolean APP_SHOW = true;
    public static boolean APP_SHOW_ONE_KEY = true;

    public static int PAGE_SIZE = 20;
    public static int CIRCLE_PAGE_SIZE = 10;


    public static String OSS_HEADER_PATH = "?x-oss-process=image/resize,w_180";
    public static String OSS_PATH = "?x-oss-process=image/auto-orient,1/resize,m_fill,w_500,h_500/quality,q_90/format,webp";
    public static String OSS_BIG_PATH = "?x-oss-process=image/resize,w_1080,h_1080";


    public static final String FLY_AUDIO_LIST = "fly_audio_list";

    public static String imAccount;


//    private static OssUrlModel ossUrlModel;
//
//
//    public static void setOssUrlModel(OssUrlModel ossUrlModel) {
//        Constant.ossUrlModel = ossUrlModel;
//    }
//
//    public static OssUrlModel getOssUrlModel() {
//        if (null == ossUrlModel) {
//            ossUrlModel = DBManager.getOssUrlModel();
//        }
//
//        return ossUrlModel;
//    }
//
//    public static String getHeaderOss(){
//        if (null != getOssUrlModel()) {
//            if (TextUtils.isEmpty(getOssUrlModel().headPicSuffix)) {
//                return "";
//            } else {
//                return getOssUrlModel().headPicSuffix;
//            }
//        } else {
//            return "";
//        }
//    }
//
//    public static String getBigOss(){
//        if (null != getOssUrlModel()) {
//            if (TextUtils.isEmpty(getOssUrlModel().bigPicSuffix)) {
//                return "";
//            } else {
//                return getOssUrlModel().bigPicSuffix;
//            }
//        } else {
//            return "";
//        }
////        return OSS_BIG_PATH;
//    }
//    public static String getMinOss(){
//        if (null != getOssUrlModel()) {
//            if (TextUtils.isEmpty(getOssUrlModel().listPicSuffix)) {
//                return OSS_PATH;
//            } else {
//                return getOssUrlModel().listPicSuffix;
//            }
//        } else {
//            return OSS_PATH;
//        }
////        return OSS_PATH;
//    }

    public interface AppType {
        int SHIPPER = 2;//货主
        int DRIVER = 1;//司机
    }
    public interface PlayType {
        int PLAY = 1;//播放
    }

    public static enum MessageType {
        Invalid(0),
        Text(1),
        Image(2),
        Sound(3),
        Custom(4),
        File(5),
        GroupTips(6),
        Face(7),
        Location(8),
        GroupSystem(9),
        SNSTips(10),
        ProfileTips(11),
        Video(12),
        ORDER(13);

        private int type = 0;

        private MessageType(int t) {
            this.type = t;
        }

        public int value() {
            return this.type;
        }
    }
    public static boolean isShow(){
        return APP_SHOW;
//        return true;
    }

    public static boolean isShowOneKey(){
        return APP_SHOW_ONE_KEY;
//        return true;
    }
    /**
     * 正则表达式
     */
    public interface Regular {
        String REGEX_ID_CARD = "^\\d{15}|\\d{18}|\\d{17}(\\d|X|x)$";//身份证
        String PHONENUMBERCHECK = "(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";//手机号
    }

    /**
     * 0-短信验证吗，1-语音验证码
     */
    public static class SmsType {
        public final static int TEXT = 0;
        public final static int VOICE = 1;

        public final static int FORGET_PASSWORD = 3;
        public final static int BIND_BANK_CARD = 4;
        public final static int CHANGE_TEL = 5;
        public final static int BIND_TEL = 6;

    }

    /**
     * 打开类型 0 普通 1 一口价
     */
    public static class DepositFreightType {
        public final static int UNLIMITED_PRICE = -1;
        public final static int NEGOTIATE_PRICE = 0;
        public final static int ONE_PRICE = 1;
    }

    /**
     * 打开类型 0 关 1 开
     */
    public static class ShareType {
        public final static int WECHAT = SendMessageToWX.Req.WXSceneSession;
        public final static int WECHAT_MOMENTS = SendMessageToWX.Req.WXSceneTimeline;
        public final static int COPY = 10;
    }

    /**
     * 打开类型 0 关 1 开
     */
    public static class AuthStatusType {
        public final static int BIND = 1;
        public final static int UNTYING = 0;
    }

    /**
     * 打开类型 0 关 1 开
     */
    public static class BtnType {
        public final static int OPEN = 1;
        public final static int CLOSE = 0;
    }

    /**
     * 打开类型 -1默认未知 1打开h5网页 2订单 3实名认证 4头像认证 5驾驶证认证 6行驶证认证 7企业认证 8 提现
     */
    public static class OpenType {
        public final static int MAIN = 0;
        public final static int PERSONAL_VERIFIED = 3;
        public final static int HEADER_VERIFIED = 4;
        public final static int DRIVER_VERIFIED = 5;
        public final static int CAR_VERIFIED = 6;
        public final static int COMPANY_VERIFIED= 7;
        public final static int WITHDRAW = 8;

        public final static int ORDER_VIEW = 2;

        public final static int ORDER_PUSH = 11;
        public final static int IM_MSG = 12;

        public final static int DEFAULT = -1;
        public final static int WEB = 1;
        public final static int ORDER = 2;
        public final static int CIRCLE_REPLY = 3;
        public final static int CIRCLE_REPLY_REPLY = 4;
        public final static int CIRCLE_REPLY2 = 5;
        public final static int CIRCLE_ZAN = 6;

        public final static int RANING_REWARD = 8;
        public final static int PRESENTS = 10;

        public final static int FLY_AUDIO_PUBLICLY = 21;
        public final static int FLY_AUDIO = 22;
        public final static int FLY_AUDIO_START = 9999;
        public final static int FLY_AUDIO_TIME = 23;
        public final static int CHAT_PRESENTS = 24;
        public final static int FLY_AUDIO_FAIL = 25;

        public final static int FLY_AUDIO_INVITATION_PUBLICLY = 26;

    }

    /**
     * 0-短信验证吗，1-语音验证码
     */
    public static class ComplaintType {
        public final static int NORMAL = 1;
        public final static int ORDER = 2;
        public final static int DRIVER = 3;
        public final static int SHIPPER = 4;
    }

    public static class CargoType {
        public static final int A = 1;//一级
        public static final int B = 2;//二级
    }

    public static class AddressType {
        public static final int PROVENCE = 1;//省
        public static final int CITY = 2;//市
        public static final int AREA = 3;//区
    }

    public static class PayType {
        public static final int WECHAT = 2;//微信
        public static final int ALIPAY = 1;//支付宝
        public static final int BALANCE = 3;//余额支付
    }

    public static class OrderPayType {
        public static final int DEPOSIT = 1;//押金
        public static final int FREIGHT = 2;//运费
        public static final int RECHARGE = 1;//充值
        public static final int SHIPPER_ONE_PRICE = 4;//一口价
        public static final int DRIVER_ONE_PRICE = 5;//一口价
    }

    public static class ContactType {
        public static final int TEL = 1;//电话
        public static final int IM = 2;//IM消息
    }
    public static class OrderDriverStatus {
        /** 1货主拒绝司机 */
        public static final int SHIPPER_CANCEL = 1;
        /** 2司机待接待状态 */
        public static final int WAIT_CONFIRM = 2;
        /** 3司机已取消 */
        public static final int DRIVER_CANCEL = 3;
        /** 待支付 */
        public static final int WAIT_PAY_DEPOSIT = -2;

    }
    public static class OrderStatus {
        /** 取消 */
        public static final int ORDER_CANCEL = -1;
        /** 待支付 */
        public static final int WAIT_PAY = 0;
        /** 待接单 */
        public static final int WAIT_GRAB = 1;
        /** 已接单 */
        public static final int WAIT_FINISH = 2;
        /** 已完成 */
        public static final int WAIT_EVALUATE = 3;
        /** 已评价 */
        public static final int FINISH = 4;

    }

    public static class EvaluateStatus {
        /** 评论失败  */
        public static final int EVALUATE_FAIL = -1;
        /** 评论成功 */
        public static final int EVALUATE = 1;
        /** 本人未评论 */
        public static final int EVALUATE_ME = 1;
        /** 已全部评论 */
        public static final int EVALUATE_FINISH = 2;
        /** 双方未评论 */
        public static final int WAIT_EVALUATE = 0;

    }

    public static class PutOrderStatus {
        /** 取消 */
        public static final int ORDER_CANCEL = -1;
        /** 进行中 */
        public static final int WORKING = 0;
        /** 已评价 */
        public static final int FINISH = 2;

    }

    public static class OrderImage {
        /** 车头 */
        public static final int CAR_HEADER = 1;
        /** 车尾 */
        public static final int CAR_TAIL = 2;
        /** 车厢 */
        public static final int TRUNK = 3;
        /** 回单 */
        public static final int RECEIPT = 4;
    }


    public static class VerifiedType {
        /** 未认证0 */
        public static final int NO_VERIFIED = 0;
        /** 审核中1 */
        public static final int VERIFIEDING = 1;
        /** 已认证2 */
        public static final int PASS_VERIFIED = 2;
        /** 未通过3 */
        public static final int NO_PASS_VERIFIED = 3;
    }

    public static class Distance {
        /** 装货地 */
        public static final float ORIGIN_DISTANCE = 5000;
        /** 卸货地 */
        public static final int DEST_DISTANCE = 30000;
    }

    /**
     * 0处理中 1处理完
     */
    public static class StatusType {
        public final static int ING = 0;
        public final static int FINISH = 1;

    }

    public static class PositionType {
        public static final int ORIGIN = 1;
        public static final int DEST = 2;

    }

    /**
     * LOAD_TYPE 装卸方式
     * VEHICLE_TYPE_1 小型车
     * VEHICLE_TYPE_2 中型车
     * VEHICLE_TYPE_3 大型车
     * CAR_TYPE 车辆类型
     * USE_TYPE 装车类型
     * CAR_COLOUR 车牌颜色
     * CAR_WHEELBASE 车辆轴数
     *
     */

    public static class CarInfo {
        public static final String LOAD_TYPE = "LOAD_TYPE";
        public static final String VEHICLE_TYPE_1 = "VEHICLE_TYPE_1";
        public static final String VEHICLE_TYPE_2 = "VEHICLE_TYPE_2";
        public static final String VEHICLE_TYPE_3 = "VEHICLE_TYPE_3";
        public static final String CAR_TYPE = "CAR_TYPE";
        public static final String USE_TYPE = "USE_TYPE";
        public static final String CAR_COLOUR = "CAR_COLOUR";
        public static final String CAR_WHEELBASE = "CAR_WHEELBASE";
        public static final String APPLICATION_TIME = "APPLICATION_TIME";

    }
    public static class TagType {
        public static final String CHAT_TAG = "[chat_tag]";
    }

    public static class ScoreType {
        public static final int HIGH = 1;
        public static final int MEDIUM = 2;
        public static final int LOW = 3;
    }

    public static class SexType {
        public static final int BOY = 1;
        public static final int GIRL = 0;
        public static final int ALL = -1;
    }

    public static class LoginType {
        public static final int SELF_TYPE = 1;
        public static final int MSG_TYPE = 2;
        public static final int WECHAT_TYPE = 3;
    }

    public static class CommentType {
        public static final int ROOT = 1;
        public static final int ONE = 2;
        public static final int TWO = 3;
    }

    public static class FromType {
        public static final int ORDER_TODAY = 1;
        public static final int ORDER_TODAY_CITY = 7;
        public static final int DELIERING = 2;
        public static final int DELIERING_CITY = 3;
        public static final int ORDER_HISTORY = 4;
        public static final int ADD_DISTRIBUTION = 5;
        public static final int VERIFIED_AGAIN = 6;
        public static final int VERIFIED_NEW = 0;
    }
    public static class DeleteCircleType {
        public static final int CIRCLE = 1;
        public static final int REPLY1 = 2;
        public static final int REPLY2 = 3;
    }


    public static class PageType {
        public static final int MAIN = 1;
        public static final int CIRCLE_DETAIL = 2;
        public static final int USER_PAGE = 3;
        public static final int MINE = 4;
        public static final int POSITION_CIRCLE = 5;

        public static final int PAGE_FRIEND = 6;
        public static final int PAGE_FOLLOW = 7;
        public static final int PAGE_FANS = 8;
    }


    public static class PutOSSType {
        public static final int CIRCLE = 1;
        public static final int ORDER = 2;
        public static final int USER_PAGE = 3;
        public static final int MINE_PAGE = 4;
        public static final int SET_USER_PAGE = 5;
        public static final int USER_HEADER = 6;
    }

    public static class ReportType {
        public static final int CIRCLE = 1;
        public static final int USER_PAGE = 2;
        public static final int FLY_AUDIO = 3;
        public static final int CHAT = 4;
    }

    public static class PresentsType {
        public static final int FLY_AUDIO = 1;
        public static final int CHAT = 2;
    }



}
