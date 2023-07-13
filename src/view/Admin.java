
package view;

import dao.DAO;

import dao.UserDAO;

import javax.swing.JOptionPane;
import model.User;
import controller.Room;
import controller.Server;
import controller.ServerThread;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.DefaultComboBoxModel;

public class Admin extends javax.swing.JFrame implements Runnable{
    private UserDAO userDAO;
    public Admin() {
        initComponents();
        userDAO = new UserDAO();
    }
    public void initComponents() {
    	setTitle("Caro Game");
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setBounds(100, 100, 871, 579);
    	setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
        
    	getContentPane = new JPanel();
    	getContentPane.setBackground(new Color(85, 97, 111));
		getContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(getContentPane);
        getContentPane().setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 857, 59);
        panel.setBackground(new Color(102, 102, 102));
        getContentPane().add(panel);
        panel.setLayout(null);
      
        
        JLabel lblNewLabel = new JLabel("ADMIN");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 25));
        lblNewLabel.setForeground(new Color(255, 255, 255));
        lblNewLabel.setBounds(380, 18, 90, 31);
        panel.add(lblNewLabel);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(0, 57, 857, 184);
        getContentPane().add(scrollPane);
        
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Username");
        tableModel.addColumn("Pass");
        tableModel.addColumn("NickName");

        table_1 = new JTable(tableModel);

        table_1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table_1.getSelectedRow();
                int option = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa hàng này không?",
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    String selectedId = table_1.getValueAt(selectedRow, 0).toString();
                    deleteRecord(selectedId);
                    tableModel.removeRow(selectedRow);
                }
            }
        });
        scrollPane.setViewportView(table_1);             
        txtNhpId = new JTextField();
        txtNhpId.setBounds(72, 305, 198, 46);
        txtNhpId.setFont(new Font("Tahoma", Font.PLAIN, 16));
        txtNhpId.setText("Nhập ID");
        getContentPane().add(txtNhpId);
        txtNhpId.setColumns(10);
        
        JButton btnNewButton = new JButton("Tìm kiếm");
        btnNewButton.setForeground(new Color(255, 255, 255));
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String tk=txtNhpId.getText();
        		searchById(tk);
        	}
        });
        btnNewButton.setBackground(new Color(49, 183, 66));
        btnNewButton.setBounds(342, 305, 158, 46);
        btnNewButton.setIcon(new ImageIcon(Admin.class.getResource("/view/9004811_search_find_magnifier_zoom_icon.png")));
        btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 16));
        getContentPane().add(btnNewButton);
        
        JButton btnNewButton_1 = new JButton("Reset");
        btnNewButton_1.setForeground(new Color(255, 255, 255));
        btnNewButton_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		fetchDataFromMySQL();
        	}
        });
        btnNewButton_1.setBackground(new Color(254, 62, 46));
        btnNewButton_1.setBounds(560, 305, 158, 46);
        btnNewButton_1.setIcon(new ImageIcon(Admin.class.getResource("/view/2571204_refresh_reload_update_recycle_sync_icon.png")));
        btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 16));
        getContentPane().add(btnNewButton_1);
        
        comboBox = new JComboBox<String>();
        comboBox.setBounds(121, 393, 280, 46);
        comboBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
        comboBox.setModel(new DefaultComboBoxModel<String>(new String[] { "Chọn lý do", "Ngôn ngữ thô tục - xúc phạm người khác", "Spam đăng nhập", "Sử dụng game với mục đích xấu", "Phát hiện rò rỉ bảo mật - tài khoản tạm thời bị khoá để kiểm tra thêm" }));
        getContentPane().add(comboBox);

        
        JButton btnNewButton_2 = new JButton("Ban");
        btnNewButton_2.setForeground(new Color(255, 255, 255));
        btnNewButton_2.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent evt) {
        		jButton4ActionPerformed(evt);
        	}
        });
        btnNewButton_2.setBackground(new Color(255, 128, 128));
        btnNewButton_2.setBounds(436, 392, 166, 46);
        btnNewButton_2.setIcon(new ImageIcon(Admin.class.getResource("/view/299051_ban_sign_icon (1).png")));
        btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 17));
        getContentPane().add(btnNewButton_2);
        
        JButton btnNewButton_3 = new JButton("Unban");
        btnNewButton_3.setBackground(new Color(185, 255, 185));
        btnNewButton_3.setForeground(new Color(255, 255, 255));
        btnNewButton_3.setIcon(new ImageIcon(Admin.class.getResource("/view/9004846_tick_check_mark_accept_icon.png")));
        btnNewButton_3.setFont(new Font("Tahoma", Font.BOLD, 17));
        btnNewButton_3.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent evt) {
        		jButton3ActionPerformed(evt);
        	}
        });
        btnNewButton_3.setBounds(630, 392, 149, 46);
        getContentPane.add(btnNewButton_3);
        
        JLabel lblNewLabel_2 = new JLabel("");
        lblNewLabel_2.setIcon(new ImageIcon(Admin.class.getResource("/view/Tic-tac-toe1.jpg")));
        lblNewLabel_2.setBounds(0, 242, 857, 313);
        getContentPane.add(lblNewLabel_2);

        
       
    }
   
    private void fetchDataFromMySQL() {
        try {
        	Connection conn = DAO.getJDBCConnection();
            String query = "SELECT I_ID,T_UserName,T_Pass,T_NickName FROM ta_lpn_user";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet resultSet = ps.executeQuery(query);
            tableModel.setRowCount(0);
            while (resultSet.next()) {
            	String id=resultSet.getString("I_ID");
                String email = resultSet.getString("T_UserName");
                String tdn = resultSet.getString("T_Pass");
                String tnd = resultSet.getString("T_NickName");
               
                tableModel.addRow(new Object[]{id,email, tdn, tnd});
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void searchById(String searchId) {
        try {
            Connection conn = DAO.getJDBCConnection();
            String query = "SELECT I_ID,T_UserName,T_Pass,T_NickName FROM ta_lpn_user WHERE I_ID = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, searchId);
            ResultSet resultSet = ps.executeQuery();
            tableModel.setRowCount(0);
            while (resultSet.next()) {
            	String id=resultSet.getString("I_ID");
                String email = resultSet.getString("T_UserName");
                String tdn = resultSet.getString("T_Pass");
                String tnd = resultSet.getString("T_NickName");
                
                tableModel.addRow(new Object[]{id, email, tdn, tnd});
            }
            
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void deleteRecord(String id) {
        try {
            Connection conn = DAO.getJDBCConnection();
            
            String deleteChildQuery = "DELETE FROM ta_lpn_banned_user WHERE I_ID_User=?";
            PreparedStatement deleteChildStatement = conn.prepareStatement(deleteChildQuery);
            deleteChildStatement.setString(1, id);
            deleteChildStatement.executeUpdate();
           
            String deleteParentQuery = "DELETE FROM ta_lpn_user WHERE I_ID=?";
            PreparedStatement deleteParentStatement = conn.prepareStatement(deleteParentQuery);
            deleteParentStatement.setString(1, id);
            deleteParentStatement.executeUpdate();
            
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if(txtNhpId.getText().length()==0){
                JOptionPane.showMessageDialog(rootPane, "Vui lòng nhập ID của User");
                return;
            }
            if(comboBox.getSelectedIndex()<1){
                JOptionPane.showMessageDialog(rootPane, "Vui lòng chọn lý do");
                return;
            }
            int userId = Integer.parseInt(txtNhpId.getText());
            User user = new User();
            user.setID(userId);
            if(userDAO.checkIsOnline(userId)) {
            	ServerThread serverThread = Server.serverThreadBus.getServerThreadByUserID(userId);
                System.out.println(serverThread + " " + userId);
                serverThread.write("banned-notice,"+comboBox.getSelectedItem());
                if(serverThread.getRoom()!=null){
                    Room room = serverThread.getRoom();
                    ServerThread competitorThread = room.getCompetitor(serverThread.getClientNumber());
                    room.setUsersToNotPlaying();
                    if(competitorThread!=null){
                        room.decreaseNumberOfGame();
                        competitorThread.write("left-room,");
                        competitorThread.setRoom(null);
                    }
                    serverThread.setRoom(null);
                }
           
                Server.serverThreadBus.boardCast(-1, "chat-server,"+"User có ID "+ userId+" đã bị BAN");
            }
            userDAO.updateBannedStatus(user, true);
            JOptionPane.showMessageDialog(rootPane, "Đã BAN user "+userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
    public void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
    	try {
            if(txtNhpId.getText().length()==0){
                JOptionPane.showMessageDialog(rootPane, "Vui lòng nhập ID của User");
                return;
            }
            int userId = Integer.parseInt(txtNhpId.getText());
            User user = new User();
            user.setID(userId);
            userDAO.updateBannedStatus(user, false); 
            JOptionPane.showMessageDialog(rootPane, "Đã UNBAN user "+userId);
        } catch (Exception e) {
            e.printStackTrace();            
        }
    }
  
	private DefaultTableModel tableModel ;
	private javax.swing.JButton jButton2;
    private javax.swing.JLabel lblNewLabel_1;
    private javax.swing.JPanel panel;
    private javax.swing.JScrollPane scrollPane;
    private JComboBox<String> comboBox;
    private JPanel getContentPane;   
    private JTable table_1;
    private JTextField txtNhpId;
    @Override
    public void run() {
        setVisible(true);
        fetchDataFromMySQL();        
    }
}
