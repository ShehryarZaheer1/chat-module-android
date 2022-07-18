package com.xevensolutions.chat.interfaces;

public class AudioPlayBackState {

    int playbackState;
    int progress;



    public AudioPlayBackState(int playbackState, int progress) {
        this.playbackState = playbackState;
        this.progress = progress;
    }

    public int getPlaybackState() {
        return playbackState;
    }

    public void setPlaybackState(int playbackState) {
        this.playbackState = playbackState;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
