package com.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.*;

import com.cms.CMSDashboard;
import com.util.DB;

public class LoginForm extends JFrame implements ActionListener {
	
	JTextField userTxt = new JTextField();
	JPasswordField passTxt = new JPasswordField();
	JButton loginbtn = new JButton("Login");
	JButton cancelbtn = new JButton("Cancel");
	
	// constructor
	public LoginForm() {
		setTitle("Login Form");
		setBounds(100, 100, 300, 200);
		setLayout(null);
		userTxt.setBounds(50, 30, 120, 25);
		passTxt.setBounds(50, 70, 120, 25);
		loginbtn.setBounds(30, 120, 100, 30);
		cancelbtn.setBounds(150, 120, 100, 30);
		
		add(userTxt);
		add(passTxt);
		add(loginbtn);
		add(cancelbtn);
		loginbtn.addActionListener(this);
		cancelbtn.addActionListener(this);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	} 

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginbtn) {
			try (Connection con = DB.getConnection()) {
				String sql = "SELECT * FROM ProjectUser WHERE username = ? AND passwordHash = ?";
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, userTxt.getText().trim());
				ps.setString(2, new String(passTxt.getPassword()).trim());
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					String role = rs.getString("role");
					dispose();
					new CMSDashboard();
				} else {
					JOptionPane.showMessageDialog(this, "Invalid Login");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else if (e.getSource() == cancelbtn) {
			System.exit(0);
		}
	}
}

