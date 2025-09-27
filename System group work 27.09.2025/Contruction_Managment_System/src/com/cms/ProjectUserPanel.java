package com.cms;

import com.util.DB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ProjectUserPanel extends JPanel implements ActionListener {
    private JTextField usernameTxt, passwordTxt, roleTxt, emailTxt;
    private JButton addBtn, updateBtn, deleteBtn, loadBtn;
    private JTable userTable;
    private DefaultTableModel model;

    public ProjectUserPanel() {
        setLayout(new BorderLayout());

        // Input panel with labels and text fields
        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Username:"));
        usernameTxt = new JTextField();
        inputPanel.add(usernameTxt);

        inputPanel.add(new JLabel("Password Hash:"));
        passwordTxt = new JTextField();
        inputPanel.add(passwordTxt);

        inputPanel.add(new JLabel("Role:"));
        roleTxt = new JTextField();
        inputPanel.add(roleTxt);

        inputPanel.add(new JLabel("Email:"));
        emailTxt = new JTextField();
        inputPanel.add(emailTxt);

        // Buttons panel
        JPanel buttonsPanel = new JPanel();
        addBtn = new JButton("Add");
        updateBtn = new JButton("Update");
        deleteBtn = new JButton("Delete");
        loadBtn = new JButton("Load");

        buttonsPanel.add(addBtn);
        buttonsPanel.add(updateBtn);
        buttonsPanel.add(deleteBtn);
        buttonsPanel.add(loadBtn);

        // Table to show users data
        model = new DefaultTableModel(new String[]{"UserID", "Username", "PasswordHash", "Role", "Email"}, 0);
        userTable = new JTable(model);
        JScrollPane tableScrollPane = new JScrollPane(userTable);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.CENTER);
        add(tableScrollPane, BorderLayout.SOUTH);

        // Add action listeners for buttons
        addBtn.addActionListener(this);
        updateBtn.addActionListener(this);
        deleteBtn.addActionListener(this);
        loadBtn.addActionListener(this);

        // On table row click, fill inputs with selected row's data
        userTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow >= 0) {
                    usernameTxt.setText(model.getValueAt(selectedRow, 1).toString());
                    passwordTxt.setText(model.getValueAt(selectedRow, 2).toString());
                    roleTxt.setText(model.getValueAt(selectedRow, 3).toString());
                    emailTxt.setText(model.getValueAt(selectedRow, 4).toString());
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addBtn) {
            addUser();
        } else if (e.getSource() == updateBtn) {
            updateUser();
        } else if (e.getSource() == deleteBtn) {
            deleteUser();
        } else if (e.getSource() == loadBtn) {
            loadUsers();
        }
    }

    private void addUser() {
        String username = usernameTxt.getText().trim();
        String passwordHash = passwordTxt.getText().trim();
        String role = roleTxt.getText().trim();
        String email = emailTxt.getText().trim();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username is required");
            return;
        }

        String sql = "INSERT INTO ProjectUser (username, passwordHash, role, email) VALUES (?, ?, ?, ?)";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            ps.setString(3, role);
            ps.setString(4, email);
            int added = ps.executeUpdate();
            if (added > 0) {
                JOptionPane.showMessageDialog(this, "User added successfully");
                loadUsers();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding user: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void updateUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select a user to update from the table");
            return;
        }
        int userID = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());

        String username = usernameTxt.getText().trim();
        String passwordHash = passwordTxt.getText().trim();
        String role = roleTxt.getText().trim();
        String email = emailTxt.getText().trim();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username is required");
            return;
        }

        String sql = "UPDATE ProjectUser SET username=?, passwordHash=?, role=?, email=? WHERE userID=?";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            ps.setString(3, role);
            ps.setString(4, email);
            ps.setInt(5, userID);
            int updated = ps.executeUpdate();
            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "User updated successfully");
                loadUsers();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating user: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select a user to delete from the table");
            return;
        }
        int userID = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM ProjectUser WHERE userID=?";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userID);
            int deleted = ps.executeUpdate();
            if (deleted > 0) {
                JOptionPane.showMessageDialog(this, "User deleted successfully");
                loadUsers();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error deleting user: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void loadUsers() {
        model.setRowCount(0); // clear existing rows
        String sql = "SELECT * FROM ProjectUser";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("userID"),
                        rs.getString("username"),
                        rs.getString("passwordHash"),
                        rs.getString("role"),
                        rs.getString("email")
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading users: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

