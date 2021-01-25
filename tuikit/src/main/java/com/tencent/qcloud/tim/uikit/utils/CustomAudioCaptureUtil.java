package com.tencent.qcloud.tim.uikit.utils;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;

import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author jalle
 * @package:com.netease.nim.avchatkit.external
 * @fileName:AVChatAudioCapture
 * @date: 2019-03-18
 * @desc:TODO
 */
public class CustomAudioCaptureUtil {
    private static final String TAG = CustomAudioCaptureUtil.class.getSimpleName();
    private AudioRecord mAudioRecord;
    private TRTCCloud trtcCloud;
    private boolean mIsCaptureStarted = false;
    int mMinBufferSize = 0; //最小缓冲区大小
    private volatile boolean mIsLoopExit = true;
    private boolean isPush = true;
    private Thread mCaptureThread;

    //指定音频源 这个和MediaRecorder是相同的
    private static final int DEFAULT_SOURCE = MediaRecorder.AudioSource.MIC;
    //指定采样率
    // 不同的Android手机硬件将能够以不同的采样率进行采样。其中11025是一个常见的采样率）
    private static final int DEFAULT_SAMPLE_RATE = 48000;
    private static final int DEFAULT_CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;   // 双声道  CHANNEL_IN_STEREO  单声道 CHANNEL_IN_MONO
    private static final int DEFAULT_AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;    // 量化位数
    private TRTCCloudDef.TRTCAudioFrame frame;
    public CustomAudioCaptureUtil(TRTCCloud trtc){
        frame = new TRTCCloudDef.TRTCAudioFrame();
        trtcCloud = trtc;
        startCapture(DEFAULT_SOURCE, DEFAULT_SAMPLE_RATE, DEFAULT_CHANNEL_CONFIG,
                DEFAULT_AUDIO_FORMAT);


    }


    public boolean startCapture(int audioSource, int sampleRateInHz, int channelConfig, int audioFormat) {

        if (mIsCaptureStarted) {
            Log.e(TAG, "audio Capture already started !");
            return false;
        }

       // mMinBufferSize = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
        //单声道 2048
        mMinBufferSize = 2048;
        if (mMinBufferSize == AudioRecord.ERROR_BAD_VALUE) {
            Log.e(TAG, "Invalid AudioRecord parameter !");
            return false;
        }
        Log.d(TAG , "getMinBufferSize = "+mMinBufferSize+" bytes !");

        mAudioRecord = new AudioRecord(audioSource,sampleRateInHz,channelConfig,audioFormat,mMinBufferSize);
        if (mAudioRecord.getState() == AudioRecord.STATE_UNINITIALIZED) {
            Log.e(TAG, "AudioRecord initialize fail !");
            return false;
        }

        mAudioRecord.startRecording();

        mIsLoopExit = false;
        mCaptureThread = new Thread(new AudioCaptureRunnable());
        mCaptureThread.start();

        mIsCaptureStarted = true;

        Log.d(TAG, "Start audio capture success !");

        return true;
    }


    public void stopCapture() {

        if (!mIsCaptureStarted) {
            return;
        }

        mIsLoopExit = true;
        try {
            mCaptureThread.interrupt();
            mCaptureThread.join(1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (mAudioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
            mAudioRecord.stop();
        }

        mAudioRecord.release();

        mIsCaptureStarted = false;

        Log.d(TAG, "Stop audio capture success !");
    }

    private class AudioCaptureRunnable implements Runnable {

        @Override
        public void run() {

            while (!mIsLoopExit) {

                byte[] buffer = new byte[mMinBufferSize];

                int ret = mAudioRecord.read(buffer, 0, mMinBufferSize);
                if (ret == AudioRecord.ERROR_INVALID_OPERATION) {
                    Log.e(TAG , "AudioRecord Error ERROR_INVALID_OPERATION");
                }
                else if (ret == AudioRecord.ERROR_BAD_VALUE) {
                    Log.e(TAG , "AudioRecord Error ERROR_BAD_VALUE");
                }
                else {


                    // 直接设置为 buffer size = 2048
                    if (isPush) {
                        frame.channel = 1;
                        frame.sampleRate = DEFAULT_SAMPLE_RATE;
                        frame.data = buffer;
                        frame.timestamp = SystemClock.elapsedRealtimeNanos();
                        Log.e("Push","----Data:"+buffer.length);
                        trtcCloud.sendCustomAudioData(frame);
                        Log.d(TAG , "success captured PCM data | "+ buffer.length +" |  bytes ");
                    }
                }
                SystemClock.sleep(21);
            }
        }
    }


//
//    public void StartAudioData(){//得到录音数据
//
//        String tmpName = System.currentTimeMillis()+"_";
//        final File tmpFile = createFile(tmpName+".pcm");
//        try {
//            FileOutputStream outputStream = new FileOutputStream(tmpFile.getAbsoluteFile());
//
//            while (mIsCaptureStarted){
//                int n_size = mAudioRecord.read(buffer, 0, 1024);
//                Log.w("audiotest", "StartAudioData: ------------------------"+n_size);
//
//                try {
//                    outputStream.write(buffer);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            outputStream.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public File createFile(String name) {
//        String dirPath = Environment.getExternalStorageDirectory().getPath()+"/AudioRecord/";
//        File file = new File(dirPath);
//        if(!file.exists()) {
//            file.mkdirs();
//        }
//        String filePath = dirPath +name;
//        File objFile = new File(filePath);
//        if (!objFile.exists()) {
//            try {
//                objFile.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return objFile;
//        }
//        return null;
//    }







}
