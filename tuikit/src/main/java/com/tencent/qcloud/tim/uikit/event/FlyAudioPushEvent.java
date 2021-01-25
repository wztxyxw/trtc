package com.tencent.qcloud.tim.uikit.event;

/**
 * @author sc
 * @since 2020-09-22
 */
public class FlyAudioPushEvent {

    public int openType;
    public String params;

    public FlyAudioPushEvent(){

    }

    public FlyAudioPushEvent(int openType, String params){
        this.openType = openType;
        this.params = params;
    }
}
