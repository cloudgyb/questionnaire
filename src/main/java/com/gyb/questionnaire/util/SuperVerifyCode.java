package com.gyb.questionnaire.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Random;

/**
 * @author geng
 * 2020/11/4
 */
public class SuperVerifyCode {
    private static final char[] operators = {'+', '-', 'x', '÷'};

    public static Verification generateRandomSumImg() {
        int fontSize = 40; //字体大小
        int letterSpacing = 10;//字母间隔
        //图片最终的宽度
        int imgWidth = fontSize * 7 + (7 - 1) * letterSpacing;
        int imgHeight = 70;
        BufferedImage verifyImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = (Graphics2D) verifyImg.getGraphics();
        graphics.setColor(Color.WHITE);//设置画笔颜色-验证码背景色
        graphics.fillRect(0, 0, imgWidth, imgHeight);//填充背景
        graphics.setFont(new Font("微软雅黑", Font.BOLD, fontSize));
        int x = 10;  //旋转原点的 x 坐标
        int num1 = 0, num2 = 0;
        char operator = ' ';
        Random random = new Random(); //1+1=2
        for (int i = 0; i < 5; i++) {
            graphics.setColor(getRandomColor());
            //设置字体旋转角度
            int degree = random.nextInt() % 30;  //角度小于30度
            //正向旋转
            graphics.rotate(degree * Math.PI / 180, x, 45);
            if (i == 0 || i == 2) {
                int num = random.nextInt(10);
                if (i == 0) {
                    num1 = num;
                } else {
                    if (operator == '÷' && num == 0) { //保证除数不为0
                        num = 1;
                    }
                    num2 = num;
                }
                graphics.drawString(num + "", x, 45);
            } else if (i == 1) {
                operator = operators[random.nextInt(operators.length)];
                graphics.drawString(operator + "", x, 45);
            } else if (i == 3) {
                graphics.drawString("=", x, 45);
            } else {
                graphics.drawString("?", x, 45);
            }
            //反向旋转
            graphics.rotate(-degree * Math.PI / 180, x, 45);
            x += (fontSize + letterSpacing);
        }
        //画干扰线
        for (int i = 0; i < 6; i++) {
            // 设置随机颜色
            graphics.setColor(getRandomColor());
            // 随机画线
            graphics.drawLine(random.nextInt(imgWidth), random.nextInt(imgHeight),
                    random.nextInt(imgWidth), random.nextInt(imgHeight));
        }
        //添加噪点
        for (int i = 0; i < 30; i++) {
            int x1 = random.nextInt(imgWidth);
            int y1 = random.nextInt(imgHeight);
            graphics.setColor(getRandomColor());
            graphics.fillRect(x1, y1, 2, 2);
        }
        try {
            int code = eval(num1, num2, operator);
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(verifyImg, "jpeg", outputStream);
            return new Verification(code + "", outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int eval(int num1, int num2, char operator) {
        if (operator == '+')
            return num1 + num2;
        else if (operator == '-')
            return num1 - num2;
        else if (operator == 'x')
            return num1 * num2;
        return num1 / num2;
    }

    public static Verification generateRandomTextImg(int textLength) {
        int fontSize = 40; //字体大小
        int letterSpacing = 10;//字母间隔
        //图片最终的宽度
        int imgWidth = fontSize * textLength + (textLength - 1) * letterSpacing;
        int imgHeight = 70;
        BufferedImage verifyImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = (Graphics2D) verifyImg.getGraphics();
        graphics.setColor(Color.WHITE);//设置画笔颜色-验证码背景色
        graphics.fillRect(0, 0, imgWidth, imgHeight);//填充背景
        graphics.setFont(new Font("微软雅黑", Font.BOLD, fontSize));
        //数字和字母的组合
        String baseNumLetter = "123456789abcdefghijklmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ";
        StringBuilder codeBuffer = new StringBuilder();
        int x = 10;  //旋转原点的 x 坐标
        String ch;
        Random random = new Random();
        for (int i = 0; i < textLength; i++) {
            graphics.setColor(getRandomColor());
            //设置字体旋转角度
            int degree = random.nextInt() % 30;  //角度小于30度
            int dot = random.nextInt(baseNumLetter.length());
            ch = baseNumLetter.charAt(dot) + "";
            codeBuffer.append(ch);
            //正向旋转
            graphics.rotate(degree * Math.PI / 180, x, 45);
            graphics.drawString(ch, x, 45);
            //反向旋转
            graphics.rotate(-degree * Math.PI / 180, x, 45);
            x += (fontSize + letterSpacing);
        }
        //画干扰线
        for (int i = 0; i < 6; i++) {
            // 设置随机颜色
            graphics.setColor(getRandomColor());
            // 随机画线
            graphics.drawLine(random.nextInt(imgWidth), random.nextInt(imgHeight),
                    random.nextInt(imgWidth), random.nextInt(imgHeight));
        }
        //添加噪点
        for (int i = 0; i < 30; i++) {
            int x1 = random.nextInt(imgWidth);
            int y1 = random.nextInt(imgHeight);
            graphics.setColor(getRandomColor());
            graphics.fillRect(x1, y1, 2, 2);
        }
        try {
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(verifyImg, "jpeg", outputStream);
            return new Verification(codeBuffer.toString(), outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 随机取色
     */
    private static Color getRandomColor() {
        Random ran = new Random();
        return new Color(ran.nextInt(256),
                ran.nextInt(256), ran.nextInt(256));
    }
}
