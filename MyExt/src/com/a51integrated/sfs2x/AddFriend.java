package com.a51integrated.sfs2x;


import java.sql.Connection;
import java.sql.PreparedStatement;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class AddFriend extends BaseClientRequestHandler {

	public void handleClientRequest(User player, ISFSObject params) {
		String friend=params.getUtfString("friend");
		MyExt parentEx=(MyExt) getParentExtension(); 
		ISFSObject response = new SFSObject();
		try {
			Connection connection = getParentExtension().getParentZone().getDBManager().getConnection();// catch the manager of the db
			PreparedStatement stmt= connection.prepareStatement("INSERT INTO friends VALUES (?,?)");
					stmt.setString(1, player.getName());
					stmt.setString(2, friend);
					int rowsafected= stmt.executeUpdate();
					if (rowsafected!=1){ // if the rows affected on the query were more than 1 send an error to the user
			        	response.putUtfString("res", "Error");
						send("addFriendRes", response, player);
			        }else{
			        	stmt.setString(1, friend);
			        	stmt.setString(2, player.getName());
			        	rowsafected= stmt.executeUpdate();
						if (rowsafected!=1){ // if the rows affected on the query were more than 1 send an error to the user
				        	response.putUtfString("res", "Error");
							send("addFriend", response, player);
				        }else{
				        	response.putUtfString("res", "Success");
				        	send("addFriend", response, player);
				        	//TODO send the success to both!
				        }
			        }
		} catch (Exception e) {
			response.putUtfString("res", "Error");
			send("addFriend", response, player);
		}
     
	}
}