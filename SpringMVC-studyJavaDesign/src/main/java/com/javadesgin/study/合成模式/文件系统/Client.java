package com.javadesgin.study.合成模式.文件系统;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import java.util.List;

/**
 * Created by sherry on 2016/11/15.
 */
public class Client {

    public static void main(String[] args) {
        Client client = new Client();
        client.test();
    }

    /**
     * 客户端测试方法
     */
    public void test() {
        //根下文件及文件夹
        Folder root = new Folder("树根");

        Folder b1_1 = new Folder("1_枝1");
        Folder b1_2 = new Folder("1_枝2");
        Folder b1_3 = new Folder("1_枝3");
        File l1_1 = new File("1_叶1");
        File l1_2 = new File("1_叶2");
        File l1_3 = new File("1_叶3");

        //b1_2下的文件及文件夹
        Folder b2_1 = new Folder("2_枝1");
        Folder b2_2 = new Folder("2_枝2");
        File l2_1 = new File("2_叶1");

        //缔造树的层次关系（简单测试，没有重复添加的控制）
        root.add(b1_1);
        root.add(b1_2);
        root.add(l1_1);
        root.add(l1_2);

        b1_2.add(b2_1);
        b1_2.add(b2_2);
        b1_2.add(l2_1);
        root.add(l1_3);
        root.add(b1_3);
        //控制台打印树的层次
        outTree(root);
    }

    public void outTree(Folder folder) {
        System.out.println("文件夹名称：" + folder.getName());
        iterator(folder);
    }

    /**
     * 遍历当前文件夹下所有的文件
     */
    public void iterator(Folder folder) {
        if (null != folder) {
            List<IFile> subFiles = folder.getAllSubFile();
            for (IFile file : subFiles) {
                if(file instanceof Folder){
                    Folder currFolder = (Folder) file;
                    System.out.println(getIndents(currFolder.getDeep())+file.getName());
                    iterator(currFolder);
                }else if(file instanceof File){
                    File currFile = (File)file;
                    System.out.println(getIndents(currFile.getDeep())+file.getName());
                }
            }
        }
    }

    /**
     * 获取缩进
     */
    public String getIndents(int index){
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<index;i++){
            sb.append("\t");
        }
        return sb.toString();
    }
}
