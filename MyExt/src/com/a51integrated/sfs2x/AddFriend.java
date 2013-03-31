package com.a51integrated.sfs2x;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class AddFriend extends BaseClientRequestHandler {

	public void handleClientRequest(User player, ISFSObject params) {
		String friend=params.getUtfString("friend");
		String res=params.getUtfString("res");
		MyExt parentEx=(MyExt) getParentExtension(); 
		ISFSObject response = new SFSObject();
		Connection connection=null;
		if (res.equals("yes")){
			try {
				connection = getParentExtension().getParentZone().getDBManager().getConnection();// catch the manager of the db
				PreparedStatement stmt= connection.prepareStatement("UPDATE friends SET status=? WHERE name=? AND friend=?;");
						stmt.setString(1, "c");
						stmt.setString(2, player.getName());
						stmt.setString(3, friend);
						int rowsafected= stmt.executeUpdate();
						if (rowsafected!=1){ // if the rows affected on the query were more than 1 send an error to the user
				        	response.putUtfString("res", "Error");
							send("AddFriendRes", response, player);
				        }else{
				        	stmt.setString(1, "a");
				        	stmt.setString(2, friend);
				        	stmt.setString(3, player.getName());
				        	rowsafected= stmt.executeUpdate();
							if (rowsafected!=1){ // if the rows affected on the query were more than 1 send an error to the user
					        	response.putUtfString("res", "Error");
								send("AddFriendRes", response, player);
					        }else{
					        	response.putUtfString("res", "SuccessYes");
					        	send("AddFriendRes", response, player);
					        	User uFriend=parentEx.getUsers().get(friend);
					        	if(uFriend!=null){
					        		if(uFriend.getJoinedRooms().iterator().next().getName().equals("The Lobby")){//si esta conectado y no jugando el amigo le mando la peticion directamente
					        		ISFSObject response2 = new SFSObject();
					        		ISFSArray friends = new SFSArray();
					        		friends.addUtfString(friend);
					        		response2.putSFSArray("Friends2", friends);
					        		send("BeFriends?",response2,uFriend);
					        		}
					        	}
					        }
				        }
						connection.close();
			} catch (Exception e) {
				response.putUtfString("res", "Error");
				send("AddFriendRes", response, player);
			}
		}
		if (res.equals("no")){
			try {
				connection = getParentExtension().getParentZone().getDBManager().getConnection();// catch the manager of the db
				PreparedStatement stmt= connection.prepareStatement("DELETE FROM friends WHERE name=? AND friend=?;");
			    stmt.setString(1, player.getName());
			    stmt.setString(2, friend);
			    stmt.executeUpdate();
			    stmt.setString(2, player.getName());
			    stmt.setString(1, friend);
			    stmt.executeUpdate();
				response.putUtfString("res", "SuccessNo");
			    send("AddFriendRes", response, player);
				connection.close();
			} catch (SQLException e) {
				response.putUtfString("res", "Error");
				send("AddFriendRes", response, player);
			}
			
		}
		// Return connection to the DBManager connection pool
		try {
			connection.close();
		} catch (SQLException e) {
		}
     
	}
}