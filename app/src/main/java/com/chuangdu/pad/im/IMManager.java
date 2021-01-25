package com.chuangdu.pad.im;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.chuangdu.library.util.LogUtil;
import com.chuangdu.library.util.SP;
import com.chuangdu.pad.common.Application;
import com.chuangdu.pad.common.Constant;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.liteav.login.UserModel;
import com.tencent.liteav.trtcvideocalldemo.ui.TRTCVideoCallActivity;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 聊天功能类
 *
 * @author sc
 * @since 2019/1/9
 */
public class IMManager {
    private static final String TAG = IMManager.class.getSimpleName();
    public static final String PROFILE_CUSTOM_INFO_TAG = "Tag_Profile_Custom_typeName";

    public static boolean isLogin(String identify){
        String loginuser = TIMManager.getInstance().getLoginUser();
        if (TextUtils.isEmpty(loginuser)){
            return false;
        }
        if (identify.equals(loginuser)){
            return true;
        } else {
            return false;
        }

    }

    public static boolean isLogin(){
        String loginuser = TIMManager.getInstance().getLoginUser();
        if (TextUtils.isEmpty(loginuser)){
            return false;
        }
        return true;
    }
    public static void initIM(String imAccount, String imUserSig, String nickName, String headPicUrl) {
        if (!IMManager.isLogin(imAccount)) {

            Constant.imAccount = imAccount;

            IMManager.loginIm(imAccount, imUserSig, new IUIKitCallBack() {
                @Override
                public void onError(String module, final int code, final String desc) {
                    LogUtil.e(Constant.TagType.CHAT_TAG, "login Error i= " + code + " s= " + desc);
                    if (code == 6013 && desc.contains("initialized")) {
                        Application.initTIMSDK(Application.getApplication());
                    } else {
                        //resetIMSign();
                    }
                }

                @Override
                public void onSuccess(Object data) {
                    LogUtil.e(Constant.TagType.CHAT_TAG, "login Success");
                    UserModel self = new UserModel();
                    if (!TextUtils.isEmpty(nickName)) {
                        IMManager.setImName(nickName, headPicUrl);
                        self.userName = nickName;
                    }
                    if (!TextUtils.isEmpty(headPicUrl)) {
                        self.userAvatar = headPicUrl;
                    }
                    self.userId = imAccount;
                    self.userSig = imUserSig;
                    ProfileManager.getInstance().setUserModel(self);
                    //FlyAudioCallManager.getInstance().init(Application.getInstance());
                }
            });
        } else {

        }
    }
    public static void initIM(String imAccount, String imUserSig, String nickName, String headPicUrl, IUIKitCallBack callBack) {
        if (!IMManager.isLogin(imAccount)) {

            Constant.imAccount = imAccount;

            IMManager.loginIm(imAccount, imUserSig, new IUIKitCallBack() {
                @Override
                public void onError(String module, final int code, final String desc) {
                    LogUtil.e(Constant.TagType.CHAT_TAG, "login Error i= " + code + " s= " + desc);
                    if (code == 6013 && desc.contains("initialized")) {
                        Application.initTIMSDK(Application.getApplication());
                    } else {
                        //resetIMSign();
                    }

                    callBack.onError(module, code, desc);
                }

                @Override
                public void onSuccess(Object data) {
                    LogUtil.e(Constant.TagType.CHAT_TAG, "login Success");
                    UserModel self = new UserModel();
                    if (!TextUtils.isEmpty(nickName)) {
                        IMManager.setImName(nickName, headPicUrl);
                        self.userName = nickName;
                    }
                    if (!TextUtils.isEmpty(headPicUrl)) {
                        self.userAvatar = headPicUrl;
                    }
                    self.userId = imAccount;
                    self.userSig = imUserSig;
                    ProfileManager.getInstance().setUserModel(self);

                    callBack.onSuccess(data);
                    //FlyAudioCallManager.getInstance().init(Application.getInstance());
                }
            });
        } else {
            callBack.onSuccess(null);
        }
    }

