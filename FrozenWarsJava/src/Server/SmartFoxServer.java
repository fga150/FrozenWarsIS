package Server;

import java.net.InetAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;


import Application.GameSettings;
import Application.LaunchFrozenWars;
import Application.MatchManager;
import Application.MatchManager.Direction;
import Screens.AcceptScreen;
import Screens.ConfirmScreen;
import Screens.InviteScreen;
import Screens.MultiplayerScreen;

import com.badlogic.gdx.math.Vector3;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;
import sfs2x.client.SmartFox;
import sfs2x.client.core.BaseEvent;
import sfs2x.client.core.IEventListener;
import sfs2x.client.core.SFSEvent;
import sfs2x.client.requests.ExtensionRequest;
import sfs2x.client.requests.JoinRoomRequest;
import sfs2x.client.requests.LoginRequest;
import sfs2x.client.requests.LogoutRequest;

public class SmartFoxServer implements IEventListener {

	private static final String SFS_ZONE = "FrozenWars";
	private static final String SFS_RegisterZone = "Register";
	private SmartFox sfsClient;
	private MatchManager manager;
	private static SmartFoxServer instance;
	private long delayTime;
	private int myId;
	private String lastUserName;
	private String lastPass;
	private boolean loggedIn;
	
	public SmartFox getSfsClient(){
		return this.sfsClient;
	}
	
	public static SmartFoxServer getInstance() {
		if (instance == null) instance = new SmartFoxServer();
		return instance;
	}
	
	public boolean isLoggedIn() {
		return loggedIn;
	}
	
	public void getTimeResponse(ISFSObject response){
		long time = response.getLong("time");
		this.delayTime = System.currentTimeMillis() - time;
	}
	
	public void getTimeRequest(){
		ExtensionRequest request = new ExtensionRequest("GetTime",null);
		sfsClient.send(request);
	}
	
	public SmartFoxServer(){
		loggedIn = false;
		instance = this;
		myId = -999;
		String ip = getServerIP();
		sfsClient = new SmartFox(false);
		while(!sfsClient.isConnected()){
			sfsClient.connect(ip,9933);
		}
		addEventListeners();
	}
	
	public void addManager(MatchManager manager){
		this.manager = manager;
	}
	
	
	public int getMyPlayerId(){
		return myId;
	}
	
	private void addEventListeners() {
		sfsClient.addEventListener(SFSEvent.CONNECTION, this);
		sfsClient.addEventListener(SFSEvent.LOGIN, new IEventListener(){

			public void dispatch(BaseEvent arg0) throws SFSException {
				if (sfsClient.getCurrentZone().equals("FrozenWars")){
					getTimeRequest();
				}
			}
			
		});
		
		sfsClient.addEventListener(SFSEvent.LOGIN_ERROR,new IEventListener(){

			public void dispatch(BaseEvent event) throws SFSException {
				Map<String,Object>args=event.getArguments();
				Short err=(Short) args.get("errorCode");
				Short error=6;
				if (!(error==err))
					AcceptScreen.getInstance().setNewAcceptScreen("NamePassNotValid", "");
				else
					AcceptScreen.getInstance().setNewAcceptScreen("AlreadyLogged", "");
			}
			
		});
		sfsClient.addEventListener(SFSEvent.USER_ENTER_ROOM, this);
		sfsClient.addEventListener(SFSEvent.USER_EXIT_ROOM, this);
		sfsClient.addEventListener(SFSEvent.EXTENSION_RESPONSE, new IEventListener(){

			@Override
			public void dispatch(BaseEvent event) throws SFSException {
				Map<String, Object> r = event.getArguments();
				ISFSObject response = (ISFSObject)r.get("params"); //Gets the ISFSObject from the BaseEvent
				String cmd = (String) r.get("cmd"); 
				//Executes the response methods according to the requests sent.
				if(cmd.equals("GetConnectedFriends"))
					getConnectedFriendsResponse(response);
				else if (cmd.equals("Invite"))
					inviteResponse(response);
				else if (cmd.equals("InviteFail"))
					inviteFail(response);
				else if (cmd.equals("AcceptedWaiting"))
					acceptedWaiting(response);
				else if (cmd.equals("RefusedWaiting"))
					refusedWaiting(response);
				else if (cmd.equals("AcceptedRefused"))
					acceptedRefused(response);
				else if (cmd.equals("ModExternalPlayers"))
					modExternalPlayersResponse(response);
				else if (cmd.equals("ModeChange"))
					modeChangeResponse(response);	
				else if (cmd.equals("GameFull"))
					gameFullResponse(response);	
				else if (cmd.equals("LeaderLeft"))
					leaderLeftResponse(response);	
				else if (cmd.equals("startGame"))
					insertInQueuesResponse(response);
				else if (cmd.equals("GameMessage"))
					gameMessage(response);
				else if (cmd.equals("modInQueue"))
					modInQueueResponse(response);
				else if (cmd.equals("GameNotFound"))
					gameNotFound(response);
				else if (cmd.equals("DisconectedOnGame"))
					DisconectedOnGame(response);
				else if (cmd.equals("putHarpoon"))
					getHarpoon(response);
				else if (cmd.equals("getTime"))
					getTimeResponse(response);
				else if (cmd.equals("dbRegister"))
					registerResponse(response);
				else if(cmd.equals("FriendRequestRes"))
					//DONE JOptionPane.showMessageDialog(null,((ISFSObject)r.get("params")).getUtfString("res"));//TODO show a better dialog
					AcceptScreen.getInstance().setNewAcceptScreen("AddFriendAdder", ((ISFSObject)r.get("params")).getUtfString("res"));
				else if(cmd.equals("BeFriends?"))
					beFriends(response);
				else if(cmd.equals("AddFriendRes"))
					//DONDE JOptionPane.showMessageDialog(null,((ISFSObject)r.get("params")).getUtfString("res"));//TODO show a better dialog
					AcceptScreen.getInstance().setNewAcceptScreen("AddFriendAdded", ((ISFSObject)r.get("params")).getUtfString("res"));
				else if(cmd.equals("ConnectRes"))
					connectRes(response);
				else if(cmd.equals("asignaMejoras"))
					asignaMejoras(response);
				}

		});
	}

