
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;



public class Forwarding {
	public static String Forward(String caller, String callee){
		String url = "jdbc:mysql://localhost:3306/soft_eng_database";
		String dbusername = "root";
		String dbpassword = "root";
		String query;
		System.out.println("Connecting database...");
		Vector<String> forwUsers = new Vector<String>();
		forwUsers.addElement(callee);
		forwUsers.addElement(caller);
		try (Connection connection = DriverManager.getConnection(url, dbusername, dbpassword)) {
			
		    
		    int i = 0;
		    do{
		    	query = "select Forwarding_user from Forwarding where Username = \""+callee+"\"";
		    	Statement stmt = connection.createStatement();
			    ResultSet rs = stmt.executeQuery(query);
			    if(rs.next()){
			    	callee = rs.getString(1);
			    	if(forwUsers.contains(callee))
			    		return "-1";
			    	forwUsers.addElement(callee);
			    }
			    else return callee;
		    	
		    } while (++i<50);
		    return "-1";
		    
		  } catch (SQLException e) {
		      throw new IllegalStateException("Cannot connect the database!", e);
		}
	}
	
	public static void main(String[] args){
		System.out.println("Forwarding from petros to kanell21 \n"
				+ "final callee is "+Forward("petros","kanell21"));
		
	}
}
