package com.a51integrated.sfs2x;

import java.util.HashMap;
import java.util.Vector;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class Accept extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(User player, ISFSObject params) {
		
		MyExt parentEx = (MyExt) getParentExtension(); 
		HashMap<String,InvitationRoom> gamesInCreation = parentEx.getGamesInCreation();
		String player2 = params.getUtfString("Inviter"); //Gets the name of the player who invited you.
		
		Vector<Player> players = gamesInCreation.get(player2).getPlayers();		
		boolean found = false;
		int i = 0;
		while (!found && i<players.size()) //search the player on the vector of the invitation room.
		{
			if (players.get(i).getName().equals(player.getName())){
				players.get(i).setState("Accepted"); //changes his state to accepted.
				gamesInCreation.get(player2).setAcceptedPlayers(gamesInCreation.get(player2).getAcceptedPlayers()+1); //increments the number of accepted players of the invitation room.
				gamesInCreation.get(player2).setPlayers(players);
				parentEx.setGamesInCreation(gamesInCreation);
				found = true;
			}
			i++;
		}
		
        
	}


}
