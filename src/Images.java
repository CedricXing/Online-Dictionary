/**
 * Created by FelixXiao on 2016/12/16.
 */
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.font.*;
import java.awt.geom.*;
import javax.imageio.*;
import java.util.ArrayList;
import sun.awt.*;
import javafx.scene.*;
import java.net.*;

//生成单词卡
public class Images {
    public static void main(String[] args) throws Exception{
        Images images = new Images("a");
    }

    public Images(String word) {
        drawImage(word);
    }

    public void drawImage(String word) {
        String s = word;
        OnlineTranslation onlineTranslation = new OnlineTranslation();
        ArrayList<String> youdaoTrans = mySplit(onlineTranslation.youdaoTranslation(s));
        ArrayList<String> icibaTrans = mySplit(onlineTranslation.icibaTranslation(s));
        ArrayList<String> bingTrans = mySplit(onlineTranslation.bingTranslation(s));
        File file = new File(word + ".png");

        Font font = new Font("Arial", Font.PLAIN, 50);
        //创建一个画布
        BufferedImage bi;
        Graphics2D g2;
        try {
             bi = ImageIO.read(new File("bgp.png"));
             g2 = (Graphics2D)bi.getGraphics();
            g2.setFont(font);


            g2.setPaint(Color.black);


            FontRenderContext context = g2.getFontRenderContext();
            Rectangle2D bounds = font.getStringBounds(s, context);
            double x = (bi.getWidth() - bounds.getWidth()) / 2;
            double y = (bi.getHeight() - bounds.getHeight()) / 2;
            double ascent = -bounds.getY();
            double baseY = y + ascent;

            //绘制字符串
            g2.drawString(s, (int)x, 50);
            g2.setFont(new Font("Tahoma", Font.BOLD, 30));
            g2.drawString("youdao:",10, 100);
            for(int i = 0; i < youdaoTrans.size(); i++) {
                String temp = youdaoTrans.get(i);
                g2.drawString(temp, 10, 150 + i * 50);
            }
            int base = 150 + youdaoTrans.size()*50 + 50;
            g2.drawString("iciba:",10, base);
            for(int i = 0; i < icibaTrans.size(); i++) {
                String temp = icibaTrans.get(i);
                g2.drawString(temp, 10, base + 50 + i * 50);
            }
            int base2 = base + 50 + icibaTrans.size()*50 + 50;
            g2.drawString("bing:",10, base2);
            for(int i = 0; i < bingTrans.size(); i++) {
                String temp = bingTrans.get(i);
                g2.drawString(temp, 10, base2 + 50 + i * 50);
            }
            ImageIO.write(bi, "png", file);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //切分搜索结果，每行30个字符
    public ArrayList<String> mySplit(ArrayList<String> str) {
        ArrayList<String> res = new ArrayList<>();
        for(int i = 0; i < str.size(); i++) {
            int length = str.get(i).length();
            if(length <= 30) {
                res.add(str.get(i));
                continue;
            }
            int base = 0;
            String part;
            while(length > 30) {
                part = str.get(i).substring(base, base + 30);
                res.add(part);
                base += 30;
                length -= 30;
            }
            part = str.get(i).substring(base, base + length);
            res.add(part);
        }
        return res;
    }
}
