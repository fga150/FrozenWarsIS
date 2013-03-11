package com.a51integrated.sfs2x;

import java.util.HashMap;
import java.util.Vector;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class ModeChange extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(User player, ISFSObject params) {
		
		MyExt parentEx = (MyExt) getParentExtension(); 
		HashMap<String,InvitationRoom> gamesInCreation = parentEx.getGamesInCreation();
		HashMap<String,User> users = parentEx.getUsers();
		int mode = params.getInt("mode");
		
		gamesInCreation.get(player.getName()).setGameMode(mode);
		parentEx.setGamesInCreation(gamesInCreation);

		
        ISFSObject rtn = new SFSObject();
        rtn.putInt("mode", mode);
        Vector<String> accplayer = gamesInCreation.get(player.getName()).getAcceptedPlayers();
        for (int j=0; j<accplayer.size();j++){ //Sends the response to the joinned players.
        	parentEx.send("ModeChange", rtn, users.get(accplayer.get(j)));
        }
		
        
	}


}