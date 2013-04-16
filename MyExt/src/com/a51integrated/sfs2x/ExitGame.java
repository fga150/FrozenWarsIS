package com.a51integrated.sfs2x;

import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSJoinRoomException;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class ExitGame extends BaseClientRequestHandler {
	
	public void handleClientRequest(User player, ISFSObject params) {
		
		ISFSObject rtn = new SFSObject();
		
		if (player.getLastJoinedRoom().getName()!="The Lobby"){
			MyExt parentEx = (MyExt) getParentExtension();
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
		
	}

}
