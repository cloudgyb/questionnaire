package com.gyb.questionnaire.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码生成工具类
 *
 * @author geng
 * 2020/11/19
 */
public class QRCodeUtil {

    public static byte[] generateQRCode(String content) {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        //编码
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        //边框距
        hints.put(EncodeHintType.MARGIN, 0);
        BitMatrix bitMatrix;
        try {
            //参数分别为：编码内容、编码类型、图片宽度、图片高度，设置参数
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 300, 300, hints);
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "png", bos);
            return bos.toByteArray();
        } catch (IOException | WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        final byte[] bytes = generateQRCode("http://www.baidu.com");
        FileOutputStream fos = new FileOutputStream("/home/gengyb/Desktop/1.png");
        assert bytes != null;
        fos.write(bytes);
        fos.close();
        System.out.println(Arrays.toString(bytes));
    }
}
