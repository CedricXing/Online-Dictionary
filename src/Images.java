/**
 * Created by FelixXiao on 2016/12/16.
 */
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.font.*;
import java.awt.geom.*;
import javax.imageio.*;

public class Images {
    public static void main(String[] args) {
        int width = 600;
        int height = 400;
        String s = "Hello";



        File file = new File("image.jpg");

        Font font = new Font("Arial", Font.PLAIN, 30);
        //创建一个画布
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //获取画布的画笔
        Graphics2D g2 = (Graphics2D)bi.getGraphics();
        g2.setFont(font);

        //开始绘图
        g2.setBackground(Color.WHITE);
        g2.clearRect(0, 0, width, height);
//        g2.setPaint(new Color(0,0,255));
//        g2.fillRect(0, 0, 100, 10);
//        g2.setPaint(new Color(253,2,0));
//        g2.fillRect(0, 10, 100, 10);
        g2.setPaint(Color.black);


        FontRenderContext context = g2.getFontRenderContext();
        Rectangle2D bounds = font.getStringBounds(s, context);
        double x = (width - bounds.getWidth()) / 2;
        double y = (height - bounds.getHeight()) / 2;
        double ascent = -bounds.getY();
        double baseY = y + ascent;

        //绘制字符串
        g2.drawString(s, (int)x, 30);

        try {
            //将生成的图片保存为jpg格式的文件。ImageIO支持jpg、png、gif等格式
            ImageIO.write(bi, "jpg", file);
        } catch (IOException e) {
            System.out.println("生成图片出错........");
            e.printStackTrace();
        }
    }
}
