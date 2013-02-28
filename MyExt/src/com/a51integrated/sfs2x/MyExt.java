package com.a51integrated.sfs2x;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import com.smartfoxserver.v2.api.CreateRoomSettings;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSCreateRoomException;
import com.smartfoxserver.v2.exceptions.SFSJoinRoomException;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class MyExt extends SFSExtension {
	
	private int nRooms;			// To set a different name for the room
	private Queue<User> queue1; // queues for the start of games
	private Queue<User[]> queue2;
	private Queue<User[]> queue3;
	private HashMap<String,User> users; // users connected

	@Override
	public void init() {				// here we initialize the queues,the hash of the users connected, and set handslers
		queue1 = new LinkedList<User>();
		queue2 = new LinkedList<User[]>();
		queue3 = new LinkedList<User[]>();
		users=new HashMap<String,User>();
		this.addRequestHandler("db",DataBase.class); // handler fired when we want to know information of the database
		this.addEventHandler(SFSEventType.USER_JOIN_ZONE, ZoneJoinEventHandler.class); // handler fired when checked the pword
		this.addEventHandler(SFSEventType.USER_LOGIN, LoginEventHandler.class); // handler fired when a user want to connect
		this.addEventHandler(SFSEventType.USER_DISCONNECT, Disconnect.class); // handler fired after a user disconnects
		this.addRequestHandler("meter1", Put1Handler.class); //handlers to introduce players into de queues
		this.addRequestHandler("meter2", Put2Handler.class);
		this.addRequestHandler("meter3", Put3Handler.class);
		this.addRequestHandler("putHarpoon", PutHarpoon.class); // handler to put an harpoon in the game.
		this.addRequestHandler("GetConnectedFriends", GetConnectedFriends.class); // handler fired when a user wants to see the connected friends

		nRooms=0;
	}

	public Queue<User> getQueue1() {
		return queue1;
	}

	public void setQueue1(Queue<User> queue1) {
		this.queue1 = queue1;
	}

	public Queue<User[]> getQueue2() {
		return queue2;
	}

	public void setQueue2(Queue<User[]> queue2) {
		this.queue2 = queue2;
	}
	
	public Queue<User[]> getQueue3() {
		return queue3;
	}

	public void setQueue3(Queue<User[]> queue3) {
		this.queue3 = queue3;
	}
	
	public HashMap<String,User> getUsers(){
		return this.users;
	}

	public void setUsers(HashMap<String,User> users) {
		this.users = users;
	}
	
	public Room creatGame(User a, User b,User c,User d){ // funtion to create a room for 4 users
		ISFSObject rtn = new SFSObject();
		try {
			CreateRoomSettings settings= new CreateRoomSettings();
			settings.setMaxUsers(4);
			settings.setGame(true);
			settings.setName("sala"+ nRooms);
			nRooms=nRooms+1;
			Room room = this.getApi().createRoom(this.getParentZone(), settings, a,true,a.getLastJoinedRoom()); // room created and user a added to the room
			b.getLastJoinedRoom().removeUser(b); // here we add the other users to the room
			room.addUser(b);
			c.getLastJoinedRoom().removeUser(c);
			room.addUser(c);
			d.getLastJoinedRoom().removeUser(d);
			room.addUser(d);
			rtn.putUtfString("res", "Game starting up with the following players: "+a.getName()+", "+b.getName()+", "+c.getName()+", "+d.getName()+", ");
			this.send("meter1", rtn, a); // we send a message to all the users of the room
			this.send("meter1", rtn, b);
			this.send("meter1", rtn, c);
			this.send("meter1", rtn, d);
			return room;
		} catch (SFSCreateRoomException e) {
			e.printStackTrace();
			rtn.putUtfString("ras", "Game couldn't start up with the following players: "+a.getName()+", "+b.getName()+", "+c.getName()+", "+d.getName()+", ");
			this.send("meter1", rtn, a);
			return null;
		} catch (SFSJoinRoomException e) {
			rtn.putUtfString("ras", "Game couldn't start up with the following players: "+a.getName()+", "+b.getName()+", "+c.getName()+", "+d.getName()+", ");
			e.printStackTrace();
			return null;
		}	

	}
}
