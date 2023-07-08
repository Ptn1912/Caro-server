
package dao;

import java.sql.*;

public class DAO {

	public static Connection getJDBCConnection() throws SQLException {
        final String url = "jdbc:mysql://localhost:3306/java";
        final String user = "root";
        final String password = "123456";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}