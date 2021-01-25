package com.huantansheng.easyphotos.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huantansheng.easyphotos.R;
import com.huantansheng.easyphotos.models.album.AlbumModel;
import com.huantansheng.easyphotos.ui.adapter.PhotosAdapter2;

import java.util.ArrayList;

/**
 *
 * @author viroyal
 * @date 2018/1/25
 */

public class EasyPhotosView extends ViewGroup {


    private RecyclerView rvPhotos;
    private PhotosAdapter2 photosAdapter;
    private ArrayList<Object> photoList = new ArrayList<>();

    public EasyPhotosView(Context context) {
        super(context);
        init(context);
    }

    public EasyPhotosView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EasyPhotosView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    PhotosAdapter2.OnClickListener mOnClickListener = new PhotosAdapter2.OnClickListener() {
        @Override
        public void onCameraClick() {

        }

        @Override
        public void onPhotoClick(int position, int realPosition) {

        }

        @Override
        public void onSelectorOutOfMax(@Nullable Integer result) {

        }

        @Override
        public void onSelectorChanged() {

        }
    };

    private void init(Context context){

//        View view = LayoutInflater.from(context).inflate(R.layout.easy_photos_view, null);
//        this.addView(view);

        View.inflate(context, R.layout.easy_photos_view, null);

//        LayoutInflater.from(context).inflate(R.layout.easy_photos_view, this);
        rvPhotos = findViewById(R.id.recycler);

//        LayoutInflater.from(context).inflate(R.layout.icon_edittext, this);
//        View.inflate(context, R.layout.icon_edittext, this);
//
//        TextView t = new TextView(context);
//        t.setText("2121212");
//        t.setTextColor(getResources().getColor(R.color.c7));
//        this.addView(t);
    }

    public void start(Activity activity){

        AlbumModel.CallBack albumModelCallBack = new AlbumModel.CallBack() {
            @Override
            public void onAlbumWorkedCallBack() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //initView();
                    }
                });
            }
        };
        albumModel = AlbumModel.getInstance();
        albumModel.query(getContext(), albumModelCallBack);

    }

    private AlbumModel albumModel;

    private void initView() {

        if (albumModel.getAlbumItems().isEmpty()) {

            return;
        }


//        rvPhotos = new RecyclerView(getContext());
        rvPhotos.setLayoutManager(new GridLayoutManager(getContext(), 3));
//        photosAdapter = new PhotosAdapter(getContext(), photoList, mOnClickListener);
//        rvPhotos.setAdapter(photosAdapter);
//        rvPhotos.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//        addView(rvPhotos);

        photoList.clear();
        photoList.addAll(albumModel.getCurrAlbumItemPhotos(0));
//        photosAdapter.change();

        photosAdapter = new PhotosAdapter2(getContext(), photoList, mOnClickListener);
        rvPhotos.setAdapter(photosAdapter);
    }

}
