package com.a51integrated.sfs2x;

import java.util.HashMap;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class Accept extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(User player, ISFSObject params) {
		
		MyExt parentEx = (MyExt) getParentExtension(); 
		HashMap<String,InvitationRoom> gamesInCreation = parentEx.getGamesInCreation();
		HashMap<String,User> users = parentEx.getUsers();
		String player2 = params.getUtfString("Inviter"); //Gets the name of the player who invited you.
		
		if(!gamesInCreation.containsKey(player2)){
			parentEx.send("GameNotFound", null, player);
		}
		else{
		if(gamesInCreation.get(player2).getNumPlayers() == 4){
			ISFSObject rtn = new SFSObject();
			rtn.putUtfString("Inviter", player2);
			parentEx.send("GameFull", rtn, player);
			
			gamesInCreation.get(player2).putRefused(player.getName());
			
			ISFSArray refused = new SFSArray();
			refused = gamesInCreation.get(player2).getRefusedPlayers();
		
		ISFSArray waiting = new SFSArray();
			waiting = gamesInCreation.get(player2).getWaitingPlayers();
		
        ISFSObject rtn2 = new SFSObject();
        rtn2.putSFSArray("refusedPlayers", refused);
        rtn2.putSFSArray("waitingPlayers", waiting);
       
        ISFSArray accplayer = gamesInCreation.get(player2).getAcceptedPlayers();
        for (int j=0; j<accplayer.size();j++){ //Sends the response to the joinned players.
        	parentEx.send("RefusedWaiting", rtn2, users.get(accplayer.getUtfString(j)));
        }
			
			
		}
		else{
		gamesInCreation.get(player2).putAccepted(player.getName());
		parentEx.setGamesInCreation(gamesInCreation);

		ISFSArray accepted = new SFSArray();
		 accepted = gamesInCreation.get(player2).getAcceptedPlayers();
		
		ISFSArray waiting = new SFSArray();
		 waiting = gamesInCreation.get(player2).getWaitingPlayers();
		
        ISFSObject rtn = new SFSObject();
        rtn.putSFSArray("acceptedPlayers", accepted);
        rtn.putSFSArray("waitingPlayers", waiting);
        ISFSArray accplayer = gamesInCreation.get(player2).getAcceptedPlayers();
        for (int j=0; j<accplayer.size();j++){ //Sends the response to the joinned players.
        	parentEx.send("AcceptedWaiting", rtn, users.get(accplayer.getUtfString(j)));
        }
		
		int mode = gamesInCreation.get(player2).getGameMode();
        boolean externalP = gamesInCreation.get(player2).isEnableExternalPlayers();
        ISFSObject rtn2 = new SFSObject();
        ISFSObject rtn3 = new SFSObject();
        rtn2.putInt("mode", mode);
        rtn3.putBool("externalPlayers",externalP);
        parentEx.send("ModeChange", rtn2, player);
        parentEx.send("ModExternalPlayers", rtn3, player);
        
		}
		}
	}

}
