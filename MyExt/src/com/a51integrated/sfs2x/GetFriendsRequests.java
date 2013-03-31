package com.a51integrated.sfs2x;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class GetFriendsRequests extends BaseClientRequestHandler{

	public void handleClientRequest(User player, ISFSObject params) {
		
		MyExt parentEx=(MyExt) getParentExtension(); 
		ISFSObject response = new SFSObject();
		ISFSArray friends = new SFSArray();
		ISFSArray friends2 = new SFSArray();
		Connection connection=null;
		try {
			connection = getParentExtension().getParentZone().getDBManager().getConnection();// catch the manager of the db
			PreparedStatement stmt = connection.prepareStatement("SELECT friend FROM friends WHERE name=? AND status=?");
		    stmt.setString(1, player.getName());
		    stmt.setString(2, "r");
			ResultSet res= stmt.executeQuery(); // send a query to know all friends with the mode said
        	while (res.next()) {
        		 friends.addUtfString(res.getString(1));
			}
        	stmt = connection.prepareStatement("SELECT friend FROM friends WHERE name=? AND status=?");
		    stmt.setString(1, player.getName());
		    stmt.setString(2, "a");
			res= stmt.executeQuery(); // send a query to know all friends with the mode said
        	while (res.next()) {
        		 friends2.addUtfString(res.getString(1));
			}
        	if(friends.size()>0 || friends2.size()>0){
        		response.putSFSArray("Friends", friends);
        		response.putSFSArray("Friends2", friends2);
        		parentEx.send("BeFriends?", response, player); //Sends the object which contains the friend requests
        	}
		}catch(Exception e){
			
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
