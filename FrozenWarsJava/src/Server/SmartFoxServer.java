package Server;

import java.net.InetAddress;
import java.net.URL;
import java.util.Map;


import Application.MatchManager;
import Application.MatchManager.Direction;

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

	private static final String SFS_ZONE = "FrozenWars";
	private SmartFox sfsClient;
	private MatchManager manager;
	
	public SmartFoxServer(){
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
		return sfsClient.getMySelf().getPlayerId();
	}
	
	private void addEventListeners() {
		sfsClient.addEventListener(SFSEvent.LOGIN, new IEventListener(){
			public void dispatch(BaseEvent event) throws SFSException {
				sfsClient.send(new JoinRoomRequest("The Lobby"));
			}
		});
		sfsClient.addEventListener(SFSEvent.PUBLIC_MESSAGE,new IEventListener(){

			public void dispatch(BaseEvent event) throws SFSException {
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
					int xHarpoonPosition = Integer.parseInt(message.substring(xPosition, yPosition));
					yPosition++;
					int yHarpoonPosition = Integer.parseInt(message.substring(yPosition));
					manager.putHarpoonEvent(xHarpoonPosition,yHarpoonPosition, manager.getMyIdPlayer());
				}else if(message.charAt(0)== 'W'){
					//when the message of sunken harpoon CALL putSunkenHarpoonEvent
					//Copy to (message.charAt(0)=='H')
					//Replace when sunken harpoon is implement
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
					int xHarpoonPosition = Integer.parseInt(message.substring(xPosition, yPosition));
					yPosition++;
					int yHarpoonPosition = Integer.parseInt(message.substring(yPosition));
					manager.putSunkenHarpoonEvent(xHarpoonPosition,yHarpoonPosition, manager.getMyIdPlayer());
				}
				
			}
			
		});
		sfsClient.addEventListener(SFSEvent.EXTENSION_RESPONSE, new IEventListener(){

			public void dispatch(BaseEvent event) throws SFSException {
				Map<String, Object> r = event.getArguments();
				String x= (String)r.get("cmd");
				if (x.equals("putHarpoon"))
					getHarpoon((ISFSObject) r.get("params"));
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
	
	public void conectaSala(String user, String pword){
		sfsClient.send(new LoginRequest(user,pword, SFS_ZONE));
	}

	public void sendMove(Direction dir, int myPlayerId,Vector3 position) {
		int dirCod = -1;
		if (dir.equals(Direction.left)) dirCod = 0;
		else if (dir.equals(Direction.right)) dirCod = 1;
		else if (dir.equals(Direction.up)) dirCod = 2;
		else if (dir.equals(Direction.down)) dirCod = 3;
		sfsClient.send(new PublicMessageRequest("M"+Integer.toString(dirCod)+Integer.toString(myPlayerId)+"X"+ position.x +"Y" + position.y));	
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

	public void dispatch(BaseEvent arg0) throws SFSException {
		
	}
	
	public void getHarpoon(ISFSObject params){
		long time=params.getLong("time")/1000;
		int x=params.getInt("x");
		int y=params.getInt("y");
		int range=params.getInt("range");
		manager.putHarpoonEvent(x,y,range);//TODO last param what is???
	}

}