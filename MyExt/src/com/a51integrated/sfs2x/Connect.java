package com.a51integrated.sfs2x;

import java.util.HashMap;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class Connect extends BaseClientRequestHandler {


	public void handleClientRequest(User player, ISFSObject params) {
		
		MyExt parentEx = (MyExt) getParentExtension(); // the user is added to the hashmap
		HashMap<String,User> users = parentEx.getUsers();
		users.put(player.getName(), player);
		parentEx.setUsers(users);
	}	
}