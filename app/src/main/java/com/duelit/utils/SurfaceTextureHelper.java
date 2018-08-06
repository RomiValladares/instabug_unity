package com.duelit.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.support.annotation.DrawableRes;
import android.support.annotation.RawRes;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Romina on 7/6/16.
 */
public class SurfaceTextureHelper implements TextureView.SurfaceTextureListener {
    // MediaPlayer instance to control playback of video file.
    private MediaPlayer mMediaPlayer;
    private int animRes;
    private Context context;
    private boolean prepared;
    private ImageView placeHolderImgView;
    private int placeholderRes;

    public SurfaceTextureHelper(Context context, @RawRes int animRes) {
        this.context = context;
        this.animRes = animRes;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
        Surface surface = new Surface(surfaceTexture);

        try {
            AssetFileDescriptor afd = context.getResources().openRawResourceFd(animRes);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            //mMediaPlayer.setDataSource(afd.getFileDescriptor());
            mMediaPlayer.setSurface(surface);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.prepareAsync();

            // Play video when the media source is ready for playback.
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    prepared = true;
                    mediaPlayer.start();
                    if (placeHolderImgView != null) {
                        placeHolderImgView.setVisibility(View.GONE);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    public void onDestroy() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void setPlaceHolder(ImageView imageView, @DrawableRes int drawable) {
        this.placeHolderImgView = imageView;
        this.placeholderRes = drawable;
        if (!prepared)
            Picasso.with(context).load(drawable).into(imageView);
    }
}
