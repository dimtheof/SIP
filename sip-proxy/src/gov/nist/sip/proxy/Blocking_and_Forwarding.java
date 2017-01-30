package gov.nist.sip.proxy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.sip.*;
import javax.sip.message.*;
import javax.sip.header.*;
import javax.sip.address.*;
import gov.nist.javax.sip.header.*;
import javax.swing.JOptionPane;

public class Blocking_and_Forwarding {

		
		private String caller;
		private String callee;
		private Vector<String> pathUsers = new Vector<String>();
		
		
		public Blocking_and_Forwarding(Request request )
		{
			ToHeader toh;
			FromHeader frh;
			toh=(ToHeader) request.getHeader(ToHeader.NAME);
			frh=(FromHeader) request.getHeader(FromHeader.NAME);
			
			caller=getUsernameFromHeader(frh); //get caller FROM HEADER
			callee=getUsernameFromHeader(toh); //get callee TO HEADER		
			
		}
		
		private String getUsernameFromHeader(ToHeader header) {
			URI uri = header.getAddress().getURI();
			String uriString = uri.toString();
			return uriString.substring(uriString.indexOf("sip:") + 4,
					uriString.indexOf("@"));
		}

		private String getUsernameFromHeader(FromHeader header) {
			URI uri = header.getAddress().getURI();
			String uriString = uri.toString();
			return uriString.substring(uriString.indexOf("sip:") + 4,
					uriString.indexOf("@"));
	}
		
		
	
		public boolean checkIfForwarding(String username) throws SQLException{
			try{
			Connection con=DriverManager.getConnection(  
	          	    "jdbc:mysql://localhost:3306/soft_eng_database","root","root");  
	          	    PreparedStatement stmt = null;
	          	    String query="select * from soft_eng_database.Forwarding "
	          	    		+ "where Username = ? ";
	          	    stmt = con.prepareStatement(query);
	          	    stmt.setString(1,username);
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
		
		public boolean checkIfBlocked(String blocked, String blocker) throws SQLException{
			try{
			Connection con=DriverManager.getConnection(  
	          	    "jdbc:mysql://localhost:3306/soft_eng_database","root","root");  
	          	    PreparedStatement stmt = null;
	          	    String query="select * from soft_eng_database.Blocking "
	          	    		+ "where Blocker = ? and Blocked = ?";
	          	    stmt = con.prepareStatement(query);
	          	    stmt.setString(1,blocker);
	          	    stmt.setString(2,blocked);
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
		
		public String findFinalReceiver(String caller, String callee){   // R E T U R N      F I N A L    C A L L E E 
			
			try{    
          	    Connection con=DriverManager.getConnection(  
          	    "jdbc:mysql://localhost:3306/soft_eng_database","root","root");  
          	    if(checkIfBlocked(caller,callee) == true){
          	    	return null;
          	    }
          	    else if(caller.equals(callee)){
          	    	return "cycle_detected";
          	    }
          	    else if(checkIfForwarding(callee) == false){
        	    	return callee;
        	    }
          	    else{
          	    		int i = 0;
          	    		PreparedStatement stmt = null;
          	    		String query = null, newCallee = callee;
          	    		ResultSet rs = null;
          	    		pathUsers.clear();
          	    		pathUsers.addElement(caller);
          	    		pathUsers.addElement(callee);
          	    		
          	    	do{
          	    		query = "select Forwarding_user from Forwarding where "
          	    				+ "Username = ?";
    	          	    stmt = con.prepareStatement(query);
    	          	    stmt.setString(1,newCallee);
    	          	    rs = stmt.executeQuery();
        			    if(rs.next()){
        			    	newCallee = rs.getString(1);
        			    	System.err.println("newCallee = "+newCallee);
        			    	if(pathUsers.contains(newCallee)){
        			    		JOptionPane.showMessageDialog(null,
        				    		    "Forwarding cycle exists.",
        				    		    "Error",
        				    		    JOptionPane.ERROR_MESSAGE);
        			    		return "cycle_detected";
        			    	}
        			    	pathUsers.addElement(newCallee);
        			    }
        			    else break;
        		    	
        		    } while (++i<50);
          	    	if(i < 50){
          	    		return newCallee;
          	    	}
          	    	else
          	    		return null;
          	    }
          	    
			 } catch (SQLException e) {
		         throw new IllegalStateException("Cannot connect the database!", e);
		       }		
		}
		
		public boolean amICaller(String username){
			
			try{
				Connection con=DriverManager.getConnection(  
		          	    "jdbc:mysql://localhost:3306/soft_eng_database","root","root");  
		          	    PreparedStatement stmt = null;
		          	    String query="select * from soft_eng_database.Calls "
		          	    		+ "where Calls_caller = ? and Calls_charge = 1";
		          	    stmt = con.prepareStatement(query);
		          	    stmt.setString(1,username);
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
}
