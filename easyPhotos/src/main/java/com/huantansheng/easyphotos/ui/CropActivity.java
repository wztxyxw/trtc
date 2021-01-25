package com.huantansheng.easyphotos.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chuangdu.library.app.ScActivity;
import com.huantansheng.easyphotos.R;
import com.huantansheng.easyphotos.ui.crop.CropImageView2;
import com.huantansheng.easyphotos.ui.crop.callback.CropCallback;
import com.huantansheng.easyphotos.ui.crop.callback.LoadCallback;
import com.huantansheng.easyphotos.ui.crop.callback.SaveCallback;
import com.huantansheng.easyphotos.ui.crop.event.CropEvent;
import com.huantansheng.easyphotos.ui.crop.util.FontUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

/**
 *
 * @author qjj
 * @date 2016/10/19
 */
public class CropActivity extends ScActivity {

    private LinearLayout mRootLayout;
    private CropImageView2 mCropView;
    private ImageButton mLeft;
    private ImageButton mRight;
    private ImageButton mDone;

    private TextView tvRightBtn;

    public static void start(Context context, Uri uri) {
        Intent intent = new Intent(context, CropActivity.class);
        intent.setData(uri);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_crop;
    }

    @Override
    public void initView() {
        ARouter.getInstance().inject(this);
        initToolBar("裁剪");

        tvRightBtn = getToolBarRightText();
        tvRightBtn.setVisibility(View.VISIBLE);
        tvRightBtn.setTextColor(getResources().getColor(R.color.c19));
        tvRightBtn.setText("完成");
        Uri uri = getIntent().getData();

        mCropView = (CropImageView2) findViewById(R.id.cropImageView);
        mLeft = (ImageButton) findViewById(R.id.buttonRotateLeft);
        mRight = (ImageButton) findViewById(R.id.buttonRotateRight);
        mDone = (ImageButton) findViewById(R.id.buttonDone);
        mRootLayout = (LinearLayout) findViewById(R.id.layout_root);
        FontUtils.setFont(mRootLayout);
        if (null != uri) {
            mCropView.setImageBitmap(BitmapFactory.decodeFile(uri.getPath()));
        }
        mCropView.setCropMode(CropImageView2.CropMode.SQUARE);
        mLeft.setOnClickListener(btnListener);
        mRight.setOnClickListener(btnListener);
        mDone.setOnClickListener(btnListener);
        tvRightBtn.setOnClickListener(btnListener);
    }

    @Override
    public void initClick() {

    }

    @Override
    public void initData() {

    }

    private final View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int i = v.getId();

            if (i == R.id.toolbar_right_txt) {
                cropImage();
            }
            if (i == R.id.buttonDone) {
                cropImage();
            }
            if (i == R.id.buttonRotateLeft) {
                mCropView.rotateImage(CropImageView2.RotateDegrees.ROTATE_M90D);
            }
            if (i == R.id.buttonRotateRight) {
                mCropView.rotateImage(CropImageView2.RotateDegrees.ROTATE_90D);
            }
        }
    };

    public void cropImage() {
        showLoadingView();
        mCropView.startCrop(createSaveUri(), mCropCallback, mSaveCallback);
    }

    public Uri createSaveUri() {
        return Uri.fromFile(new File(getCacheDir(), "cropped"));
    }

    private final LoadCallback mLoadCallback = new LoadCallback() {
        @Override
        public void onSuccess() {
            hideLoadingView();
        }

        @Override
        public void onError() {
            hideLoadingView();
        }
    };

    private final CropCallback mCropCallback = new CropCallback() {
        @Override
        public void onSuccess(Bitmap cropped) {
        }

        @Override
        public void onError() {
        }
    };

    private final SaveCallback mSaveCallback = new SaveCallback() {
        @Override
        public void onSuccess(Uri outputUri) {
            hideLoadingView();
            EventBus.getDefault().post(new CropEvent(outputUri));
            finish();
        }

        @Override
        public void onError() {
            hideLoadingView();
        }
    };
}
