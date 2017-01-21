import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class Billing_server {

			private long tStart ;
			private long tEnd ;
			private long tDelta ;
			private double elapsedSeconds;
			private String Username;
			private String Policy;
			private double debt;
			private Connection con;
			
			
			
			public Billing_server(String username)
			{
				try{
				Class.forName("com.mysql.jdbc.Driver");  
			    con=DriverManager.getConnection(  
			    "jdbc:mysql://localhost:3306/soft_eng_database","root","root");
				}
				catch(Exception e){}
				
				this.Username=username;
				GetPolicy_and_Debt();	
			}
			
			public void GetPolicy_and_Debt(){
				try{   
				   
				    PreparedStatement select_login= null;
				    String selectQuery="select Billing_policy,Billing_Debt from soft_eng_database.BIlling where Billing_username = ? ";
				    select_login = con.prepareStatement(selectQuery);
				    select_login.setString(1,Username);
				   // System.out.println(userName);
				    //System.out.println(new String(password));
				    
				    
				    int exists=0;	 
				    ResultSet rs;
				    rs=select_login.executeQuery();
				    rs.next();
				    Policy=rs.getString(1);
				    debt=rs.getDouble(2);
				}
				catch(Exception e){}
				
			}
			
			public void Timer_start(){				
				this.tStart=	System.currentTimeMillis();
			}
			public void Timer_end(){				
				this.tEnd=	System.currentTimeMillis();
				this.tDelta = tEnd - tStart;
				this.elapsedSeconds = tDelta / 1000.0;
				double factor=0.;
				if(Policy.equals("high")) factor=1.0;
				else if(Policy.equals("medium")) factor=2.0;
				else if(Policy.equals("low")) factor=3.0;
				
				double new_debt=debt+elapsedSeconds*factor;
				try{   
					   
				    PreparedStatement update_debt= null;
				    String selectQuery="UPDATE soft_eng_database.BIlling set Billing_Debt= ? where Billing_username = ? ";
				    update_debt = con.prepareStatement(selectQuery);
				    update_debt.setString(2,Username);
				    update_debt.setDouble(1,new_debt);
				   // System.out.println(userName);
				    //System.out.println(new String(password));
				    update_debt.executeUpdate();
				   

				}
				catch(Exception e){}
				
				
				
				
			}
			
			
}