	private String getServerIP() {
		String ip = "";
		try {
			//InetAddress address = InetAddress.getByName(new URL("http://boomwars-server.no-ip.org").getHost());
			//ip = address.getHostAddress();
			ip="127.0.0.1";
		} catch (Exception e){
			
		}
		return ip;
	}

	public void conectaSala(String user,String pword){
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		sfsClient.send(new LoginRequest(user,pword, SFS_ZONE));
		lastUserName = user;
		lastPass = pword;
		System.out.println(lastUserName + " " + lastPass);

	}
	
	public void connectRes(ISFSObject response) {
		if (response.getUtfString("Response").equals("Success")){
			//FIXME probablemente no funcione (alguna imagen se pinte mal, pinguinos desaparezcan...). Contactar con Fede. 
			MultiplayerScreen.getInstance().setMyName(sfsClient.getMySelf().getName());
			loggedIn = true;
  			GameSettings.getInstance().setUserName(lastUserName);
  			GameSettings.getInstance().setUserPassword(lastPass);
			LaunchFrozenWars.getGame().setScreen(MultiplayerScreen.getInstance());
        	ExtensionRequest request2 = new ExtensionRequest("GetFriendsRequests",new SFSObject());
			sfsClient.send(request2);
		}
	}

	public void sendMove(Direction dir, int myPlayerId,Vector3 position) {
		int dirCod = -1;
		if (dir.equals(Direction.left)) dirCod = 0;
		else if (dir.equals(Direction.right)) dirCod = 1;
		else if (dir.equals(Direction.up)) dirCod = 2;
		else if (dir.equals(Direction.down)) dirCod = 3;
		ISFSObject params = new SFSObject();
		params.putUtfString("message", "M"+Integer.toString(dirCod)+Integer.toString(myPlayerId)+"X"+ position.x +"Y" + position.y);
		ExtensionRequest request = new ExtensionRequest("GameMessage",params);
		sfsClient.send(request);
		//sfsClient.send(new PublicMessageRequest("M"+Integer.toString(dirCod)+Integer.toString(myPlayerId)+"X"+ position.x +"Y" + position.y));	
	}
	
	public void sendLance(int x,int y) {
		//sfsClient.send(new PublicMessageRequest("H"+"X"+x+"Y"+y));
		ISFSObject params = new SFSObject();
		params.putUtfString("message", "H"+"X"+x+"Y"+y);
		ExtensionRequest request = new ExtensionRequest("GameMessage",params);
		sfsClient.send(request);
	}
	
