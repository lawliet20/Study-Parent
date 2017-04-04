package com.javadesgin.study.命令模式;

/**
 * 倒带命令
 * Created by sherry on 2016/11/21.
 */
public class RewindCommand implements Command{

    private AudioPlayer audioPlayer;

    public RewindCommand(AudioPlayer audioPlayer){
        this.audioPlayer = audioPlayer;
    }

    @Override
    public void execute() {
        audioPlayer.rewind();
    }
}
