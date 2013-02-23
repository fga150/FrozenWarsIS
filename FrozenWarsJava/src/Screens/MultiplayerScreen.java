package Screens;

import java.io.IOException;


import Application.Assets;
import Application.GameSettings;
import Application.MatchManager;
import Server.SmartFoxServer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class MultiplayerScreen implements Screen{


	    /** The gui cam. */
	private OrthographicCamera guiCam;
	
	/** Se utiliza para dibujar y optimizar las imagenes en el renderizado de la pantalla. */
	private SpriteBatch batcher;
	private GameSettings gSettings;
	private Vector3 touchPoint;
	private String message;
	private Game game;
	private SmartFoxServer sfsClient;
	
	    
    private  BoundingBox externalPlayerButtonClick;
    private  BoundingBox externalPlayerTextClick;
    private  BoundingBox externalPlayerTickClick;
    
    private  BoundingBox inviteButtonClick;
    private  BoundingBox playButtonClick;
    
    private  BoundingBox mapLeftArrowClick;
    private  BoundingBox mapRightArrowClick;
    private  BoundingBox modeLeftArrowClick;
    private  BoundingBox modeRightArrowClick;
	
    private BoundingBox scrollDownPlayersClick;
    private BoundingBox scrollUpPlayersClick;
    
    // Settings
    boolean externalPlayers;
    int gameMode;

    public MultiplayerScreen(Game game, GameSettings gSettings,InitialScreen initialScreen) {
		this.game = game;
		//this.sfsClient = initialScreen;
		//this.gSettings = gSettings;
		guiCam = new OrthographicCamera(1024,630);
		guiCam.position.set(512,315,0);
		
	    batcher = new SpriteBatch();
	    touchPoint = new Vector3();
	    
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
	    
	    inviteButtonClick = new BoundingBox(new Vector3(550,50,0), new Vector3(790,90,0));
	    playButtonClick = new BoundingBox(new Vector3(200,50,0), new Vector3(440,90,0));
	    
	    mapLeftArrowClick = new BoundingBox(new Vector3(50,200,0), new Vector3(100,290,0));
	    mapRightArrowClick = new BoundingBox(new Vector3(450,200,0), new Vector3(510,280,0));
	    modeLeftArrowClick = new BoundingBox(new Vector3(80,450,0), new Vector3(120,500,0));
	    modeRightArrowClick = new BoundingBox(new Vector3(425,450,0), new Vector3(460,500,0));
	    
	    scrollDownPlayersClick = new BoundingBox(new Vector3(900,190,0), new Vector3(950,405,0));
	    scrollUpPlayersClick = new BoundingBox(new Vector3(900,365,0), new Vector3(950,225,0));
	    
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
      		} else if (externalPlayerTickClick.contains(touchPoint)){
      			externalPlayers = !externalPlayers;
      		} else if (modeLeftArrowClick.contains(touchPoint)){
      			if (gameMode == 0) gameMode = 4;
      			else gameMode--;
      		} else if (modeRightArrowClick.contains(touchPoint)){
      			gameMode = (gameMode + 1) % 5;
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
            batcher.draw(Assets.backMultiplayer,0,0,1024,630);
            batcher.end();

            //Dibujando elementos en pantalla activamos el Blending
            batcher.enableBlending();
            batcher.begin();
            batcher.draw(Assets.multiplayerGameTitle, 320, 540);
            drawMode(batcher);
            batcher.draw(Assets.modeLeftArrow, 85, 455);
            batcher.draw(Assets.modeRightArrow, 435, 455);   
            
            batcher.draw(Assets.externalPlayerButton, 130, 380);   
            if (externalPlayers) batcher.draw(Assets.externalPlayerTick, 130, 380);   
            batcher.draw(Assets.externalPlayerText, 200, 380);   
            
            
            batcher.draw(Assets.map, 130, 150);   
            batcher.draw(Assets.mapLeftArrow, 45, 200);   
            batcher.draw(Assets.mapRightArrow, 450, 200);   
            
            
            batcher.draw(Assets.playerList, 630, 180); 
            
            batcher.draw(Assets.playButton, 200, 50); 
            batcher.draw(Assets.inviteButton, 550, 50); 
            
            batcher.draw(Assets.pingu, 565, 82);
            
            batcher.draw(Assets.statusCancel, 648, 190);
            batcher.draw(Assets.statusTick, 648, 232);
            batcher.draw(Assets.statusInterrogation, 648, 278);
            batcher.draw(Assets.statusTick, 648, 325);
            batcher.draw(Assets.statusInterrogation, 648, 367);
            

	  
	        
	         // batcher.draw(Assets.settings, 225, 300);
	         // batcher.draw(Assets.help, 25, 240);
	         // batcher.draw(Assets.exit, 225, 240);
	          
            batcher.end();

	}

	private void drawMode(SpriteBatch batcher) {
		if (gameMode == 0) batcher.draw(Assets.normalRoyalMode, 100, 450);
		else if (gameMode == 1) batcher.draw(Assets.teamPlayMode, 100, 450);
		else if (gameMode == 2) batcher.draw(Assets._1vsAllMode, 100, 450);
		else if (gameMode == 3) batcher.draw(Assets.survivalMode, 100, 450);
		else if (gameMode == 4) batcher.draw(Assets.battleRoyalMode, 100, 450);
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