	public void getConnectedFriendsResponse(ISFSObject params){
		Vector<String> friends = new Vector<String>();
		for(int i= 0;i<params.getSFSArray("ConnectedFriends").size();i++)
			friends.add((String) params.getSFSArray("ConnectedFriends").getElementAt(i));//Builds the friends array.
		InviteScreen.getInstance().setNotInvited(friends); //Sends the array to the InviteScreen instance.
	}
	
	public void getConnectedFriendsRequest(){
		 ExtensionRequest request = new ExtensionRequest("GetConnectedFriends",null);
		 sfsClient.send(request);//Executes the request.
	}
	
	public void inviteRequest(String name){
		 ISFSObject params = new SFSObject();
		 params.putUtfString("Invited", name);
		 ExtensionRequest request = new ExtensionRequest("Invite",params);
		 sfsClient.send(request);
	}
	
	public void inviteResponse(ISFSObject response){
		String inviter = response.getUtfString("Inviter");
		ConfirmScreen.getInstance().setNewConfirmScreen("InviteGame", inviter);
	}


	public void inviteFail(ISFSObject response){
		String invited = response.getUtfString("Invited");
		ConfirmScreen.getInstance().setNewConfirmScreen("InvitedDisconnected", invited);
	}
	
	public void acceptRequest(String inviter) {
		this.groupExitRequest(MultiplayerScreen.getInstance().getGameAdmin());
		
		ISFSObject params = new SFSObject();
		params.putUtfString("Inviter", inviter);
		ExtensionRequest request = new ExtensionRequest("Accept",params);
		sfsClient.send(request);
	}
	
	
	private void acceptedWaiting(ISFSObject params) {
	
		Vector<String> accepted = new Vector<String>();
		for(int i= 0;i<params.getSFSArray("acceptedPlayers").size();i++)
			accepted.add((String) params.getSFSArray("acceptedPlayers").getElementAt(i));//Builds the friends array.
		
		Vector<String> waiting = new Vector<String>();
		for(int i= 0;i<params.getSFSArray("waitingPlayers").size();i++)
			waiting.add((String) params.getSFSArray("waitingPlayers").getElementAt(i));//Builds the friends array.
		
		MultiplayerScreen.getInstance().setAcceptedPlayers(accepted);
		MultiplayerScreen.getInstance().setWaitingPlayers(waiting);
		
	}
	
	public void acceptedRefused(ISFSObject params) {
		Vector<String> accepted = new Vector<String>();
		for(int i= 0;i<params.getSFSArray("acceptedPlayers").size();i++)
			accepted.add((String) params.getSFSArray("acceptedPlayers").getElementAt(i));//Builds the friends array.
		
		Vector<String> refused = new Vector<String>();
		for(int i= 0;i<params.getSFSArray("refusedPlayers").size();i++)
			refused.add((String) params.getSFSArray("refusedPlayers").getElementAt(i));//Builds the friends array.
		
		MultiplayerScreen.getInstance().setAcceptedPlayers(accepted);
		MultiplayerScreen.getInstance().setRefusedPlayers(refused);
		
	}
	
	private void refusedWaiting(ISFSObject response) {
		
		Vector<String> refused = new Vector<String>();
		for(int i= 0;i<response.getSFSArray("refusedPlayers").size();i++)
			refused.add((String) response.getSFSArray("refusedPlayers").getElementAt(i));//Builds the friends array.
		
		Vector<String> waiting = new Vector<String>();
		for(int i= 0;i<response.getSFSArray("waitingPlayers").size();i++)
			waiting.add((String) response.getSFSArray("waitingPlayers").getElementAt(i));//Builds the friends array.
		
		MultiplayerScreen.getInstance().setRefusedPlayers(refused);
		MultiplayerScreen.getInstance().setWaitingPlayers(waiting);
		
	}
		
	public void insertInQueuesResponse(ISFSObject response){
		myId = response.getInt("id");
		MultiplayerScreen.getInstance().setEmpiezaPartida(true);
	} 

