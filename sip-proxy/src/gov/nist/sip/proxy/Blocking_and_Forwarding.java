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

		private Vector usersWhoBlockedMe=new Vector();
		private boolean i_am_blocked;
		private String caller;
		private String callee;
		private Vector forwUsers=new Vector();
		
		
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
	
		public String check_block(){   // R E T U R N      F I N A L    C A L L E E 
			
			try{  
          	    //Class.forName("com.mysql.jdbc.Driver");  
          	    Connection con=DriverManager.getConnection(  
          	    "jdbc:mysql://localhost:3306/soft_eng_database","root","root");  
          	    
          	    PreparedStatement check_block= null;
          	    String updateQuery="select * from Blocking where Blocked= ? ";
          	    check_block = con.prepareStatement(updateQuery);
          	    check_block.setString(1,caller);
          	    
          	    //check_block.setString(2,callee);
          	    ResultSet rs;
          	    rs=check_block.executeQuery();
          	    i_am_blocked=false;
          	    while(rs.next()){
          	    	System.out.println(rs.getString(1));
          	    	usersWhoBlockedMe.addElement(rs.getString(1));
          	    	
          	    }
          	    
          	    
          	   // System.out.println(userName);
          	    //System.out.println(new String(password));

            
            if(usersWhoBlockedMe.contains(callee)){

            	return  null;
            }
            
            int i = 0;
		    do{
		    	String query = "select Forwarding_user from Forwarding where Username = \""+callee+"\"";
		    	Statement stmt = con.createStatement();
			     rs = stmt.executeQuery(query);
			    if(rs.next()){
			    	callee = rs.getString(1);
			    	System.out.println(callee);
			    	if(usersWhoBlockedMe.contains(callee)){
		            	return null;
		            }
			    	if(forwUsers.contains(callee)){
			    		JOptionPane.showMessageDialog(null,
				    		    "Cycle exists",
				    		    "Error",
				    		    JOptionPane.ERROR_MESSAGE);
				    	System.out.println("User does not exist");
			    		return "CYCLE";
			    	}
			    	forwUsers.addElement(callee);
			    }
			    else break;
		    	
		    } while (++i<50);
		    if(i==50){
		    	return null;
		    }
		    
		  } catch (SQLException e) {
		      throw new IllegalStateException("Cannot connect the database!", e);
		}

			return callee;
			
			
		}
}