    /**
     * 登录imsdk
     *
     * @param identify 用户id
     * @param userSig 用户签名
     * @param callBack 登录后回调
     */
    public static void loginIm(String identify, String userSig, IUIKitCallBack callBack){

//        if (Constant.APP_TYPE == Constant.AppType.SHIPPER) {
//            identify = "1";
//            userSig = "eJxlz01Pg0AQgOE7v4LstUZn*AqYeGispQ1QQsCIJ4K7C11b6AqrQY3-3YgaN3Guz5uZzLthmiYp4vy8pvT03KtKvUpOzEuTADn7QykFq2pV2QP7h3ySYuBV3Sg*zIiu61oAeiMY75VoxG*h0cgO1bz-WxwA9DwbHT0R7YzJze31NlsxWKPYyZDeIR6C8D7abHx04kWZ2l6co8rao6J2krRPy23bQPM4pusL2uVHKyuW4bQo3va7NPJXufWQdFA2Po3KcdrTK*2kEh3-eSYAhCBwXE1f*DCKUz8HFqCLlg1fQ4wP4xM2S1nc";
//        }
//        if (Constant.APP_TYPE == Constant.AppType.DRIVER) {
//            identify = "2";
//            userSig = "eJxlj9FOwjAUhu-3FE1vMdpua3UmXDBgxsWOACPKVVPXMipxa7YKW4zvbhgkNvHcft9-zvm-PQAAzF-Wt6Io6q-KctsbBcEjgAje-EFjtOTC8qCR-6DqjG4UFzurmgFiQoiPkOtoqSqrd-pq*A5q5YEP*y-ZECFMaYBDV9HlANl8OX1O2CJV*06f-PkkZfT9ru46mpviKaVvfR7Z8BCP2AdZHhfJRMex6l9xuS0rsZGyTtqHVbIfZeZ*tsKbqRIoy7JcmC2zs9N47Jy0*lNdy0QIoygi7kNH1bS6ri5dECbYD9B5oPfj-QLkQFr5";
//        }
        LogUtil.e(Constant.TagType.CHAT_TAG, "loginIm identify   " + identify + "  userSig  " + userSig);
        if (identify == null || userSig == null) {
            return;
        }
        //发起登录请求
//        TIMManager.getInstance().login(
//                identify,
//                //用户帐号签名，由私钥加密获得，具体请参考文档
//                userSig,
//                callBack);
        TUIKit.login(identify, userSig, callBack);
    }

    /**
     * 登出imsdk
     *
     */
    public static void logout(){
        TIMManager.getInstance().logout(new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                LogUtil.e(Constant.TagType.CHAT_TAG, "im logout Error:" + i + ", " + s);
            }

            @Override
            public void onSuccess() {
                LogUtil.e(Constant.TagType.CHAT_TAG, "im logout Success");
            }
        });
    }
    /**
     * 设置用户信息
     *
     */
    public static void setImName(String name, String url){

        String nickName = name;

//        if (!TextUtils.isEmpty(name) && name.length() > 1){
//            nickName = name.substring(0, 1) + "师傅";
//        }
        LogUtil.e(Constant.TagType.CHAT_TAG, "modifySelfProfile nickName   " + nickName + "  url  " + url);
        HashMap<String, Object> profileMap = new HashMap<>();
        profileMap.put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_NICK, nickName);
        if (!TextUtils.isEmpty(url)) {
            profileMap.put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_FACEURL, url);
        }

        TIMFriendshipManager.getInstance().modifySelfProfile(profileMap, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                LogUtil.e(Constant.TagType.CHAT_TAG, "modifySelfProfile failed: " + code + " desc" + desc);
            }

            @Override
            public void onSuccess() {
                LogUtil.e(Constant.TagType.CHAT_TAG, "modifySelfProfile success ");
            }
        });
    }

    public interface ConversationCallback{
        void onSuccess(List<V2TIMConversation> conversations);
    }

    public static void getConversationList(int start, ConversationCallback callback){
        V2TIMManager.getConversationManager().getConversationList(start, 100, new V2TIMValueCallback<V2TIMConversationResult>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(V2TIMConversationResult v2TIMConversationResult) {
                List<V2TIMConversation> conversations = v2TIMConversationResult.getConversationList();

                if (null != conversations) {
                    if (conversations.size() < 100) {
                        getConversationList(start + 1, callback);
                    }
                    callback.onSuccess(conversations);
                }
            }
        });
    }

