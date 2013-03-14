package com.a51integrated.sfs2x;

import java.util.HashMap;
import java.util.Queue;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class Put3Handler extends BaseClientRequestHandler {
	
	public void handleClientRequest(User player, ISFSObject params) { // a group of 3 want to join a game

		MyExt parentEx = (MyExt) getParentExtension();
		Queue<User[]> queue3 = parentEx.getQueue3();
		User[] aux= new User[3];// we set the players into an array
		aux[0]=player;
		aux[1]=parentEx.getUsers().get(params.getUtfString("pfriend1"));
		aux[2]=parentEx.getUsers().get(params.getUtfString("pfriend2"));
		queue3.add(aux);// add the array to the queue
		
		HashMap<String,InvitationRoom> gamesInCreation = parentEx.getGamesInCreation();
		HashMap<String,User> users = parentEx.getUsers();
		
        ISFSArray accplayer = gamesInCreation.get(player.getName()).getAcceptedPlayers();
        for (int j=0; j<accplayer.size();j++){ //Sends the queue-waiting message to the joinned players.
        	parentEx.send("modInQueue", null, users.get(accplayer.getUtfString(j)));
        }
		Queue<User> queue1=parentEx.getQueue1();
		if(queue3.size() == 1 && queue1.size()== 1){ //here we see if with this players we can create a room
			User[] aux2= queue3.poll();
			parentEx.creatGame(queue1.poll(), aux2[0], aux2[1], aux2[2]); // call to the method of creating a room
			parentEx.setQueue1(queue1);// update the queue for single players
		}
		parentEx.setQueue3(queue3); // update the queue for groups of 3
	}

}
