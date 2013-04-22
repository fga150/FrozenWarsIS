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
		
		
		HashMap<String,InvitationRoom> gamesInCreation = parentEx.getGamesInCreation();
		HashMap<String,User> users = parentEx.getUsers();
		int mode = gamesInCreation.get(player.getName()).getGameMode();
		
        ISFSArray accplayer = gamesInCreation.get(player.getName()).getAcceptedPlayers();
        for (int j=0; j<accplayer.size();j++){ //Sends the queue-waiting message to the joinned players.
        	parentEx.send("modInQueue", null, users.get(accplayer.getUtfString(j)));
        }
        
        gamesInCreation.remove(player.getName());
        parentEx.setGamesInCreation(gamesInCreation);
		
        if(mode == 0){
		Queue<User[]> queue3 = parentEx.getQueue03();
		User[] aux= new User[3];// we set the players into an array
		aux[0]=player;
		aux[1]=parentEx.getUsers().get(params.getUtfString("pfriend1"));
		aux[2]=parentEx.getUsers().get(params.getUtfString("pfriend2"));
		queue3.add(aux);// add the array to the queue
		
		
		Queue<User> queue1=parentEx.getQueue01();
		if(queue3.size() == 1 && queue1.size()== 1){ //here we see if with this players we can create a room
			User[] aux2= queue3.poll();
			parentEx.creatGame(queue1.poll(), aux2[0], aux2[1], aux2[2]); // call to the method of creating a room
			parentEx.setQueue01(queue1);// update the queue for single players
		}
		parentEx.setQueue03(queue3); // update the queue for groups of 3
	}
        else if (mode == 1){
        	Queue<User[]> queue3 = parentEx.getQueue13();
    		User[] aux= new User[3];// we set the players into an array
    		aux[0]=player;
    		aux[1]=parentEx.getUsers().get(params.getUtfString("pfriend1"));
    		aux[2]=parentEx.getUsers().get(params.getUtfString("pfriend2"));
    		queue3.add(aux);// add the array to the queue
    		
    		
    		Queue<User> queue1=parentEx.getQueue11();
    		if(queue3.size() == 1 && queue1.size()== 1){ //here we see if with this players we can create a room
    			User[] aux2= queue3.poll();
    			parentEx.creatGame(queue1.poll(), aux2[0], aux2[1], aux2[2]); // call to the method of creating a room
    			parentEx.setQueue11(queue1);// update the queue for single players
    		}
    		parentEx.setQueue13(queue3); // update the queue for groups of 3
        }
        
        else if (mode == 2){
        	Queue<User[]> queue3 = parentEx.getQueue23();
    		User[] aux= new User[3];// we set the players into an array
    		aux[0]=player;
    		aux[1]=parentEx.getUsers().get(params.getUtfString("pfriend1"));
    		aux[2]=parentEx.getUsers().get(params.getUtfString("pfriend2"));
    		queue3.add(aux);// add the array to the queue
    		
    		
    		Queue<User> queue1=parentEx.getQueue21();
    		if(queue3.size() == 1 && queue1.size()== 1){ //here we see if with this players we can create a room
    			User[] aux2= queue3.poll();
    			parentEx.creatGame(queue1.poll(), aux2[0], aux2[1], aux2[2]); // call to the method of creating a room
    			parentEx.setQueue21(queue1);// update the queue for single players
    		}
    		parentEx.setQueue23(queue3); // update the queue for groups of 3
        }
        
        else if (mode == 3){
        	
        	Queue<User[]> queue3 = parentEx.getQueue33();
    		User[] aux= new User[3];// we set the players into an array
    		aux[0]=player;
    		aux[1]=parentEx.getUsers().get(params.getUtfString("pfriend1"));
    		aux[2]=parentEx.getUsers().get(params.getUtfString("pfriend2"));
    		queue3.add(aux);// add the array to the queue
    		
    		
    		Queue<User> queue1=parentEx.getQueue31();
    		if(queue3.size() == 1 && queue1.size()== 1){ //here we see if with this players we can create a room
    			User[] aux2= queue3.poll();
    			parentEx.creatGame(queue1.poll(), aux2[0], aux2[1], aux2[2]); // call to the method of creating a room
    			parentEx.setQueue31(queue1);// update the queue for single players
    		}
    		parentEx.setQueue33(queue3); // update the queue for groups of 3
        }
        
        else if (mode == 4){
        	
        	Queue<User[]> queue3 = parentEx.getQueue43();
    		User[] aux= new User[3];// we set the players into an array
    		aux[0]=player;
    		aux[1]=parentEx.getUsers().get(params.getUtfString("pfriend1"));
    		aux[2]=parentEx.getUsers().get(params.getUtfString("pfriend2"));
    		queue3.add(aux);// add the array to the queue
    		
    		
    		Queue<User> queue1=parentEx.getQueue41();
    		if(queue3.size() == 1 && queue1.size()== 1){ //here we see if with this players we can create a room
    			User[] aux2= queue3.poll();
    			parentEx.creatGame(queue1.poll(), aux2[0], aux2[1], aux2[2]); // call to the method of creating a room
    			parentEx.setQueue41(queue1);// update the queue for single players
    		}
    		parentEx.setQueue43(queue3); // update the queue for groups of 3
        }
	}

}