	public void insertInQueuesRequest(Vector<String> names, boolean externalPlayers){
		if(names.size()==1){		  //El que lanza la partida debe estar en la posicion 0 del vector.
			  ExtensionRequest request2 = new ExtensionRequest("meter1",new SFSObject());
			  sfsClient.send(request2);
		}
		else if(names.size()==2 && externalPlayers==true){
			  String friend=names.get(1);
			  SFSObject params = new SFSObject();
			  params.putUtfString("pfriend1",friend);
			  ExtensionRequest request2 = new ExtensionRequest("meter2",params);
			  sfsClient.send(request2);
		}
		else if(names.size()==3 && externalPlayers==true){
			  String friend1=names.get(1);
			  String friend2=names.get(2);
			  SFSObject params = new SFSObject();
			  params.putUtfString("pfriend1", friend1);
			  params.putUtfString("pfriend2", friend2);
			  ExtensionRequest request2 = new ExtensionRequest("meter3",params);
			  sfsClient.send(request2);
		}
		else if(names.size()==4 || externalPlayers==false){
			String friend1;
			String friend2;
			String friend3;
			SFSObject params = new SFSObject();
			if(names.size()>1){
				friend1=names.get(1);
				params.putUtfString("pfriend1", friend1);
			}
			if(names.size()>2){
				friend2=names.get(2);
				params.putUtfString("pfriend2", friend2);
			}
			if(names.size()>3){
				friend3=names.get(3);
				params.putUtfString("pfriend3", friend3);
			}
			  ExtensionRequest request2 = new ExtensionRequest("lanzarPartida",params);
			  sfsClient.send(request2);
		}
	}
	
	public void register(String user,String email, String pword, String confpword){ //method that sends the user that wants to regist
		if (!user.equals("")){
			String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
			Pattern pattern = Pattern.compile(EMAIL_PATTERN);
			Matcher matcher = pattern.matcher(email);
			if (matcher.matches()){
				if (pword.length()>=4 && pword.length()<=8){
					if (pword.equals(confpword)){
						sfsClient.send(new LoginRequest("","", SFS_RegisterZone));
						SFSObject params = new SFSObject();
						params.putUtfString("name", user);
						params.putUtfString("pword", pword);
						params.putUtfString("email", email);
						sfsClient.send(new ExtensionRequest("register",params));
					}else{
						AcceptScreen.getInstance().setNewAcceptScreen("DiffPasswords", "");
					}
				}else{
					AcceptScreen.getInstance().setNewAcceptScreen("PasswordChars", "");
				}
			}else{
				AcceptScreen.getInstance().setNewAcceptScreen("Email", "");
			}
		}else{
			AcceptScreen.getInstance().setNewAcceptScreen("Username", "");
		}
	}
	
	public void registerResponse(ISFSObject response){ //response of the regist call
		String str=response.getUtfString("res");
		AcceptScreen.getInstance().setNewAcceptScreen("RegisterSuccess", str);
		
		sfsClient.send(new LogoutRequest());
	}
	
	public void modInQueueResponse(ISFSObject response){
		MultiplayerScreen.getInstance().setInQueue(true);
	} 
	
	public void removeOfQueue(int playersNumber){//Funcion invocada por el usuario que quiere salirse de cola.
		SFSObject params = new SFSObject();
		params.putInt("numJugadores", playersNumber);//playersNumber es el numero de amigos con los que metiste cola(incluido el que lanza).
		ExtensionRequest request2 = new ExtensionRequest("sacardecola",params);
		sfsClient.send(request2);
	}
	
	public void refuseRequest(String inviter) {
		ISFSObject params = new SFSObject();
		params.putUtfString("Inviter", inviter);
		ExtensionRequest request = new ExtensionRequest("Refuse",params);
		sfsClient.send(request);
	}
	
	public void modeChangeRequest(int mode){
		ISFSObject params = new SFSObject();
		params.putInt("mode", mode);
		ExtensionRequest request = new ExtensionRequest("ModeChange",params);
		sfsClient.send(request);
	}
	
	public void modeChangeResponse(ISFSObject response){
		int mode = response.getInt("mode");
		MultiplayerScreen.getInstance().setGameMode(mode);	
	}
	
	public void modExternalPlayersRequest(boolean external){
		ISFSObject params = new SFSObject();
		params.putBool("externalPlayers", external);
		ExtensionRequest request = new ExtensionRequest("ModExternalPlayers",params);
		sfsClient.send(request);
	}
	
	public void modExternalPlayersResponse(ISFSObject response){
		Boolean external = response.getBool("externalPlayers");
		MultiplayerScreen.getInstance().setExternalPlayers(external);
	}
	
	public void gameFullResponse(ISFSObject response) {
		AcceptScreen.getInstance().setNewAcceptScreen("FullTeam", "");
	}
	
	public void groupExitRequest(String name){
		//The name contains the leaders name.
		ISFSObject params = new SFSObject();
		params.putUtfString("Inviter", name);
		ExtensionRequest request = new ExtensionRequest("ExitGroup",params);
		sfsClient.send(request);
		 MultiplayerScreen.getInstance().setDefault();
		
	}
	
