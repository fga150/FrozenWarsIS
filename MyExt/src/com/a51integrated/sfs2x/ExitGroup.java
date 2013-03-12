package com.a51integrated.sfs2x;

import java.util.HashMap;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class ExitGroup extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(User player, ISFSObject params) {
		
		MyExt parentEx = (MyExt) getParentExtension(); 
		HashMap<String,InvitationRoom> gamesInCreation = parentEx.getGamesInCreation();
		HashMap<String,User> users = parentEx.getUsers();
		String player2 = params.getUtfString("Inviter"); //Gets the name of the player who invited you.
			
		if(player2.equals(player.getName())){
	        ISFSArray accplayer = gamesInCreation.get(player2).getAcceptedPlayers();
	        for (int j=0; j<accplayer.size();j++){ //Sends the response to the joinned players.
	        	parentEx.send("LeaderLeft", null, users.get(accplayer.getUtfString(j)));
	        }
	        
	        gamesInCreation.remove(player2);
		}
		else{
		gamesInCreation.get(player2).putRefused(player.getName());
		
		ISFSArray accepted = new SFSArray();
		 accepted = gamesInCreation.get(player2).getAcceptedPlayers();
		
		ISFSArray refused = new SFSArray();
		 refused = gamesInCreation.get(player2).getRefusedPlayers();
		
       ISFSObject rtn = new SFSObject();
       rtn.putSFSArray("acceptedPlayers", accepted);
       rtn.putSFSArray("refusedPlayers", refused);
       ISFSArray accplayer = gamesInCreation.get(player2).getAcceptedPlayers();
       for (int j=0; j<accplayer.size();j++){ //Sends the response to the joinned players.
       	parentEx.send("AcceptedRefused", rtn, users.get(accplayer.getUtfString(j)));
       }
		
	}
	}
}
