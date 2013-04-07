package Screens;

import java.util.Vector;

import sfs2x.client.requests.ExtensionRequest;


import Application.Assets;
import Application.GameSettings;
import Application.LaunchFrozenWars;
import Application.MatchManager;
import Server.SmartFoxServer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.ScreenUtils;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class FriendsListScreen implements Screen{


	private static FriendsListScreen instance;

	public static FriendsListScreen getInstance() {
		if (instance == null) instance = new FriendsListScreen();
		return instance;
	}
	
	private class InvitedInfo{
		private String userName;
		private String status;
		public String getStatus() {
			return status;
		}
		public String getUserName() {
			return userName;
		}
		public InvitedInfo(String userName, String status) {
			this.userName = userName;
			this.status = status;
		}
	}	
	    /** The gui cam. */
	private OrthographicCamera guiCam;	
	
	/** Se utiliza para dibujar y optimizar las imagenes en el renderizado de la pantalla. */
	private SpriteBatch batcher;
	private GameSettings gSettings;
	private Vector3 touchPoint;
	private Game game;
	private SmartFoxServer sfsClient;
	private BitmapFont font;
	     
    private BoundingBox MultiplayerClick;
    
    private  BoundingBox backButtonClick;
    private  BoundingBox addFriendClick;
    
    // Settings
    private boolean externalPlayers;
	private int gameMode;
	private String myName = "";
	private boolean empiezaPartida;
	private boolean inQueue;


	private String gameAdmin;
	private int invitedScroll;
    
    private Vector<String> acceptedPlayers;
    private Vector<String> refusedPlayers;
    private Vector<String> waitingPlayers;
    
    private Vector<InvitedInfo> drawPlayers;
    
    public void setAcceptedPlayers(Vector<String> acceptedPlayers) {
		this.acceptedPlayers = acceptedPlayers;
		drawPlayers = new Vector<InvitedInfo>();
		for (int i = 0; i < acceptedPlayers.size(); i++) {
			String name = acceptedPlayers.elementAt(i);
			if (font.getBounds(name).width > 195){
				int j = 5;
				while (font.getBounds(name.substring(0, j)).width < 168) j++;
				name = name.substring(0, j)+"...";
			}
			drawPlayers.add(new InvitedInfo(name, "Accepted"));
		}
		for (int i = 0; i < waitingPlayers.size(); i++) {
			String name = waitingPlayers.elementAt(i);
			if (font.getBounds(name).width > 195){
				int j = 5;
				while (font.getBounds(name.substring(0, j)).width < 168) j++;
				name = name.substring(0, j)+"...";
			}
			drawPlayers.add(new InvitedInfo(name, "Waiting"));		
		}
		for (int i = 0; i < refusedPlayers.size(); i++) {
			String name = refusedPlayers.elementAt(i);
			if (font.getBounds(name).width > 195){
				int j = 5;
				while (font.getBounds(name.substring(0, j)).width < 168) j++;
				name = name.substring(0, j)+"...";
			}
			drawPlayers.add(new InvitedInfo(name, "Cancelled"));
		}
		
	}
    
    public void setRefusedPlayers(Vector<String> refusedPlayers) {
		this.refusedPlayers = refusedPlayers;
		drawPlayers = new Vector<InvitedInfo>();
		for (int i = 0; i < acceptedPlayers.size(); i++) {
			String name = acceptedPlayers.elementAt(i);
			if (font.getBounds(name).width > 195){
				int j = 5;
				while (font.getBounds(name.substring(0, j)).width < 168) j++;
				name = name.substring(0, j)+"...";
			}
			drawPlayers.add(new InvitedInfo(name, "Accepted"));
		}
		for (int i = 0; i < waitingPlayers.size(); i++) {
			String name = waitingPlayers.elementAt(i);
			if (font.getBounds(name).width > 195){
				int j = 5;
				while (font.getBounds(name.substring(0, j)).width < 168) j++;
				name = name.substring(0, j)+"...";
			}
			drawPlayers.add(new InvitedInfo(name, "Waiting"));		
		}
		for (int i = 0; i < refusedPlayers.size(); i++) {
			String name = refusedPlayers.elementAt(i);
			if (font.getBounds(name).width > 195){
				int j = 5;
				while (font.getBounds(name.substring(0, j)).width < 168) j++;
				name = name.substring(0, j)+"...";
			}
			drawPlayers.add(new InvitedInfo(name, "Cancelled"));
		}
	}
    
    public void setWaitingPlayers(Vector<String> waitingPlayers) {
		this.waitingPlayers = waitingPlayers;
		drawPlayers = new Vector<InvitedInfo>();
		for (int i = 0; i < acceptedPlayers.size(); i++) {
			String name = acceptedPlayers.elementAt(i);
			if (font.getBounds(name).width > 195){
				int j = 5;
				while (font.getBounds(name.substring(0, j)).width < 168) j++;
				name = name.substring(0, j)+"...";
			}
			drawPlayers.add(new InvitedInfo(name, "Accepted"));
		}
		for (int i = 0; i < waitingPlayers.size(); i++) {
			String name = waitingPlayers.elementAt(i);
			if (font.getBounds(name).width > 195){
				int j = 5;
				while (font.getBounds(name.substring(0, j)).width < 168) j++;
				name = name.substring(0, j)+"...";
			}
			drawPlayers.add(new InvitedInfo(name, "Waiting"));		
		}
		for (int i = 0; i < refusedPlayers.size(); i++) {
			String name = refusedPlayers.elementAt(i);
			if (font.getBounds(name).width > 195){
				int j = 5;
				while (font.getBounds(name.substring(0, j)).width < 168) j++;
				name = name.substring(0, j)+"...";
			}
			drawPlayers.add(new InvitedInfo(name, "Cancelled"));
		}
	}
    
    public void setExternalPlayers(boolean externalPlayers) {
		this.externalPlayers = externalPlayers;
	}

	public void setGameMode(int gameMode) {
		this.gameMode = gameMode;
	}
	
	public boolean amIAdmin(){
		return myName.equals(gameAdmin);
	}
	
	public void setEmpiezaPartida(boolean b) {
		empiezaPartida = b;
	}
	
	public void setMyName(String myName) {
		this.myName = myName;
		acceptedPlayers.add(myName);
		
		String name = myName;
		if (font.getBounds(name).width > 195){
			int j = 5;
			while (font.getBounds(name.substring(0, j)).width < 168) j++;
			name = name.substring(0, j)+"...";
		}
		drawPlayers.add(new InvitedInfo(name, "Accepted"));
	
		gameAdmin = myName;
	}
	
	public void setGameAdmin(String gameAdmin){
		this.gameAdmin = gameAdmin;
	}
	

	public String getGameAdmin() {
		return gameAdmin;
	}
	
	public void setInQueue(boolean inQueue) {
		this.inQueue = inQueue;
	}
    

	public boolean isInQueue() {
		return inQueue;
	}
	
    public FriendsListScreen() {
		instance = this;
    	this.game = LaunchFrozenWars.getGame();

		guiCam = new OrthographicCamera(1024,630);
		guiCam.position.set(512,315,0);
		
	    batcher = new SpriteBatch();
	    touchPoint = new Vector3();
	    font = new BitmapFont(Gdx.files.internal("data/first.fnt"), Gdx.files.internal("data/first.png"), false);
	    
	    acceptedPlayers = new Vector<String>();
	    refusedPlayers = new Vector<String>();
	    waitingPlayers = new Vector<String>();
	    drawPlayers = new Vector<InvitedInfo>();
	    
	    invitedScroll = 0;
	    
	    sfsClient = SmartFoxServer.getInstance();
	    
	    /*Gdx.input.getTextInput(new TextInputListener() {
	         public void input(String text) {
	        	 MultiplayerScreen.getInstance().setMyName(text);
	        	 sfsClient.conectaSala(text,"");//TODO pass also the password
	         }
	
	         public void canceled() {
	        	 String user = "user".concat(Long.toString(Math.round(Math.random()*1000)));
	        	 MultiplayerScreen.getInstance().setMyName(user);
	        	 sfsClient.conectaSala(user,""); //TODO pass also the password
	         }
	    }, "Enter user: ","");

	    */

	    backButtonClick = new BoundingBox(new Vector3(320,20,0), new Vector3(560,60,0));
	    addFriendClick = new BoundingBox(new Vector3(395,118,0), new Vector3(439,155,0));
	    MultiplayerClick = new BoundingBox(new Vector3(30,540,0), new Vector3(510,605,0));
	      
	    inQueue = false;
	    empiezaPartida = false;
	    externalPlayers = true;
	    gameMode = 0;
	}

	
	@Override
	public void dispose() {
		batcher.dispose();
		Assets.music.dispose();
		System.exit(0);	
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	public void creaPartida(){
		MatchManager manager = new MatchManager(sfsClient);
		sfsClient.addManager(manager);
		GameScreen gameScreen = new GameScreen(game,manager);
		manager.setGameScreen(gameScreen);
		game.setScreen(gameScreen);
		System.out.println(sfsClient.getMyPlayerId());
		if(sfsClient.getMyPlayerId()==1){     //PROBAR CUANDO DANI SUBA CAMBIOS DEL SERVER
			sfsClient.sendAsign();
		}
	}
	
	@Override
	public void render(float arg0) {
		
		//detectamos si se ha tocado la pantalla
		if (Gdx.input.justTouched()){
			guiCam.unproject(touchPoint.set(Gdx.input.getX(),Gdx.input.getY(),0));
      		System.out.println(Integer.toString((int)touchPoint.x).concat(",").concat(Integer.toString((int)touchPoint.y)));
			//compruebo si he tocado play (se abre ventana de introduccion de usuario si no esta logeado)
			
      		if (backButtonClick.contains(touchPoint)){
      			game.setScreen(InitialScreen.getInstance());
      		 } else if (MultiplayerClick.contains(touchPoint)){
      			this.game.setScreen(MultiplayerScreen.getInstance());
      		}
		}
		
		if (empiezaPartida) creaPartida();
		//crear solamente un batcher por pantalla y eliminarlo cuando no se use
	
      		GL10 gl = Gdx.graphics.getGL10(); //referencia a OpenGL 1.0
            gl.glClearColor(0,1,0,1);
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
           
            guiCam.update();
            batcher.setProjectionMatrix(guiCam.combined);
             
            //Dibujando el Background
            batcher.disableBlending();
            //se elimina graficamente la transparencia ya que es un fondo
            batcher.begin();
            batcher.draw(Assets.backGrey,0,0,1024,630);
            batcher.end();

            //Dibujando elementos en pantalla activamos el Blending
            batcher.enableBlending();
            batcher.begin();
            batcher.draw(Assets.backButton, 320, 20); 
            batcher.draw(Assets.multiplayerButtonUnpressed, 27, 540);
            batcher.draw(Assets.inviteFriendsButtonPressed, 521, 540);
 
            batcher.draw(Assets.listOfPeopleOn, 60, 180); 
            batcher.draw(Assets.listOfPeopleOff, 560, 180); 
            
            batcher.draw(Assets.addFriend, 290, 90); 

            	          
            batcher.end();
            
            ConfirmScreen.getInstance().createConfirmIfNeeded();
            AcceptScreen.getInstance().createAcceptIfNeeded();

	}

	private void drawInvited() {
		batcher.draw(Assets.list, 630, 180); 
		batcher.draw(Assets.playersText, 720, 407); 
		
		for (int i = 0; i < Math.min(drawPlayers.size(), 5); i++){
			String name = drawPlayers.elementAt(i+invitedScroll).getUserName();
			
			font.draw(batcher, name, 700,(402-45*i));
			if (drawPlayers.elementAt(i+invitedScroll).getStatus().equals("Accepted")) 
				batcher.draw(Assets.statusTick, 650, (367-45*i));
			else if (drawPlayers.elementAt(i+invitedScroll).getStatus().equals("Waiting")) 
				batcher.draw(Assets.statusInterrogation, 650, (367-45*i));
			else if (drawPlayers.elementAt(i+invitedScroll).getStatus().equals("Cancelled"))
				batcher.draw(Assets.statusCancel, 650, (367-45*i));
		}
	}


	public void setDefault() {
	    acceptedPlayers.clear();
	    refusedPlayers.clear();
	    waitingPlayers.clear();
	    drawPlayers.clear();
	    
	    invitedScroll = 0;
	    externalPlayers = true;
	    gameMode = 0;
	    inQueue = false;
	    
		acceptedPlayers.add(myName);
		
		String name = myName;
		if (font.getBounds(name).width > 195){
			int j = 5;
			while (font.getBounds(name.substring(0, j)).width < 168) j++;
			name = name.substring(0, j)+"...";
		}
		drawPlayers.add(new InvitedInfo(name, "Accepted"));
	
		gameAdmin = myName;
	}
	

	
	
	@Override
	public void resize(int arg0, int arg1) {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void show() {
		
	}



}