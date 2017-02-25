package gov.nist.sip.proxy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterServices {

	public boolean isRegistered(String username, String password){
		try{
			Connection con=DriverManager.getConnection(  
	          	    "jdbc:mysql://localhost:3306/soft_eng_database","root","root");  
	          	    PreparedStatement stmt = null;
	          	    String query="SELECT * FROM soft_eng_database.Registrations "
	          	    		+ "WHERE reg_username = ? AND reg_pass = ?";
	          	    stmt = con.prepareStatement(query);
	          	    stmt.setString(1,username);
	          	    stmt.setString(2,password);
	          	    ResultSet rs = stmt.executeQuery();
	          	    
	          	    if(rs.next()){
	          	    	
	          	    	return true;
	          	    }
	          	    else
	          	    	return false;
			}
			catch(SQLException e){
				throw new IllegalStateException("SQLError!", e);
			}
	}
	
	public void insertRegistration(String username, String password, String email){
		try{
			Connection con=DriverManager.getConnection(  
	          	    "jdbc:mysql://localhost:3306/soft_eng_database","root","root");  
	          	    PreparedStatement stmt = null;
	          	    String query="INSERT INTO soft_eng_database.Registrations (reg_username, reg_pass, reg_email_address)"
	          	    		+ " VALUES (?, ?, ?)";
	          	    stmt = con.prepareStatement(query);
	          	    stmt.setString(1,username);
	          	    stmt.setString(2,password);
	          	    stmt.setString(3,email);
	          	    stmt.executeUpdate();
	          	    
	          	    query="INSERT INTO soft_eng_database.Calls (Calls_caller, Calls_tcallee, Calls_charge, Calls_start_time)"
	          	    		+ " VALUES (?, ?, ?, ?)";
	          	    stmt = con.prepareStatement(query);
	          	    stmt.setString(1,username);
	          	    stmt.setString(2,"none");
	          	    stmt.setInt(3, 0);
	          	    stmt.setInt(4, 0);
	          	    stmt.executeUpdate();
	          	    
	          	    query="INSERT INTO soft_eng_database.BIlling (Billing_username, Billing_Policy, Billing_Debt)"
	          	    		+ " VALUES (?, ?, ?)";
	          	    stmt = con.prepareStatement(query);
	          	    stmt.setString(1,username);
	          	    stmt.setString(2,"high");
	          	    stmt.setInt(3, 0);
	          	    stmt.executeUpdate();
	          	    
			}
			catch(SQLException e){
				throw new IllegalStateException("SQLError!", e);
			}
	}
	
}
