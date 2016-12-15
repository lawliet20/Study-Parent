package com.javadesgin.study.合成模式.文件系统;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件夹角色（树枝节点）
 * Created by sherry on 2016/11/15.
 */
public class Folder implements IFile{
    private List<IFile> subFiles = new ArrayList<IFile>();
    private String name;//文件名
    private int deep;//层级深度

    public Folder(){

    }

    public Folder(String name){
        this.name = name;
    }

    @Override
    public IFile getComposite() {
        return this;
    }

    @Override
    public void simpleOperation() {
        System.out.println("树枝,简单商业逻辑...");
    }

    @Override
    public int getDeep() {
        return deep;
    }

    @Override
    public void setDeep(int deep) {
        this.deep = deep;
    }

    /**
     * 获取所有的子文件
     */
    public List<IFile> getAllSubFile(){
        return this.subFiles;
    }

    /**
     * 添加一个文件或文件夹
     */
    public void add(IFile iFile){
        this.subFiles.add(iFile);
        iFile.setDeep(deep+1);
    }

    /**
     * 删除一个文件或文件夹
     */
    public void remove(IFile iFile){
        this.subFiles.remove(iFile);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
