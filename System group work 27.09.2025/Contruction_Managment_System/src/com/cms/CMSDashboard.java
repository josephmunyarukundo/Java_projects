package com.cms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Main dashboard frame
public class CMSDashboard extends JFrame implements ActionListener {

    private JPanel contentPanel;

    private JButton dashboardBtn, projectUserBtn, projectBtn, taskBtn, resourceBtn, paymentBtn, categoryBtn;

    public CMSDashboard() {
        super("CONSTRUCTION MANAGEMENT SYSTEM");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top panel for header
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel("CONSTRUCTION MANAGEMENT SYSTEM");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        topPanel.add(titleLabel);

        // Left menu panel
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(new Color(248, 249, 250));
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setPreferredSize(new Dimension(210, getHeight()));

        // Create buttons
        dashboardBtn = new JButton("Dashboard");
        projectUserBtn = new JButton("Users");
        projectBtn = new JButton("Projects");
        taskBtn = new JButton("Tasks");
        resourceBtn = new JButton("Resources");
        paymentBtn = new JButton("Payments");
        categoryBtn = new JButton("Categories");

        JButton[] buttons = {dashboardBtn, projectUserBtn, projectBtn, taskBtn, resourceBtn, paymentBtn, categoryBtn};
        for (JButton b : buttons) {
            b.setAlignmentX(Component.LEFT_ALIGNMENT);
            b.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            b.setBorderPainted(false);
            b.setContentAreaFilled(false);
            b.setFocusPainted(false);
            b.setMaximumSize(new Dimension(210, 40));
            b.addActionListener(this);
            menuPanel.add(b);
            menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        // Content panel where components load
        contentPanel = new JPanel(new BorderLayout());

        // Initial screen: welcome message
        JLabel welcomeLabel = new JLabel("welcome user", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        contentPanel.add(welcomeLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(menuPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        contentPanel.removeAll();

        if (e.getSource() == dashboardBtn) {
            JLabel welcomeLabel = new JLabel("welcome user", SwingConstants.CENTER);
            welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 28));
            contentPanel.add(welcomeLabel, BorderLayout.CENTER);
        } else if (e.getSource() == projectUserBtn) {
            contentPanel.add(new ProjectUserPanel());
        } 
         else if (e.getSource() == projectBtn) {
            contentPanel.add(new ProjectPanel());
        } else if (e.getSource() == taskBtn) {
            contentPanel.add(new TaskPanel());
        } else if (e.getSource() == resourceBtn) {
            contentPanel.add(new ResourcePanel());
        } else if (e.getSource() == paymentBtn) {
            contentPanel.add(new PaymentPanel());
        } else if (e.getSource() == categoryBtn) {
            contentPanel.add(new CategoryPanel());
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    
}
