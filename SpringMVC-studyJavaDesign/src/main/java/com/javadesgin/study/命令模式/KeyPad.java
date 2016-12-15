package com.javadesgin.study.命令模式;

import java.security.Key;

/**
 * 键盘（命令的请求者）
 * Created by sherry on 2016/11/21.
 */
public class KeyPad {

    private Command playCommand;
    private Command stopCommand;
    private Command rewindCommand;

    public KeyPad(Command playCommand,Command stopCommand,Command rewindCommand){
        this.playCommand = playCommand;
        this.stopCommand = stopCommand;
        this.rewindCommand = rewindCommand;
    }

    public void play(){
        playCommand.execute();
    }

    public void stop(){
        stopCommand.execute();
    }

    public void rewind(){
        rewindCommand.execute();
    }
}
