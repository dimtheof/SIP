package gov.nist.sip.proxy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class BlockingService {
	public Vector<String> getBlocked_and_unBlocked(String username){
		try{
			Vector<String> blunbl=new Vector<String>();
			Connection con=DriverManager.getConnection(  
	          	    "jdbc:mysql://localhost:3306/soft_eng_database","root","root");  
	          	    PreparedStatement stmt = null;
	          	    String query="SELECT * FROM soft_eng_database.Blocking "
	          	    		+ "WHERE Blocker=?";
	          	    stmt = con.prepareStatement(query);
	          	    stmt.setString(1,username);
	          	    ResultSet rs = stmt.executeQuery();
	          	    
	          	    while(rs.next()){
	          	    	blunbl.addElement(rs.getString(2));
	          	    }
	          	    blunbl.addElement("next");
	          	    query="SELECT reg_username as Username "+
	    	    		    "FROM soft_eng_database.Registrations as R "+
	    	    		    "where not exists(select * from soft_eng_database.Blocking where Blocker= ? and Blocked=R.reg_username ) and reg_username!= ?" ;
	    	    		
		    	    PreparedStatement unblocked_st= null;
		    	    unblocked_st= con.prepareStatement(query);
		    	    unblocked_st.setString(1,username);
		    	    unblocked_st.setString(2,username);
		    	    //System.out.println(selectQuery);
		    	    rs=unblocked_st.executeQuery();
		    	    while(rs.next()){  
		    	    	//System.out.println(rs2.getString(1));
		    	    	blunbl.addElement(rs.getString(1));
		    	    
		    	    	}
		    	    con.close();
		    	    return blunbl;
			}
			catch(SQLException e){
				throw new IllegalStateException("SQLError!", e);
			}
	}
	public void block(String username,String Blocked){
		try{  
	  	    //Class.forName("com.mysql.jdbc.Driver");  
	  	    Connection con=DriverManager.getConnection(  
	  	    "jdbc:mysql://localhost:3306/soft_eng_database","root","root");  
	  	   
	  	    
	  	    PreparedStatement add_block= null;
	  	    String updateQuery="INSERT INTO soft_eng_database.Blocking VALUES (?,?);";
	  	    add_block = con.prepareStatement(updateQuery);
	  	    add_block.setString(1,username);
	  	    add_block.setString(2,Blocked);
	  	    add_block.executeUpdate();
	  	    
	  	  con.close();
	  	    
	  	    
	  	   // System.out.println(userName);
	  	    //System.out.println(new String(password));
	  	    
	      
	     }
	      catch(SQLException e1)
	     {
	    	  System.err.println("Message: " + e1.getMessage());
	     
	     }
		
		return ;
	
	}	
	
	public void unblock(String username,String Blocked){
		try{  
	  	    //Class.forName("com.mysql.jdbc.Driver");  
	  	    Connection con=DriverManager.getConnection(  
	  	    "jdbc:mysql://localhost:3306/soft_eng_database","root","root");  
	  	   
	  	    
	  	    PreparedStatement rm_block= null;
	  	    String updateQuery="DELETE from soft_eng_database.Blocking where Blocker= ? and Blocked= ? ";
	  	    rm_block = con.prepareStatement(updateQuery);
	  	    rm_block.setString(1,username);
	  	    rm_block.setString(2,Blocked);
	  	    rm_block.executeUpdate();
	  	    
	  	  con.close();
	  	    
	  	    
	  	   // System.out.println(userName);
	  	    //System.out.println(new String(password));
	  	    
	      
	     }
	      catch(SQLException e1)
	     {
	    	  System.err.println("Message: " + e1.getMessage());
	     
	     }
		return ;
	
	}
}
