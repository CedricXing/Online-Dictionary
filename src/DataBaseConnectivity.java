/**
 * Created by FelixXiao on 2016/11/21.
 */
import java.sql.*;


public class DataBaseConnectivity {
//    public static void main(String[] args){
//        DataBaseConnectivity DBC = new DataBaseConnectivity();
//        System.out.println(DBC.getPasswordByID(117));
//        DBC.insertUserData(114,"wu","wu");
//
//    }

    //constructor
    public DataBaseConnectivity() {

    }
    //Connect to Database
    private Connection getConnection() {
        Connection conn = null;
        try{
            //Load the JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded");

            //Establish a connection
            String url = "jdbc:mysql://localhost:3306/OnlineDictionaryUsers?useSSL=true";
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
        Connection connection = getConnection();
        try {
            String sql = "insert into Users(ID,name,password)" + "values (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, ID);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, password);
            preparedStatement.executeUpdate();
            connection.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Get user name by ID
    public String getNameByID(int ID) {
        Connection connection = getConnection();
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
        Connection connection = getConnection();
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
}
