package com.a51integrated.sfs2x;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSJoinRoomException;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class ExitGame extends BaseClientRequestHandler {
	
	public void handleClientRequest(User player, ISFSObject params) {
		
		ISFSObject rtn = new SFSObject();
		MyExt parentEx = (MyExt) getParentExtension();
		HashMap<String,User> users=parentEx.getUsers(); // get the hashmap with all the current users conected
		
		if (player.getLastJoinedRoom().getName()!="The Lobby"){
			player.getLastJoinedRoom().removeUser(player);//remove the user from the game room
			Room lobby = getParentExtension().getParentZone().getRoomByName("The Lobby");
			try {
				getApi().joinRoom(player, lobby);// the user joins the room The lobby
				rtn.putUtfString("res", "Success");
				parentEx.send("ExitGameRes", rtn, player);
			} catch (SFSJoinRoomException e) {
				rtn.putUtfString("res", "Error");
				parentEx.send("ExitGameRes", rtn, player);
			}  
		}
		
		Connection connection=null;
		try {
			connection = getParentExtension().getParentZone().getDBManager().getConnection();// catch the manager of the db
			PreparedStatement stmt = connection.prepareStatement("SELECT friend FROM friends WHERE name=? AND status=?");
		    stmt.setString(1, player.getName());
		    stmt.setString(2, "c");
			ResultSet res= stmt.executeQuery(); // send a query to know all friends with the mode said
        	while (res.next()) {
        		if (users.get(res.getString(1))!=null)
        			parentEx.send("UpdateFriendList", rtn, users.get(res.getString(1)));
        	}
        	connection.close();
		}catch(Exception e){}
		
	}

}
