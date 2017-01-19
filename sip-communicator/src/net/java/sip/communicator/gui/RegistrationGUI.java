package net.java.sip.communicator.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.mysql.jdbc.PreparedStatement;


public class RegistrationGUI extends JDialog{

		private static final long serialVersionUID = 1L;
		private JFrame frmRegistrationForm;
		private JTextField txtEmail;
		private JTextField txtUsername;
		private JPasswordField passwordField;
		private JLabel lblNewLabel;
		private JLabel lblNewLabel_1;
		private JLabel lblNewLabel_2;
		private JButton btnNewButton;

		/**
		 * Launch the application.
		 */


		/**
		 * Create the application.
		 */
		public RegistrationGUI() {
			initialize();
			pack();
			this.setModal(true);
			this.toFront();
		}

		/**
		 * Initialize the contents of the frame.
		 */
		private void initialize() {
			
			frmRegistrationForm = new JFrame();
			frmRegistrationForm.setAlwaysOnTop(true);
			frmRegistrationForm.setTitle("Registration Form");
			frmRegistrationForm.setBounds(100, 100, 375, 286);
			frmRegistrationForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frmRegistrationForm.setVisible(true);
			frmRegistrationForm.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			JPanel panel = new JPanel();
			panel.setToolTipText("");
			frmRegistrationForm.getContentPane().add(panel, BorderLayout.WEST);
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[]{34, 89, 89, 89, 86, 0};
			gbl_panel.rowHeights = new int[]{23, 23, 0, 0, 0, 0, 0, 30, 31, 0, 0};
			gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
			gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			panel.setLayout(gbl_panel);
			
			txtUsername = new JTextField();
			
			lblNewLabel = new JLabel("Username");
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel.gridx = 1;
			gbc_lblNewLabel.gridy = 2;
			panel.add(lblNewLabel, gbc_lblNewLabel);
			GridBagConstraints gbc_txtUsername = new GridBagConstraints();
			gbc_txtUsername.gridwidth = 2;
			gbc_txtUsername.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtUsername.insets = new Insets(0, 0, 5, 5);
			gbc_txtUsername.gridx = 2;
			gbc_txtUsername.gridy = 2;
			panel.add(txtUsername, gbc_txtUsername);
			txtUsername.setColumns(10);
			
			lblNewLabel_1 = new JLabel("email");
			lblNewLabel_1.setToolTipText("");
			GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
			gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel_1.gridx = 1;
			gbc_lblNewLabel_1.gridy = 4;
			panel.add(lblNewLabel_1, gbc_lblNewLabel_1);
			
			txtEmail = new JTextField();
			txtEmail.addPropertyChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent arg0) {
				}
			});
			txtEmail.setToolTipText("");
			GridBagConstraints gbc_txtEmail = new GridBagConstraints();
			gbc_txtEmail.gridwidth = 2;
			gbc_txtEmail.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtEmail.insets = new Insets(0, 0, 5, 5);
			gbc_txtEmail.gridx = 2;
			gbc_txtEmail.gridy = 4;
			panel.add(txtEmail, gbc_txtEmail);
			txtEmail.setColumns(10);
			
			lblNewLabel_2 = new JLabel("Password");
			GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
			gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel_2.gridx = 1;
			gbc_lblNewLabel_2.gridy = 6;
			panel.add(lblNewLabel_2, gbc_lblNewLabel_2);
			
			passwordField = new JPasswordField();
			passwordField.setToolTipText("");
			GridBagConstraints gbc_passwordField = new GridBagConstraints();
			gbc_passwordField.gridwidth = 2;
			gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
			gbc_passwordField.insets = new Insets(0, 0, 5, 5);
			gbc_passwordField.gridx = 2;
			gbc_passwordField.gridy = 6;
			panel.add(passwordField, gbc_passwordField);
			
			btnNewButton = new JButton("Submit");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String username = txtUsername.getText();
					char[] password = passwordField.getPassword();
					String email = txtEmail.getText();
					
					String url = "jdbc:mysql://localhost:3306/soft_eng_database";
					String dbusername = "root";
					String dbpassword = "root";
					String query;
					System.out.println("Connecting database...");

					try (Connection connection = DriverManager.getConnection(url, dbusername, dbpassword)) {
					    System.out.println("Database connected!");
					    query = "select * from registrations where reg_username = \""+username+"\"";
					    Statement stmt = connection.createStatement();
					    ResultSet rs = stmt.executeQuery(query);
					    if(rs.next()){
					    	JOptionPane.showMessageDialog(frmRegistrationForm, "Username already exists.");
					    	return;
					    }
					    else System.out.println("acceptable username");
					    query = "select * from registrations where reg_email_address = \""+email+"\"";
					    stmt = connection.createStatement();
					    rs = stmt.executeQuery(query);
					    if(rs.next()){
					    	JOptionPane.showMessageDialog(frmRegistrationForm, "Email already registered.");
					    	return;
					    }
					    else System.out.println("acceptable email");
					    query = " insert into registrations (reg_username, reg_pass, reg_email_address)"
					            + " values (?, ?, ?)";
					    PreparedStatement preparedStmt = (PreparedStatement) connection.prepareStatement(query);
					    preparedStmt.setString(1,username);
					    preparedStmt.setString(2,new String(password));
					    preparedStmt.setString(3,email);
					    if(preparedStmt.executeUpdate()==0){
					    	JOptionPane.showMessageDialog(frmRegistrationForm, "Registration failed.");
					    	System.out.println("Registration failed.");
					    }
					    else{
					    	JOptionPane.showMessageDialog(frmRegistrationForm, "Registration complete.");
					    	System.out.println("Registration complete.");
					    }
					    frmRegistrationForm.dispatchEvent(new WindowEvent(frmRegistrationForm, WindowEvent.WINDOW_CLOSING));
					} catch (SQLException e) {
					    throw new IllegalStateException("Cannot connect the database!", e);
					}
				}
			});
			GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
			gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
			gbc_btnNewButton.gridx = 2;
			gbc_btnNewButton.gridy = 8;
			panel.add(btnNewButton, gbc_btnNewButton);
		}
	
}