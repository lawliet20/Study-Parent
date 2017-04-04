package com.javadesgin.study.命令模式;

/**
 * 播放命令
 * Created by sherry on 2016/11/21.
 */
public class PlayCommand implements Command{
    private AudioPlayer audioPlayer;

    public PlayCommand(AudioPlayer audioPlayer){
        this.audioPlayer = audioPlayer;
    }

    @Override
    public void execute() {
        audioPlayer.play();
    }
}
