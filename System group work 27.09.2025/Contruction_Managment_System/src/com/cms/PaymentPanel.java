package com.cms;

import com.util.DB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PaymentPanel extends JPanel implements ActionListener {
    private JTextField amountTxt, paymentDateTxt, paymentMethodTxt;
    private JButton addBtn, updateBtn, deleteBtn, loadBtn;
    private JTable paymentTable;
    private DefaultTableModel model;

    public PaymentPanel() {
        setLayout(new BorderLayout());

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("Amount:"));
        amountTxt = new JTextField();
        inputPanel.add(amountTxt);

        inputPanel.add(new JLabel("Payment Date (YYYY-MM-DD):"));
        paymentDateTxt = new JTextField();
        inputPanel.add(paymentDateTxt);

        inputPanel.add(new JLabel("Payment Method:"));
        paymentMethodTxt = new JTextField();
        inputPanel.add(paymentMethodTxt);

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

        // Table for payments
        model = new DefaultTableModel(new String[]{"PaymentID", "Amount", "PaymentDate", "PaymentMethod"}, 0);
        paymentTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(paymentTable);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Event listeners
        addBtn.addActionListener(this);
        updateBtn.addActionListener(this);
        deleteBtn.addActionListener(this);
        loadBtn.addActionListener(this);

        paymentTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = paymentTable.getSelectedRow();
                if (row >= 0) {
                    amountTxt.setText(model.getValueAt(row, 1).toString());
                    paymentDateTxt.setText(model.getValueAt(row, 2).toString());
                    paymentMethodTxt.setText(model.getValueAt(row, 3).toString());
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addBtn) addPayment();
        else if (e.getSource() == updateBtn) updatePayment();
        else if (e.getSource() == deleteBtn) deletePayment();
        else if (e.getSource() == loadBtn) loadPayments();
    }

    private void addPayment() {
        String amount = amountTxt.getText().trim();
        String date = paymentDateTxt.getText().trim();
        String method = paymentMethodTxt.getText().trim();

        if (amount.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Amount is required");
            return;
        }
        if (date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Payment Date is required");
            return;
        }

        String sql = "INSERT INTO Payment (amount, paymentDate, paymentMethod) VALUES (?, ?, ?)";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBigDecimal(1, new java.math.BigDecimal(amount));
            ps.setDate(2, Date.valueOf(date));
            ps.setString(3, method);
            int res = ps.executeUpdate();
            if (res > 0) {
                JOptionPane.showMessageDialog(this, "Payment added successfully");
                loadPayments();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding payment: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void updatePayment() {
        int row = paymentTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a payment to update");
            return;
        }
        int paymentID = Integer.parseInt(model.getValueAt(row, 0).toString());

        String amount = amountTxt.getText().trim();
        String date = paymentDateTxt.getText().trim();
        String method = paymentMethodTxt.getText().trim();

        if (amount.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Amount is required");
            return;
        }
        if (date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Payment Date is required");
            return;
        }

        String sql = "UPDATE Payment SET amount=?, paymentDate=?, paymentMethod=? WHERE paymentID=?";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBigDecimal(1, new java.math.BigDecimal(amount));
            ps.setDate(2, Date.valueOf(date));
            ps.setString(3, method);
            ps.setInt(4, paymentID);
            int res = ps.executeUpdate();
            if (res > 0) {
                JOptionPane.showMessageDialog(this, "Payment updated successfully");
                loadPayments();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating payment: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void deletePayment() {
        int row = paymentTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a payment to delete");
            return;
        }
        int paymentID = Integer.parseInt(model.getValueAt(row, 0).toString());

        int confirm = JOptionPane.showConfirmDialog(this, "Confirm delete payment?", "Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM Payment WHERE paymentID=?";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, paymentID);
            int res = ps.executeUpdate();
            if (res > 0) {
                JOptionPane.showMessageDialog(this, "Payment deleted successfully");
                loadPayments();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error deleting payment: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void loadPayments() {
        model.setRowCount(0);
        String sql = "SELECT * FROM Payment";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("paymentID"),
                        rs.getBigDecimal("amount"),
                        rs.getDate("paymentDate"),
                        rs.getString("paymentMethod")
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading payments: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
