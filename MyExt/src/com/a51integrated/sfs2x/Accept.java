package com.a51integrated.sfs2x;

import java.util.HashMap;
import java.util.Vector;

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
		
		gamesInCreation.get(player2).putAccepted(player.getName());
		parentEx.setGamesInCreation(gamesInCreation);

		ISFSArray accepted = new SFSArray();
		Vector<String> acceptedPlayers = gamesInCreation.get(player2).getAcceptedPlayers();
		for(int i=0; i<acceptedPlayers.size();i++){
				accepted.addUtfString(acceptedPlayers.get(i)); //Adds the player who accepted the invitation
		}
		
		ISFSArray waiting = new SFSArray();
		Vector<String> waitingPlayers = gamesInCreation.get(player2).getWaitingPlayers();
		for(int i=0; i<waitingPlayers.size();i++){
				waiting.addUtfString(waitingPlayers.get(i)); //Adds the players who are waiting.
		}
		
        ISFSObject rtn = new SFSObject();
        rtn.putSFSArray("acceptedPlayers", accepted);
        rtn.putSFSArray("waitingPlayers", waiting);
        Vector<String> accplayer = gamesInCreation.get(player2).getAcceptedPlayers();
        for (int j=0; j<accplayer.size();j++){ //Sends the response to the joinned players.
        	parentEx.send("AcceptedWaiting", rtn, users.get(accplayer.get(j)));
        }
		
        
	}


}
