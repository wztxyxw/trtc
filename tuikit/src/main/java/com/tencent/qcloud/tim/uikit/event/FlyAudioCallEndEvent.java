package com.tencent.qcloud.tim.uikit.event;

/**
 * @author sc
 * @since 2020-09-19
 */
public class FlyAudioCallEndEvent {
    public long duration;
    public FlyAudioCallEndEvent(){

    }
    public FlyAudioCallEndEvent(long duration){
        this.duration = duration;
    }
}
