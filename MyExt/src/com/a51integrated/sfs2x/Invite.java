package com.a51integrated.sfs2x;

import java.util.HashMap;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class Invite extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(User player, ISFSObject params) {
		
		MyExt parentEx = (MyExt) getParentExtension(); 
		HashMap<String,User> users = parentEx.getUsers();
		HashMap<String,InvitationRoom> gamesInCreation = parentEx.getGamesInCreation();
		String player2 = params.getUtfString("Invited"); //Gets the name of the player who's going to be invited.
		
		if(!gamesInCreation.containsKey(player.getName())){ //If this is the first invitation from that player, an invitation room is needed.
			InvitationRoom room = new InvitationRoom(player.getName());
			gamesInCreation.put(player.getName(), room);
		}
		
		if(users.containsKey(player2)){//If the player is connected, the invitation is sent.
			ISFSObject rtn = new SFSObject();
			rtn.putUtfString("Inviter", player.getName());
			parentEx.send("Invite", rtn, users.get(player2));//Sends the invitation to the player.
				
			InvitationRoom room = gamesInCreation.get(player.getName());
			room.putWaiting(player2);//Adds the player as invited on the invitation room.
			
			ISFSArray waiting = new SFSArray();
			 waiting = gamesInCreation.get(player.getName()).getWaitingPlayers();
			
			ISFSArray refused = new SFSArray();
			 refused = gamesInCreation.get(player.getName()).getRefusedPlayers();
			
			ISFSObject rtn2 = new SFSObject();
			rtn2.putSFSArray("waitingPlayers", waiting);
			rtn2.putSFSArray("refusedPlayers", refused);
			ISFSArray accplayer = gamesInCreation.get(player.getName()).getAcceptedPlayers();
		        for (int j=0; j<accplayer.size();j++){ //Sends the response to the joinned players.
		        	parentEx.send("RefusedWaiting",rtn2 , users.get(accplayer.getUtfString(j)));//Sends the waiting players to all the joinned ones.
		        }
		}
		else{
			ISFSObject rtn = new SFSObject(); //If the player is not connected, a message is sent to the game leader.
			rtn.putUtfString("Invited", player2);
			parentEx.send("InviteFail", rtn, player);
		}
		
		
		parentEx.setGamesInCreation(gamesInCreation);
        
	}


}