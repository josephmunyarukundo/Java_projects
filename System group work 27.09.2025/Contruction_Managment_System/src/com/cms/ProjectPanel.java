package com.cms;

import com.util.DB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ProjectPanel extends JPanel implements ActionListener {
	private JTextField nameTxt, descriptionTxt, locationTxt, clientTxt;
	private JButton addBtn, updateBtn, deleteBtn, loadBtn;
	private JTable projectsTable;
	private DefaultTableModel model;

	public ProjectPanel() {
		setLayout(new BorderLayout());

		// Input fields
		JPanel inputPanel = new JPanel(new GridLayout(4, 2));
		inputPanel.add(new JLabel("Project Name:"));
		nameTxt = new JTextField();
		inputPanel.add(nameTxt);

		inputPanel.add(new JLabel("Description:"));
		descriptionTxt = new JTextField();
		inputPanel.add(descriptionTxt);

		inputPanel.add(new JLabel("Location:"));
		locationTxt = new JTextField();
		inputPanel.add(locationTxt);

		inputPanel.add(new JLabel("Client:"));
		clientTxt = new JTextField();
		inputPanel.add(clientTxt);

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
		model = new DefaultTableModel(new String[]{"ProjectID", "Name", "Description", "Location", "Client"}, 0);
		projectsTable = new JTable(model);
		JScrollPane tableScroll = new JScrollPane(projectsTable);

		add(inputPanel, BorderLayout.NORTH);
		add(buttonsPanel, BorderLayout.CENTER);
		add(tableScroll, BorderLayout.SOUTH);

		// Attach event listeners
		addBtn.addActionListener(this);
		updateBtn.addActionListener(this);
		deleteBtn.addActionListener(this);
		loadBtn.addActionListener(this);

		// Fill input fields on table row click
		projectsTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row = projectsTable.getSelectedRow();
				if (row != -1) {
					nameTxt.setText(model.getValueAt(row, 1).toString());
					descriptionTxt.setText(model.getValueAt(row, 2).toString());
					locationTxt.setText(model.getValueAt(row, 3).toString());
					clientTxt.setText(model.getValueAt(row, 4).toString());
				}
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addBtn)
			try {
				addProject();
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		else if (e.getSource() == updateBtn)
			try {
				updateProject();
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		else if (e.getSource() == deleteBtn)
			try {
				deleteProject();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		else if (e.getSource() == loadBtn)
			try {
				loadProjects();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	}

	private void addProject() throws Exception {
		String name = nameTxt.getText().trim();
		String description = descriptionTxt.getText().trim();
		String location = locationTxt.getText().trim();
		String client = clientTxt.getText().trim();

		if (name == null || name.trim().isEmpty()) {
		    JOptionPane.showMessageDialog(this, "Project name is required.");
		    return;
		}

		String sql = "INSERT INTO Project (projectName, description, location, client) VALUES (?, ?, ?, ?)";

		try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, name);
			ps.setString(2, description);
			ps.setString(3, location);
			ps.setString(4, client);
			ps.executeUpdate();
			JOptionPane.showMessageDialog(this, "Project added successfully.");
			loadProjects();
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Error adding project: " + ex.getMessage());
			ex.printStackTrace();
		}
		}

		private void updateProject() throws Exception {
			int selectedRow = projectsTable.getSelectedRow();
			if (selectedRow < 0) {
				JOptionPane.showMessageDialog(this, "Select a project to update.");
				return;
			}

			int projectId = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
			String name = nameTxt.getText().trim();
			String description = descriptionTxt.getText().trim();
			String location = locationTxt.getText().trim();
			String client = clientTxt.getText().trim();

			if (name == null || name.trim().isEmpty()) {
			    JOptionPane.showMessageDialog(this, "Project name is required.");
			    return;
			}

			String sql = "UPDATE Project SET projectName=?, description=?, location=?, client=? WHERE projectID=?";

			try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
				ps.setString(1, name);
				ps.setString(2, description);
				ps.setString(3, location);
				ps.setString(4, client);
				ps.setInt(5, projectId);
				ps.executeUpdate();
				JOptionPane.showMessageDialog(this, "Project updated successfully.");
				loadProjects();
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(this, "Error updating project: " + ex.getMessage());
				ex.printStackTrace();
			}
			}

			private void deleteProject() throws Exception {
				int selectedRow = projectsTable.getSelectedRow();
				if (selectedRow < 0) {
					JOptionPane.showMessageDialog(this, "Select a project to delete.");
					return;
				}

				int projectId = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());

				int confirm = JOptionPane.showConfirmDialog(this, "Are you sure want to delete this project?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
				if (confirm != JOptionPane.YES_OPTION) return;

				String sql = "DELETE FROM Project WHERE projectID=?";

				try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
					ps.setInt(1, projectId);
					ps.executeUpdate();
					JOptionPane.showMessageDialog(this, "Project deleted successfully.");
					loadProjects();
				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(this, "Error deleting project: " + ex.getMessage());
					ex.printStackTrace();
				}
			}

			private void loadProjects() throws Exception {
				model.setRowCount(0);

				String sql = "SELECT * FROM Project";

				try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						model.addRow(new Object[]{
								rs.getInt("projectID"),
								rs.getString("projectName"),
								rs.getString("description"),
								rs.getString("location"),
								rs.getString("client")
						});
					}
				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(this, "Error loading projects: " + ex.getMessage());
					ex.printStackTrace();
				}
			}
		}
