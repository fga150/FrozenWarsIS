package com.a51integrated.sfs2x;

import java.util.Queue;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class Put1Handler extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(User player, ISFSObject params) { // a single player wants to join a game
		
		MyExt parentEx = (MyExt) getParentExtension();
		Queue<User> queue1 = parentEx.getQueue1();
		queue1.add(player); // we add the player to the queue
		
		Queue<User[]> queue2=parentEx.getQueue3();
		if(queue1.size()==1 && queue2.size()==1){ //in the next ifs we see if with this player we can create a room
			User a = queue1.poll();
			User[] aux= queue2.poll();
			parentEx.creatGame(a, aux[0], aux[1], aux[2]); // call to the method of creating a room
			parentEx.setQueue3(queue2); // update the queue for groups of 3
		}
		
		queue2=parentEx.getQueue2();
		if(queue1.size()==2 && queue2.size()==1){
			User a = queue1.poll();
			User b = queue1.poll();
			User[] aux= queue2.poll();
			parentEx.creatGame(a, b, aux[0], aux[1]);
			parentEx.setQueue2(queue2); // update the queue for groups of 2
		}
		
		if(queue1.size() == 4){
			User a = queue1.poll();
			User b = queue1.poll();
			User c = queue1.poll();
			User d = queue1.poll();
			parentEx.creatGame(a, b, c, d);
		}
		parentEx.setQueue1(queue1); // update the queue for single players
	}

}
