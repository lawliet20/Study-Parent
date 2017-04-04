package com.wwj.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FileUtil {

    /**
     * 获取文件大小
     */
    public long getUploadFileSize(File file) {
        if (file != null) {
            return file.length();
        }
        return 0;
    }

    /**
     * 获取文件的后缀
     */
    public String getFileExt(File file) {
        if (file != null) {
            String fileName = file.getName();
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return null;
    }

    /**
     * 获取文件后缀名
     *
     * @param fileName
     * @return
     */
    public String getFileExt(String fileName) {

        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 删除空目录
     *
     * @param dir 将要删除的目录路径
     */
    public static void doDeleteEmptyDir(String dir) {
        boolean success = (new File(dir)).delete();
        if (success) {
            System.out.println("Successfully deleted empty directory: " + dir);
        } else {
            System.out.println("Failed to delete empty directory: " + dir);
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful. If a deletion fails, the method stops attempting to delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 删除文件下所有文件
     */
    public static void deleteFile(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (null != files && files.length > 0) {
                for (File file : files) {
                    deleteFile(file);
                }
            }
        } else {
            dir.delete();
        }
    }

    public static List<String> listFiles(String filePath) {
        File file = new File(filePath);
        List<String> filesPath = new ArrayList<String>();
        listFiles(file, filesPath);
        return filesPath;
    }

    public static void listFiles(File file, List<String> list) {
        listFiles(file, list, null, null);
    }

    /**
     * 正则过滤文件与文件夹
     * @param file    需要递归扫描的文件
     * @param list    用于保存所有文件名的list（文件绝对路径）
     * @param dirRex  文件夹正则过滤字符串
     * @param fileRex 文件正则过滤字符串
     */
    public static void listFiles(File file, List<String> list, String dirRex, String fileRex) {
        final String dirrex = dirRex;
        final String filerex = fileRex;
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles(new FileFilter() {
                @Override
                public boolean accept(File thisFile) {
                    if (thisFile.isFile()) {
                        return regexFile(thisFile, filerex);
                    } else if (thisFile.isDirectory()) {
                        return regexFile(thisFile, dirrex);
                    } else {
                        return false;
                    }
                }
            });
            for (File thisfile : files) {
                listFiles(thisfile, list, dirRex, fileRex);
            }
        } else if (file.exists() && file.isFile()) {
            if (regexFile(file, filerex)) {
                list.add(file.getAbsolutePath());
            }
        }
    }

    /**
     * 判断文件的名称是否符合正则
     */
    public static boolean regexFile(File file, String regex) {
        if (StrUtil.isNotEmpty(regex) && Pattern.compile(regex).matcher(file.getName()).matches()) {
            return true;
        } else if (StrUtil.isEmpty(regex)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 如果文件路径不存在则创建一个
     */
    public static void mkDir(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

}