	public void gameMessage(ISFSObject response){
		String message = response.getUtfString("message").toString();
		if (message.charAt(0)=='M'){
			Direction dir = null;
			if (message.charAt(1)=='0') dir = Direction.left;
			else if (message.charAt(1)=='1') dir = Direction.right;
			else if (message.charAt(1)=='2') dir = Direction.up;
			else if (message.charAt(1)=='3') dir = Direction.down;
			String text = (message.substring(2,3));
			int jugador = Integer.parseInt(text);
			int xPosition = 0;
			int yPosition = 0;
			boolean found = false;
			int i = 0;
			while (i<message.length() & !found){
				found = (message.charAt(i)=='X');
				if (found) xPosition = i;
				else i++;
			}
			i = 0;
			found = false;
			while (i<message.length() & !found){
				found = (message.charAt(i)=='Y');
				if (found) yPosition = i;
				else i++;
			}
			xPosition++;
			float xPlayerPosition = Float.parseFloat(message.substring(xPosition, yPosition));
			yPosition++;
			float yPlayerPosition = Float.parseFloat(message.substring(yPosition));
			manager.movePlayerEvent(dir,jugador,xPlayerPosition,yPlayerPosition);
		}		
	}
	
	private void DisconectedOnGame(ISFSObject response){
		System.out.println(response.getInt("id"));
	}
	
	private void leaderLeftResponse(ISFSObject response) {
		ConfirmScreen.getInstance().setNewConfirmScreen("LeaderLeft", "");
	}
	
	private void gameNotFound(ISFSObject response) {
		ConfirmScreen.getInstance().setNewConfirmScreen("GameNotFound", "");
	} 
	
	
	public String getMyName() {
		return sfsClient.getMySelf().getName();		
	}
	
	public void putHarpoon(int x,int y,int range){
		SFSObject params = new SFSObject();
		params.putInt("range", range);
		params.putInt("x", x);
		params.putInt("y", y);
		sfsClient.send(new ExtensionRequest("putHarpoon",params));
	}
	
	public void getHarpoon(ISFSObject response){
		long time=response.getLong("time");		
		int x=response.getInt("x");
		int y=response.getInt("y");
		int range=response.getInt("range");
		manager.putHarpoonEvent(x,y,range,time+delayTime);
	}
	
	public void beFriends(ISFSObject params){
		SFSObject params2 = new SFSObject();
		if (params.getSFSArray("Friends")!=null){
			for(int i= 0;i<params.getSFSArray("Friends").size();i++){
				int confirmado = JOptionPane.showConfirmDialog(null,((String) params.getSFSArray("Friends").getElementAt(i))+" wants to be your friend");//DONE TODO show a better dialog in which the user can confirm or reject
				ConfirmScreen.getInstance().setNewConfirmScreen("BeFriends?", ((String) params.getSFSArray("Friends").getElementAt(i)));
				params2.putUtfString("friend", ((String) params.getSFSArray("Friends").getElementAt(i)));	
				if (JOptionPane.OK_OPTION == confirmado){
					   System.out.println("confirmado");	   
					   params2.putUtfString("res", "yes");
					}	
					else{
					   System.out.println("vale... no borro nada...");
					   params2.putUtfString("res", "no");
					}
				ExtensionRequest request2 = new ExtensionRequest("AddFriend",params2);
				sfsClient.send(request2);
			}
		}
		if (params.getSFSArray("Friends2")!=null){
			for(int i=0;i<params.getSFSArray("Friends2").size();i++){
				// DONE JOptionPane.showMessageDialog(null,((String) params.getSFSArray("Friends2").getElementAt(i))+" accepted your friend request");//TODO show a better dialog
				AcceptScreen.getInstance().setNewAcceptScreen("AcceptedFriendRequest", ((String) params.getSFSArray("Friends2").getElementAt(i)));
				params2.putUtfString("friend", ((String) params.getSFSArray("Friends2").getElementAt(i)));
				ExtensionRequest request2 = new ExtensionRequest("ViewedConfFriend",params2);
				sfsClient.send(request2);
			}
		}
	
	}

	
	public void dispatch(BaseEvent event) throws SFSException {
	}

	public void asignaMejoras(ISFSObject params) {
		int numBarriles=((ISFSObject)params.get("params")).getInt("nBarriles");
		for(int i=0;i<numBarriles;i++){
			System.out.println(((ISFSObject)params.get("params")).getSFSArray("arrayBarriles").getInt(i));
		}
	}
	
	
	
}