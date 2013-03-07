package Screens;

import Application.Assets;
import Application.LaunchFrozenWars;

import Server.SmartFoxServer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class ConfirmScreen implements Screen{
	private Screen ancestor;
	private OrthographicCamera guiCam;
	private SpriteBatch batcher; 
	private Vector3 touchPoint;
	private BoundingBox yesClick;
	private BoundingBox noClick;
	private Game game;
	private BitmapFont font;
	private TextureRegion background;
	private String screenMode;
	private String user;
	
	

	public ConfirmScreen(Screen ancestor, TextureRegion background, String screenMode, String user) {
		this.game = LaunchFrozenWars.getGame();
		this.ancestor = ancestor;
		
		guiCam = new OrthographicCamera(1024,629);
		guiCam.position.set(512,315,0);
		
		this.background = background;
		this.screenMode = screenMode;
		this.user = user;
		
		font = new BitmapFont();
	    font.setColor(Color.BLACK);
		
	    batcher = new SpriteBatch();
	    touchPoint = new Vector3();

	    yesClick = new BoundingBox(new Vector3(350,340,0), new Vector3(500,400,0));
	    noClick = new BoundingBox(new Vector3(510,340,0), new Vector3(660,660,0));        
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

      		//compruebo si he tocado yes 
      		if (yesClick.contains(touchPoint)){
      			if (screenMode.equals("Exit")) this.dispose();
      			else if (screenMode.equals("InviteGame")) {
      				SmartFoxServer.getInstance().acceptRequest(user);
      				game.setScreen(MultiplayerScreen.getInstance());
      			}
      		} else if(noClick.contains(touchPoint)){ //compruebo si he tocado no
      			if (screenMode.equals("Exit")) game.setScreen(ancestor);
      			else if (screenMode.equals("InviteGame")) {
      				SmartFoxServer.getInstance().refuseRequest(user);
      				game.setScreen(ancestor);
      			}
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
            batcher.draw(background,0,0);
            batcher.end();

          //Dibujando elementos en pantalla activamos el Blending
            batcher.enableBlending();
            batcher.begin();    
            batcher.draw(Assets.window, 330, 300);
            if (screenMode.equals("Exit")) batcher.draw(Assets.exitText, 330, 300);
            if (screenMode.equals("InviteGame")) {
            	String message = user.concat(" wants you to join his game");
            	font.draw(batcher, message, 415, 465);
            }
           // batcher.draw(Assets.noConf, 175, 180);
            batcher.end();	
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
