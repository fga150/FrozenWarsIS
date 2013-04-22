package com.a51integrated.sfs2x;

import java.util.HashMap;
import java.util.Queue;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;

import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class Put2Handler extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(User player, ISFSObject params) { // a groups of 2 want to join a game
		
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
		Queue<User[]> queue2 = parentEx.getQueue02();
		User[] aux= new User[2];// we set in the array the group of 2 players that fired this handler
		aux[0]=player;
		aux[1]=parentEx.getUsers().get(params.getUtfString("pfriend1"));
		queue2.add(aux);// add the array to the queue for groups of 2
		
		Queue<User> queue1=parentEx.getQueue01();
		if(queue2.size()==1 && queue1.size()>=2){ //in the next ifs we see if with this players we can create a room
			User[] aux2= queue2.poll();
			parentEx.creatGame(queue1.poll(), queue1.poll(), aux2[0], aux2[1]);// call to the method of creating a room
			parentEx.setQueue01(queue1);  // update the queue for groups of single players
		}
		
		if(queue2.size()== 2){
			User[] aux2= queue2.poll();
			User[] aux3= queue2.poll();
			parentEx.creatGame(aux2[0], aux2[1], aux3[0], aux3[1]); // call to the method of creating a room
		}
		parentEx.setQueue02(queue2); // update the queue for groups of 2
	}
        
        else if (mode == 1){
        	Queue<User[]> queue2 = parentEx.getQueue12();
    		User[] aux= new User[2];// we set in the array the group of 2 players that fired this handler
    		aux[0]=player;
    		aux[1]=parentEx.getUsers().get(params.getUtfString("pfriend1"));
    		queue2.add(aux);// add the array to the queue for groups of 2
    		
    		Queue<User> queue1=parentEx.getQueue11();
    		if(queue2.size()==1 && queue1.size()>=2){ //in the next ifs we see if with this players we can create a room
    			User[] aux2= queue2.poll();
    			parentEx.creatGame(queue1.poll(), queue1.poll(), aux2[0], aux2[1]);// call to the method of creating a room
    			parentEx.setQueue11(queue1);  // update the queue for groups of single players
    		}
    		
    		if(queue2.size()== 2){
    			User[] aux2= queue2.poll();
    			User[] aux3= queue2.poll();
    			parentEx.creatGame(aux2[0], aux2[1], aux3[0], aux3[1]); // call to the method of creating a room
    		}
    		parentEx.setQueue12(queue2); // update the queue for groups of 2
        }
        
        else if (mode == 2){
        	
        	Queue<User[]> queue2 = parentEx.getQueue22();
    		User[] aux= new User[2];// we set in the array the group of 2 players that fired this handler
    		aux[0]=player;
    		aux[1]=parentEx.getUsers().get(params.getUtfString("pfriend1"));
    		queue2.add(aux);// add the array to the queue for groups of 2
    		
    		Queue<User> queue1=parentEx.getQueue21();
    		if(queue2.size()==1 && queue1.size()>=2){ //in the next ifs we see if with this players we can create a room
    			User[] aux2= queue2.poll();
    			parentEx.creatGame(queue1.poll(), queue1.poll(), aux2[0], aux2[1]);// call to the method of creating a room
    			parentEx.setQueue21(queue1);  // update the queue for groups of single players
    		}
    		
    		if(queue2.size()== 2){
    			User[] aux2= queue2.poll();
    			User[] aux3= queue2.poll();
    			parentEx.creatGame(aux2[0], aux2[1], aux3[0], aux3[1]); // call to the method of creating a room
    		}
    		parentEx.setQueue22(queue2); // update the queue for groups of 2
        }
        
        else if (mode == 3){
        	
        	Queue<User[]> queue2 = parentEx.getQueue32();
    		User[] aux= new User[2];// we set in the array the group of 2 players that fired this handler
    		aux[0]=player;
    		aux[1]=parentEx.getUsers().get(params.getUtfString("pfriend1"));
    		queue2.add(aux);// add the array to the queue for groups of 2
    		
    		Queue<User> queue1=parentEx.getQueue31();
    		if(queue2.size()==1 && queue1.size()>=2){ //in the next ifs we see if with this players we can create a room
    			User[] aux2= queue2.poll();
    			parentEx.creatGame(queue1.poll(), queue1.poll(), aux2[0], aux2[1]);// call to the method of creating a room
    			parentEx.setQueue31(queue1);  // update the queue for groups of single players
    		}
    		
    		if(queue2.size()== 2){
    			User[] aux2= queue2.poll();
    			User[] aux3= queue2.poll();
    			parentEx.creatGame(aux2[0], aux2[1], aux3[0], aux3[1]); // call to the method of creating a room
    		}
    		parentEx.setQueue32(queue2); // update the queue for groups of 2
        }
        
        else if (mode == 4){
        	
        	Queue<User[]> queue2 = parentEx.getQueue42();
    		User[] aux= new User[2];// we set in the array the group of 2 players that fired this handler
    		aux[0]=player;
    		aux[1]=parentEx.getUsers().get(params.getUtfString("pfriend1"));
    		queue2.add(aux);// add the array to the queue for groups of 2
    		
    		Queue<User> queue1=parentEx.getQueue41();
    		if(queue2.size()==1 && queue1.size()>=2){ //in the next ifs we see if with this players we can create a room
    			User[] aux2= queue2.poll();
    			parentEx.creatGame(queue1.poll(), queue1.poll(), aux2[0], aux2[1]);// call to the method of creating a room
    			parentEx.setQueue41(queue1);  // update the queue for groups of single players
    		}
    		
    		if(queue2.size()== 2){
    			User[] aux2= queue2.poll();
    			User[] aux3= queue2.poll();
    			parentEx.creatGame(aux2[0], aux2[1], aux3[0], aux3[1]); // call to the method of creating a room
    		}
    		parentEx.setQueue42(queue2); // update the queue for groups of 2
        }
		
	}

}