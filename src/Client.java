/**
 * Created by cedricxing on 2016/11/22.
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class Client extends JFrame{
    private String client_name;

    private JPanel up_panel;//up panel

    private JPanel set_panel;//login in and set
    private JLabel set_label;
    private ImageIcon set_icon = new ImageIcon("set.png");
    private JLabel user_label;
    private ImageIcon user_icon = new ImageIcon("user.png");

    private JPanel search_panel; //Panel to search
    private JTextField search_field;//Panel to input words
    private JLabel search_label;
    private ImageIcon search_icon = new ImageIcon("search.png");//icon

    private JPanel main_panel;//Panel to Display explaination
    private JPanel explaination_panel;
    private JPanel first_jp;
    private JPanel first_up_panel;
    private JTextArea trans_field_first;
    private JLabel like_label_first;
    private JLabel label_first = new JLabel("");
    private JPanel second_jp;
    private JPanel second_up_panel;
    private JTextArea trans_field_second;
    private JLabel like_label_second;
    private JLabel label_second = new JLabel("");
    private JPanel third_jp;
    private JPanel third_up_panel;
    private JTextArea trans_field_third;
    private JLabel like_label_third;
    private JLabel label_third = new JLabel("");
    private JCheckBox jch_biying;
    private JCheckBox jch_youdao;
    private JCheckBox jch_jinshan;

    private ImageIcon like_icon = new ImageIcon("like.png");
    private ImageIcon like_icon2 = new ImageIcon("like2.png");

    private Border line_border = new LineBorder(Color.LIGHT_GRAY,1);

    private Font font1 = new Font("Times",Font.BOLD,15);

    final private String default_string = new String("search...");

    ObjectOutputStream to_server;
    ObjectInputStream from_server;

    OnlineTranslation onlineTranslation;

    public Client(){
        init_search_panel();
        init_set_panel();
        init_main_panel();
        init_up_panel();

        this.add(up_panel,BorderLayout.NORTH);
        this.add(main_panel,BorderLayout.CENTER);

        this.setTitle("Welcome ");
        this.setSize(800,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        onlineTranslation = new OnlineTranslation();

        try{
            Socket socket = new Socket("localhost",8000);
            to_server = new ObjectOutputStream(socket.getOutputStream());
            from_server = new ObjectInputStream(socket.getInputStream());
        }
        catch (IOException ex){
            System.out.println(ex);
        }
    }

    private void init_set_panel(){
        jch_biying = new JCheckBox("Bing");
        jch_jinshan = new JCheckBox("JinShan");
        jch_youdao = new JCheckBox("YouDao");

        set_panel = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));

        set_label = new JLabel(set_icon);
        user_label = new JLabel(user_icon);

        user_label.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new Client_Login(to_server,from_server);
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        set_panel.add(jch_biying);
        set_panel.add(jch_jinshan);
        set_panel.add(jch_youdao);

        set_panel.add(user_label);
        set_panel.add(set_label);

    }

    private void init_up_panel(){
        up_panel = new JPanel(new GridLayout(1,2));

        up_panel.add(search_panel);
        if(set_panel == null)
            System.out.println("h");
        up_panel.add(set_panel);

    }

    private void init_search_panel(){
        search_panel = new JPanel(new FlowLayout(FlowLayout.CENTER,5,10));

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

        search_field.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                search_begin();
            }
        });


        search_label = new JLabel(search_icon);
        search_label.setBorder(line_border);
        search_label.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e){search_begin();}
            public void mouseEntered(MouseEvent e){}
            public void mouseExited(MouseEvent e){}
            public void mousePressed(MouseEvent e){}
            public void mouseReleased(MouseEvent e){}
        });

        search_panel.add(search_field);
        search_panel.add(search_label);

    }

    private void init_main_panel(){
        main_panel = new JPanel();
        main_panel.setLayout(new BorderLayout(0,0));

        explaination_panel = new JPanel(new GridLayout(1,1));

        first_jp = new JPanel(new BorderLayout());first_jp.setBorder(line_border);
        second_jp = new JPanel(new BorderLayout());second_jp.setBorder(line_border);
        third_jp = new JPanel(new BorderLayout());third_jp.setBorder(line_border);

        trans_field_first = new JTextArea();
        trans_field_first.setLineWrap(true);
        trans_field_third = new JTextArea();
        trans_field_third.setLineWrap(true);
        trans_field_second = new JTextArea();
        trans_field_second.setLineWrap(true);

        first_up_panel = new JPanel(new FlowLayout(FlowLayout.CENTER,20,5));
        second_up_panel = new JPanel(new FlowLayout(FlowLayout.CENTER,20,5));
        third_up_panel = new JPanel(new FlowLayout(FlowLayout.CENTER,20,5));

        like_label_first = new JLabel(like_icon);
        like_label_third = new JLabel(like_icon);
        like_label_second = new JLabel(like_icon);

        like_label_first.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(like_label_first.getIcon() == like_icon) {
                    like_label_first.setIcon(like_icon2);
                    user_liked(1);
                }
                else if(like_label_first.getIcon() == like_icon2){
                    like_label_first.setIcon(like_icon);
                    user_liked_canceled(1);
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        like_label_second.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                like_label_second.setIcon(like_icon2);
                user_liked(2);
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        like_label_third.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                like_label_third.setIcon(like_icon2);
                user_liked(3);
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        first_up_panel.add(label_first);
        first_up_panel.add(like_label_first);
        second_up_panel.add(label_second);
        second_up_panel.add(like_label_second);
        third_up_panel.add(label_third);
        third_up_panel.add(like_label_third);

        first_jp.add(first_up_panel,BorderLayout.NORTH);
        first_jp.add(new JScrollPane(trans_field_first),BorderLayout.CENTER);
        second_jp.add(second_up_panel,BorderLayout.NORTH);
        second_jp.add(new JScrollPane(trans_field_second),BorderLayout.CENTER);
        third_jp.add(third_up_panel,BorderLayout.NORTH);
        third_jp.add(new JScrollPane(trans_field_third),BorderLayout.CENTER);

        explaination_panel.add(first_jp);
        explaination_panel.add(third_jp);
        explaination_panel.add(second_jp);


        main_panel.add(explaination_panel,BorderLayout.CENTER);

        main_panel.setBorder(line_border);
    }

    public void search_begin(){
        ArrayList<String> trans_youdao = onlineTranslation.youdaoTranslation(search_field.getText());
        ArrayList<String> trans_biying = onlineTranslation.bingTranslation(search_field.getText());
        ArrayList<String> trans_jinshan = onlineTranslation.icibaTranslation(search_field.getText());


        StringBuffer result_youdao = new StringBuffer("");
        for(int i = 0;i < trans_youdao.size();++ i){
            result_youdao.append(trans_youdao.get(i)+'\n');
        }
        trans_field_first.setText(result_youdao.toString());
    }

    private void user_liked(int index){

    }

    private void user_liked_canceled(int index){

    }

    public static void main(String[] args){
        new Client();
    }

}
