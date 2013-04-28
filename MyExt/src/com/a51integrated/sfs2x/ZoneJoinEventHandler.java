package com.a51integrated.sfs2x;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

public class ZoneJoinEventHandler extends BaseServerEventHandler{

	@Override
	public void handleServerEvent(ISFSEvent event) throws SFSException {
		
		MyExt parentEx = (MyExt) getParentExtension(); // the user is added to the hashmap
		HashMap<String,User> users = parentEx.getUsers();
		User player = (User) event.getParameter(SFSEventParam.USER);
		users.put(player.getName(), player);
		parentEx.setUsers(users);
		Room lobby = getParentExtension().getParentZone().getRoomByName("The Lobby");
		getApi().joinRoom(player, lobby);  // the user joins the room The lobby
		ISFSObject rtn = new SFSObject();
		rtn.putUtfString("Response", "Success");
		parentEx.send("ConnectRes", rtn, player);
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