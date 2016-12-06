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
    //private ImageIcon add_client_icon = new ImageIcon("add_client.png");//icon

    private JPanel main_panel;//Panel to Display explaination
    private JPanel explaination_panel;
    private JPanel check_box;
    private JPanel jp_biying;
    private JPanel biying_up_panel;
    private JTextArea trans_field_biying;
    private JLabel like_label_biying;
    private JLabel label_biying = new JLabel("Bing");
    private JPanel jp_youdao;
    private JPanel youdao_up_panel;
    private JTextArea trans_field_youdao;
    private JLabel like_label_youdao;
    private JLabel label_youdao = new JLabel("Youdao");
    private JPanel jp_jinshan;
    private JPanel jinshan_up_panel;
    private JTextArea trans_field_jinshan;
    private JLabel like_label_jinshan;
    private JLabel label_jinshan = new JLabel("Jinshan");
    private JCheckBox jch_biying;
    private JCheckBox jch_youdao;
    private JCheckBox jch_jinshan;

    private ImageIcon like_icon = new ImageIcon("like.png");

    private Border line_border = new LineBorder(Color.LIGHT_GRAY,1);

    private Font font1 = new Font("Times",Font.BOLD,15);

    final private String default_string = new String("search...");

    ObjectOutputStream to_server;
    ObjectInputStream from_server;

    OnlineTranslation onlineTranslation;

    public Client(){
        //this.to_server = to_server;
        //this.from_server = from_server;

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
        set_panel = new JPanel(new FlowLayout(FlowLayout.RIGHT,5,10));

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

        check_box = new JPanel(new FlowLayout(FlowLayout.CENTER,180,10));
        jch_biying = new JCheckBox("BiYing");
        jch_jinshan = new JCheckBox("JinShan");
        jch_youdao = new JCheckBox("YouDao");

        check_box.add(jch_biying);
        check_box.add(jch_jinshan);
        check_box.add(jch_youdao);
        check_box.setBorder(line_border);

        explaination_panel = new JPanel(new GridLayout(1,1));

        jp_biying = new JPanel(new BorderLayout());jp_biying.setBorder(line_border);
        jp_youdao = new JPanel(new BorderLayout());jp_youdao.setBorder(line_border);
        jp_jinshan = new JPanel(new BorderLayout());jp_jinshan.setBorder(line_border);

        trans_field_biying = new JTextArea();
        trans_field_jinshan = new JTextArea();
        trans_field_youdao = new JTextArea();

        biying_up_panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        youdao_up_panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jinshan_up_panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        like_label_biying = new JLabel(like_icon);
        like_label_jinshan = new JLabel(like_icon);
        like_label_youdao = new JLabel(like_icon);

        biying_up_panel.add(label_biying);
        biying_up_panel.add(like_label_biying);
        youdao_up_panel.add(label_youdao);
        youdao_up_panel.add(like_label_youdao);
        jinshan_up_panel.add(label_jinshan);
        jinshan_up_panel.add(like_label_jinshan);

        jp_biying.add(biying_up_panel,BorderLayout.NORTH);
        jp_biying.add(trans_field_biying,BorderLayout.CENTER);
        jp_youdao.add(youdao_up_panel,BorderLayout.NORTH);
        jp_youdao.add(trans_field_youdao,BorderLayout.CENTER);
        jp_jinshan.add(jinshan_up_panel,BorderLayout.NORTH);
        jp_jinshan.add(trans_field_jinshan,BorderLayout.CENTER);

        explaination_panel.add(jp_biying);
        explaination_panel.add(jp_jinshan);
        explaination_panel.add(jp_youdao);


        main_panel.add(check_box,BorderLayout.NORTH);
        main_panel.add(explaination_panel,BorderLayout.CENTER);

        main_panel.setBorder(line_border);
    }

    public void search_begin(){
        ArrayList<String> trans_youdao = onlineTranslation.youdaoTranslation(search_field.getText());
        ArrayList<String> trans_biying = onlineTranslation.bingTranslation(search_field.getText());
        ArrayList<String> trans_jinshan = onlineTranslation.icibaTranslation(search_field.getText());


        for(int i = 0;i < trans_youdao.size();++ i){
            System.out.println(trans_youdao.get(i));
        }
        //System.out.println(trans_youdao);
        //System.out.println(trans_biying);
        //System.out.println(trans_jinshan);
    }

    public static void main(String[] args){
        new Client();
    }

}
