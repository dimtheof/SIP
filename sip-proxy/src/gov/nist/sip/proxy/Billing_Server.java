package gov.nist.sip.proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sip.address.URI;
import javax.sip.header.FromHeader;
import javax.sip.header.ToHeader;
import javax.sip.message.Request;
import javax.swing.JOptionPane;


public class Billing_Server {

			
			public class Policy_and_debt{
				public double debt;
				public String Policy;
			}
			private Connection con;
			
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
			
			
			public Billing_Server()
			{
				try{
				Class.forName("com.mysql.jdbc.Driver");  
			    con=DriverManager.getConnection(  
			    "jdbc:mysql://localhost:3306/soft_eng_database","root","root");
				}
				catch(Exception e){}
			}
			
			public Policy_and_debt GetPolicy_and_Debt(String Caller){
				try{   
				   
				    PreparedStatement select_login= null;
				    String selectQuery="select Billing_policy,Billing_Debt from soft_eng_database.BIlling where Billing_username = ? ";
				    select_login = con.prepareStatement(selectQuery);
				    select_login.setString(1,Caller);
				   // System.out.println(userName);
				    //System.out.println(new String(password));
				    
				    
				    int exists=0;	 
				    ResultSet rs;
				    rs=select_login.executeQuery();
				    rs.next();
				    Policy_and_debt pad= new Policy_and_debt();
				    pad.Policy=rs.getString(1);
				    pad.debt=rs.getDouble(2);
				    return pad;
				}
				catch(SQLException e){
					JOptionPane.showMessageDialog(null,
			    		    e.getMessage()+"POLICY-DEBT",
			    		    "Error",
			    		    JOptionPane.ERROR_MESSAGE);
			    	
				}
				return null;
			}
			
			public void Timer_start(Request request ){				
				long tStart=	System.currentTimeMillis();
				 FromHeader fr= (FromHeader) request.getHeader(FromHeader.NAME);
	             String username = getUsernameFromHeader(fr);
	              	
				try{   
					   
				    PreparedStatement update_debt= null;
				    String Query="UPDATE soft_eng_database.Calls SET Calls_start_time = ? where  Calls_caller = ?";
				    update_debt = con.prepareStatement(Query);
				    update_debt.setLong(1,tStart);
				    update_debt.setString(2,username);
				   // System.out.println(userName);
				    //System.out.println(new String(password));
				    update_debt.executeUpdate();
				    
				    Query="UPDATE soft_eng_database.Calls SET Calls_charge = 1 where  Calls_caller = ?";
				    update_debt = con.prepareStatement(Query);
				    update_debt.setString(1,username);
				   // System.out.println(userName);
				    //System.out.println(new String(password));
				    update_debt.executeUpdate();
				   

				}
				catch(SQLException e){
					JOptionPane.showMessageDialog(null,
			    		    e.getMessage(),
			    		    "Error",
			    		    JOptionPane.ERROR_MESSAGE);
			    	
				}
			}
			public void Timer_end(Request request){		
				FromHeader fr= (FromHeader) request.getHeader(FromHeader.NAME);
	            String fromUsername = getUsernameFromHeader(fr);
	           
	            ToHeader to= (ToHeader) request.getHeader(ToHeader.NAME);
	            String toUsername = getUsernameFromHeader(to);
	    
	            long tStart;
				try{   
					   
				    PreparedStatement update_debt= null;
				    String selectQuery="select Calls_caller,Calls_start_time from soft_eng_database.Calls where (Calls_caller = ? or Calls_caller = ?) and Calls_charge=1";
				    update_debt = con.prepareStatement(selectQuery);
				    update_debt.setString(1,fromUsername);
				    update_debt.setString(2,toUsername);
				   // System.out.println(userName);
				    //System.out.println(new String(password));
				    ResultSet rs = update_debt.executeQuery();
				    rs.next();
				    String caller = rs.getString(1);
				    tStart = rs.getLong(2);
				    long tEnd=	System.currentTimeMillis();
					long tDelta = tEnd - tStart;
					long elapsedSeconds = (long) (tDelta / 1000.0);
					double factor=0.;
					Policy_and_debt pad = GetPolicy_and_Debt(caller);
					if(pad.Policy.equals("high")) factor=1.0;
					else if(pad.Policy.equals("medium")) factor=2.0;
					else if(pad.Policy.equals("low")) factor=3.0;
					double new_debt=pad.debt+elapsedSeconds*factor;
					String updateQuery="UPDATE soft_eng_database.BIlling set Billing_Debt= ? where Billing_username = ? ";
				    update_debt = con.prepareStatement(updateQuery);
				    update_debt.setString(2,caller);
				    update_debt.setDouble(1,new_debt);
				    update_debt.executeUpdate();
				    
				    String Query="UPDATE soft_eng_database.Calls SET Calls_charge = 0 where  Calls_caller = ?";
				    update_debt = con.prepareStatement(Query);
				    update_debt.setString(1,caller);
				   // System.out.println(userName);
				    //System.out.println(new String(password));
				    update_debt.executeUpdate();
					
					
					

				}
				catch(SQLException e){
					JOptionPane.showMessageDialog(null,
			    		    e.getMessage()+"TIMER-END",
			    		    "Error",
			    		    JOptionPane.ERROR_MESSAGE);
			    	
				}
				
				
				
				
				
			}
			
			
}
