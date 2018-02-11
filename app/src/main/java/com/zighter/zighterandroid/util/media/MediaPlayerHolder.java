package com.zighter.zighterandroid.util.media;

import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.widget.MediaController;

public class MediaPlayerHolder implements MediaController.MediaPlayerControl,
        MediaPlayer.OnBufferingUpdateListener {

    @NonNull
    private final MediaPlayer mediaPlayer;
    private int bufferingPercent;

    public MediaPlayerHolder(@NonNull MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        this.bufferingPercent = 0;
    }

    @Override
    public void start() {
        mediaPlayer.start();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
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
    public int getAudioSessionId() {
        return mediaPlayer.getAudioSessionId();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        this.bufferingPercent = percent;
    }
}
