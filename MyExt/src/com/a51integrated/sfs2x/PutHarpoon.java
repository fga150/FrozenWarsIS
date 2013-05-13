	package com.a51integrated.sfs2x;
	import java.util.Date;
import java.util.Iterator;
	
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
	
	
	public class PutHarpoon extends BaseClientRequestHandler {
	
		@Override
		public void handleClientRequest(User player, ISFSObject params) {
			
			MyExt parentEx=(MyExt) getParentExtension();
			Date time = new Date(); //Create a new Date object
			params.putLong("time", time.getTime()+3000); //time right now + 3000 ms is the time when the bomb is going to explote
			Room room=player.getJoinedRooms().iterator().next(); //get the room of the player
			Iterator<User> it=room.getPlayersList().iterator();
			while (it.hasNext()){ // send it to all users of the room
				parentEx.send("putHarpoon",params, it.next());
			}
			/*try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}
			Iterator<User> it2=room.getPlayersList().iterator();
			while (it2.hasNext()){ // send it to all users of the room
				parentEx.send("exploteHarpoon",params, it2.next());
			}*/
		}
	
	
	}
