package com.a51integrated.sfs2x;

import java.util.Iterator;

import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class GameMessage extends BaseClientRequestHandler {

	public void handleClientRequest(User player, ISFSObject params) {
		MyExt parentEx = (MyExt) getParentExtension();
		ISFSObject rtn = new SFSObject();
		rtn.putUtfString("nada","1"); //Prepare the SFSObject which is going to be sent with the server time.
		parentEx.send("hola", rtn, player);
		Room room=player.getJoinedRooms().iterator().next(); //get the room of the player
		Iterator<User> it=room.getPlayersList().iterator();
		while (it.hasNext()){ // send it to all users of the room
			parentEx.send("GameMessage",params, it.next());
		}
	}
}