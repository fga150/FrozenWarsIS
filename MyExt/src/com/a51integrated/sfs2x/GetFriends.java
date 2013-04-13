package com.a51integrated.sfs2x;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class GetFriends  extends BaseClientRequestHandler {

	public void handleClientRequest(User player, ISFSObject params) {
		MyExt parentEx = (MyExt) getParentExtension();
		ISFSObject rtn = new SFSObject();
		ISFSArray connectedFriends = new SFSArray();
		ISFSArray disconnectedFriends = new SFSArray();
		ISFSArray playingFriends = new SFSArray();
		Connection connection=null;
		HashMap<String,User> users = parentEx.getUsers(); //Gets the hasmap which contains the connected users.
		try {
			connection = getParentExtension().getParentZone().getDBManager().getConnection();
			PreparedStatement stmt = connection.prepareStatement("SELECT friend FROM friends WHERE name=? AND status=?");
		    stmt.setString(1, player.getName());
		    stmt.setString(2, "c");
			ResultSet res= stmt.executeQuery(); // send a query to know all friends he has
	    	while (res.next()) {
	    		if (users.get(res.getString(1))!=null){//si esta conectado
	    			if(users.get(res.getString(1)).getLastJoinedRoom().equals("The Lobby")){//connected but not playing
	    				connectedFriends.addUtfString(res.getString(1));
	    			}else{//connected and playing
	    				playingFriends.addUtfString(res.getString(1));
	    			}
	    		}else{//not connected
	    			disconnectedFriends.addUtfString(res.getString(1));
	    		}
	    	}
		} catch (SQLException e) {
		}
		finally{
			try {// Return connection to the DBManager connection pool
				connection.close();
			} catch (SQLException e) {
			}
		}
		rtn.putSFSArray("ConnectedFriends", connectedFriends);
		rtn.putSFSArray("DisconnectedFriends", disconnectedFriends);
		rtn.putSFSArray("PlayingFriends", playingFriends);
		parentEx.send("getMyFriendsRequestRes", rtn, player); //Sends the object which contains the users friends
	}

}
