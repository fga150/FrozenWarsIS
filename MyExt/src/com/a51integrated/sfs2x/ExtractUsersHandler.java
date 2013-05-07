package com.a51integrated.sfs2x;

import java.util.Iterator;
import java.util.Queue;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class ExtractUsersHandler extends BaseClientRequestHandler {

	public void handleClientRequest(User player, ISFSObject params) {
		MyExt parentEx = (MyExt) getParentExtension();
		try{
			Queue<User> queue1 = parentEx.getQueue1();
			queue1.remove(player);
		}catch(Exception e){};
		try{
			Queue<User[]> queue2 = parentEx.getQueue2();
			Iterator<User[]> it=queue2.iterator();
			boolean enc=false;
			User[] arrayUsers=null;
			int i;
			while(it.hasNext()&&enc==false){
				arrayUsers=it.next();
					i=0;
					while(i<arrayUsers.length&&enc==false){
						enc= arrayUsers[i]==player;
						i++;
					}
			}
			if(enc==true){
				ISFSObject rtn = new SFSObject();
				rtn.putUtfString("playerName", player.getName());
				if(arrayUsers[0]!=player)this.send("OutOfQueue", rtn, arrayUsers[0]);
				if(arrayUsers[1]!=player)this.send("OutOfQueue", rtn, arrayUsers[1]);
				queue2.remove(arrayUsers);		
			}
		}catch(Exception e){};
		try{
			Queue<User[]> queue3 = parentEx.getQueue3();
			Iterator<User[]> it=queue3.iterator();
			boolean enc=false;
			User[] arrayUsers=null;
			int i;
			while(it.hasNext()&&enc==false){
				arrayUsers=it.next();
					i=0;
					while(i<arrayUsers.length && enc==false){
						enc= arrayUsers[i]==player;
						i++;
					}
			}
			if(enc==true){
				ISFSObject rtn = new SFSObject();
				rtn.putUtfString("playerName", player.getName());
				if(arrayUsers[0]!=player)this.send("OutOfQueue", rtn, arrayUsers[0]);
				if(arrayUsers[1]!=player)this.send("OutOfQueue", rtn, arrayUsers[1]);
				if(arrayUsers[2]!=player)this.send("OutOfQueue", rtn, arrayUsers[2]);
				queue3.remove(arrayUsers);
			}
		}catch(Exception e){};


	}
	
}
