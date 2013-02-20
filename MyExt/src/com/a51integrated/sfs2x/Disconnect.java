package com.a51integrated.sfs2x;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

public class Disconnect extends BaseServerEventHandler {

	public void handleServerEvent(ISFSEvent event) throws SFSException {

		MyExt parentEx = (MyExt) getParentExtension();
		HashMap<String,User> users=parentEx.getUsers(); // get the hashmap with all the current users conected
		User player=(User) event.getParameter(SFSEventParam.USER); // get the player that fired the event
		users.remove(player.getName());	//remove the player from the hashmap
		
		List<Room> rooms= (List<Room>) event.getParameter(SFSEventParam.JOINED_ROOMS); // list of all the rooms he was joinend
		Iterator<Room> itrooms=rooms.iterator();
		Room room= itrooms.next();
		if (room.getName()!="The Lobby"){// send a message to all the players he was playing with
			
			List<User> list=room.getUserList(); // list with all the players of the room he was joined
			Iterator<User> iterator=list.iterator();
			ISFSObject rtn = new SFSObject();
			rtn.putUtfString("res", player.getName()+" got disconnected2");
			while (iterator.hasNext()){
				parentEx.send("meter1", rtn, iterator.next());
			}
		}
		
		Set<String> names= users.keySet();
		Iterator<String> it=names.iterator();
		if (it.hasNext()){				// here we tell to another user the disconection of this player (TO DO "tell" friends got disconnected)
			 String otherplayer=it.next();
			 User oplayer=users.get(otherplayer);
			 ISFSObject rtn = new SFSObject();
			 rtn.putUtfString("res", player.getName()+" got disconnected");
			 parentEx.send("meter1", rtn, oplayer);
		}
		parentEx.setUsers(users);

	}

}
