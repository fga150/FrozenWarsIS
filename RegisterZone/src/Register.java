import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class Register extends BaseClientRequestHandler {

		public void handleClientRequest(User player, ISFSObject params) {
			RegisterZone parentEx=(RegisterZone) getParentExtension(); 
			ISFSObject response = new SFSObject();
			String user=params.getUtfString("name"); // get the name, email and password that sends the user
			String pword=params.getUtfString("pword");
			String email=params.getUtfString("email");
			Connection connection = null;
			try {
				connection = parentEx.dbmanager.getConnection();
				PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(name) FROM users WHERE name=?");
			    stmt.setString(1, user);
				ResultSet res= stmt.executeQuery(); // send a query to know if there are more users with that name already
		        if (!res.first()){
		        	response.putUtfString("res", "Error"); // if the query sends nothing send an error to the user
					send("dbRegister", response, player);
		        }else{
					if (res.getInt(1)!=0){ // if there is more than 1 user with that name tell him to try another
						response.putUtfString("res", "UserExits");
				        send("dbRegister", response, player);
					}else{        // if see if the email dosn't owe to a user
						stmt = connection.prepareStatement("SELECT COUNT(email) FROM users WHERE email=?");
					    stmt.setString(1, email);
						res= stmt.executeQuery(); // send a query to know if there are more users with that email already
				        if (!res.first()){
				        	response.putUtfString("res", "Error"); // if the query sends nothing send an error to the user
							send("dbRegister", response, player);
				        }else{
							if (res.getInt(1)!=0){ // if there is more than 1 user with that email tell him to try another
								response.putUtfString("res", "EmailExits");
						        send("dbRegister", response, player);
							}else{
								stmt= connection.prepareStatement("INSERT INTO users VALUES (?,?,?)");
								stmt.setString(1, user);
								stmt.setString(2, pword);
								stmt.setString(3, email);
								int rowsafected= stmt.executeUpdate();
								if (rowsafected!=1){ // if the rows affected on the query were more than 1 send an error to the user
						        	response.putUtfString("res", "Error");
									send("dbRegister", response, player);
						        }else{ // send the succes to the user
									response.putUtfString("res", "Registered");
							        send("dbRegister", response, player);
						        }
							}
				        }
					}
		        }
			} catch (Exception e) {
				response.putUtfString("res", "Error");
				send("dbRegister", response, player);
			}
			finally{
				// Return connection to the DBManager connection pool
				try {
					connection.close();
				} catch (SQLException e) {
				}
			}
	     
		}
}