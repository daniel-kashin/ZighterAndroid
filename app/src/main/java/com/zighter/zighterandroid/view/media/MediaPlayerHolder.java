package com.zighter.zighterandroid.view.media;

import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.widget.CustomMediaController;

public class MediaPlayerHolder
        implements
        CustomMediaController.MediaPlayerControl,
        MediaPlayer.OnBufferingUpdateListener {

    @NonNull
    private final MediaPlayer mediaPlayer;
    private int bufferingPercent;

    public MediaPlayerHolder(@NonNull MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        this.mediaPlayer.setOnBufferingUpdateListener(this);
        this.bufferingPercent = 0;
    }

    @Override
    public void start() {
        try {
            mediaPlayer.start();
        } catch (IllegalStateException e) {
            // do nothing
        }
    }

    @Override
    public void pause() {
        try {
            mediaPlayer.pause();
        } catch (IllegalStateException e) {
            // do nothing
        }
    }

    @Override
    public int getDuration() {
        try {
            return mediaPlayer.getDuration();
        } catch (IllegalStateException e) {
            return 0;
        }
    }

    @Override
    public int getCurrentPosition() {
        try {
            return mediaPlayer.getCurrentPosition();
        } catch (IllegalStateException e) {
            return 0;
        }
    }

    @Override
    public void seekTo(int pos) {
        try {
            mediaPlayer.seekTo(pos);
        } catch (IllegalStateException e) {
            // do nothing
        }
    }

    @Override
    public boolean isPlaying() {
        try {
            return mediaPlayer.isPlaying();
        } catch (IllegalStateException e) {
            return false;
        }
    }

    @Override
    public int getBufferPercentage() {
        return bufferingPercent;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        this.bufferingPercent = percent;
    }
}
