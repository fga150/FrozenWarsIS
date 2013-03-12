package Server;

import java.net.InetAddress;
import java.net.URL;
import java.util.Map;
import java.util.Vector;


import Application.LaunchFrozenWars;
import Application.MatchManager;
import Application.MatchManager.Direction;
import Screens.ConfirmScreen;
import Screens.InviteScreen;
import Screens.MultiplayerScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
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
import sfs2x.client.requests.PublicMessageRequest;

public class SmartFoxServer implements IEventListener {

	private static final String SFS_ZONE = "FrozenWars";
	private SmartFox sfsClient;
	private MatchManager manager;
	private static SmartFoxServer instance;
	
	public static SmartFoxServer getInstance() {
		if (instance == null) instance = new SmartFoxServer();
		return instance;
	}
	
	public SmartFoxServer(){
		instance = this;
		String ip = getServerIP();
		sfsClient = new SmartFox(false);
		sfsClient.connect(ip,9933);
		addEventListeners();
	}
	
	public void addManager(MatchManager manager){
		this.manager = manager;
	}
	
	
	public int getMyPlayerId(){
		return sfsClient.getMySelf().getPlayerId();
	}
	
	private void addEventListeners() {
		sfsClient.addEventListener(SFSEvent.CONNECTION, this);
		sfsClient.addEventListener(SFSEvent.LOGIN, this);
		sfsClient.addEventListener(SFSEvent.ROOM_JOIN, this);
		sfsClient.addEventListener(SFSEvent.USER_ENTER_ROOM, this);
		sfsClient.addEventListener(SFSEvent.USER_EXIT_ROOM, this);
		sfsClient.addEventListener(SFSEvent.PUBLIC_MESSAGE,this);
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
				}




			
		});
	}

	private String getServerIP() {
		String ip = "";
		try {
			InetAddress address = InetAddress.getByName(new URL("http://boomwars-server.no-ip.org").getHost());
			ip = address.getHostAddress();
		} catch (Exception e){
			
		}
		return ip;
	}

	public void dispatch(BaseEvent event) throws SFSException {

		if(event.getType().equals(SFSEvent.PUBLIC_MESSAGE)){
			String message = event.getArguments().get("message").toString();
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
			else if (message.charAt(0)=='H'){
				int xPosition = 0;
				int yPosition = 0;
				boolean found = false;
				int i = 1;
				while (i<message.length() & !found){
					found = (message.charAt(i)=='X');
					if (found) xPosition = i;
					else i++;
				}
				found = false;
				while (i<message.length() & !found){
					found = (message.charAt(i)=='Y');
					if (found) yPosition = i;
					else i++;
				}
				xPosition++;
				int xLancePosition = Integer.parseInt(message.substring(xPosition, yPosition));
				yPosition++;
				int yLancePosition = Integer.parseInt(message.substring(yPosition));
				manager.putLanceEvent(xLancePosition,yLancePosition);
			}
		}
		else if(event.getType().equalsIgnoreCase(SFSEvent.LOGIN))
		{					
			// Join The Lobby room
			sfsClient.send(new JoinRoomRequest("The Lobby"));			    	
		}
		else if (event.getType().equalsIgnoreCase(SFSEvent.ROOM_JOIN)){
			
		}
	}
	
	public void conectaSala(String user){
		sfsClient.send(new LoginRequest(user,"", SFS_ZONE));
		ExtensionRequest request = new ExtensionRequest("conectarse", null);
		sfsClient.send(request);
	}

	public void sendMove(Direction dir, int myPlayerId,Vector3 position) {
		int dirCod = -1;
		if (dir.equals(Direction.left)) dirCod = 0;
		else if (dir.equals(Direction.right)) dirCod = 1;
		else if (dir.equals(Direction.up)) dirCod = 2;
		else if (dir.equals(Direction.down)) dirCod = 3;
		sfsClient.send(new PublicMessageRequest("M"+Integer.toString(dirCod)+Integer.toString(myPlayerId)+"X"+ position.x +"Y" + position.y));	
	}
	
	public void sendLance(int x,int y) {
		sfsClient.send(new PublicMessageRequest("H"+"X"+x+"Y"+y));	
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
		//Aqui se recibe la invitacion, el nombre del lider esta en inviter.
		Game game = LaunchFrozenWars.getGame();
		ConfirmScreen confirmScreen = new ConfirmScreen(game.getScreen(), ScreenUtils.getFrameBufferTexture(), "InviteGame", inviter);
		game.setScreen(confirmScreen);
	}


	public void inviteFail(ISFSObject response){
		// TODO : Aqui se recibe mensaje de que el usuario invitado no esta conectado.
	}
	
	public void acceptRequest(String inviter) {
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
		
	 

	public void InsertInQueues(Vector<String> names, boolean externalPlayers){
		if(names.size()==1 && externalPlayers==true){		  //El que lanza la partida debe estar en la posicion 0 del vector.
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
		Game game = LaunchFrozenWars.getGame();
		ConfirmScreen confirmScreen = new ConfirmScreen(game.getScreen(), ScreenUtils.getFrameBufferTexture(), "FullTeam", "");
		game.setScreen(confirmScreen);
	}
	
	public void groupExitRequest(String name){
		//The name contains the leaders name.
		ISFSObject params = new SFSObject();
		params.putUtfString("Inviter", name);
		ExtensionRequest request = new ExtensionRequest("ExitGroup",params);
		sfsClient.send(request);
		 MultiplayerScreen.getInstance().setDefault();
		
	}
	
	private void leaderLeftResponse(ISFSObject response) {
		MultiplayerScreen.getInstance().setDefault();
	}


	
	
}