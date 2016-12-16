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
    private String client_name = new String("");

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
    private JLabel like_num_first = new JLabel("");
    private JPanel second_jp;
    private JPanel second_up_panel;
    private JTextArea trans_field_second;
    private JLabel like_label_second;
    private JLabel label_second = new JLabel("");
    private JLabel like_num_second = new JLabel("");
    private JPanel third_jp;
    private JPanel third_up_panel;
    private JTextArea trans_field_third;
    private JLabel like_label_third;
    private JLabel label_third = new JLabel("");
    private JLabel like_num_third = new JLabel("");
    private JCheckBox jch_biying;
    private JCheckBox jch_youdao;
    private JCheckBox jch_jinshan;

    private String first;
    private String second;
    private String third;
    private String youdao = new String("youdao");
    private String biying = new String("bing");
    private String jinshan = new String("iciba");

    private ImageIcon like_icon = new ImageIcon("like.png");
    private ImageIcon like_icon2 = new ImageIcon("like2.png");

    private Border line_border = new LineBorder(Color.LIGHT_GRAY,1);

    private Font font1 = new Font("Times",Font.BOLD,15);

    final private String default_string = new String("search...");

    ObjectOutputStream to_server;
    ObjectInputStream from_server;

    OnlineTranslation onlineTranslation;

    boolean is_online = false;

    Client_Login client_login;

    ArrayList<String> trans_youdao ;
    ArrayList<String> trans_biying ;
    ArrayList<String> trans_jinshan;

    int panel_num = 0;

    String current_word;

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
            Socket socket = new Socket("114.212.131.80",8000);
            to_server = new ObjectOutputStream(socket.getOutputStream());
            from_server = new ObjectInputStream(socket.getInputStream());
            while(true){
                try {
                    String reply = (String)from_server.readObject();
                    String[] reply_processed = reply.split("[:]");
                    if(reply_processed[0].equals("register")) {
                        if (reply_processed[1].equals("success")) {
                            JOptionPane.showMessageDialog(null, "Register Success! Your ID is " + reply_processed[2] + " ,please remember it!");
                            is_online = true;
                            client_name = new String(reply_processed[3]);
                            this.setTitle("Welcome " + client_name);
                            client_login.dispose();
                        } else {
                            JOptionPane.showMessageDialog(null, "Sorry! Register Fail!");
                        }
                    }
                    else if(reply_processed[0].equals("login")){
                        if(reply_processed[1].equals("success")) {
                            is_online = true;
                            JOptionPane.showMessageDialog(null, "Login success!");
                            client_name = new String(reply_processed[2]);
                            this.setTitle("Welcome " + client_name);
                            client_login.dispose();
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "ID or password Wrong!");
                        }
                    }
                    else if(reply_processed[0].equals("search")){
                        display_search_result(reply_processed);
                    }
                }
                catch (Exception ex){
                    System.out.println(ex);
                }

            }
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
                System.out.println(is_online);
                if(!is_online) {
                    client_login = new Client_Login(to_server, from_server);
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

        set_panel.add(jch_biying);
        set_panel.add(jch_jinshan);
        set_panel.add(jch_youdao);

        set_panel.add(user_label);
        set_panel.add(set_label);

    }

    private void init_up_panel(){
        up_panel = new JPanel(new GridLayout(1,2));

        up_panel.add(search_panel);
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
                if(panel_num < 1)
                    return;
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
                if(panel_num < 2)
                    return;
                if(like_label_second.getIcon() == like_icon) {
                    like_label_second.setIcon(like_icon2);
                    user_liked(2);
                }
                else if(like_label_second.getIcon() == like_icon2){
                    like_label_second.setIcon(like_icon);
                    user_liked_canceled(2);
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

        like_label_third.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(panel_num < 3)
                    return;
                if(like_label_third.getIcon() == like_icon) {
                    like_label_third.setIcon(like_icon2);
                    user_liked(3);
                }
                else if(like_label_third.getIcon() == like_icon2){
                    like_label_third.setIcon(like_icon);
                    user_liked_canceled(3);
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

        first_up_panel.add(label_first);
        first_up_panel.add(like_label_first);
        first_up_panel.add(like_num_first);
        second_up_panel.add(label_second);
        second_up_panel.add(like_label_second);
        second_up_panel.add(like_num_second);
        third_up_panel.add(label_third);
        third_up_panel.add(like_label_third);
        third_up_panel.add(like_num_third);

        first_jp.add(first_up_panel,BorderLayout.NORTH);
        first_jp.add(new JScrollPane(trans_field_first),BorderLayout.CENTER);
        second_jp.add(second_up_panel,BorderLayout.NORTH);
        second_jp.add(new JScrollPane(trans_field_second),BorderLayout.CENTER);
        third_jp.add(third_up_panel,BorderLayout.NORTH);
        third_jp.add(new JScrollPane(trans_field_third),BorderLayout.CENTER);

        explaination_panel.add(first_jp);
        explaination_panel.add(second_jp);
        explaination_panel.add(third_jp);


        main_panel.add(explaination_panel,BorderLayout.CENTER);

        main_panel.setBorder(line_border);
    }

    public void search_begin(){
        trans_youdao = onlineTranslation.youdaoTranslation(search_field.getText());
        trans_biying = onlineTranslation.bingTranslation(search_field.getText());
        trans_jinshan = onlineTranslation.icibaTranslation(search_field.getText());

        current_word = search_field.getText();

        try{
            String word_mes = new String("search:" + search_field.getText());
            to_server.writeObject(word_mes);
            to_server.flush();
        }
        catch (Exception e){
            System.out.println(e);
        }

    }

    private void display_search_result(String []reply){
        label_first.setText("");
        like_num_first.setText("");
        label_second.setText("");
        like_num_second.setText("");
        label_third.setText("");
        like_num_third.setText("");
        trans_field_first.setText("");
        trans_field_second.setText("");
        trans_field_third.setText("");
        like_label_first.setIcon(like_icon);
        like_label_second.setIcon(like_icon);
        like_label_third.setIcon(like_icon);
        panel_num = 0;

        StringBuffer result_youdao = new StringBuffer("");
        for(int i = 0;i < trans_youdao.size();++i){
            result_youdao.append(trans_youdao.get(i) + '\n');
        }
        StringBuffer result_biying = new StringBuffer("");
        for(int i = 0;i < trans_biying.size();++i){
            result_biying.append(trans_biying.get(i) + '\n');
        }
        StringBuffer result_jinshan = new StringBuffer("");
        for(int i = 0;i < trans_jinshan.size();++i){
            result_jinshan.append(trans_jinshan.get(i) + '\n');
        }

        int num_youdao = Integer.parseInt(reply[1]);
        int num_jinshan = Integer.parseInt(reply[2]);
        int num_biying = Integer.parseInt(reply[3]);

        if(jch_youdao.isSelected() && jch_biying.isSelected() && jch_jinshan .isSelected()) {
            panel_num = 3;
            if (num_youdao >= num_jinshan && num_youdao >= num_biying) {
                label_first.setText("Youdao");
                trans_field_first.setText(result_youdao.toString());
                first = youdao;
                like_num_first.setText(reply[1]);
                if (num_jinshan >= num_biying) {
                    label_second.setText("Jinshan");
                    trans_field_second.setText(result_jinshan.toString());
                    label_third.setText("Bing");
                    trans_field_third.setText(result_biying.toString());
                    second = jinshan;
                    like_num_second.setText(reply[2]);
                    third = biying;
                    like_num_third.setText(reply[3]);
                } else {
                    label_second.setText("Bing");
                    trans_field_second.setText(result_biying.toString());
                    label_third.setText("Jinshan");
                    trans_field_third.setText(result_jinshan.toString());
                    second = biying;
                    like_num_second.setText(reply[3]);
                    third = jinshan;
                    like_num_third.setText(reply[2]);
                }
            } else if (num_biying >= num_youdao && num_biying >= num_jinshan) {
                label_first.setText("Bing");
                trans_field_first.setText(result_biying.toString());
                first = biying;
                like_num_first.setText(reply[3]);
                if (num_youdao >= num_jinshan) {
                    label_second.setText("Youdao");
                    trans_field_second.setText(result_youdao.toString());
                    label_third.setText("Jinshan");
                    trans_field_third.setText(result_jinshan.toString());
                    second = youdao;
                    like_num_second.setText(reply[1]);
                    third = jinshan;
                    like_num_third.setText(reply[2]);
                } else {
                    label_second.setText("Jinshan");
                    trans_field_second.setText(result_jinshan.toString());
                    label_third.setText("Youdao");
                    trans_field_third.setText(result_youdao.toString());
                    second = jinshan;
                    like_num_second.setText(reply[2]);
                    third = youdao;
                    like_num_third.setText(reply[1]);
                }
            } else {
                label_first.setText("Jinshan");
                trans_field_first.setText(result_jinshan.toString());
                first = jinshan;
                like_num_first.setText(reply[2]);
                if (num_youdao >= num_biying) {
                    label_second.setText("Youdao");
                    trans_field_second.setText(result_youdao.toString());
                    label_third.setText("Bing");
                    trans_field_third.setText(result_biying.toString());
                    second = youdao;
                    like_num_second.setText(reply[1]);
                    third = biying;
                    like_num_third.setText(reply[3]);
                } else {
                    label_second.setText("Bing");
                    trans_field_second.setText(result_biying.toString());
                    label_third.setText("Youdao");
                    trans_field_third.setText(result_youdao.toString());
                    second = biying;
                    like_num_second.setText(reply[3]);
                    third = youdao;
                    like_num_third.setText(reply[1]);
                }
            }
        }
        else if(jch_biying.isSelected() && jch_jinshan.isSelected()){
            panel_num = 2;
            if(num_biying >= num_jinshan){
                label_first.setText("Bing");
                trans_field_first.setText(result_biying.toString());
                label_second.setText("Jinshan");
                trans_field_second.setText(result_jinshan.toString());
                first = biying;
                like_num_first.setText(reply[3]);
                second = jinshan;
                like_num_second.setText(reply[2]);
            }
            else {
                label_first.setText("Jinshan");
                trans_field_first.setText(result_jinshan.toString());
                label_second.setText("Bing");
                trans_field_second.setText(result_biying.toString());
                first = jinshan;
                like_num_first.setText(reply[2]);
                second = biying;
                like_num_second.setText(reply[3]);
            }
        }
        else if(jch_biying.isSelected() && jch_youdao.isSelected()){
            panel_num = 2;
            if(num_biying >= num_youdao){
                label_first.setText("Bing");
                trans_field_first.setText(result_biying.toString());
                label_second.setText("Youdao");
                trans_field_second.setText(result_youdao.toString());
                first = biying;
                like_num_first.setText(reply[3]);
                second = youdao;
                like_num_second.setText(reply[1]);
            }
            else {
                label_first.setText("Youdao");
                trans_field_first.setText(result_youdao.toString());
                label_second.setText("Bing");
                trans_field_second.setText(result_biying.toString());
                first = youdao;
                like_num_first.setText(reply[1]);
                second = biying;
                like_num_second.setText(reply[3]);
            }
        }
        else if(jch_jinshan.isSelected() && jch_youdao.isSelected()){
            panel_num = 2;
            if(num_jinshan >= num_youdao){
                label_first.setText("Jinshan");
                trans_field_first.setText(result_jinshan.toString());
                label_second.setText("Youdao");
                trans_field_second.setText(result_youdao.toString());
                first = jinshan;
                like_num_first.setText(reply[2]);
                second = youdao;
                like_num_second.setText(reply[1]);
            }
            else {
                label_first.setText("Youdao");
                trans_field_first.setText(result_youdao.toString());
                label_second.setText("Jinshan");
                trans_field_second.setText(result_jinshan.toString());
                first = youdao;
                like_num_first.setText(reply[1]);
                second = jinshan;
                like_num_second.setText(reply[2]);
            }
        }
        else if(jch_jinshan.isSelected()){
            panel_num = 1;
            label_first.setText("Jinshan");
            trans_field_first.setText(result_jinshan.toString());
            first = jinshan;
            like_num_first.setText(reply[2]);
        }
        else if(jch_youdao.isSelected()){
            panel_num = 1;
            label_first.setText("youdao");
            trans_field_first.setText(result_youdao.toString());
            first = youdao;
            like_num_first.setText(reply[1]);
        }
        else if(jch_biying.isSelected()){
            panel_num = 1;
            label_first.setText("Bing");
            trans_field_first.setText(result_biying.toString());
            first = biying;
            like_num_first.setText(reply[3]);
        }
    }

    private void user_liked(int index){
        String buf;
        if(index == 1) {
            buf = first;
            int temp = Integer.parseInt(like_num_first.getText());
            temp += 1;
            like_num_first.setText(Integer.toString(temp));
        }
        else if(index == 2) {
            buf = second;
            int temp = Integer.parseInt(like_num_second.getText());
            temp += 1;
            like_num_second.setText(Integer.toString(temp));
        }
        else {
            buf = third;
            int temp = Integer.parseInt(like_num_third.getText());
            temp += 1;
            like_num_third.setText(Integer.toString(temp));
        }
        String mes = new String("like:" + current_word + ":" + buf);
        try {
            to_server.writeObject(mes);
            to_server.flush();
        }
        catch (IOException ex){
            System.out.println(ex);
        }
    }

    private void user_liked_canceled(int index){
        String buf;
        if(index == 1) {
            buf = first;
            int temp = Integer.parseInt(like_num_first.getText());
            temp -= 1;
            like_num_first.setText(Integer.toString(temp));
        }
        else if(index == 2) {
            buf = second;
            int temp = Integer.parseInt(like_num_second.getText());
            temp -= 1;
            like_num_second.setText(Integer.toString(temp));
        }
        else {
            buf = third;
            int temp = Integer.parseInt(like_num_third.getText());
            temp -= 1;
            like_num_third.setText(Integer.toString(temp));
        }
        String mes = new String("unlike:" + current_word + ":" + buf);
        try {
            to_server.writeObject(mes);
            to_server.flush();
        }
        catch (IOException ex){
            System.out.println(ex);
        }
    }


    class Client_Login extends JFrame{
        private String client_name;//User name
        private String client_password;//User password
        private String client_password_confirmed;//User password confirm

        private JPanel head_pic;//User head portrait
        private JLabel head_port;
        private ImageIcon head_portrait = new ImageIcon("head_portrait2.png");

        private JPanel login;
        private JPanel name_panel;
        private JLabel user_name_hint;
        private JTextField user_name;
        private JPanel password_panel;
        private JLabel user_password_hint;
        private JPasswordField user_password;
        private JLabel password_confirmed_hint;
        private JPasswordField password_confirmed;
        private JPanel password_confirm_panel;


        private JPanel options;//sign in or sign out
        private JButton sign_in;
        private JButton sign_up;
        private JButton sign_up_ready;

        private Border line_border = new LineBorder(Color.LIGHT_GRAY,1);
        private Border line_border_bold = new LineBorder(Color.GRAY,1);

        private Font font1 = new Font("Times",Font.PLAIN,15);
        private Font font2 = new Font("Times",Font.BOLD,20);

        ObjectInputStream from_server;
        ObjectOutputStream to_server;

        public Client_Login(ObjectOutputStream to_server,ObjectInputStream from_server){
            this.to_server = to_server;
            this.from_server = from_server;


            init_head_pic();
            init_login();
            init_options();

            this.setLayout(new GridLayout(3,1));
            this.add(head_pic);
            this.add(login);
            this.add(options);

            this.setTitle("Online Dictionary");
            this.setSize(400,300);
            this.setLocationRelativeTo(null);
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.setVisible(true);

        }

        private void init_head_pic(){
            head_pic = new JPanel(new FlowLayout(FlowLayout.CENTER,0,15));

            head_port = new JLabel(head_portrait);

            head_pic.add(head_port);
        }

        private void init_login(){
            login = new JPanel(new GridLayout(2,1,0,15));

            user_name_hint = new JLabel("ID          ");
            user_name_hint.setFont(font1);
            user_name = new JTextField(15);
            user_password_hint = new JLabel("Password ");
            user_password_hint.setFont(font1);
            user_password = new JPasswordField(15);
            password_listener();

            name_panel = new JPanel(new FlowLayout(FlowLayout.CENTER,5,0));
            name_panel.add(user_name_hint);
            name_panel.add(user_name);

            password_panel = new JPanel(new FlowLayout(FlowLayout.CENTER,5,0));
            password_panel.add(user_password_hint);
            password_panel.add(user_password);

            login.add(name_panel);
            login.add(password_panel);

        }

        private void init_options(){
            options = new JPanel(new FlowLayout(FlowLayout.CENTER,80,20));

            sign_in = new JButton("Sign in");
            sign_up = new JButton("Sign up");

            sign_in.setBorder(line_border_bold);
            sign_up.setBorder(line_border_bold);

            sign_in.setFont(font2);
            sign_up.setFont(font2);

            sign_up.setOpaque(true);
            sign_up.setBackground(Color.GREEN);
            //sign_up.setForeground(Color.WHITE);

            sign_in.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

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

            sign_up.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    options.remove(sign_in);
                    options.remove(sign_up);

                    init_sign_up_ready();

                    user_name_hint.setText("User Name");
                    options.add(sign_up_ready);

                    options.repaint();
                    options.revalidate();
                    options.updateUI();

                    password_confirm_panel = new JPanel(new FlowLayout(FlowLayout.CENTER,5,0));
                    password_confirmed_hint = new JLabel("Confirm   ");
                    password_confirmed_hint.setFont(font1);
                    password_confirmed = new JPasswordField(15);
                    password_confirmed_listener();
                    password_confirm_panel.add(password_confirmed_hint);
                    password_confirm_panel.add(password_confirmed);

                    login.remove(name_panel);
                    login.remove(password_panel);

                    login.setLayout(new GridLayout(3,1,0,5));
                    login.add(name_panel);
                    login.add(password_panel);
                    login.add(password_confirm_panel);

                    login.repaint();
                    login.revalidate();
                    login.updateUI();

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

            options.add(sign_in);
            options.add(sign_up);
        }

        private void init_sign_up_ready(){
            sign_up_ready = new JButton("Sign up");
            sign_up_ready.setBorder(line_border_bold);
            sign_up_ready.setFont(font2);
            sign_up_ready.setOpaque(true);
            sign_up_ready.setBackground(Color.GREEN);


            sign_up_ready.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(user_password.getText().equals(password_confirmed.getText())){
                        register();
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


        }

        private void client_start(){
            try{
                String mess = new String("hello");
                to_server.writeObject(mess);
                to_server.flush();
            }
            catch (IOException e){
                System.out.println(e);
            }

        }

        private void password_listener(){
            user_password.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    login();
                }
            });
        }

        private void password_confirmed_listener(){
            password_confirmed.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(user_password.getText().equals(password_confirmed.getText())){
                        register();
                    }
                }
            });
        }

        private void register(){
            try{
                String register_mess = new String("register:" + user_name.getText() + ":" + user_password.getText());
                to_server.writeObject(register_mess);
                to_server.flush();

            }
            catch (Exception e){
                System.out.println(e);
            }
        }

        private void login(){
            try{
                String login_mess = new String("login:" + user_name.getText() + ":" + user_password.getText());
                to_server.writeObject(login_mess);
                to_server.flush();
            }
            catch (Exception e){
                System.out.println(e);
            }
        }
    }

    public static void main(String[] args){
        new Client();
    }

}
