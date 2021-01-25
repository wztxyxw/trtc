package com.tencent.qcloud.tim.uikit.event;

/**
 * @author sc
 * @since 2020-10-22
 */
public class PutMsgEvent {

    public String userId;

    public PutMsgEvent(){

    }
    public PutMsgEvent(String userId){
        this.userId = userId;
    }
}
