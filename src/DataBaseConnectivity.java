/**
 * Created by FelixXiao on 2016/11/21.
 */
import java.io.IOException;
import java.sql.*;
import java.util.ConcurrentModificationException;
import java.util.*;


public class DataBaseConnectivity {
    public static void main(String[] args){
        DataBaseConnectivity DBC = new DataBaseConnectivity();
 //       DBC.deleteFriend(119,120);
//        int[] test = DBC.SearchLikeInfo("get");
//        System.out.println(test[0] + " " + test[1] + " " + test[2]);
//        DBC.insertUserData(114,"wu","wu");
        System.out.println(DBC.getFriendInfo(119));
//        DBC.addLikeInfo("xing", "youdao");
    }


    private Connection connection;

    //constructor
    public DataBaseConnectivity() {
        connection = getConnection();
    }
    //Connect to Database
    private Connection getConnection() {
        Connection conn = null;
        try{
            //Load the JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded");

            //Establish a connection
            String url = "jdbc:mysql://localhost:3306/OnlineDictionary?useSSL=true";
            conn = DriverManager.getConnection(url, "root", "960805");
            System.out.println("Database connected");

        } catch (Exception e) {
            System.out.println("Database connection failed");
            e.printStackTrace();
        }
        return conn;
    }

    //Insert User data
    public void insertUserData(int ID, String name, String password) {
        try {
            String sql = "insert into Users(ID,name,password)" + "values (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, ID);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, password);
            preparedStatement.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Get user name by ID
    public String getNameByID(int ID) {
        try {
            String sql = "select name from Users where ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
                return resultSet.getString(1);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Get user password by ID
    public String getPasswordByID(int ID) {
        try {
            String sql = "select password from Users where ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
                return resultSet.getString(1);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int distrubuteID() {
        try {
            String sql = "select ID from Users order by ID desc";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet.next())
                return resultSet.getInt(1) + 1;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean isIDExist(int id) {
        try {
            String sql = "select password from Users where ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
                return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addLikeInfo(String word, String name) {
        try {
            String sql = new String();
            if(name.equals("youdao")) {
                sql = "update OnlineDictionary.Words set youdao = youdao + 1 where word = ?";
            }
            else if(name.equals("iciba")) {
                sql = "update OnlineDictionary.Words set iciba = iciba + 1 where word = ?";
            }
            else if(name.equals("bing")) {
                sql = "update OnlineDictionary.Words set bing = bing + 1 where word = ?";
            }
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, word);
            preparedStatement.executeUpdate();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void addUnlikeInfo(String word, String name) {
        try {
            String sql = new String();
            if(name.equals("youdao")) {
                sql = "update OnlineDictionary.Words set youdao = youdao - 1 where word = ?";
            }
            else if(name.equals("iciba")) {
                sql = "update OnlineDictionary.Words set iciba = iciba - 1 where word = ?";
            }
            else if(name.equals("bing")) {
                sql = "update OnlineDictionary.Words set bing = bing - 1 where word = ?";
            }
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, word);
            preparedStatement.executeUpdate();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertWordInfo(String word) {
        try {
            String sql = "insert into Words(word, youdao, iciba, bing)" + "values (?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, word);
            preparedStatement.setInt(2, 0);
            preparedStatement.setInt(3, 0);
            preparedStatement.setInt(4, 0);
            preparedStatement.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int[] SearchLikeInfo(String word) {
        int[] result = new int[3];
        try {
            String sql = "select * from Words where word = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, word);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                result[0] = resultSet.getInt(2);
                result[1] = resultSet.getInt(3);
                result[2] = resultSet.getInt(4);
            }
            else {
                result[0] = 0;
                result[1] = 0;
                result[2] = 0;
                insertWordInfo(word);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean addFriend(int userID, int friendID) {
        if(isIDExist(userID) && isIDExist(friendID)) {
            try {
                String sql = "insert into Friendship(userID,friendID)values (?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, userID);
                preparedStatement.setInt(2, friendID);
                preparedStatement.executeUpdate();
                String sql2 = "insert into Friendship(userID,friendID)values (?, ?)";
                PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
                preparedStatement2.setInt(1, friendID);
                preparedStatement2.setInt(2, userID);
                preparedStatement2.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isFriend(int userID, int friendID) {
        try {
            String sql = "select friendID from Friendship where userID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean isfriend;
            while(resultSet.next()) {
                if(resultSet.getInt(1) == friendID)
                    return true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Map<Integer,String> getFriendInfo(int userID) {
        Map<Integer,String> result = new TreeMap<>();
        try {
            String sql = "select friendID from Friendship where userID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.put(resultSet.getInt(1),getNameByID(resultSet.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void deleteFriend(int userID, int friendID) {
        try {
            String sql1 = "delete from Friendship where userID = ? and friendID = ?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
            preparedStatement1.setInt(1, userID);
            preparedStatement1.setInt(2, friendID);
            preparedStatement1.executeUpdate();
            String sql2 = "delete from Friendship where userID = ? and friendID = ?";
            PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
            preparedStatement2.setInt(1, friendID);
            preparedStatement2.setInt(2, userID);
            preparedStatement2.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changePassword(int userID, String newPassword) {
        try {
            String sql = "update Users set password = ? where ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, newPassword);
            preparedStatement.setInt(2, userID);
            preparedStatement.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
