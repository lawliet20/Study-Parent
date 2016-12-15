package com.javadesgin.study.命令模式;

/**
 * 停止命令
 * Created by sherry on 2016/11/21.
 */
public class StopCommand implements Command{
    private AudioPlayer audioPlayer;

    public StopCommand(AudioPlayer audioPlayer){
        this.audioPlayer = audioPlayer;
    }

    @Override
    public void execute() {
        audioPlayer.stop();
    }
}
