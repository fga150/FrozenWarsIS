package com.a51integrated.sfs2x;

import java.util.HashMap;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class ModExternalPlayers extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(User player, ISFSObject params) {
		
		MyExt parentEx = (MyExt) getParentExtension(); 
		HashMap<String,InvitationRoom> gamesInCreation = parentEx.getGamesInCreation();
		HashMap<String,User> users = parentEx.getUsers();
		boolean externalPlayers = params.getBool("externalPlayers");
		
		gamesInCreation.get(player.getName()).setEnableExternalPlayers(externalPlayers);
		parentEx.setGamesInCreation(gamesInCreation);

		
        ISFSObject rtn = new SFSObject();
        rtn.putBool("externalPlayers", externalPlayers);
        ISFSArray accplayer = gamesInCreation.get(player.getName()).getAcceptedPlayers();
        for (int j=0; j<accplayer.size();j++){ //Sends the response to the joinned players.
        	parentEx.send("ModExternalPlayers", rtn, users.get(accplayer.getUtfString(j)));
        }
		
        
	}


}