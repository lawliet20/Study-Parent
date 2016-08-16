package com.spring.study.utils.others;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import com.sun.image.codec.jpeg.*;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("restriction")
public class CouponImgUtil {
	/**
	 * 把图片印刷到图片上
	 * 
	 * @param pressImg
	 *            -- 水印文件
	 * @param targetImg
	 *            -- 目标文件
	 * @param x
	 *            --x坐标
	 * @param y
	 *            --y坐标
	 */
	public final static void pressImage(String pressImg, String targetImg, int x, int y) {
		try {
			// 目标文件
			File _file = new File(targetImg);
			Image src = ImageIO.read(_file);
			int wideth = src.getWidth(null);
			int height = src.getHeight(null);
			BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = image.createGraphics();
			g.drawImage(src, 0, 0, wideth, height, null);

			// 水印文件
			File _filebiao = new File(pressImg);
			Image src_biao = ImageIO.read(_filebiao);
			int wideth_biao = src_biao.getWidth(null);
			int height_biao = src_biao.getHeight(null);
			g.drawImage(src_biao, x, y, wideth_biao, height_biao, null);
			// 水印文件结束
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
	 * @param targetImg
	 *            -- 目标图片
	 * @param fontName
	 *            -- 字体名
	 * @param fontStyle
	 *            -- 字体样式
	 * @param color
	 *            -- 字体颜色
	 * @param fontSize
	 *            -- 字体大小
	 * @param x
	 *            -- 偏移量
	 * @param y
	 */
	public static void pressText(String pressText, String targetImg, String fontName, int fontStyle, int color, int fontSize, int x, int y) {
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
			BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
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
	
	/**
	 * 优惠券带一维码图片
	 */
	public static void couponOneBarCodeImage(String couponNo,String couponValue, String expireDt, String conditionAmt,String actType,  String targetImg,String filePath, String fileName){
		if (filePath.lastIndexOf("/") != filePath.length()) {
			filePath = filePath + "/";
		}
		
		//1、生成一维码图片
		OneBarcodeUtil.createOneBarcode(couponNo, "onebarcode-"+fileName, filePath);
		//2、生成优惠券
		couponImage(couponValue, expireDt, conditionAmt,actType, targetImg,filePath, fileName);
		//3、生成带一维码的优惠券
		pressImage(filePath+"onebarcode-"+fileName, filePath+fileName, 100, 240);
	}

	/**
	 * 优惠券生成模板
	 */
	public static void couponImage(String couponValue, String expireDt, String conditionAmt,String actType,  String targetImg,String filePath, String fileName) {
		try {
			if (null == couponValue) {
				couponValue = "";
			}
			if (null == expireDt) {
				expireDt = "";
			}
			if (null == conditionAmt) {
				conditionAmt = "";
			}
			if (null == targetImg) {
				targetImg = "";
			}
			if (null == filePath) {
				filePath = "";
			}
			if (null == fileName) {
				fileName = "";
			}

			String filePathName = filePath + fileName;
			if (filePath.lastIndexOf("/") != filePath.length()) {
				filePathName = filePath + "/" + fileName;
			}
			File fileMkdir = new File(filePath);
			if (!fileMkdir.exists() || !fileMkdir.isDirectory()) {
				fileMkdir.mkdirs();
			}

			File _file = new File(targetImg);
			BufferedImage src = ImageIO.read(_file);
			File fileNew = new File(filePathName);
			ImageIO.write(src, "png", fileNew);
			BufferedImage target = ImageIO.read(fileNew);
			int wideth = src.getWidth(null);
			int height = src.getHeight(null);
			BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			g.drawImage(target, 0, 0, wideth, height, null);
			g.setColor(Color.WHITE);
			//优惠券类型
			if("0".equals(actType)){
				// 填写优惠券面值
				g.setFont(new Font("impact", Font.BOLD, 90));
				g.drawString(couponValue, 65, 120);

				// 填写优惠券使用条件
				g.setFont(new Font("宋体", Font.BOLD, 22));
				g.drawString(conditionAmt, 295, 155);
				g.drawString(couponValue + "元", 352, 155);

				// 填写优惠券有效期
				g.setFont(new Font("宋体", Font.PLAIN, 15));
				g.drawString("有效期：" + expireDt, 235, 194);
				
				//填写商家名称  商家地址
				/*g.setFont(new Font("宋体", Font.PLAIN, 10));
				g.drawString("汤臣于吉大场店", 180, 194);
				g.drawString("蒲州北路777号" + expireDt, 200, 194);*/
			}else if("2".equals(actType)){//现金券
				// 填写优惠券面值
				g.setFont(new Font("impact", Font.BOLD, 90));
				g.drawString(couponValue, 70, 120);

				// 填写优惠券有效期
				g.setFont(new Font("宋体", Font.PLAIN, 15));
				g.drawString("有效期：" + expireDt, 235, 194);
			}
			

			g.dispose();
			FileOutputStream out = new FileOutputStream(fileNew);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(image);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 图片下载功能
	 * 
	 * @param urlString
	 *            图片的路径
	 * @param filename
	 *            图片要下载到哪里的路径
	 * @throws Exception
	 */
	public static void download(HttpServletRequest request, HttpServletResponse response, String urlString, String filename) throws Exception {
		// String path = request.getContextPath()+"/pages/uploadImages/file/temp/terplate.png";
		// 暂时用的绝对路劲 获取项目路径+图片绝对路劲
		String path = request.getSession().getServletContext().getRealPath("") + urlString;
		// String urls = request.getScheme()+"://"+request.getServerName ()+":"+request.getServerPort()+path;
		File file = new File(path);
		// 以流的形式下载文件。
		InputStream fis = new BufferedInputStream(new FileInputStream(path));
		byte[] buffer = new byte[fis.available()];
		fis.read(buffer);
		fis.close();
		// 清空response
		response.reset();
		// 设置response的Header
		response.addHeader("Content-Disposition", "attachment;filename=" + filename+".png");
		response.addHeader("Content-Length", "" + file.length());
		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
		response.setContentType("application/octet-stream");
		response.setContentType("image/png;charset=utf-8");
		toClient.write(buffer);
		toClient.flush();
		toClient.close();
	}

	public static void main(String[] args) {
		 //pressImage("F:/code.png", "F:/coupon.png", 100, 250);
		 //pressText("5元", "f:/coupon.png","宋体",36,36,25,131,44);
		 couponImage("5", "2016年07月15日", "20","2", "f:/coupon.png", "f:/", "yhq" + System.currentTimeMillis() + ".png");
	}
}
