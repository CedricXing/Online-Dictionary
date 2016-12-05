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
    private JPanel jp_youdao;
    private JPanel jp_jinshan;
    private JCheckBox jch_biying;
    private JCheckBox jch_youdao;
    private JCheckBox jch_jinshan;

    private Border line_border = new LineBorder(Color.LIGHT_GRAY,1);

    private Font font1 = new Font("Times",Font.BOLD,15);

    final private String default_string = new String("search...");

    ObjectOutputStream to_server;
    ObjectInputStream from_server;

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

        search_field.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        String word = search_field.getText();
                        //to_server.writeObject(word);
                        //to_server.flush();
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            }


            @Override
            public void keyReleased(KeyEvent e){
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        try {
                            String word = search_field.getText();
                            //to_server.writeObject(word);
                            //to_server.flush();
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                    }
                }
        });


        search_label = new JLabel(search_icon);
        search_label.setBorder(line_border);
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

        jp_biying = new JPanel();jp_biying.setBorder(line_border);
        jp_youdao = new JPanel();jp_youdao.setBorder(line_border);
        jp_jinshan = new JPanel();jp_jinshan.setBorder(line_border);

        explaination_panel.add(jp_biying);
        explaination_panel.add(jp_jinshan);
        explaination_panel.add(jp_youdao);


        main_panel.add(check_box,BorderLayout.NORTH);
        main_panel.add(explaination_panel,BorderLayout.CENTER);

        main_panel.setBorder(line_border);
    }

    public static void main(String[] args){
        new Client();
    }

}
