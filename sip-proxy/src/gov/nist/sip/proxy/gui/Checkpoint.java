package gov.nist.sip.proxy.gui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Checkpoint implements Runnable{

	@Override
	public void run() {
		Connection con;
		while (true){
			long time;
			try {
				con = DriverManager.getConnection(  
				  	    "jdbc:mysql://localhost:3306/soft_eng_database","root","root");
				PreparedStatement stmt = null;
	      	    String query="UPDATE soft_eng_database.Checkpoint SET timestamp = ?"
	      	    		+ " WHERE idCheckpoint = 1";
	      	    stmt = con.prepareStatement(query);
	      	    time = System.currentTimeMillis();
	      	    stmt.setLong(1, time);
	      	    stmt.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	        try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		
	}
	public static void main(String args[]) {
	        (new Thread(new Checkpoint())).start();
	    }

}
