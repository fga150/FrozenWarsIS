package com.a51integrated.sfs2x;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
		ISFSObject rtn = new SFSObject();
		ISFSArray connectedFriends = new SFSArray();
		
		HashMap<String,User> users = parentEx.getUsers(); //Gets the hasmap which contains the connected users.
		@SuppressWarnings("rawtypes")
		Set set = users.entrySet();

		@SuppressWarnings("rawtypes")
		Iterator iter = set.iterator();

		while (iter.hasNext()) { //Gets the keys of the hasmap. This keys are the user names
		@SuppressWarnings("rawtypes")
		Map.Entry entry = (Map.Entry) iter.next();
		connectedFriends.addUtfString((String) entry.getKey());
		}
		
		rtn.putSFSArray("ConnectedFriends", connectedFriends);
		parentEx.send("GetConnectedFriends", rtn, player); //Sends the object which contains the connected users
		
		
		
		
		
		
        
	}


}