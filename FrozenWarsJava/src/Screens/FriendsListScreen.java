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
import com.badlogic.gdx.Input.Keys;
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
	private BoundingBox userClick;
    
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
	    
	    acceptedPlayers = new Vector<String>();
	    refusedPlayers = new Vector<String>();
	    waitingPlayers = new Vector<String>();
	    
	    infoPressed = false;
	    user = "";
	    this.time = System.nanoTime();
	    
	    invitedScroll = 0;
	    
	    sfsClient = SmartFoxServer.getInstance();

	    backButtonClick = new BoundingBox(new Vector3(320,20,0), new Vector3(560,60,0));
	    addFriendClick = new BoundingBox(new Vector3(615,110,0), new Vector3(660,145,0));
	    MultiplayerClick = new BoundingBox(new Vector3(30,540,0), new Vector3(510,605,0));
	    userClick = new BoundingBox(new Vector3(310,105,0), new Vector3(605,140,0));
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
      		System.out.println(Integer.toString((int)touchPoint.x).concat(",").concat(Integer.toString((int)touchPoint.y)));
			//compruebo si he tocado play (se abre ventana de introduccion de usuario si no esta logeado)
			
      		if (backButtonClick.contains(touchPoint)){
      			game.setScreen(InitialScreen.getInstance());
      		 } else if (MultiplayerClick.contains(touchPoint)){
      			this.game.setScreen(MultiplayerScreen.getInstance());
      		 } else if (userClick.contains(touchPoint)){
      			Gdx.input.setOnscreenKeyboardVisible(true);
      			this.infoPressed = true;
      			System.out.println("user");
      		 } else if (addFriendClick.contains(touchPoint)){
       			System.out.println("AddFriend");
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
 
            batcher.draw(Assets.listOfPeopleOn, 60, 180); 
            batcher.draw(Assets.listOfPeopleOff, 560, 180); 
            
            batcher.draw(Assets.addFriend, 290, 90); 

            font.drawWrapped(batcher, user, 320,140, 260);
 
            batcher.end();
            
            if ( this.infoPressed && ((System.nanoTime() - this.time) > 135000000)){
            	this.completeMessagePc();
            	this.time = System.nanoTime();
            }
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