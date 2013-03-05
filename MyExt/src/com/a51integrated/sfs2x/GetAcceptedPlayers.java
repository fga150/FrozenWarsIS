package com.a51integrated.sfs2x;

import java.util.HashMap;
import java.util.Vector;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class GetAcceptedPlayers extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(User player, ISFSObject params) {
		
		MyExt parentEx = (MyExt) getParentExtension(); 
		HashMap<String,InvitationRoom> gamesInCreation = parentEx.getGamesInCreation();
		String player2 = params.getUtfString("Inviter"); //Gets the name of the player who invited you.
		Vector<Player> players = gamesInCreation.get(player2).getPlayers();
		
		ISFSArray accepted = new SFSArray();
		
		for(int i=0; i<players.size();i++){
			if(players.get(i).getState().equals("Accepted")){ //adds the players who accepted the invitation to a SFSArray.
				accepted.addUtfString(players.get(i).getName());
			}
		}
		
        ISFSObject rtn = new SFSObject();
        rtn.putSFSArray("acceptedPlayers", accepted);
        parentEx.send("GetAcceptedPlayers", rtn, player);//sends back the response with all the accepted players.
	}


}
