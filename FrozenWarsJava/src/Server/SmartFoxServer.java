package Server;

import java.net.InetAddress;
import java.net.URL;
import java.util.Map;
import java.util.Vector;


import Application.MatchManager;
import Application.MatchManager.Direction;
import Screens.InviteScreen;

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
import sfs2x.client.requests.PublicMessageRequest;

public class SmartFoxServer implements IEventListener {

	private static final String SFS_ZONE = "BasicExamples";
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
		System.currentTimeMillis();
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

	public void InsertInQueues(Vector<String> names){//Pasamos sólo los amigos, no el usuario que la lanza.
		if(names.size()==0){
			  ExtensionRequest request2 = new ExtensionRequest("meter1",new SFSObject());
			  sfsClient.send(request2);
		}
		else if(names.size()==1){
			  String friend=names.get(0);
			  SFSObject params = new SFSObject();
			  params.putUtfString("pfriend1",friend);
			  ExtensionRequest request2 = new ExtensionRequest("meter2",params);
			  sfsClient.send(request2);
		}
		else if(names.size()==2){
			  String friend1=names.get(0);
			  String friend2=names.get(1);
			  SFSObject params = new SFSObject();
			  //User sender = (User)evt.getArguments().get("sender"); evt es un baseEvent mirar private message 
			  params.putUtfString("pfriend1", friend1);
			  params.putUtfString("pfriend2", friend2);
			  ExtensionRequest request2 = new ExtensionRequest("meter3",params);
			  sfsClient.send(request2);
		}
		else if(names.size()==3){
			  String friend1=names.get(0);
			  String friend2=names.get(1);
			  String friend3=names.get(2);
			  SFSObject params = new SFSObject();
			  //User sender = (User)evt.getArguments().get("sender"); evt es un baseEvent mirar private message 
			  params.putUtfString("pfriend1", friend1);
			  params.putUtfString("pfriend2", friend2);
			  params.putUtfString("pfriend3", friend3);
			  ExtensionRequest request2 = new ExtensionRequest("meter4",params);
			  sfsClient.send(request2);
		}
	}
	
	
	
}