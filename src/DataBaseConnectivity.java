/**
 * Created by FelixXiao on 2016/11/21.
 */
import java.sql.*;


public class DataBaseConnectivity {
    public static void main(String[] args) {
        DataBaseConnectivity DBC = new DataBaseConnectivity();
        if(DBC.getConnection() != null)
            System.out.println("Open Database successfully");
    }

    public DataBaseConnectivity() {

    }

    public Connection getConnection() {
        Connection conn = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/OnlineDictionaryUsers";
            conn = DriverManager.getConnection(url, "root", "960805");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
