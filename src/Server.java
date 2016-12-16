/**
 * Created by cedricxing on 2016/11/22.
 */
import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    private DataBaseConnectivity dataBaseConnectivity;
    private Map<Integer, ObjectOutputStream> userOutputStream;
    //
    public Server(){
        dataBaseConnectivity = new DataBaseConnectivity();
        userOutputStream = new HashMap<Integer, ObjectOutputStream>();
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
                        int id = register(info[1], info[2]);
                        String reply = new String("register:" + "success:" + id + ":" + dataBaseConnectivity.getNameByID(id));
                        outputToClient.writeObject(reply);
                        outputToClient.flush();
                        System.out.println("User = " + id + " register successfully");
                        userOutputStream.put(new Integer(id), outputToClient);
                    }
                    else if(info[0].equals("login")){
                        int id = Integer.parseInt(info[1]);
                        String reply;
                        if(login(id, info[2])) {
                            reply = new String("login:success:" + dataBaseConnectivity.getNameByID(id));
                        }
                        else {
                            reply = new String("login:fail");
                        }
                        outputToClient.writeObject(reply);
                        outputToClient.flush();
                        System.out.println("User = " + id + " " + reply);
                        userOutputStream.put(new Integer(id), outputToClient);
                    }
                    else if(info[0].equals("search")) {
                        int[] likeInfo = search(info[1]);
                        String reply = new String(info[0] + ":" + likeInfo[0] + ":" +likeInfo[1] + ":" + likeInfo[2]);
                        outputToClient.writeObject(reply);
                        outputToClient.flush();
                        System.out.println("word: " + likeInfo[0] + " " + likeInfo[1] + " " +likeInfo[2]);
                    }
                    else if(info[0].equals("like")) {
                        like(info[1], info[2]);
                    }
                    else if(info[0].equals("unlike")) {
                        unlike(info[1], info[2]);
                    }
                }
            }
            catch (EOFException eofe) {
                System.out.println("断开连接");
            }
            catch (Exception e){
                System.out.println(e);
            }
        }

        public int register(String name, String password) {
            int ID = dataBaseConnectivity.distrubuteID();
            dataBaseConnectivity.insertUserData(ID, name, password);
            return ID;
        }

        public boolean login(int id, String password) {
            if(!dataBaseConnectivity.isIDExist(id))
                return false;
            String truePassword = dataBaseConnectivity.getPasswordByID(id);
//        System.out.println(truePassword);
            if(!truePassword.equals(password))
                return false;
            return true;
        }

        public int[] search(String word) {
            int[] result = dataBaseConnectivity.SearchLikeInfo(word);
            return result;
        }

        public void like(String word, String name) {
            dataBaseConnectivity.addLikeInfo(word, name);
        }

        public void unlike(String word, String name) {
            dataBaseConnectivity.addUnlikeInfo(word, name);
        }
    }

}
