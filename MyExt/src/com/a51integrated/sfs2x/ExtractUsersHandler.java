package com.a51integrated.sfs2x;

import java.util.Iterator;
import java.util.Queue;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class ExtractUsersHandler extends BaseClientRequestHandler {

	public void handleClientRequest(User player, ISFSObject params) {

		MyExt parentEx = (MyExt) getParentExtension();
		int numJug=params.getInt("numJugadores");
		int mode = params.getInt("mode");
		
		if(mode == 0){
		if(numJug==1){
			Queue<User> queue1 = parentEx.getQueue01();
			queue1.remove(player);
		}
		else if(numJug==2){
			Queue<User[]> queue2 = parentEx.getQueue02();
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
			if(enc==true)queue2.remove(arrayUsers);
		}
		else if(numJug==3){
			Queue<User[]> queue3 = parentEx.getQueue03();
			Iterator<User[]> it=queue3.iterator();
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
			if(enc==true)queue3.remove(arrayUsers);
		}
		
	}
		
		else if (mode == 1){
			if(numJug==1){
				Queue<User> queue1 = parentEx.getQueue11();
				queue1.remove(player);
			}
			else if(numJug==2){
				Queue<User[]> queue2 = parentEx.getQueue12();
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
				if(enc==true)queue2.remove(arrayUsers);
			}
			else if(numJug==3){
				Queue<User[]> queue3 = parentEx.getQueue13();
				Iterator<User[]> it=queue3.iterator();
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
				if(enc==true)queue3.remove(arrayUsers);
			}
		}
		
		else if (mode == 2){
			if(numJug==1){
				Queue<User> queue1 = parentEx.getQueue21();
				queue1.remove(player);
			}
			else if(numJug==2){
				Queue<User[]> queue2 = parentEx.getQueue22();
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
				if(enc==true)queue2.remove(arrayUsers);
			}
			else if(numJug==3){
				Queue<User[]> queue3 = parentEx.getQueue23();
				Iterator<User[]> it=queue3.iterator();
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
				if(enc==true)queue3.remove(arrayUsers);
			}
		}
		
		else if (mode == 3){
			
			if(numJug==1){
				Queue<User> queue1 = parentEx.getQueue31();
				queue1.remove(player);
			}
			else if(numJug==2){
				Queue<User[]> queue2 = parentEx.getQueue32();
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
				if(enc==true)queue2.remove(arrayUsers);
			}
			else if(numJug==3){
				Queue<User[]> queue3 = parentEx.getQueue33();
				Iterator<User[]> it=queue3.iterator();
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
				if(enc==true)queue3.remove(arrayUsers);
			}
		}
		
		else if (mode == 4){
			
			if(numJug==1){
				Queue<User> queue1 = parentEx.getQueue41();
				queue1.remove(player);
			}
			else if(numJug==2){
				Queue<User[]> queue2 = parentEx.getQueue42();
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
				if(enc==true)queue2.remove(arrayUsers);
			}
			else if(numJug==3){
				Queue<User[]> queue3 = parentEx.getQueue43();
				Iterator<User[]> it=queue3.iterator();
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
				if(enc==true)queue3.remove(arrayUsers);
			}
		}


	}
	
}
