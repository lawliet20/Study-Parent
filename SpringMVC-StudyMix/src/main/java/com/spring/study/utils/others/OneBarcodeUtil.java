package com.spring.study.utils.others;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;

import org.jbarcode.JBarcode;
import org.jbarcode.encode.Code39Encoder;
import org.jbarcode.encode.Code93Encoder;
import org.jbarcode.encode.EAN13Encoder;
import org.jbarcode.paint.BaseLineTextPainter;
import org.jbarcode.paint.EAN13TextPainter;
import org.jbarcode.paint.WideRatioCodedPainter;
import org.jbarcode.paint.WidthCodedPainter;
import org.jbarcode.util.ImageUtil;

/**
 * 
 * 一维码图片生成器
 * 
 * @author winter.liu
 * 
 */
public class OneBarcodeUtil {

	public static void main(String[] paramArrayOfString) {
		createOneBarcode("123456789012","Code39.png", "C:/Users/Administrator/Desktop/");
	}
	
	/**
	 * 创建 一维码
	 */
	public static void createOneBarcode(String onebarcode,String codePicName,String codeUrl){
		try {
			JBarcode localJBarcode = new JBarcode(EAN13Encoder.getInstance(), WidthCodedPainter.getInstance(), EAN13TextPainter.getInstance());
			// 生成. 欧洲商品条码(=European Article Number)
			// 这里我们用作图书条码
			// 支持EAN13, EAN8, UPCA, UPCE, Code 3 of 9, Codabar, Code 11
			// String str = "788515004012";
			// saveToGIF(localBufferedImage, "EAN13.gif");
			localJBarcode.setEncoder(Code39Encoder.getInstance());
			localJBarcode.setPainter(WideRatioCodedPainter.getInstance());
			localJBarcode.setTextPainter(BaseLineTextPainter.getInstance());
			localJBarcode.setShowCheckDigit(false);
			BufferedImage localBufferedImage = localJBarcode.createBarcode(onebarcode);
			localBufferedImage = localJBarcode.createBarcode(onebarcode);
			saveToPNG(localBufferedImage, codePicName, codeUrl);

		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	// 生成code39类型一维码
	public static void getCode39(String str, String imgUrl,String imgName) {
		try {
			JBarcode localJBarcode = new JBarcode(EAN13Encoder.getInstance(), WidthCodedPainter.getInstance(), EAN13TextPainter.getInstance());
			// 生成. 欧洲商品条码(=European Article Number)
			// 这里我们用作图书条码
			// 支持EAN13, EAN8, UPCA, UPCE, Code 3 of 9, Codabar, Code 11
			localJBarcode.setEncoder(Code93Encoder.getInstance());
			localJBarcode.setPainter(WideRatioCodedPainter.getInstance());
			localJBarcode.setTextPainter(BaseLineTextPainter.getInstance());
			localJBarcode.setShowCheckDigit(false);

			BufferedImage localBufferedImage = localJBarcode.createBarcode(str);
			saveToPNG(localBufferedImage, imgName+".png", imgUrl);

		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	public static void saveToJPEG(BufferedImage paramBufferedImage, String paramString, String url) {
		saveToFile(paramBufferedImage, paramString, "jpeg", url);
	}

	public static void saveToPNG(BufferedImage paramBufferedImage, String paramString, String url) {
		saveToFile(paramBufferedImage, paramString, "png", url);
	}

	public static void saveToGIF(BufferedImage paramBufferedImage, String paramString, String url) {
		saveToFile(paramBufferedImage, paramString, "gif", url);
	}

	public static void saveToFile(BufferedImage paramBufferedImage, String paramString1, String paramString2, String url) {
		try {
			FileOutputStream localFileOutputStream = new FileOutputStream(url + paramString1);
			ImageUtil.encodeAndWrite(paramBufferedImage, paramString2, localFileOutputStream, 96, 96);
			localFileOutputStream.close();
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

}
