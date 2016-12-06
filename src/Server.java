/**
 * Created by cedricxing on 2016/11/22.
 */
import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    //
    public Server(){

        try{
            ServerSocket serversocket = new ServerSocket(8000);//port number 8000

            //int client_no = 1;

            while(true){
                Socket socket = serversocket.accept();

                InetAddress inetAddress = socket.getInetAddress();

                HandleClient client_task = new HandleClient(socket);

                new Thread(client_task).start();

            }

        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    public static void main(String[] args){
        new Server();
    }
}

class HandleClient implements Runnable{
    private Socket socket;

    public HandleClient(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run(){
        try{
            ObjectInputStream inputFromClient = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputToClient = new ObjectOutputStream(socket.getOutputStream());

            while(true){
                String mess = (String)inputFromClient.readObject();
                String []info = mess.split("[:]");
                if(info[0].equals("register")){

                }
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}