package com.chuangdu.pad.common.retrofit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * RxJava 工具类
 * <p>
 * Created on 2016/11/1 下午7:32.
 *
 * @author binwin20
 */

public final class RxUtil {
    private static FlowableTransformer<Object, Object> sApplyTransformer =
            observable -> observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

    private static FlowableTransformer<Object, Object> sSaveTransformer =
            observable -> observable.observeOn(Schedulers.io());

    private static ObservableTransformer<Object, Object> applyTransformer =
            observable -> observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
    /**
     * 在 IO 线程执行数据操作，UI线程执行订阅操作
     * <p>
     * 效果等同于：
     * <p>
     * Flowable.just().subscribeOn(Schedules.io()).observableOn(AndroidSchedulers.mainThread()).subscribe();
     * <p>
     * 使用方式：
     * <p>
     * Flowable.just().compose(RxUtils.applySchedulers()).subscribe();
     *
     * @return
     */
    public static <T> FlowableTransformer<T, T> applySchedulers() {
        return (FlowableTransformer<T, T>) sApplyTransformer;
    }
    public static <T> ObservableTransformer<T, T> applyScheduler() {
        return (ObservableTransformer<T, T>) applyTransformer;
    }
    /**
     * 在 IO 线程执行订阅操作
     * <p>
     * 效果等同于：
     * <p>
     * Flowable.just().observableOn(Schedules.io()).subscribe();
     * <p>
     * 使用方式：
     * <p>
     * Flowable.just().compose(RxUtils.saveSchedulers()).subscribe();
     */
    public static <T> FlowableTransformer<T, T> saveSchedulers() {
        return (FlowableTransformer<T, T>) sSaveTransformer;
    }

    /**
     * @return Schedulers.io();
     */
    public static Scheduler ioThread() {
        return Schedulers.io();
    }

    /**
     * @return AndroidSchedulers.mainThread();
     */
    public static Scheduler mainThread() {
        return AndroidSchedulers.mainThread();
    }

    /**
     * 文件下载转换工具，如果文件不存在，会创建文件；已存在，则被替换
     * <p>
     * 定义 retrofit 接口<br>
     * (@)Streaming (@)GET("https://github.com/binwin20/my.apk")<br>
     * <p>
     * 使用方式<br>
     * Api.downloadFile()<br>
     * .flatMap(RxUtils.downloadFlapMap(file))<br>
     * .compose(RxUtils.applySchedulers())<br>
     * .subscribe(progress -> showProgress(progress));
     *
     * @param saveFile 保存文件的地址
     * @return
     */
    public static Function<? super ResponseBody, ? extends Flowable<Integer>> downloadFlapMap(File saveFile) {
        return responseBody -> Flowable.create((FlowableOnSubscribe<Integer>) subscriber ->
                writeResponseBodyToDisk(responseBody, saveFile, subscriber), BackpressureStrategy.DROP)
                // 必须加上这个，否则报错 rx.exceptions.MissingBackpressureException
                .compose(RxUtil.applySchedulers());
    }

    private static void writeResponseBodyToDisk(ResponseBody body, File file, FlowableEmitter<? super Integer> subscriber) {
        Throwable throwable = null;
        try {

            InputStream inputStream = null;
            OutputStream outputStream = null;
            long lastTime = System.currentTimeMillis();

            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {break;}

                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    if (System.currentTimeMillis() - lastTime > 200) {
                        lastTime = System.currentTimeMillis();
                        subscriber.onNext((int) (fileSizeDownloaded * 100 / fileSize));
                    }
                }
                outputStream.flush();
            } catch (IOException e) {
                throwable = e;
            } finally {
                if (inputStream != null) {inputStream.close();}
                if (outputStream != null) {outputStream.close();}
            }
        } catch (IOException e) {
            throwable = e;
        } finally {
            if (throwable == null) {
                subscriber.onComplete();
            } else {
                subscriber.onError(throwable);
            }
        }
    }

    public static void unsubscribe(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
