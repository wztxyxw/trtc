package com.chuangdu.library.network;

/**
 * 从网上找的一段代码
 * http://blog.csdn.net/ljd2038/article/details/51189334
 */
public interface ProgressListener {
    /**
     * @param progress 已经下载或上传字节数
     * @param total    总字节数
     * @param done     是否完成
     */
    void onProgress(long progress, long total, boolean done);
}
