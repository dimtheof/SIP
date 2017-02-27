package gov.nist.sip.proxy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class ForwardingService {
	public Vector<String> getForwardingList(String username){
		try{
			Vector<String> list=new Vector<String>();
			Connection con=DriverManager.getConnection(  
	          	    "jdbc:mysql://localhost:3306/soft_eng_database","root","root");  
	          	    PreparedStatement stmt = null;
	          	    String query="SELECT Forwarding_user FROM soft_eng_database.Forwarding "
	          	    		+ "WHERE Username = ?";
	          	    stmt = con.prepareStatement(query);
	          	    stmt.setString(1,username);
	          	    ResultSet rs = stmt.executeQuery();
	          	    if(rs.next()){
	          	    	list.addElement(rs.getString(1));
	          	    }
	          	    else
	          	    	list.addElement("none");
	          	    
	          	    query="SELECT reg_username as Username FROM soft_eng_database.Registrations "+
	    	    		    "where reg_username != ?" ;
	    	    		
		    	    stmt= null;
		    	    stmt= con.prepareStatement(query);
		    	    stmt.setString(1,username);
		    	    rs=stmt.executeQuery();
		    	    while(rs.next()){  
		    	    	list.addElement(rs.getString(1));
		    	    
		    	    	}
		    	    con.close();
		    	    return list;
			}
			catch(SQLException e){
				throw new IllegalStateException("SQLError!", e);
			}
	}
	
	
	public void addForwarding(String username, String forwardee){
		try{
			Connection con=DriverManager.getConnection(  
	          	    "jdbc:mysql://localhost:3306/soft_eng_database","root","root");  
	          	    PreparedStatement stmt = null;
	          	    String query="DELETE FROM soft_eng_database.Forwarding "
	          	    		+ "WHERE Username = ?";
	          	    stmt = con.prepareStatement(query);
	          	    stmt.setString(1,username);
	          	    stmt.executeUpdate();
	          	    
	          	    if(forwardee == null) return;
	          	    if(forwardee.equals("null")) return;
	          	  
	          	    query="INSERT INTO soft_eng_database.Forwarding "+
	    	    		    " VALUES (?,?)" ;
	    	    		
		    	    stmt= null;
		    	    stmt= con.prepareStatement(query);
		    	    stmt.setString(1,username);
		    	    stmt.setString(2,forwardee);
		    	    stmt.executeUpdate();
		    	    con.close();
		    	    return;
		    	    
			}
			catch(SQLException e){
				throw new IllegalStateException("SQLError!", e);
			}
	}
}
