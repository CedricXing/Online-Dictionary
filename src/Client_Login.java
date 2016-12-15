import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.server.ExportException;

/**
 * Created by cedricxing on 2016/11/25.
 */
public class Client_Login extends JFrame{
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
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private void init_head_pic(){
        head_pic = new JPanel(new FlowLayout(FlowLayout.CENTER,0,15));

        head_port = new JLabel(head_portrait);

        head_pic.add(head_port);
    }

    private void init_login(){
        login = new JPanel(new GridLayout(2,1,0,15));

        user_name_hint = new JLabel("User         ");
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
        catch (IOException e){
            System.out.println(e);
        }
    }

    private void login(){
        try{
            String login_mess = new String("login:" + user_name.getText() + ":" + user_password.getText());
            to_server.writeObject(login_mess);
            to_server.flush();
            String mess = (String)from_server.readObject();
            if(mess.equals("true")){

            }
            else{
                JOptionPane.showMessageDialog(null,"Wrong!");
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public static void main(String[] args){
        //new Client_Login();
    }
}

