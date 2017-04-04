package com.javadesgin.study.合成模式.文件系统;

/**
 * 文件角色（叶子节点）
 * Created by sherry on 2016/11/15.
 */
public class File implements IFile {
    //文件名称
    private String name;
    private int deep;

    public File(){

    }

    public File(String name){
        this.name = name;
    }

    @Override
    public IFile getComposite() {
        return this;
    }

    @Override
    public void simpleOperation() {
        System.out.println("叶子,简单商业逻辑...");
    }

    @Override
    public int getDeep() {
        return deep;
    }

    @Override
    public void setDeep(int deep) {
        this.deep = deep;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
