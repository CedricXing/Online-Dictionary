/**
 * Created by cedricxing on 2016/11/22.
 */

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class Client extends JFrame{
    private JPanel search_panel; //Panel to search
    private JTextField search_field;//Panel to input words
    private JLabel search_label;
    private ImageIcon search_icon = new ImageIcon("search.png");//icon
    //private ImageIcon add_client_icon = new ImageIcon("add_client.png");//icon

    private JPanel main_panel;//Panel to Display explaination
    private JPanel explaination_panel;
    private JPanel check_box;
    private JPanel jp_baidu;
    private JPanel jp_youdao;
    private JPanel jp_google;
    private JCheckBox jch_baidu;
    private JCheckBox jch_youdao;
    private JCheckBox jch_google;

    private Border line_border = new LineBorder(Color.LIGHT_GRAY,1);

    private Font font1 = new Font("Times",Font.BOLD,15);

    final private String default_string = new String("search...");

    public Client(){
        init_search_panel();
        init_main_panel();

        this.add(search_panel,BorderLayout.NORTH);
        this.add(main_panel,BorderLayout.CENTER);

        this.setTitle("Online Dictionary");
        this.setSize(800,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private void init_search_panel(){
        search_panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        search_field = new JTextField(default_string,20);//set default string
        search_field.setForeground(Color.black);//set color
        search_field.setFont(font1);

        search_field.addMouseListener(new MouseListener(){
            public void mouseClicked(MouseEvent e){
                if(search_field.getText().equals(default_string))
                    search_field.setText("");
            }
            public void mouseEntered(MouseEvent e){}
            public void mouseExited(MouseEvent e){}
            public void mousePressed(MouseEvent e){}
            public void mouseReleased(MouseEvent e){}
        });

        search_label = new JLabel(search_icon);
        search_label.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e){
                System.out.println(search_field.getText());
            }
            public void mouseEntered(MouseEvent e){}
            public void mouseExited(MouseEvent e){}
            public void mousePressed(MouseEvent e){}
            public void mouseReleased(MouseEvent e){}
        });

        search_panel.add(search_field);
        search_panel.add(search_label);
        //search_panel.add(new JLabel(add_client_icon));

        search_panel.setBorder(line_border);
    }

    private void init_main_panel(){
        main_panel = new JPanel();
        main_panel.setLayout(new BorderLayout(0,0));

        check_box = new JPanel(new FlowLayout(FlowLayout.CENTER,180,10));
        jch_baidu = new JCheckBox("BaiDu");
        jch_google = new JCheckBox("Google");
        jch_youdao = new JCheckBox("YouDao");

        check_box.add(jch_baidu);
        check_box.add(jch_google);
        check_box.add(jch_youdao);
        check_box.setBorder(line_border);

        explaination_panel = new JPanel(new GridLayout(1,1));

        jp_baidu = new JPanel();jp_baidu.setBorder(line_border);
        jp_youdao = new JPanel();jp_youdao.setBorder(line_border);
        jp_google = new JPanel();jp_google.setBorder(line_border);

        explaination_panel.add(jp_baidu);
        explaination_panel.add(jp_google);
        explaination_panel.add(jp_youdao);


        main_panel.add(check_box,BorderLayout.NORTH);
        main_panel.add(explaination_panel,BorderLayout.CENTER);

        main_panel.setBorder(line_border);
    }

    public static void main(String[] args){
        new Client();
    }

}
