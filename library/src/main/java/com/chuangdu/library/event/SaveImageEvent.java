package com.chuangdu.library.event;

/**
 * @author sc
 * @since 2020-08-22
 */
public class SaveImageEvent {
    public boolean status;
    public String path;
    public SaveImageEvent(){}
    public SaveImageEvent(String path, boolean status){
        this.status = status;
        this.path = path;
    }
}
