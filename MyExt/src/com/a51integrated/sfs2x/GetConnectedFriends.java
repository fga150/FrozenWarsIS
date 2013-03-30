package com.a51integrated.sfs2x;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;


public class GetConnectedFriends extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(User player, ISFSObject params) {
		
		MyExt parentEx = (MyExt) getParentExtension();
		HashMap<String,InvitationRoom> gamesInCreation = parentEx.getGamesInCreation();
		ISFSObject rtn = new SFSObject();
		ISFSArray connectedFriends = new SFSArray();
		
		HashMap<String,User> users = parentEx.getUsers(); //Gets the hasmap which contains the connected users.

		/*@SuppressWarnings("rawtypes")
		Iterator iter = users.entrySet().iterator();

		while (iter.hasNext()) { //Gets the keys of the hashmap. This keys are the user names
		@SuppressWarnings("rawtypes")
		Map.Entry entry = (Map.Entry) iter.next();
		if(gamesInCreation.containsKey(player.getName())){
		if(!gamesInCreation.get(player.getName()).getAcceptedPlayers().contains(entry.getKey()) && !gamesInCreation.get(player.getName()).getWaitingPlayers().contains(entry.getKey())
			&& !users.get(entry.getKey()).getLastJoinedRoom().isDynamic())
			connectedFriends.addUtfString((String) entry.getKey());
		}else 
			if(!player.getName().equals(entry.getKey()) && !users.get(entry.getKey()).getLastJoinedRoom().isDynamic())
			connectedFriends.addUtfString((String) entry.getKey());
		}*/
		try {
			Connection connection = getParentExtension().getParentZone().getDBManager().getConnection();// catch the manager of the db
			PreparedStatement stmt = connection.prepareStatement("SELECT friend FROM friends WHERE name=? AND status=?");
		    stmt.setString(1, player.getName());
		    stmt.setString(2, "c");
			ResultSet res= stmt.executeQuery(); // send a query to know all friends with the mode said
        	while (res.next()) {
        		if (users.get(res.getString(1))!=null)
        			if(gamesInCreation.containsKey(player.getName())){
	        			if(!gamesInCreation.get(player.getName()).getAcceptedPlayers().contains(res.getString(1)) && !gamesInCreation.get(player.getName()).getWaitingPlayers().contains(res.getString(1))
	        					&& !users.get(res.getString(1)).getLastJoinedRoom().isDynamic())
	        			connectedFriends.addUtfString(res.getString(1));
        			}else{ 
        				if(!player.getName().equals(res.getString(1)) && !users.get(res.getString(1)).getLastJoinedRoom().isDynamic())
        				connectedFriends.addUtfString(res.getString(1));
        			}
			}
		}catch(Exception e){
			
		}
		rtn.putSFSArray("ConnectedFriends", connectedFriends);
		parentEx.send("GetConnectedFriends", rtn, player); //Sends the object which contains the connected users	      
	}
}