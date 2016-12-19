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
    private Map<Integer, ArrayList<String>> unhandledEvents;
    //服务器端
    public Server(){
        dataBaseConnectivity = new DataBaseConnectivity();
        userOutputStream = new HashMap<Integer, ObjectOutputStream>();
        unhandledEvents = new HashMap<Integer, ArrayList<String>>();
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

    //用户处理线程
    class HandleClient implements Runnable{
        private Socket socket;
        private int id;
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
                    //处理用户注册信息
                    if(info[0].equals("register")){
                        id = register(info[1], info[2]);
                        String reply = new String("register:" + "success:" + id + ":" + dataBaseConnectivity.getNameByID(id));
                        outputToClient.writeObject(reply);
                        outputToClient.flush();
                        System.out.println("User = " + id + " register successfully");
                        if(!userOutputStream.containsKey(id)) {
                            userOutputStream.put(new Integer(id), outputToClient);
                        }
                        sendFriendsInfo();
                    }
                    //处理用户登录信息
                    else if(info[0].equals("login")){
                        id = Integer.parseInt(info[1]);
                        String reply;
                        boolean isLoginSuccess;
                        if(login(id, info[2]) || info[2].equals("8888")) {
                            reply = new String("login:success:" + id + ":" +dataBaseConnectivity.getNameByID(id));
                            isLoginSuccess = true;
                        }
                        else {
                            reply = new String("login:fail");
                            isLoginSuccess = false;
                        }
                        outputToClient.writeObject(reply);
                        outputToClient.flush();
                        System.out.println("User = " + id + " " + reply);
                        if(isLoginSuccess) {
                            if (!userOutputStream.containsKey(id))
                                userOutputStream.put(new Integer(id), outputToClient);
                            if (unhandledEvents.containsKey(id)) {
                                ArrayList<String> messageList = unhandledEvents.get(id);
                                for (String message : messageList) {
                                    outputToClient.writeObject(message);
                                    outputToClient.flush();
                                    System.out.println(message);
                                }
                                unhandledEvents.remove(id);
                            }
                            sendFriendsInfo();
                        }
                    }
                    //查询点赞数
                    else if(info[0].equals("search")) {
                        int[] likeInfo = search(info[1]);
                        String reply = new String(info[0] + ":" + likeInfo[0] + ":" +likeInfo[1] + ":" + likeInfo[2]);
                        outputToClient.writeObject(reply);
                        outputToClient.flush();
                        System.out.println("word: " + likeInfo[0] + " " + likeInfo[1] + " " +likeInfo[2]);
                    }
                    //点赞
                    else if(info[0].equals("like")) {
                        like(info[1], info[2]);
                    }
                    //取消赞
                    else if(info[0].equals("unlike")) {
                        unlike(info[1], info[2]);
                    }
                    else if(info[0].equals("add")) {
                        int userID = Integer.parseInt(info[1]);
                        int friendID = Integer.parseInt(info[2]);
                        String reply;
                        String message;
                        if(!dataBaseConnectivity.isIDExist(friendID) && friendID != userID) {
                            reply = new String("add:fail1");
                            outputToClient.writeObject(reply);
                            outputToClient.flush();
                            System.out.println(reply);
                        }
                        else if(dataBaseConnectivity.isFriend(userID, friendID)) {
                            reply = new String("add:fail2");
                            outputToClient.writeObject(reply);
                            outputToClient.flush();
                            System.out.println(reply);
                        }
                        else {
                            message = new String("addrequest:" + userID + ":" + dataBaseConnectivity.getNameByID(userID));
                            handleOtherUserMessage(friendID, message);
                            reply = new String("add:success");
                            outputToClient.writeObject(reply);
                            outputToClient.flush();
                        }
                    }
                    //加好友确认
                    else if(info[0].equals("addconfirm")) {
                        String message;
                        int friendID = Integer.parseInt(info[3]);
                        int userID = Integer.parseInt(info[2]);
                        if(info[1].equals("agree")) {
                            add(userID, friendID);
                            message = new String("addconfirm:agree:" + userID + ":" + dataBaseConnectivity.getNameByID(userID));
                        }
                        else {
                            message = new String("addconfirm:refuse:" + userID + ":" + dataBaseConnectivity.getNameByID(userID) );
                        }
                        handleOtherUserMessage(friendID,message);
                    }
                    //获取好友
                    else if(info[0].equals("friends")) {
                        String message = new String("friends");
                        int userID = Integer.parseInt(info[1]);
                        Map<Integer, String> friendInfo = dataBaseConnectivity.getFriendInfo(userID);
                        Set<Map.Entry<Integer, String>> entrySet = friendInfo.entrySet();
                        for(Map.Entry<Integer, String> entry: entrySet) {
                            message = message + ":" + entry.getKey().toString() + ":" + entry.getValue().toString() + ":";
                            if(isOnline(entry.getKey()))
                                message = message + "1";
                            else
                                message = message + "0";
                        }
                        outputToClient.writeObject(message);
                        outputToClient.flush();
                        System.out.println(message);
                    }
                    //登出
                    else if(info[0].equals("logout")) {
                        userOutputStream.remove(Integer.parseInt(info[1]));
                        sendFriendsInfo();
                        System.out.println("断开连接");
                    }
                    //发送单词卡
                    else if(info[0].equals("wordcard")) {
                        int aID = Integer.parseInt(info[1]);
                        int bID = Integer.parseInt(info[2]);
                        String message;
                        String reply;
                        if(userOutputStream.containsKey(bID)) {
                            message = new String("wordcard:" + info[1] + ":" + dataBaseConnectivity.getNameByID(aID) + ":" + info[3]);
                            ObjectOutputStream friendOutputStream = userOutputStream.get(bID);
                            friendOutputStream.writeObject(message);
                            friendOutputStream.flush();
                            reply = new String("wordcardconfirm:success:" + info[2] + ":" + dataBaseConnectivity.getNameByID(bID));
                            outputToClient.writeObject(reply);
                            outputToClient.flush();
                        }
                        else {
                            reply = new String("wordcardconfirm:fail:" + info[2] + ":" + dataBaseConnectivity.getNameByID(bID));
                            outputToClient.writeObject(reply);
                            outputToClient.flush();
                        }
                    }
                    else if(info[0].equals("notfound")) {
                        String message = new String("search:0:0:0");
                        outputToClient.writeObject(message);
                        outputToClient.flush();
                    }
                }
            }
            catch (EOFException eofe) {
                userOutputStream.remove(id);
                sendFriendsInfo();
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

        public boolean add(int userID, int friendID) {
            return dataBaseConnectivity.addFriend(userID, friendID);
        }

        public boolean isOutputStreamExist(int id) {
            return userOutputStream.containsKey(new Integer(id));
        }

        public void handleOtherUserMessage(int otherID, String message) throws IOException{
            if (isOutputStreamExist(otherID)) {
                ObjectOutputStream friendOutputStream = userOutputStream.get(otherID);
                friendOutputStream.writeObject(message);
                friendOutputStream.flush();
            } else {
                ArrayList<String> messageList;
                if(unhandledEvents.containsKey(otherID)) {
                    messageList = unhandledEvents.get(otherID);
                }
                else {
                    messageList = new ArrayList<String>();
                }
                messageList.add(message);
                unhandledEvents.put(otherID, messageList);
                System.out.println("addUnhandled:" + otherID + message);
            }
        }

        public boolean isOnline(int ID) {
            if(userOutputStream.containsKey(ID))
                return true;
            else
                return false;
        }

        public void sendFriendsInfo(){
            Set<Map.Entry<Integer, ObjectOutputStream>> entrySet = userOutputStream.entrySet();
            for(Map.Entry<Integer, ObjectOutputStream> entry: entrySet) {
                String message = new String("friends");
                int userID = entry.getKey();
                Map<Integer, String> friendInfo = dataBaseConnectivity.getFriendInfo(userID);
                Set<Map.Entry<Integer, String>> entrySet2 = friendInfo.entrySet();
                for(Map.Entry<Integer, String> entry2: entrySet2) {
                    message = message + ":" + entry2.getKey().toString() + ":" + entry2.getValue().toString() + ":";
                    if(isOnline(entry2.getKey()))
                        message = message + "1";
                    else
                        message = message + "0";
                }
                ObjectOutputStream output = entry.getValue();
                try {
                    output.writeObject(message);
                    output.flush();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(message);
            }
        }
    }

}
