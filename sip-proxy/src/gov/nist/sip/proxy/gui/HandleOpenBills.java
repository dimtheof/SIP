package gov.nist.sip.proxy.gui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import gov.nist.sip.proxy.Billing_Server;
import gov.nist.sip.proxy.Billing_Server.Policy_and_debt;

public class HandleOpenBills {
	
	private Billing_Server billing_serv;
	public void handle(){
		try{
			long tStart, tEnd;
			String caller;
			billing_serv = new Billing_Server();
			Connection con=DriverManager.getConnection(  
	          	    "jdbc:mysql://localhost:3306/soft_eng_database","root","root");
	          	    PreparedStatement stmt = null;
	          	    PreparedStatement update_debt = null;
	          	    String query = "SELECT timestamp FROM soft_eng_database.Checkpoint WHERE idCheckpoint = 1";
	          	    stmt = con.prepareStatement(query);
	          	    ResultSet rs = stmt.executeQuery();
	          	  	if(!rs.next())
	          	  		return;
	          	    tEnd = rs.getLong(1);
	          	    System.out.println("got timestamp "+tEnd);
	          	    query="SELECT Calls_caller,Calls_start_time FROM soft_eng_database.Calls "
	          	    		+ "WHERE Calls_charge = 1";
	          	    stmt = con.prepareStatement(query);
	          	    rs = stmt.executeQuery();
	          	    System.out.println("bfr while...");
	          	    while(rs.next()){
	          	    	System.out.println("Open bill...");
	          	    	caller = rs.getString(1);
					    tStart = rs.getLong(2);
						long tDelta = tEnd - tStart;
						long elapsedSeconds = (long) (tDelta / 1000.0);
						double factor=0.;
						Policy_and_debt pad = billing_serv.GetPolicy_and_Debt(caller);
						
						if(pad.Policy.equals("high")) factor=1.0;
						else if(pad.Policy.equals("medium")) factor=2.0;
						else if(pad.Policy.equals("low")) factor=3.0;
						double new_debt=pad.debt+elapsedSeconds*factor;
						
						String updateQuery="UPDATE soft_eng_database.BIlling SET Billing_Debt= ? where Billing_username = ? ";
					    update_debt = con.prepareStatement(updateQuery);
					    update_debt.setString(2,caller);
					    update_debt.setDouble(1,new_debt);
					    update_debt.executeUpdate();
					    
					    String Query="UPDATE soft_eng_database.Calls SET Calls_charge = 0 where  Calls_caller = ?";
					    update_debt = con.prepareStatement(Query);
					    update_debt.setString(1,caller);
					    update_debt.executeUpdate();
						
	          	    }
	          	    
	          	    	
			}
			catch(SQLException e){
				throw new IllegalStateException("SQLError!", e);
			}
	}

}
