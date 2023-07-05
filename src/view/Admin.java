/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author Admin
 */
public class Admin extends javax.swing.JFrame implements Runnable{
    private UserDAO userDAO;
    /**
     * Creates new form Admin
     */
    public Admin() {
    	
        initComponents();
        userDAO = new UserDAO();
    }
    public void initComponents() {
    	setTitle("Caro Game");
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setBounds(100, 100, 871, 596);
    	setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
        
    	getContentPane = new JPanel();
    	getContentPane.setBackground(new Color(85, 97, 111));
		getContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(getContentPane);
        getContentPane().setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 857, 68);
        panel.setBackground(new Color(102, 102, 102));
        getContentPane().add(panel);
      
        
        JLabel lblNewLabel = new JLabel("ADMIN");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 25));
        lblNewLabel.setForeground(new Color(255, 255, 255));
        lblNewLabel.setBounds(355, 20, 146, 25);
        panel.add(lblNewLabel);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 67, 839, 209);
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
                // Get the selected row index
                int selectedRow = table_1.getSelectedRow();

                // Display a confirmation dialog
                int option = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa hàng này không?",
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                // If the user confirms the deletion, proceed with deleting the row
                if (option == JOptionPane.YES_OPTION) {
                    // Get the ID of the selected record
                    String selectedId = table_1.getValueAt(selectedRow, 0).toString();

                    // Delete the record from the database
                    deleteRecord(selectedId);

                    // Remove the selected row from the table
                    tableModel.removeRow(selectedRow);
                }
            }
        });

        scrollPane.setViewportView(table_1);
       

        
        txtNhpId = new JTextField();
        txtNhpId.setBounds(73, 329, 198, 46);
        txtNhpId.setFont(new Font("Tahoma", Font.PLAIN, 16));
        txtNhpId.setText("Nhập ID");
        getContentPane().add(txtNhpId);
        txtNhpId.setColumns(10);
        
        JButton btnNewButton = new JButton("Tìm kiếm");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String tk=txtNhpId.getText();
        		searchById(tk);
        	}
        });
        btnNewButton.setBackground(new Color(49, 183, 66));
        btnNewButton.setBounds(344, 329, 158, 46);
        btnNewButton.setIcon(new ImageIcon(Admin.class.getResource("/view/9004811_search_find_magnifier_zoom_icon.png")));
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
        getContentPane().add(btnNewButton);
        
        JButton btnNewButton_1 = new JButton("Reset");
        btnNewButton_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		fetchDataFromMySQL();
        	}
        });
        btnNewButton_1.setBackground(new Color(254, 62, 46));
        btnNewButton_1.setBounds(562, 329, 158, 46);
        btnNewButton_1.setIcon(new ImageIcon(Admin.class.getResource("/view/8111405_reset_reload_refresh_sync_arrow_icon.png")));
        btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
        getContentPane().add(btnNewButton_1);
        
        comboBox = new JComboBox<String>();
        comboBox.setBounds(140, 436, 236, 46);
        comboBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
        comboBox.setModel(new DefaultComboBoxModel<String>(new String[] { "Chọn lý do", "Ngôn ngữ thô tục - xúc phạm người khác", "Spam đăng nhập", "Sử dụng game với mục đích xấu", "Phát hiện rò rỉ bảo mật - tài khoản tạm thời bị khoá để kiểm tra thêm" }));
        getContentPane().add(comboBox);

        
        JButton btnNewButton_2 = new JButton("Ban");
        btnNewButton_2.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent evt) {
        		jButton4ActionPerformed(evt);
        	}
        });
        btnNewButton_2.setBackground(new Color(113, 255, 255));
        btnNewButton_2.setBounds(432, 436, 147, 46);
        btnNewButton_2.setIcon(new ImageIcon(Admin.class.getResource("/view/9004743_trash_delete_bin_remove_icon.png")));
        btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 16));
        getContentPane().add(btnNewButton_2);
     
        
        JLabel lblNewLabel_1 = new JLabel("");
        lblNewLabel_1.setIcon(new ImageIcon(Admin.class.getResource("/view/Tic-tac-toe1.jpg")));
        lblNewLabel_1.setBounds(0, 279, 857, 280);
        getContentPane().add(lblNewLabel_1);

        
       
    }
    // </editor-fold>//GEN-END:initComponents
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
	        String query = "DELETE FROM ta_lpn_user where I_ID=?";
	        PreparedStatement ps = conn.prepareStatement(query);
	        ps.setString(1, id);
	        ps.executeUpdate();
	        conn.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
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
            userDAO.updateBannedStatus(user, true);
            ServerThread serverThread = Server.serverThreadBus.getServerThreadByUserID(userId);
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
            Server.admin.addMessage("User có ID "+ userId+" đã bị BAN");
            serverThread.setUser(null);
            Server.serverThreadBus.boardCast(-1, "chat-server,"+"User có ID "+ userId+" đã bị BAN");
            JOptionPane.showMessageDialog(rootPane, "Đã BAN user "+userId);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Có lỗi xảy ra");
        }
    }//GEN-LAST:event_jButton4ActionPerformed
   
    public void addMessage(String message) {
       
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
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {
        setVisible(true);
        fetchDataFromMySQL();        
    }
}
