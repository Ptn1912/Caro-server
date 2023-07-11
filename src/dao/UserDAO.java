
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import model.User;

public class UserDAO extends DAO{

    public UserDAO() {
        super();
    }
    
    public User verifyUser(User user) {
        try {
            PreparedStatement preparedStatement = DAO.getJDBCConnection().prepareStatement("SELECT *\n"
                    + "FROM ta_lpn_user\n"
                    + "WHERE T_UserName = ?\n"
                    + "AND T_Pass = ?");
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getInt(7),
                        rs.getInt(8),
                        (rs.getInt(9) != 0),
                        (rs.getInt(10) != 0),
                        getRank(rs.getInt(1)));    
            }
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    public User getUserByID(int ID) {
        try {
            PreparedStatement preparedStatement = DAO.getJDBCConnection().prepareStatement("SELECT * FROM ta_lpn_user\n"
                    + "WHERE I_ID=?");
            preparedStatement.setInt(1, ID);
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getInt(7),
                        rs.getInt(8),
                        (rs.getInt(9) != 0),
                        (rs.getInt(10) != 0),
                        getRank(rs.getInt(1)));
                        
            }
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    public void addUser(User user) {
        try {
            PreparedStatement preparedStatement = DAO.getJDBCConnection().prepareStatement("INSERT INTO ta_lpn_user(T_UserName,T_Pass,T_NickName)\n"
                    + "VALUES(?,?,?)");
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getNickname());
          
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public boolean checkDuplicated(String username){
        try {
            PreparedStatement preparedStatement = DAO.getJDBCConnection().prepareStatement("SELECT * FROM ta_lpn_user WHERE T_UserName = ?");
            preparedStatement.setString(1, username);
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return true;
            }
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean checkIsBanned(User user){
        try {
            PreparedStatement preparedStatement = DAO.getJDBCConnection().prepareStatement("SELECT * FROM ta_lpn_banned_user WHERE I_ID_User = ?");
            preparedStatement.setInt(1, user.getID());
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return true;
            }
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
    
    public void updateBannedStatus(User user, boolean ban) {
        try {
            PreparedStatement preparedStatement1 = DAO.getJDBCConnection().prepareStatement("SELECT * FROM `ta_lpn_banned_user` WHERE `I_ID_User` = ?");
            preparedStatement1.setInt(1, user.getID());
            ResultSet resultSet = preparedStatement1.executeQuery();

            if (resultSet.next() && ban) {
                // User đã bị cấm trước đó, không cần chèn lại
                JOptionPane.showMessageDialog(null, "User đã bị cấm trước đó");
            } else {
                if (ban) {
                	String insertQuery = "INSERT INTO `ta_lpn_banned_user` (`I_ID_User`) VALUES (?);";
                	String updateQuery = "UPDATE `ta_lpn_user` SET `IsOnline` = '0' WHERE (`I_ID` = ?);";

                	try (PreparedStatement preparedStatement = DAO.getJDBCConnection().prepareStatement(insertQuery);
                	     PreparedStatement preparedStatement2 = DAO.getJDBCConnection().prepareStatement(updateQuery)) {

                	    preparedStatement.setInt(1, user.getID());
                	    preparedStatement.executeUpdate();
                	    preparedStatement.close();

                	    preparedStatement2.setInt(1, user.getID());
                	    preparedStatement2.executeUpdate();
                	    preparedStatement2.close();
                	    
                	} catch (SQLException e) {
                	    // Xử lý ngoại lệ (exception) nếu cần thiết
                	    e.printStackTrace();
                	}

               
                } else {
                    PreparedStatement preparedStatement4 = DAO.getJDBCConnection().prepareStatement("DELETE FROM `ta_lpn_banned_user` WHERE `I_ID_User` = ?");
                    preparedStatement4.setInt(1, user.getID());
                    preparedStatement4.executeUpdate();
                    preparedStatement4.close();
                }
            }

            preparedStatement1.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public void updateToOnline(int ID) {
        try {
            PreparedStatement preparedStatement = DAO.getJDBCConnection().prepareStatement("UPDATE ta_lpn_user\n"
                    + "SET IsOnline = 1\n"
                    + "WHERE I_ID = ?");
            preparedStatement.setInt(1, ID);
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void updateToOffline(int ID) {
        try {
            PreparedStatement preparedStatement = DAO.getJDBCConnection().prepareStatement("UPDATE ta_lpn_user\n"
                    + "SET IsOnline = 0\n"
                    + "WHERE I_ID = ?");
            preparedStatement.setInt(1, ID);
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
  
    public boolean checkIsOnline(int ID) {
        try {
            PreparedStatement preparedStatement = DAO.getJDBCConnection().prepareStatement("SELECT `IsOnline` FROM `ta_lpn_user` WHERE `I_ID` = ?");
            preparedStatement.setInt(1, ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                int isOnline = resultSet.getInt("IsOnline");
                return isOnline == 1;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    public void updateToPlaying(int ID){
        try {
            PreparedStatement preparedStatement = DAO.getJDBCConnection().prepareStatement("UPDATE ta_lpn_user\n"
                    + "SET IsPlaying = 1\n"
                    + "WHERE I_ID = ?");
            preparedStatement.setInt(1, ID);
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void updateToNotPlaying(int ID){
        try {
            PreparedStatement preparedStatement = DAO.getJDBCConnection().prepareStatement("UPDATE ta_lpn_user\n"
                    + "SET IsPlaying = 0\n"
                    + "WHERE I_ID = ?");
            preparedStatement.setInt(1, ID);
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public int getRank(int ID) {
        int rank = 1;
        try {
            PreparedStatement preparedStatement = DAO.getJDBCConnection().prepareStatement("SELECT I_ID\n"
                    + "FROM ta_lpn_user\n"
                    + "ORDER BY (NumberOfGame+numberOfDraw*5+NumberOfWin*10) DESC");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                if(rs.getInt(1)==ID)
                    return rank;
                rank++;
            }
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }
    public List<User> getUserStaticRank() {
        List<User> list = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = DAO.getJDBCConnection().prepareStatement("SELECT *\n"
                    + "FROM ta_lpn_user\n"
                    + "ORDER BY(NumberOfGame+numberOfDraw*5+NumberOfWin*10) DESC\n"
                    + "LIMIT 8");
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                list.add(new User(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getInt(7),
                        rs.getInt(8),
                        (rs.getInt(9) != 0),
                        (rs.getInt(10) != 0),
                        getRank(rs.getInt(1))));
            }
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }
    public int getNumberOfWin(int ID) {
        try {
            PreparedStatement preparedStatement = DAO.getJDBCConnection().prepareStatement("SELECT NumberOfWin\n"
                    + "FROM ta_lpn_user\n"
                    + "WHERE I_ID = ?");
            preparedStatement.setInt(1, ID);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }
    public int getNumberOfDraw(int ID) {
        try {
            PreparedStatement preparedStatement = DAO.getJDBCConnection().prepareStatement("SELECT NumberOfDraw\n"
                    + "FROM ta_lpn_user\n"
                    + "WHERE I_ID = ?");
            preparedStatement.setInt(1, ID);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }
    
    public void addDrawGame(int ID){
        try {
            PreparedStatement preparedStatement = DAO.getJDBCConnection().prepareStatement("UPDATE ta_lpn_user\n"
                    + "SET NumberOfDraw = ?\n"
                    + "WHERE I_ID = ?");
            preparedStatement.setInt(1, new UserDAO().getNumberOfDraw(ID)+1);
            preparedStatement.setInt(2, ID);
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void addWinGame(int ID){
        try {
            PreparedStatement preparedStatement = DAO.getJDBCConnection().prepareStatement("UPDATE ta_lpn_user\n"
                    + "SET NumberOfWin = ?\n"
                    + "WHERE I_ID = ?");
            preparedStatement.setInt(1, new UserDAO().getNumberOfWin(ID)+1);
            preparedStatement.setInt(2, ID);
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public int getNumberOfGame(int ID) {
        try {
            PreparedStatement preparedStatement = DAO.getJDBCConnection().prepareStatement("SELECT NumberOfGame\n"
                    + "FROM ta_lpn_user\n"
                    + "WHERE I_ID = ?");
            preparedStatement.setInt(1, ID);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }

    public void addGame(int ID) {
        try {
            PreparedStatement preparedStatement = DAO.getJDBCConnection().prepareStatement("UPDATE ta_lpn_user\n"
                    + "SET NumberOfGame = ?\n"
                    + "WHERE I_ID = ?");
            preparedStatement.setInt(1, new UserDAO().getNumberOfGame(ID) + 1);
            preparedStatement.setInt(2, ID);
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public void decreaseGame(int ID){
        try {
            PreparedStatement preparedStatement = DAO.getJDBCConnection().prepareStatement("UPDATE ta_lpn_user\n"
                    + "SET NumberOfGame = ?\n"
                    + "WHERE I_ID = ?");
            preparedStatement.setInt(1, new UserDAO().getNumberOfGame(ID) - 1);
            preparedStatement.setInt(2, ID);
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public String getNickNameByID(int ID) {
        try {
            PreparedStatement preparedStatement = DAO.getJDBCConnection().prepareStatement("SELECT T_NickName\n"
                    + "FROM ta_lpn_user\n"
                    + "WHERE I_ID=?");
            preparedStatement.setInt(1, ID);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
}
