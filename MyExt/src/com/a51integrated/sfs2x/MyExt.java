package com.a51integrated.sfs2x;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import com.smartfoxserver.v2.api.CreateRoomSettings;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.SFSRoomRemoveMode;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSCreateRoomException;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class MyExt extends SFSExtension {
	
	private int nRooms;			// To set a different name for the room
	//private Long harpoonId;
	private Queue<User> queue01; // queues for the start of games
	private Queue<User[]> queue02;
	private Queue<User[]> queue03;
	private Queue<User> queue11; 
	private Queue<User[]> queue12;
	private Queue<User[]> queue13;
	private Queue<User> queue21; 
	private Queue<User[]> queue22;
	private Queue<User[]> queue23;
	private Queue<User> queue31; 
	private Queue<User[]> queue32;
	private Queue<User[]> queue33;
	private Queue<User> queue41; 
	private Queue<User[]> queue42;
	private Queue<User[]> queue43;
	private HashMap<String,User> users; // users connected
	private HashMap<String,InvitationRoom> gamesInCreation; // String: leader name, games in creation.

	@Override
	public void init() {				// here we initialize the queues,the hash of the users connected, and set handslers
		queue01 = new LinkedList<User>();
		queue02 = new LinkedList<User[]>();
		queue03 = new LinkedList<User[]>();
		queue11 = new LinkedList<User>();
		queue12 = new LinkedList<User[]>();
		queue13 = new LinkedList<User[]>();
		queue21 = new LinkedList<User>();
		queue22 = new LinkedList<User[]>();
		queue23 = new LinkedList<User[]>();
		queue31 = new LinkedList<User>();
		queue32 = new LinkedList<User[]>();
		queue33 = new LinkedList<User[]>();
		queue41 = new LinkedList<User>();
		queue42 = new LinkedList<User[]>();
		queue43 = new LinkedList<User[]>();
		users=new HashMap<String,User>();
		gamesInCreation=new HashMap<String,InvitationRoom>();	
		this.addEventHandler(SFSEventType.USER_JOIN_ZONE, ZoneJoinEventHandler.class); // handler fired when checked the pword
		this.addEventHandler(SFSEventType.USER_LOGIN, LoginEventHandler.class); // handler fired when a user want to connect
		this.addEventHandler(SFSEventType.USER_DISCONNECT, Disconnect.class); // handler fired after a user disconnects
		this.addRequestHandler("meter1", Put1Handler.class); //handlers to introduce players into de queues
		this.addRequestHandler("meter2", Put2Handler.class);
		this.addRequestHandler("meter3", Put3Handler.class);
		this.addRequestHandler("lanzarPartida", ThrowGameHandler.class);
		this.addRequestHandler("sacardecola", ExtractUsersHandler.class); //handlers to introduce players into de queues
		this.addRequestHandler("putHarpoon", PutHarpoon.class); // handler to put an harpoon in the game.
		this.addRequestHandler("GetConnectedFriends", GetConnectedFriends.class); // handler fired when a user wants to see the connected friends
		this.addRequestHandler("asignImprovements", AsignImprovementsHandler.class);
		this.addRequestHandler("GetTime", GetTime.class);//Gets the servers time.
		this.addRequestHandler("Invite", Invite.class); // handler fired when a user invite someone.
		this.addRequestHandler("Accept", Accept.class); // handler fired when a user accepts an invitation.
		this.addRequestHandler("Refuse", Refuse.class); // handler fired when a user refuses an invitation.
		this.addRequestHandler("ModeChange", ModeChange.class); // handler fired when a user changes the mode of his game.
		this.addRequestHandler("ModExternalPlayers", ModExternalPlayers.class); // handler fired when a user changes the "external players" option.
		this.addRequestHandler("ExitGroup", ExitGroup.class); // handler fired when a user want to exit the current team.
		this.addRequestHandler("GameMessage", GameMessage.class);
		this.addRequestHandler("AddFriend",AddFriend.class); // handler fired when 2 users confirm get friends
		this.addRequestHandler("FriendRequest", FriendRequest.class);// handler fired when a user sends a friend request
		this.addRequestHandler("GetFriendsRequests", GetFriendsRequests.class); // handler fired when user wants to get all his friends
		this.addRequestHandler("ViewedConfFriend", ViewedConfFriend.class);
		this.addRequestHandler("GetFriends",GetFriends.class);//handler fired when we want to know all friends, connected or not
		this.addRequestHandler("AsignaMejoras", AsignImprovementsHandler.class);
		this.addRequestHandler("ExitGame", ExitGame.class);//handler fired when the game is finished and the user wants to go back to the lobby
		this.addRequestHandler("ExploteHarpoon", ExploteHarpoon.class);// handler fired when it's the time to explote an harpoon
		
		nRooms=0;
		//harpoonId=0L;
	}

	public HashMap<String, InvitationRoom> getGamesInCreation() {
		return gamesInCreation;
	}

	public void setGamesInCreation(HashMap<String, InvitationRoom> gamesInCreation) {
		this.gamesInCreation = gamesInCreation;
	}

	
	
	public HashMap<String,User> getUsers(){
		return this.users;
	}

	public void setUsers(HashMap<String,User> users) {
		this.users = users;
	}
	
	public Room creatGame(User a, User b,User c,User d){
		ISFSObject rtn = new SFSObject();
		ISFSObject rtnNames = new SFSObject();
		int n=0;
		try {
			CreateRoomSettings settings= new CreateRoomSettings();
			settings.setMaxUsers(4);
			settings.setGame(true);
			settings.setName("sala"+ nRooms);
			nRooms++;
			 settings.setDynamic(true);
			 settings.setAutoRemoveMode(SFSRoomRemoveMode.WHEN_EMPTY);
			//Room room = this.getApi().createRoom(this.getParentZone(), settings, a,true,a.getLastJoinedRoom());
			Room room = this.getApi().createRoom(this.getParentZone(), settings, a);
			
			try{	
				a.getLastJoinedRoom().removeUser(a);
				room.addUser(a);
				b.getLastJoinedRoom().removeUser(b);
				room.addUser(b);
				c.getLastJoinedRoom().removeUser(c);
				room.addUser(c);
				d.getLastJoinedRoom().removeUser(d);
				room.addUser(d);
			}catch(Exception e){};
			try{
				rtn.putInt("id", a.getPlayerId(room));
				rtnNames.putInt("id1", a.getPlayerId(room));
				rtnNames.putUtfString("name1", a.getName());
				n++;
				this.send("startGame", rtn, a);
				rtn.putInt("id", b.getPlayerId(room));
				rtnNames.putInt("id2", b.getPlayerId(room));
				rtnNames.putUtfString("name2", b.getName());
				n++;
				this.send("startGame", rtn, b);         //Sends the ids of each player at the new room.
				rtn.putInt("id", c.getPlayerId(room));
				rtnNames.putInt("id3", c.getPlayerId(room));
				rtnNames.putUtfString("name3", c.getName());
				n++;
				this.send("startGame", rtn, c);
				rtn.putInt("id", d.getPlayerId(room));
				rtnNames.putInt("id4", d.getPlayerId(room));
				rtnNames.putUtfString("name4", d.getName());
				n++;
				this.send("startGame", rtn, d);
			}catch(Exception e){};
				rtnNames.putInt("n", n);
				Iterator<User> it=room.getPlayersList().iterator();
				while (it.hasNext()){ // send it to all users of the room
					User user=it.next();
					this.send("NamesGame",rtnNames, user);
					Connection connection=null;
					try {
						connection = this.getParentZone().getDBManager().getConnection();// catch the manager of the db
						PreparedStatement stmt = connection.prepareStatement("SELECT friend FROM friends WHERE name=? AND status=?");
					    stmt.setString(1, user.getName());
					    stmt.setString(2, "c");
						ResultSet res= stmt.executeQuery(); // send a query to know all friends with the mode said
			        	while (res.next()) {
			        		if (users.get(res.getString(1))!=null)
			        			this.send("UpdateFriendList", rtn, users.get(res.getString(1)));
			        	}
			        	connection.close();
					}catch(Exception e){}
				}
			return room;
		} catch (SFSCreateRoomException e) {
			e.printStackTrace();
			rtn.putUtfString("res", "Internal server error");
			this.send("startGame", rtn, a);
			return null;
		}	

	}
	
	public int getnRooms() {
		return nRooms;
	}

	public void setnRooms(int nRooms) {
		this.nRooms = nRooms;
	}
	
/*	public Long getHarpoonId() {
		return harpoonId;
	}

	public void setHarpoonId(Long harpoonId) {
		this.harpoonId = harpoonId;
	}
*/
	public Queue<User> getQueue01() {
		return queue01;
	}

	public void setQueue01(Queue<User> queue01) {
		this.queue01 = queue01;
	}

	public Queue<User[]> getQueue02() {
		return queue02;
	}

	public void setQueue02(Queue<User[]> queue02) {
		this.queue02 = queue02;
	}

	public Queue<User[]> getQueue03() {
		return queue03;
	}

	public void setQueue03(Queue<User[]> queue03) {
		this.queue03 = queue03;
	}

	public Queue<User> getQueue11() {
		return queue11;
	}

	public void setQueue11(Queue<User> queue11) {
		this.queue11 = queue11;
	}

	public Queue<User[]> getQueue12() {
		return queue12;
	}

	public void setQueue12(Queue<User[]> queue12) {
		this.queue12 = queue12;
	}

	public Queue<User[]> getQueue13() {
		return queue13;
	}

	public void setQueue13(Queue<User[]> queue13) {
		this.queue13 = queue13;
	}

	public Queue<User> getQueue21() {
		return queue21;
	}

	public void setQueue21(Queue<User> queue21) {
		this.queue21 = queue21;
	}

	public Queue<User[]> getQueue22() {
		return queue22;
	}

	public void setQueue22(Queue<User[]> queue22) {
		this.queue22 = queue22;
	}

	public Queue<User[]> getQueue23() {
		return queue23;
	}

	public void setQueue23(Queue<User[]> queue23) {
		this.queue23 = queue23;
	}

	public Queue<User> getQueue31() {
		return queue31;
	}

	public void setQueue31(Queue<User> queue31) {
		this.queue31 = queue31;
	}

	public Queue<User[]> getQueue32() {
		return queue32;
	}

	public void setQueue32(Queue<User[]> queue32) {
		this.queue32 = queue32;
	}

	public Queue<User[]> getQueue33() {
		return queue33;
	}

	public void setQueue33(Queue<User[]> queue33) {
		this.queue33 = queue33;
	}

	public Queue<User> getQueue41() {
		return queue41;
	}

	public void setQueue41(Queue<User> queue41) {
		this.queue41 = queue41;
	}

	public Queue<User[]> getQueue42() {
		return queue42;
	}

	public void setQueue42(Queue<User[]> queue42) {
		this.queue42 = queue42;
	}

	public Queue<User[]> getQueue43() {
		return queue43;
	}

	public void setQueue43(Queue<User[]> queue43) {
		this.queue43 = queue43;
	}
}
