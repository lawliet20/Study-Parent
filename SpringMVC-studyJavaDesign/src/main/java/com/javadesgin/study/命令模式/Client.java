package com.javadesgin.study.命令模式;

/**
 * 客户端
 * Created by sherry on 2016/11/21.
 */
public class Client {

    public static void main(String[] args) {
        AudioPlayer audioPlayer = new AudioPlayer();
        Command playCommand = new PlayCommand(audioPlayer);
        Command stopCommand = new StopCommand(audioPlayer);
        Command rewindCommand = new RewindCommand(audioPlayer);
        KeyPad keyPad = new KeyPad(playCommand,stopCommand,rewindCommand);

        keyPad.play();
        keyPad.rewind();
        keyPad.stop();
        keyPad.play();


    }
}