//    public static void sendMessage(String id, String name, OrderMessage orderMessage ){
//        ChatUserModel chatInfo = new ChatUserModel(id, name, V2TIMConversation.V2TIM_C2C);
//
//        ARouter.getInstance().build(PathUtil.PATH_CHAT_CONVERSATION)
//                .withSerializable(PathUtil.DATA_TYPE_USER, chatInfo)
//                .withSerializable(PathUtil.DATA_TYPE_MESSAGE, orderMessage)
//                .navigation();
//
//
//    }

    public static void sendMessage(String id, TIMMessage message ){
//        TIMConversation conversation = TIMManager.getInstance().getConversation(V2TIMConversation.V2TIM_C2C, id);
//
//        conversation.sendMessage(message, new TIMValueCallBack<TIMMessage>() {
//            @Override
//            public void onError(int i, String s) {
//
//            }
//
//            @Override
//            public void onSuccess(TIMMessage message) {
//
//            }
//        });
//
//        conversation.setReadMessage(null, new TIMCallBack() {
//            @Override
//            public void onError(int code, String desc) {
//                LogUtil.e(Constant.TagType.CHAT_TAG,"setReadMessage failed, code: " + code + "|desc: " + desc);
//            }
//
//            @Override
//            public void onSuccess() {
//                LogUtil.e(Constant.TagType.CHAT_TAG, "setReadMessage succ");
//            }
//        });
    }

    public static void removeMessage(V2TIMMessage message){
        V2TIMManager.getMessageManager().deleteMessageFromLocalStorage(message, new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
                // 删除消息失败
            }
            @Override
            public void onSuccess() {
                // 删除消息成功
            }
        });
    }
    public static void deleteConversation(String userId){
        V2TIMManager.getConversationManager().deleteConversation(userId, new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {

            }
        });
    }

    public static List<String> deleteList = new ArrayList<>();

    public static List<String> getDeleteList(SP sp){
        if (null == deleteList || deleteList.size() > 0) {
            deleteList = sp.getList(Constant.FLY_AUDIO_LIST, String.class);
        }
        return deleteList;
    }


    public static void addDeleteList(SP sp, String userId){
        List<String> list = getDeleteList(sp);
        if (null == list) {
            list = new ArrayList<>();
        }
        if (!list.contains(userId)) {
            list.add(userId);
            sp.putList(Constant.FLY_AUDIO_LIST, list);
        }
        deleteList = list;
    }

    public static void removeDeleteList(SP sp, String userId){
        List<String> list = getDeleteList(sp);
        if (null == list) {
            list = new ArrayList<>();
        }
        if (!list.contains(userId)) {
            list.remove(userId);
            sp.putList(Constant.FLY_AUDIO_LIST, list);
        }
        deleteList = list;
    }

    public static void removeDeleteList(SP sp){
        sp.removeList(Constant.FLY_AUDIO_LIST);
        deleteList.clear();
    }

    public static void messageAsRead(String userId) {
        V2TIMManager.getMessageManager().markC2CMessageAsRead(userId, new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
                LogUtil.e(TAG, "设置消息已读失败 userId " + userId + " code " + code + " desc " + desc);
                // 设置消息已读失败
            }

            @Override
            public void onSuccess() {
                // 设置消息已读成功
                LogUtil.e(TAG, "设置消息已读成功 userId " + userId);
            }
        });
    }

    public static void startVideoCall(String userId, String nickname){
        List<UserModel> contactList = new ArrayList<>();
        UserModel model = new UserModel();
        model.userId = userId;
        model.userName = nickname;
        model.userSig = TUIKitConfigs.getConfigs().getGeneralConfig().getUserSig();
        contactList.add(model);
        TRTCVideoCallActivity.startCallSomeone(Application.getApplication(), contactList);
    }

    public static void startVideoCall(String userId, String nickname, String path){
        List<UserModel> contactList = new ArrayList<>();
        UserModel model = new UserModel();
        model.userId = userId;
        model.userName = nickname;
        model.userSig = TUIKitConfigs.getConfigs().getGeneralConfig().getUserSig();
        contactList.add(model);
        TRTCVideoCallActivity.startCallSomeone(Application.getApplication(), contactList, path);
    }

}
