package com.huantansheng.easyphotos.ui.crop.event;

import android.net.Uri;

/**
 * @author sc
 */
public class CropEvent {
    public Uri outputUri;
    public int type;
    public CropEvent(){

    }
    public CropEvent(Uri outputUri){
        this.outputUri = outputUri;
    }
    public CropEvent(int type, Uri outputUri){
        this.type = type;
        this.outputUri = outputUri;
    }
}
