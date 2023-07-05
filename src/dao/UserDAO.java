/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author Admin
 */
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

            if (resultSet.next()) {
                // User đã bị cấm trước đó, không cần chèn lại
                JOptionPane.showMessageDialog(null, "User đã bị cấm trước đó");
            } else {
                if (ban) {
                    PreparedStatement preparedStatement2 = DAO.getJDBCConnection().prepareStatement("INSERT INTO `ta_lpn_banned_user`(`I_ID_User`) VALUES (?)");
                    preparedStatement2.setInt(1, user.getID());
                    preparedStatement2.executeUpdate();
                    preparedStatement2.close();
                } else {
                    PreparedStatement preparedStatement3 = DAO.getJDBCConnection().prepareStatement("DELETE FROM `ta_lpn_user` WHERE `I_ID` = ?");
                    preparedStatement3.setInt(1, user.getID());
                    preparedStatement3.executeUpdate();
                    preparedStatement3.close();
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
    
    public List<User> getListFriend(int ID) {
        List<User> ListFriend = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = DAO.getJDBCConnection().prepareStatement("SELECT I_ID, User.T_NickName, IsOnline, IsPlaying\n"
                    + "FROM ta_lpn_user\n"
                    + "WHERE I_ID IN (\n"
                    + "	SELECT I_ID_User1\n"
                    + "    FROM ta_lpn_friend\n"
                    + "    WHERE I_ID_User2 = ?\n"
                    + ")\n"
                    + "OR I_ID IN(\n"
                    + "	SELECT I_ID_User2\n"
                    + "    FROM ta_lpn_friend\n"
                    + "    WHERE I_ID_User1 = ?\n"
                    + ")");
            preparedStatement.setInt(1, ID);
            preparedStatement.setInt(2, ID);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                ListFriend.add(new User(rs.getInt(1),
                        rs.getString(2),
                        (rs.getInt(3)==1),
                        (rs.getInt(4))==1));
            }
            ListFriend.sort(new Comparator<User>(){
                @Override
                public int compare(User o1, User o2) {
                    if(o1.getIsOnline()&&!o2.getIsOnline())
                        return -1;
                    if(o1.getIsPlaying()&&!o2.getIsOnline())
                        return -1;
                    if(!o1.getIsPlaying()&&o1.getIsOnline()&&o2.getIsPlaying()&&o2.getIsOnline())
                        return -1;
                    return 0;
                }
                
            });
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ListFriend;
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
