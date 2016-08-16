package com.spring.study.utils.others;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import com.sun.image.codec.jpeg.*;

import javax.imageio.ImageIO;
@SuppressWarnings("restriction")
public class ImageUtils {
	/**
     * 把图片印刷到图片上
     * 
     * @param pressImg --
     *            水印文件
     * @param targetImg --
     *            目标文件
     * @param x
     *            --x坐标
     * @param y
     *            --y坐标
     */
    public final static void pressImage(String pressImg, String targetImg,
            int x, int y) {
        try {
            //目标文件
            File _file = new File(targetImg);
            Image src = ImageIO.read(_file);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(wideth, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            g.drawImage(src, 0, 0, wideth, height, null);

            //水印文件
            File _filebiao = new File(pressImg);
            Image src_biao = ImageIO.read(_filebiao);
            int wideth_biao = src_biao.getWidth(null);
            int height_biao = src_biao.getHeight(null);
            g.drawImage(src_biao, (wideth - wideth_biao) / 2,
                    (height - height_biao) / 2, wideth_biao, height_biao, null);
            //水印文件结束
            g.dispose();
            FileOutputStream out = new FileOutputStream(targetImg);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(image);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印文字水印图片
     * 
     * @param pressText
     *            --文字
     * @param targetImg --
     *            目标图片
     * @param fontName --
     *            字体名
     * @param fontStyle --
     *            字体样式
     * @param color --
     *            字体颜色
     * @param fontSize --
     *            字体大小
     * @param x --
     *            偏移量
     * @param y
     */
    public static void pressText(String pressText, String targetImg,
            String fontName, int fontStyle, int color, int fontSize, int x,
            int y) {
        try {
            File _file = new File(targetImg);
            BufferedImage src = ImageIO.read(_file);
            File fileNew = new File("f:/yhq" + System.currentTimeMillis() + ".png");
            ImageIO.write(src, "png", fileNew);
            BufferedImage target = ImageIO.read(fileNew);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            System.out.println("wideth:" + wideth);
            System.out.println("height:" + height);
            BufferedImage image = new BufferedImage(wideth, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            g.drawImage(target, 0, 0, wideth, height, null);
            g.setColor(Color.WHITE);
            g.setFont(new Font(fontName, fontStyle, fontSize));

            g.drawString(pressText, x, y);
            g.dispose();
            FileOutputStream out = new FileOutputStream(fileNew);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(image);
            out.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
    public static void couponImage(String couponValue, String expireDt, String conditionAmt, String targetImg) {
        try {
            File _file = new File(targetImg);
            BufferedImage src = ImageIO.read(_file);
            File fileNew = new File("f:/yhq" + System.currentTimeMillis() + ".png");
            ImageIO.write(src, "png", fileNew);
            BufferedImage target = ImageIO.read(fileNew);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(wideth, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            g.drawImage(target, 0, 0, wideth, height, null);
            g.setColor(Color.WHITE);
            //填写优惠券面值
            g.setFont(new Font("宋体", Font.BOLD, 100));
            g.drawString(couponValue + "元", 50, 140);
            
            //填写优惠券使用条件
            g.setFont(new Font("宋体", Font.BOLD, 25));
            g.drawString("单笔满" + conditionAmt + "元", 220, 95);
            g.drawString("立减" + couponValue + "元", 220, 130);
            
            //填写优惠券有效期
            g.setFont(new Font("宋体", Font.PLAIN, 15));
            g.drawString("有效期：" + expireDt, 220, 196);
            
            g.dispose();
            FileOutputStream out = new FileOutputStream(fileNew);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(image);
            out.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //pressImage("F:/logo.png", "F:/123.jpg", 0, 0);
        //pressText("5元", "f:/coupon.png","宋体",36,36,25,131,44);
    	couponImage("5", "2016年07月15日", "100", "f:/coupon.png");
    }
}
