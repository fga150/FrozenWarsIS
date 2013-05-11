package com.a51integrated.sfs2x;

import java.util.HashMap;
import java.util.Queue;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class Put1Handler extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(User player, ISFSObject params) { // a single player wants to join a game
	
	MyExt parentEx = (MyExt) getParentExtension();

	HashMap<String,InvitationRoom> gamesInCreation = parentEx.getGamesInCreation();
	if(gamesInCreation.containsKey(player.getName())){
    
    gamesInCreation.remove(player.getName());
    parentEx.setGamesInCreation(gamesInCreation);
	
	}
	
		int mode = params.getInt("mode");
		
		if (mode == 0){
		Queue<User> queue1 = parentEx.getQueue01();
		queue1.add(player); // we add the player to the queue
		
        parentEx.send("modInQueue", null, player);
       
		Queue<User[]> queue2=parentEx.getQueue03();
		if(queue1.size()==1 && queue2.size()==1){ //in the next ifs we see if with this player we can create a room
			User a = queue1.poll();
			User[] aux= queue2.poll();
			parentEx.creatGame(a, aux[0], aux[1], aux[2]); // call to the method of creating a room
			parentEx.setQueue03(queue2); // update the queue for groups of 3
		}
		
		queue2=parentEx.getQueue02();
		if(queue1.size()==2 && queue2.size()==1){
			User a = queue1.poll();
			User b = queue1.poll();
			User[] aux= queue2.poll();
			parentEx.creatGame(a, b, aux[0], aux[1]);
			parentEx.setQueue02(queue2); // update the queue for groups of 2
		}
		
		if(queue1.size() == 4){
			User a = queue1.poll();
			User b = queue1.poll();
			User c = queue1.poll();
			User d = queue1.poll();
			parentEx.creatGame(a, b, c, d);
		}
		parentEx.setQueue01(queue1); // update the queue for single players
	}
		
		else if (mode == 1){
			Queue<User> queue1 = parentEx.getQueue11();
			queue1.add(player); // we add the player to the queue
			
	        parentEx.send("modInQueue", null, player);
	       
			Queue<User[]> queue2=parentEx.getQueue13();
			if(queue1.size()==1 && queue2.size()==1){ //in the next ifs we see if with this player we can create a room
				User a = queue1.poll();
				User[] aux= queue2.poll();
				parentEx.creatGame(a, aux[0], aux[1], aux[2]); // call to the method of creating a room
				parentEx.setQueue13(queue2); // update the queue for groups of 3
			}
			
			queue2=parentEx.getQueue12();
			if(queue1.size()==2 && queue2.size()==1){
				User a = queue1.poll();
				User b = queue1.poll();
				User[] aux= queue2.poll();
				parentEx.creatGame(a, b, aux[0], aux[1]);
				parentEx.setQueue12(queue2); // update the queue for groups of 2
			}
			
			if(queue1.size() == 4){
				User a = queue1.poll();
				User b = queue1.poll();
				User c = queue1.poll();
				User d = queue1.poll();
				parentEx.creatGame(a, b, c, d);
			}
			parentEx.setQueue11(queue1); // update the queue for single players
		}
		
		else if (mode == 2){
			Queue<User> queue1 = parentEx.getQueue21();
			queue1.add(player); // we add the player to the queue
			
	        parentEx.send("modInQueue", null, player);
	       
			Queue<User[]> queue2=parentEx.getQueue23();
			if(queue1.size()==1 && queue2.size()==1){ //in the next ifs we see if with this player we can create a room
				User a = queue1.poll();
				User[] aux= queue2.poll();
				parentEx.creatGame(a, aux[0], aux[1], aux[2]); // call to the method of creating a room
				parentEx.setQueue23(queue2); // update the queue for groups of 3
			}
			
			queue2=parentEx.getQueue22();
			if(queue1.size()==2 && queue2.size()==1){
				User a = queue1.poll();
				User b = queue1.poll();
				User[] aux= queue2.poll();
				parentEx.creatGame(a, b, aux[0], aux[1]);
				parentEx.setQueue22(queue2); // update the queue for groups of 2
			}
			
			if(queue1.size() == 4){
				User a = queue1.poll();
				User b = queue1.poll();
				User c = queue1.poll();
				User d = queue1.poll();
				parentEx.creatGame(a, b, c, d);
			}
			parentEx.setQueue21(queue1); // update the queue for single players
		}
		
		else if (mode == 3){
			Queue<User> queue1 = parentEx.getQueue31();
			queue1.add(player); // we add the player to the queue
			
	        parentEx.send("modInQueue", null, player);
	       
			Queue<User[]> queue2=parentEx.getQueue33();
			if(queue1.size()==1 && queue2.size()==1){ //in the next ifs we see if with this player we can create a room
				User a = queue1.poll();
				User[] aux= queue2.poll();
				parentEx.creatGame(a, aux[0], aux[1], aux[2]); // call to the method of creating a room
				parentEx.setQueue33(queue2); // update the queue for groups of 3
			}
			
			queue2=parentEx.getQueue32();
			if(queue1.size()==2 && queue2.size()==1){
				User a = queue1.poll();
				User b = queue1.poll();
				User[] aux= queue2.poll();
				parentEx.creatGame(a, b, aux[0], aux[1]);
				parentEx.setQueue32(queue2); // update the queue for groups of 2
			}
			
			if(queue1.size() == 4){
				User a = queue1.poll();
				User b = queue1.poll();
				User c = queue1.poll();
				User d = queue1.poll();
				parentEx.creatGame(a, b, c, d);
			}
			parentEx.setQueue31(queue1); // update the queue for single players
		}
		
		else if (mode == 4){
			Queue<User> queue1 = parentEx.getQueue41();
			queue1.add(player); // we add the player to the queue
			
	        parentEx.send("modInQueue", null, player);
	       
			Queue<User[]> queue2=parentEx.getQueue43();
			if(queue1.size()==1 && queue2.size()==1){ //in the next ifs we see if with this player we can create a room
				User a = queue1.poll();
				User[] aux= queue2.poll();
				parentEx.creatGame(a, aux[0], aux[1], aux[2]); // call to the method of creating a room
				parentEx.setQueue43(queue2); // update the queue for groups of 3
			}
			
			queue2=parentEx.getQueue42();
			if(queue1.size()==2 && queue2.size()==1){
				User a = queue1.poll();
				User b = queue1.poll();
				User[] aux= queue2.poll();
				parentEx.creatGame(a, b, aux[0], aux[1]);
				parentEx.setQueue42(queue2); // update the queue for groups of 2
			}
			
			if(queue1.size() == 4){
				User a = queue1.poll();
				User b = queue1.poll();
				User c = queue1.poll();
				User d = queue1.poll();
				parentEx.creatGame(a, b, c, d);
			}
			parentEx.setQueue41(queue1); // update the queue for single players
		}
	}

}
