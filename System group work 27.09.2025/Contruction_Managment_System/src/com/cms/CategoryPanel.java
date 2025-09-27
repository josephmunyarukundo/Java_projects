package com.cms;

import com.util.DB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CategoryPanel extends JPanel implements ActionListener {
    private JTextField categoryNameTxt, descriptionTxt;
    private JButton addBtn, updateBtn, deleteBtn, loadBtn;
    private JTable categoryTable;
    private DefaultTableModel model;

    public CategoryPanel() {
        setLayout(new BorderLayout());

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(new JLabel("Category Name:"));
        categoryNameTxt = new JTextField();
        inputPanel.add(categoryNameTxt);

        inputPanel.add(new JLabel("Description:"));
        descriptionTxt = new JTextField();
        inputPanel.add(descriptionTxt);

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

        // Table setup
        model = new DefaultTableModel(new String[]{"CategoryID", "CategoryName", "Description"}, 0);
        categoryTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(categoryTable);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Attach button event listeners
        addBtn.addActionListener(this);
        updateBtn.addActionListener(this);
        deleteBtn.addActionListener(this);
        loadBtn.addActionListener(this);

        // Load data on table row clicked
        categoryTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = categoryTable.getSelectedRow();
                if (row >= 0) {
                    categoryNameTxt.setText(model.getValueAt(row, 1).toString());
                    descriptionTxt.setText(model.getValueAt(row, 2).toString());
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addBtn) addCategory();
        else if (e.getSource() == updateBtn) updateCategory();
        else if (e.getSource() == deleteBtn) deleteCategory();
        else if (e.getSource() == loadBtn) loadCategories();
    }

    private void addCategory() {
        String name = categoryNameTxt.getText().trim();
        String desc = descriptionTxt.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Category name is required");
            return;
        }

        String sql = "INSERT INTO Category (categoryName, description) VALUES (?, ?)";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, desc);
            int res = ps.executeUpdate();
            if (res > 0) {
                JOptionPane.showMessageDialog(this, "Category added successfully");
                loadCategories();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding category: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void updateCategory() {
        int row = categoryTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a category to update");
            return;
        }

        int categoryId = Integer.parseInt(model.getValueAt(row, 0).toString());
        String name = categoryNameTxt.getText().trim();
        String desc = descriptionTxt.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Category name is required");
            return;
        }

        String sql = "UPDATE Category SET categoryName=?, description=? WHERE categoryID=?";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, desc);
            ps.setInt(3, categoryId);
            int res = ps.executeUpdate();
            if (res > 0) {
                JOptionPane.showMessageDialog(this, "Category updated successfully");
                loadCategories();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating category: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void deleteCategory() {
        int row = categoryTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a category to delete");
            return;
        }

        int categoryId = Integer.parseInt(model.getValueAt(row, 0).toString());

        int confirm = JOptionPane.showConfirmDialog(this, "Confirm delete category?", "Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM Category WHERE categoryID=?";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            int res = ps.executeUpdate();
            if (res > 0) {
                JOptionPane.showMessageDialog(this, "Category deleted successfully");
                loadCategories();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error deleting category: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void loadCategories() {
        model.setRowCount(0);
        String sql = "SELECT * FROM Category";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("categoryID"),
                        rs.getString("categoryName"),
                        rs.getString("description")
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading categories: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
