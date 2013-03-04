package Screens;

import java.io.IOException;
import java.util.Vector;


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
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class MultiplayerScreen implements Screen{


	private static MultiplayerScreen instance;

	public static MultiplayerScreen getInstance() {
		if (instance == null) instance = new MultiplayerScreen();
		return instance;
	}
	
	    /** The gui cam. */
	private OrthographicCamera guiCam;	
	
	/** Se utiliza para dibujar y optimizar las imagenes en el renderizado de la pantalla. */
	private SpriteBatch batcher;
	private GameSettings gSettings;
	private Vector3 touchPoint;
	private String message;
	private Game game;
	private SmartFoxServer sfsClient;
	private BitmapFont font;
	private InitialScreen initialScreen;
	    
    private  BoundingBox externalPlayerButtonClick;
    private  BoundingBox externalPlayerTextClick;
    private  BoundingBox externalPlayerTickClick;
    
    private  BoundingBox inviteButtonClick;
    private  BoundingBox playButtonClick;
    private  BoundingBox backButtonClick;
    
    private  BoundingBox mapLeftArrowClick;
    private  BoundingBox mapRightArrowClick;
    private  BoundingBox modeLeftArrowClick;
    private  BoundingBox modeRightArrowClick;
	
    private BoundingBox scrollDownPlayersClick;
    private BoundingBox scrollUpPlayersClick;
    
    // Settings
    boolean externalPlayers;
    int gameMode;
    
    private class invitedInfo{
    	private String userName;
    	private String status;
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getUserName() {
			return userName;
		}
		public invitedInfo(String userName, String status) {
			this.userName = userName;
			this.status = status;
		}
    }	
	
    private Vector<invitedInfo> invited;
	private int invitedScroll;
    
    
    public MultiplayerScreen() {
		instance = this;
    	this.game = LaunchFrozenWars.getGame();
		this.initialScreen = InitialScreen.getInstance();

		guiCam = new OrthographicCamera(1024,630);
		guiCam.position.set(512,315,0);
		
	    batcher = new SpriteBatch();
	    touchPoint = new Vector3();
	    font = new BitmapFont();
	    font.setColor(Color.BLACK);
	    
	    invited = new Vector<invitedInfo>();
	    
	    invitedScroll = 0;
	    
	    sfsClient = new SmartFoxServer();

        Gdx.input.getTextInput(new TextInputListener() {
            public void input(String text) {
              message = text;
              sfsClient.conectaSala(text);
            }

            public void canceled() {
                // TODO Auto-generated method stub

            }
          }, "Enter user: ","");
	    
	    //Esquina inferior izq y superior derecha
	    //externalPlayerButtonClick = new BoundingBox(new Vector3(130,380,0), new Vector3(290,400,0));
	    //externalPlayerTextClick = new BoundingBox(new Vector3(45,300,0), new Vector3(195,320,0));
	    externalPlayerTickClick = new BoundingBox(new Vector3(120,370,0), new Vector3(170,410,0));
	    
	    inviteButtonClick = new BoundingBox(new Vector3(550,80,0), new Vector3(790,120,0));
	    playButtonClick = new BoundingBox(new Vector3(200,80,0), new Vector3(440,120,0));
	    backButtonClick = new BoundingBox(new Vector3(370,20,0), new Vector3(610,60,0));
	    
	    mapLeftArrowClick = new BoundingBox(new Vector3(50,200,0), new Vector3(100,290,0));
	    mapRightArrowClick = new BoundingBox(new Vector3(450,200,0), new Vector3(510,280,0));
	    modeLeftArrowClick = new BoundingBox(new Vector3(80,450,0), new Vector3(120,500,0));
	    modeRightArrowClick = new BoundingBox(new Vector3(425,450,0), new Vector3(460,500,0));
	    
	    scrollDownPlayersClick = new BoundingBox(new Vector3(900,190,0), new Vector3(950,225,0));
	    scrollUpPlayersClick = new BoundingBox(new Vector3(900,365,0), new Vector3(950,400,0));
	    
	    externalPlayers = false;
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
		// TODO Auto-generated method stub	
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub	
	}

	@Override
	public void render(float arg0) {
		
		//detectamos si se ha tocado la pantalla
		if (Gdx.input.justTouched()){
			guiCam.unproject(touchPoint.set(Gdx.input.getX(),Gdx.input.getY(),0));
      		//System.out.println(Integer.toString((int)touchPoint.x).concat(",").concat(Integer.toString((int)touchPoint.y)));
			//compruebo si he tocado play (se abre ventana de introduccion de usuario si no esta logeado)
			if (playButtonClick.contains(touchPoint)){
				System.out.println("Entrando a partida");
				MatchManager manager = new MatchManager(sfsClient);
				sfsClient.addManager(manager);
				GameScreen gameScreen = new GameScreen(game,manager);
				manager.setGameScreen(gameScreen);
  				game.setScreen(gameScreen);
      		} else if (inviteButtonClick.contains(touchPoint)) {
      			InviteScreen inviteScreen = new InviteScreen();
      			game.setScreen(inviteScreen);
      		}else if (externalPlayerTickClick.contains(touchPoint)){
      			externalPlayers = !externalPlayers;
      		} else if (modeLeftArrowClick.contains(touchPoint)){
      			if (gameMode == 0) gameMode = 4;
      			else gameMode--;
      		} else if (modeRightArrowClick.contains(touchPoint)){
      			gameMode = (gameMode + 1) % 5;
      		} else if (scrollDownPlayersClick.contains(touchPoint)){
      			if (invitedScroll < invited.size() - 5) invitedScroll++;
      		} else if (scrollUpPlayersClick.contains(touchPoint)){
      			if (invitedScroll != 0) invitedScroll--;     	
			} else if (backButtonClick.contains(touchPoint)){
      			game.setScreen(initialScreen);
      		}
			return;
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
            batcher.draw(Assets.multiplayerGameTitle, 320, 540);
            drawMode();
            batcher.draw(Assets.modeLeftArrow, 85, 455);
            batcher.draw(Assets.modeRightArrow, 435, 455);   
            
            batcher.draw(Assets.externalPlayerButton, 130, 380);   
            if (externalPlayers) batcher.draw(Assets.externalPlayerTick, 130, 380);   
            batcher.draw(Assets.externalPlayerText, 200, 380);   
            
            
            batcher.draw(Assets.map, 130, 150);   
            batcher.draw(Assets.mapLeftArrow, 45, 200);   
            batcher.draw(Assets.mapRightArrow, 450, 200);   
            
            batcher.draw(Assets.playButton, 200, 80); 
            batcher.draw(Assets.inviteButton, 550, 80); 
            batcher.draw(Assets.backButton, 370, 20); 
           
            batcher.draw(Assets.pingu, 565, 112);
            
            drawInvited();
            	          
            batcher.end();

	}

	private void drawInvited() {
		batcher.draw(Assets.list, 630, 180); 
		batcher.draw(Assets.playersText, 720, 407); 
		for (int i = 0; i < Math.min(invited.size(), 5); i++){
			font.draw(batcher, invited.elementAt(i+invitedScroll).getUserName(), 700,(395-45*i));
			if (invited.elementAt(i+invitedScroll).getStatus().equals("Accepted")) 
				batcher.draw(Assets.statusTick, 650, (370-45*i));
			else if (invited.elementAt(i+invitedScroll).getStatus().equals("Waiting")) 
				batcher.draw(Assets.statusInterrogation, 650, (370-45*i));
			else if (invited.elementAt(i+invitedScroll).getStatus().equals("Cancelled")) 
			batcher.draw(Assets.statusCancel, 650, (370-45*i));
		}
	}


	private void drawMode() {
		if (gameMode == 0) batcher.draw(Assets.normalRoyalMode, 100, 450);
		else if (gameMode == 1) batcher.draw(Assets.teamPlayMode, 100, 450);
		else if (gameMode == 2) batcher.draw(Assets._1vsAllMode, 100, 450);
		else if (gameMode == 3) batcher.draw(Assets.survivalMode, 100, 450);
		else if (gameMode == 4) batcher.draw(Assets.battleRoyalMode, 100, 450);
	}


	public void inviteFriends(Vector<String> users) {
		for (int i = 0; i < users.size(); i++){
			invited.add(new invitedInfo(users.elementAt(i), "Waiting"));
		}	    		
	}
	
	@Override
	public void resize(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}




	

}