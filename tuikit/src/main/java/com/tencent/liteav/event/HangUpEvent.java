package com.tencent.liteav.event;

/**
 * @author sc
 * @since 2020-10-13
 */
public class HangUpEvent {
    public boolean status;

    public HangUpEvent() {

    }
    public HangUpEvent(boolean status) {
        this.status = status;
    }
}
