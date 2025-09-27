package com.cms;

import com.util.DB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ResourcePanel extends JPanel implements ActionListener {
    private JTextField resourceNameTxt, resourceTypeTxt, resourceDescriptionTxt;
    private JButton addBtn, updateBtn, deleteBtn, loadBtn;
    private JTable resourceTable;
    private DefaultTableModel model;

    public ResourcePanel() {
        setLayout(new BorderLayout());

        // Input controls panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("Resource Name:"));
        resourceNameTxt = new JTextField();
        inputPanel.add(resourceNameTxt);

        inputPanel.add(new JLabel("Resource Type:"));
        resourceTypeTxt = new JTextField();
        inputPanel.add(resourceTypeTxt);

        inputPanel.add(new JLabel("Description:"));
        resourceDescriptionTxt = new JTextField();
        inputPanel.add(resourceDescriptionTxt);

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

        // Table to display resource data
        model = new DefaultTableModel(new String[]{"ResourceID", "ResourceName", "ResourceType", "Description"}, 0);
        resourceTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(resourceTable);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Add listeners
        addBtn.addActionListener(this);
        updateBtn.addActionListener(this);
        deleteBtn.addActionListener(this);
        loadBtn.addActionListener(this);

        // Populate inputs on row selection
        resourceTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = resourceTable.getSelectedRow();
                if (row >= 0) {
                    resourceNameTxt.setText(model.getValueAt(row, 1).toString());
                    resourceTypeTxt.setText(model.getValueAt(row, 2).toString());
                    resourceDescriptionTxt.setText(model.getValueAt(row, 3).toString());
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addBtn) addResource();
        else if (e.getSource() == updateBtn) updateResource();
        else if (e.getSource() == deleteBtn) deleteResource();
        else if (e.getSource() == loadBtn) loadResources();
    }

    private void addResource() {
        String name = resourceNameTxt.getText().trim();
        String type = resourceTypeTxt.getText().trim();
        String description = resourceDescriptionTxt.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Resource Name is required");
            return;
        }

        String sql = "INSERT INTO Resource (resourceName, resourceType, description) VALUES (?, ?, ?)";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, type);
            ps.setString(3, description);
            int res = ps.executeUpdate();
            if (res > 0) {
                JOptionPane.showMessageDialog(this, "Resource added successfully");
                loadResources();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding resource: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void updateResource() {
        int row = resourceTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a resource to update");
            return;
        }

        int resourceID = Integer.parseInt(model.getValueAt(row, 0).toString());

        String name = resourceNameTxt.getText().trim();
        String type = resourceTypeTxt.getText().trim();
        String description = resourceDescriptionTxt.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Resource Name is required");
            return;
        }

        String sql = "UPDATE Resource SET resourceName=?, resourceType=?, description=? WHERE resourceID=?";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, type);
            ps.setString(3, description);
            ps.setInt(4, resourceID);
            int res = ps.executeUpdate();
            if (res > 0) {
                JOptionPane.showMessageDialog(this, "Resource updated successfully");
                loadResources();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating resource: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void deleteResource() {
        int row = resourceTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a resource to delete");
            return;
        }

        int resourceID = Integer.parseInt(model.getValueAt(row, 0).toString());

        int confirm = JOptionPane.showConfirmDialog(this, "Confirm delete resource?", "Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM Resource WHERE resourceID=?";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, resourceID);
            int res = ps.executeUpdate();
            if (res > 0) {
                JOptionPane.showMessageDialog(this, "Resource deleted successfully");
                loadResources();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error deleting resource: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void loadResources() {
        model.setRowCount(0);
        String sql = "SELECT * FROM Resource";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("resourceID"),
                        rs.getString("resourceName"),
                        rs.getString("resourceType"),
                        rs.getString("description")
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading resources: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
