package com.chuangdu.pad.event;

/**
 * The type Login event.
 *
 * @author sc
 * @since 2020 -05-14
 */
public class LoginEvent {
    /**
     * The Status.
     */
    public boolean status;

    /**
     * Instantiates a new Login event.
     */
    public LoginEvent() {
    }

    /**
     * Instantiates a new Login event.
     *
     * @param status the status
     */
    public LoginEvent(boolean status) {
        this.status = status;
    }
}
