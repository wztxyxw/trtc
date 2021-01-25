package com.huantansheng.easyphotos.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huantansheng.easyphotos.R;
import com.huantansheng.easyphotos.models.album.AlbumModel;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.huantansheng.easyphotos.result.Result;
import com.huantansheng.easyphotos.setting.Setting;
import com.huantansheng.easyphotos.ui.adapter.PhotosAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc
 */
public class EasyPhotosUtils {


    private Activity mActivity;

    private RecyclerView rvPhotos;
    private PhotosAdapter photosAdapter;
    private ArrayList<Object> photoList = new ArrayList<>();

    private Listener mListener;
    public interface Listener {
        void onSelectorChanged(List<Photo> photos);
    }

    public static EasyPhotosUtils newInstance(Activity activity, ViewGroup view, Listener listener) {
        EasyPhotosUtils f = new EasyPhotosUtils();
        f.mListener = listener;
        f.mActivity = activity;
        f.onCreateView();
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        activity.addContentView(f.mView, params);
//        f.mView.setVisibility(View.GONE);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.addView(f.mView, params);
        return f;
    }

    View mView;

    public View onCreateView() {
        mView = LayoutInflater.from(mActivity).inflate(R.layout.easy_photos_view, null, false);

        rvPhotos = mView.findViewById(R.id.recycler);

        rvPhotos.setLayoutManager(new GridLayoutManager(mActivity, 4));

        photoList.clear();

        photosAdapter = new PhotosAdapter(mActivity, photoList, mOnClickListener);

        albumModel = AlbumModel.getInstance();
        rvPhotos.setAdapter(photosAdapter);
        return mView;
    }

    AlbumModel.CallBack albumModelCallBack = new AlbumModel.CallBack() {
        @Override
        public void onAlbumWorkedCallBack() {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initView();
                }
            });
        }
    };

    public void show(){
        mView.setVisibility(View.VISIBLE);
    }
    public void hint(){
        mView.setVisibility(View.GONE);
    }

    public void start(){

        albumModel.query(mActivity, albumModelCallBack);

    }
    public void start(List<Photo> photos){

    }

    public void addPhoto(Photo photo){
        photoList.add(0, photo);
        photosAdapter.change();
    }

    private AlbumModel albumModel;

    private void initView() {

        if (albumModel.getAlbumItems().isEmpty()) {

            return;
        }

        photoList.clear();
        photoList.addAll(albumModel.getCurrAlbumItemPhotos(0));
        photosAdapter.change();
    }

    public void unSelect(Photo photo){
        Result.removePhoto(photo);
        photosAdapter.change();
    }

    public void change(List<Photo> photos){
        Result.removeAll();
        for (Photo p : photos) {
            Result.addPhoto(p);
        }
        photosAdapter.change();
    }
    public void change(){
        photosAdapter.change();
    }

    PhotosAdapter.OnClickListener mOnClickListener = new PhotosAdapter.OnClickListener() {
        @Override
        public void onCameraClick() {

        }

        @Override
        public void onPhotoClick(int position, int realPosition) {

        }

        @Override
        public void onSelectorOutOfMax(@Nullable Integer result) {
            if (result == null) {
                if (Setting.isOnlyVideo()) {
                    Toast.makeText(mActivity, mActivity.getString(R.string.selector_reach_max_video_hint_easy_photos
                            , Setting.count), Toast.LENGTH_SHORT).show();

                } else if (Setting.showVideo) {
                    Toast.makeText(mActivity, mActivity.getString(R.string.selector_reach_max_hint_easy_photos,
                            Setting.count), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mActivity, mActivity.getString(R.string.selector_reach_max_image_hint_easy_photos,
                            Setting.count), Toast.LENGTH_SHORT).show();
                }
                return;
            }
            switch (result) {
                case -1:
                    Toast.makeText(mActivity, mActivity.getString(R.string.selector_reach_max_image_hint_easy_photos
                            , Setting.pictureCount), Toast.LENGTH_SHORT).show();
                    break;
                case -2:
                    Toast.makeText(mActivity, mActivity.getString(R.string.selector_reach_max_video_hint_easy_photos
                            , Setting.videoCount), Toast.LENGTH_SHORT).show();
                    break;
                case -3:
                    Toast.makeText(mActivity, mActivity.getString(R.string.selector_reach_video_or_photos), Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void onSelectorChanged() {
            if (null != mListener) {
                mListener.onSelectorChanged(Result.photos);
            }
        }
    };

}
