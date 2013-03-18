package com.a51integrated.sfs2x;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

public class Disconnect extends BaseServerEventHandler {

	public void handleServerEvent(ISFSEvent event) throws SFSException {

		MyExt parentEx = (MyExt) getParentExtension();
		HashMap<String,User> users=parentEx.getUsers(); // get the hashmap with all the current users conected
		HashMap<String,InvitationRoom> gamesInCreation = parentEx.getGamesInCreation(); // Get the games in creation.
		User player=(User) event.getParameter(SFSEventParam.USER); // get the player that fired the event
		users.remove(player.getName());	//remove the player from the hashmap
		
		@SuppressWarnings("unchecked")
		List<Room> rooms= (List<Room>) event.getParameter(SFSEventParam.JOINED_ROOMS); // list of all the rooms he was joinend
		Iterator<Room> itrooms=rooms.iterator();
		Room room= itrooms.next();
		if (room.getName()!="The Lobby"){// send a message to all the players he was playing with
			
			List<User> list=room.getUserList(); // list with all the players of the room he was joined
			Iterator<User> iterator=list.iterator();
			ISFSObject rtn = new SFSObject();
			rtn.putUtfString("res", player.getName()+" got disconnected");
			while (iterator.hasNext()){
				parentEx.send("meter1", rtn, iterator.next());
			}
		}
		if(gamesInCreation.containsKey(player.getName())){
			 ISFSArray accplayer = gamesInCreation.get(player.getName()).getAcceptedPlayers();
		        for (int j=0; j<accplayer.size();j++){ //Sends the response to the joinned players.
		        	if(accplayer.getUtfString(j) != player.getName())
		        	parentEx.send("LeaderLeft", null, users.get(accplayer.getUtfString(j)));
		        }
		        
		        gamesInCreation.remove(player.getName());
			
		}
		else{
			@SuppressWarnings("rawtypes")
			Set set = gamesInCreation.entrySet();

			@SuppressWarnings("rawtypes")
			Iterator iter = set.iterator();
			boolean found = false;
			while (iter.hasNext() && !found) { //Gets the keys of the hashmap. This keys are the user names
			@SuppressWarnings({ "rawtypes" })
			Map.Entry entry = (Map.Entry) iter.next();
			if(gamesInCreation.get(entry.getKey()).getAcceptedPlayers().contains(player.getName())){
				gamesInCreation.get(entry.getKey()).putRefused(player.getName());
				
				ISFSArray accepted = new SFSArray();
				 accepted = gamesInCreation.get(entry.getKey()).getAcceptedPlayers();
				
				ISFSArray refused = new SFSArray();
				 refused = gamesInCreation.get(entry.getKey()).getRefusedPlayers();
				
		       ISFSObject rtn = new SFSObject();
		       rtn.putSFSArray("acceptedPlayers", accepted);
		       rtn.putSFSArray("refusedPlayers", refused);
		       ISFSArray accplayer = gamesInCreation.get(entry.getKey()).getAcceptedPlayers();
		       for (int j=0; j<accplayer.size();j++){ //Sends the response to the joinned players.
		       	parentEx.send("AcceptedRefused", rtn, users.get(accplayer.getUtfString(j)));
		       }
		       found =true;
			}
			}
		}
		
		try{
			Queue<User> queue1 = parentEx.getQueue1();
			queue1.remove(player);
		}catch(Exception e){};
		try{
			Queue<User[]> queue2 = parentEx.getQueue2();
			Iterator<User[]> it=queue2.iterator();
			boolean enc=false;
			User[] arrayUsers=null;
			int i;
			while(it.hasNext()&&enc==false){
				arrayUsers=it.next();
					i=0;
					while(i<arrayUsers.length&&enc==false){
						enc= arrayUsers[i]==player;
						i++;
					}
			}
			if(enc==true){
				if(arrayUsers[0]!=player)this.send("OutOfQueue", new SFSObject(), arrayUsers[0]);
				if(arrayUsers[1]!=player)this.send("OutOfQueue", new SFSObject(), arrayUsers[1]);
				queue2.remove(arrayUsers);		
			}
		}catch(Exception e){};
		try{
			Queue<User[]> queue3 = parentEx.getQueue3();
			Iterator<User[]> it=queue3.iterator();
			boolean enc=false;
			User[] arrayUsers=null;
			int i;
			while(it.hasNext()&&enc==false){
				arrayUsers=it.next();
					i=0;
					while(i<arrayUsers.length && enc==false){
						enc= arrayUsers[i]==player;
						i++;
					}
			}
			if(enc==true){
				if(arrayUsers[0]!=player)this.send("OutOfQueue", new SFSObject(), arrayUsers[0]);
				if(arrayUsers[1]!=player)this.send("OutOfQueue", new SFSObject(), arrayUsers[1]);
				if(arrayUsers[2]!=player)this.send("OutOfQueue", new SFSObject(), arrayUsers[2]);
				queue3.remove(arrayUsers);
			}
		}catch(Exception e){};
		/*Set<String> names= users.keySet(); //TODO tell friends he was disconected
		Iterator<String> it=names.iterator();
		if (it.hasNext()){				// here we tell to another user the disconection of this player (TO DO "tell" friends got disconnected)
			 String otherplayer=it.next();
			 User oplayer=users.get(otherplayer);
			 ISFSObject rtn = new SFSObject();
			 rtn.putUtfString("res", player.getName()+" got disconnected");
			 parentEx.send("meter1", rtn, oplayer);
		}*/
		parentEx.setUsers(users);

	}

}
