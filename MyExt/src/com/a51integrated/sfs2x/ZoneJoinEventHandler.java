package com.a51integrated.sfs2x;

import java.util.HashMap;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
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
	}	
}