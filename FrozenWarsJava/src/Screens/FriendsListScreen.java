package Screens;

import java.util.Iterator;
import java.util.Vector;

import Application.Assets;
import Application.LaunchFrozenWars;
import Server.SmartFoxServer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class FriendsListScreen implements Screen{


	private static FriendsListScreen instance;

	public static FriendsListScreen getInstance() {
		if (instance == null) instance = new FriendsListScreen();
		return instance;
	}
	
	private class ConnectedInfo{
		private String userName;
		private boolean playing;
		public boolean isPlaying() {
			return playing;
		}
		public String getUserName() {
			return userName;
		}
		public ConnectedInfo(String userName, boolean playing) {
			this.userName = userName;
			this.playing = playing;
		}
	}
	
	    /** The gui cam. */
	private OrthographicCamera guiCam;	
	
	/** Se utiliza para dibujar y optimizar las imagenes en el renderizado de la pantalla. */
	private SpriteBatch batcher;
	private Vector3 touchPoint;
	private Game game;
	private SmartFoxServer sfsClient;
	private BitmapFont font;
	     
    private BoundingBox MultiplayerClick; 
    private  BoundingBox backButtonClick;
    private  BoundingBox addFriendClick;
	private BoundingBox userClick;
	private BoundingBox scrollDownConnectedClick;
    private BoundingBox scrollUpConnectedClick;
    private BoundingBox scrollDownDisconnectedClick;
    private BoundingBox scrollUpDisconnectedClick;
    
	private int connectedScroll;
	private int disconnectedScroll;
    
    private Vector<String> disconnectedFriends;
    
    private Vector<ConnectedInfo> drawConnected;
    
	private String user;	
	private long time;
	private boolean infoPressed;
    
	

    public FriendsListScreen() {
		instance = this;
    	this.game = LaunchFrozenWars.getGame();

		guiCam = new OrthographicCamera(1024,630);
		guiCam.position.set(512,315,0);
		
	    batcher = new SpriteBatch();
	    touchPoint = new Vector3();
	    font = new BitmapFont(Gdx.files.internal("data/first.fnt"), Gdx.files.internal("data/first.png"), false);


	    disconnectedFriends = new Vector<String>();
	    
	    drawConnected = new Vector<ConnectedInfo>();
	    
	    infoPressed = false;
	    user = "";
	    this.time = System.nanoTime();
	    
	    connectedScroll = 0;
	    disconnectedScroll = 0;
	    
	    sfsClient = SmartFoxServer.getInstance();

	    backButtonClick = new BoundingBox(new Vector3(320,20,0), new Vector3(560,60,0));
	    addFriendClick = new BoundingBox(new Vector3(615,110,0), new Vector3(660,145,0));
	    MultiplayerClick = new BoundingBox(new Vector3(30,540,0), new Vector3(510,605,0));
	    userClick = new BoundingBox(new Vector3(310,105,0), new Vector3(605,140,0));
	    
	    scrollDownConnectedClick = new BoundingBox(new Vector3(423,183,0), new Vector3(475,225,0));
	    scrollUpConnectedClick = new BoundingBox(new Vector3(426,416,0), new Vector3(475,454,0));
	    scrollDownDisconnectedClick = new BoundingBox(new Vector3(926,185,0), new Vector3(974,224,0));
	    scrollUpDisconnectedClick = new BoundingBox(new Vector3(925,417,0), new Vector3(977,456,0));
	    
	    //------------Testing
	    disconnectedFriends.add("disconn1");
	    disconnectedFriends.add("disconn2");
	    disconnectedFriends.add("disconn3");
	    disconnectedFriends.add("disconn4");
	    disconnectedFriends.add("disconn5");
	    disconnectedFriends.add("disconn6");
	    disconnectedFriends.add("disconn7");
	    disconnectedFriends.add("disconn8");
	    disconnectedFriends.add("disconn9");
	    
	    drawConnected.add(new ConnectedInfo("playin1", true));
	    drawConnected.add(new ConnectedInfo("playin2", true));
	    drawConnected.add(new ConnectedInfo("playin3", true));
	    drawConnected.add(new ConnectedInfo("playin4", true));
	    
	    drawConnected.add(new ConnectedInfo("noplay1", false));
	    drawConnected.add(new ConnectedInfo("noplay2", false));
	    drawConnected.add(new ConnectedInfo("noplay3", false));
	    drawConnected.add(new ConnectedInfo("noplay4", false));
	    drawConnected.add(new ConnectedInfo("noplay5", false));
	    //------------Testing
	    
	    
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

	
	@Override
	public void render(float arg0) {
		
		//detectamos si se ha tocado la pantalla
		if (Gdx.input.justTouched()){
			guiCam.unproject(touchPoint.set(Gdx.input.getX(),Gdx.input.getY(),0));
      		//System.out.println(Integer.toString((int)touchPoint.x).concat(",").concat(Integer.toString((int)touchPoint.y)));
			//compruebo si he tocado play (se abre ventana de introduccion de usuario si no esta logeado)
			
      		if (backButtonClick.contains(touchPoint)){
      			game.setScreen(InitialScreen.getInstance());
      		 } else if (MultiplayerClick.contains(touchPoint)){
      			this.game.setScreen(MultiplayerScreen.getInstance());
      		 } else if (userClick.contains(touchPoint)){
      			Gdx.input.setOnscreenKeyboardVisible(true);
      			this.infoPressed = true;
      		 } else if (addFriendClick.contains(touchPoint)){
       			SmartFoxServer.getInstance().sendFriendRequest(user);
       		 } else if (scrollDownConnectedClick.contains(touchPoint)){
       			if (connectedScroll < (drawConnected.size()) - 6) connectedScroll++;
       		 } else if (scrollUpConnectedClick.contains(touchPoint)){
       			if (connectedScroll != 0) connectedScroll--;     	
 			 } else if (scrollDownDisconnectedClick.contains(touchPoint)){
        		if (disconnectedScroll < (disconnectedFriends.size()) - 6) disconnectedScroll++;
        	 } else if (scrollUpDisconnectedClick.contains(touchPoint)){
        		if (disconnectedScroll != 0) disconnectedScroll--;     	
  			 } else {
      			this.infoPressed = false;
      		 }
		}
		
		
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
            
            drawConnected();
            drawDisconnected();
            
            batcher.draw(Assets.addFriend, 290, 90); 

            font.drawWrapped(batcher, user, 320,140, 260);
 
            batcher.end();
            
            if ( this.infoPressed && ((System.nanoTime() - this.time) > 135000000)){
            	this.completeMessagePc();
            	this.time = System.nanoTime();
            }
            
            ConfirmScreen.getInstance().createConfirmIfNeeded();
            AcceptScreen.getInstance().createAcceptIfNeeded();
	}

	
	private void completeMessagePc(){
		char aux = 0;
		
		if(Gdx.input.isKeyPressed(Keys.ANY_KEY)){
			aux = ScreensKeyboard.keyPc();
		}

        if (aux != 0){
	        user += aux;	        
        }  
        
        if (ScreensKeyboard.delete() && user.length()>0){
	        user = (String)user.subSequence(0, user.length()-1);    
        }
	}
	
	public void updateFriends(){
		sfsClient.getMyFriendsRequest();
	}
	
	public void updateFriends(Vector<String> playingFriends, Vector<String> connectedNotPlayingFriends, Vector<String> disconnectedFriends) {
		drawConnected.clear();
		
		Iterator<String> itNotPlaying = connectedNotPlayingFriends.iterator();
		while (itNotPlaying.hasNext()) drawConnected.add(new ConnectedInfo(itNotPlaying.next(),false));
		
		Iterator<String> itPlaying = playingFriends.iterator();
		while (itPlaying.hasNext()) drawConnected.add(new ConnectedInfo(itPlaying.next(),true));
		
		this.disconnectedFriends = disconnectedFriends;
	}
	
	private void drawConnected() {
		batcher.draw(Assets.listOfPeopleOn, 60, 180);  
		for (int i = 0; i < Math.min(drawConnected.size(), 6); i++){
			String name = drawConnected.elementAt(i+connectedScroll).getUserName();
			
			font.draw(batcher, name, 125,(447-45*i));
			if (!drawConnected.elementAt(i+connectedScroll).isPlaying()) 
				batcher.draw(Assets.statusTick, 75, (415-45*i));
			else if (drawConnected.elementAt(i+connectedScroll).isPlaying()) 
				batcher.draw(Assets.statusInterrogation, 75, (415-45*i));
		}
	}
	
	private void drawDisconnected() {
		batcher.draw(Assets.listOfPeopleOff, 560, 180); 
		
		for (int i = 0; i < Math.min(disconnectedFriends.size(), 6); i++){
			String name = disconnectedFriends.elementAt(i+disconnectedScroll);
			
			font.draw(batcher, name, 615,(447-45*i));
			batcher.draw(Assets.statusCancel, 575, (415-45*i));
		}
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