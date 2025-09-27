package com.cms;

import com.util.DB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class TaskPanel extends JPanel implements ActionListener {
    private JTextField taskNameTxt, taskDescTxt, statusTxt, startDateTxt, endDateTxt;
    private JButton addBtn, updateBtn, deleteBtn, loadBtn;
    private JTable taskTable;
    private DefaultTableModel model;

    public TaskPanel() {
        setLayout(new BorderLayout());

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.add(new JLabel("Task Name:"));
        taskNameTxt = new JTextField();
        inputPanel.add(taskNameTxt);

        inputPanel.add(new JLabel("Task Description:"));
        taskDescTxt = new JTextField();
        inputPanel.add(taskDescTxt);

        inputPanel.add(new JLabel("Status:"));
        statusTxt = new JTextField();
        inputPanel.add(statusTxt);

        inputPanel.add(new JLabel("Start Date (YYYY-MM-DD):"));
        startDateTxt = new JTextField();
        inputPanel.add(startDateTxt);

        inputPanel.add(new JLabel("End Date (YYYY-MM-DD):"));
        endDateTxt = new JTextField();
        inputPanel.add(endDateTxt);

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
        model = new DefaultTableModel(new String[]{"TaskID", "TaskName", "Description", "Status", "StartDate", "EndDate"}, 0);
        taskTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(taskTable);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Attach event listeners
        addBtn.addActionListener(this);
        updateBtn.addActionListener(this);
        deleteBtn.addActionListener(this);
        loadBtn.addActionListener(this);

        taskTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = taskTable.getSelectedRow();
                if(row >= 0) {
                    taskNameTxt.setText(model.getValueAt(row, 1).toString());
                    taskDescTxt.setText(model.getValueAt(row, 2).toString());
                    statusTxt.setText(model.getValueAt(row, 3).toString());
                    startDateTxt.setText(model.getValueAt(row, 4).toString());
                    endDateTxt.setText(model.getValueAt(row, 5).toString());
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == addBtn) addTask();
        else if(e.getSource() == updateBtn) updateTask();
        else if(e.getSource() == deleteBtn) deleteTask();
        else if(e.getSource() == loadBtn) loadTasks();
    }

    private void addTask() {
        String name = taskNameTxt.getText().trim();
        String desc = taskDescTxt.getText().trim();
        String status = statusTxt.getText().trim();
        String start = startDateTxt.getText().trim();
        String end = endDateTxt.getText().trim();

        if(name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Task name is required.");
            return;
        }

        String sql = "INSERT INTO Task (taskName, taskDescription, status, startDate, endDate) VALUES (?, ?, ?, ?, ?)";
        try(Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, desc);
            ps.setString(3, status);
            ps.setDate(4, Date.valueOf(start));
            ps.setDate(5, Date.valueOf(end));
            int res = ps.executeUpdate();
            if(res > 0) {
                JOptionPane.showMessageDialog(this, "Task added successfully.");
                loadTasks();
            }
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding task: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void updateTask() {
        int row = taskTable.getSelectedRow();
        if(row < 0) {
            JOptionPane.showMessageDialog(this, "Select a task to update.");
            return;
        }
        int taskId = Integer.parseInt(model.getValueAt(row, 0).toString());

        String name = taskNameTxt.getText().trim();
        String desc = taskDescTxt.getText().trim();
        String status = statusTxt.getText().trim();
        String start = startDateTxt.getText().trim();
        String end = endDateTxt.getText().trim();

        if(name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Task name is required.");
            return;
        }

        String sql = "UPDATE Task SET taskName=?, taskDescription=?, status=?, startDate=?, endDate=? WHERE taskID=?";
        try(Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, desc);
            ps.setString(3, status);
            ps.setDate(4, Date.valueOf(start));
            ps.setDate(5, Date.valueOf(end));
            ps.setInt(6, taskId);
            int res = ps.executeUpdate();
            if(res > 0) {
                JOptionPane.showMessageDialog(this, "Task updated successfully.");
                loadTasks();
            }
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating task: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void deleteTask() {
        int row = taskTable.getSelectedRow();
        if(row < 0) {
            JOptionPane.showMessageDialog(this, "Select a task to delete.");
            return;
        }

        int taskId = Integer.parseInt(model.getValueAt(row, 0).toString());
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure want to delete this task?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if(confirm != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM Task WHERE taskID=?";
        try(Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, taskId);
            int res = ps.executeUpdate();
            if(res > 0) {
                JOptionPane.showMessageDialog(this, "Task deleted successfully.");
                loadTasks();
            }
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this, "Error deleting task: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void loadTasks() {
        model.setRowCount(0);
        String sql = "SELECT * FROM Task";
        try(Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("taskID"),
                        rs.getString("taskName"),
                        rs.getString("taskDescription"),
                        rs.getString("status"),
                        rs.getDate("startDate"),
                        rs.getDate("endDate")
                });
            }
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading tasks: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
