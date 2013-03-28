package Screens;

import java.util.Vector;

import Application.Assets;
import Application.LaunchFrozenWars;

import Server.SmartFoxServer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.ScreenUtils;

public class AcceptScreen implements Screen{
	private static AcceptScreen instance;
	private OrthographicCamera guiCam;
	private SpriteBatch batcher; 
	private Vector3 touchPoint;
	private BoundingBox acceptClick;
	private Game game;
	private BitmapFont font;
	private TextureRegion background;
	private Vector<String> screenModeV;
	private Vector<String> userV;
	private Screen ancestor; 
	private String screenMode;
	private String user;
	
	public static AcceptScreen getInstance() {
		if (instance == null) instance = new AcceptScreen();
		return instance;
	}

	public AcceptScreen() {
		instance = this;
		this.game = LaunchFrozenWars.getGame();
		screenModeV = new Vector<String>();
		userV = new Vector<String>();
		guiCam = new OrthographicCamera(1024,629);
		guiCam.position.set(512,315,0);
		
		font = new BitmapFont(Gdx.files.internal("data/first.fnt"), Gdx.files.internal("data/first.png"), false);;

	    batcher = new SpriteBatch();
	    touchPoint = new Vector3();

	    acceptClick = new BoundingBox(new Vector3(350,340,0), new Vector3(500,400,0));
	}
	
	public void setNewAcceptScreen(String mode, String usr){
		if (!(mode.equals("InviteGame") && MultiplayerScreen.getInstance().isInQueue())){
			screenModeV.add(mode);
			userV.add(usr);
		}
	}
	
	public void createAcceptIfNeeded(){
		if (!screenModeV.isEmpty()){
			background = ScreenUtils.getFrameBufferTexture();
			ancestor = game.getScreen();
			screenMode = screenModeV.remove(0);
			user = userV.remove(0);
			game.setScreen(this);
		}
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
      		System.out.println(Integer.toString((int)touchPoint.x).concat(",").concat(Integer.toString((int)touchPoint.y)));

      		//compruebo si he tocado yes 
      		if (screenMode.equals("FullTeam") || screenMode.equals("QueueExit") || screenMode.equals("GameNotFound") || screenMode.equals("LeaderLeft") || screenMode.equals("UserOutOfQueue")){
  				MultiplayerScreen.getInstance().setDefault();
  				game.setScreen(MultiplayerScreen.getInstance());
  			} else if (screenMode.equals("DiffPasswords") || screenMode.equals("PasswordChars") || screenMode.equals("Email") || screenMode.equals("Username") || screenMode.equals("NamePassNotValid")){
  				game.setScreen(ancestor);
  			} else if (screenMode.equals("RegisterSuccess")){
  				game.setScreen(LogSignScreen.getInstance());
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
            batcher.draw(Assets.okWindow, 330, 300);
            if (screenMode.equals("FullTeam")){
            	String message = "You can't join this game because the team is full.";
            	font.drawWrapped(batcher, message, 350, 538, 330);
            } else if (screenMode.equals("QueueExit")){
            	String message = "Someone in your team left so you all have left the queue.";
            	font.drawWrapped(batcher, message, 350, 538, 330);
            }  else if (screenMode.equals("GameNotFound")){
            	String message = "Game not found.";
            	font.drawWrapped(batcher, message, 350, 538, 330);
            } else if (screenMode.equals("LeaderLeft")){
            	String message = "Game's leader left.";
            	font.drawWrapped(batcher, message, 350, 538, 330);
            } else if (screenMode.equals("UserOutOfQueue")){
            	String message = "A teammate has disconnected so you all have left the queue.";
            	font.drawWrapped(batcher, message, 350, 538, 330);
            } else if (screenMode.equals("DiffPasswords")){
            	String message = "Passwords must be different.";
            	font.drawWrapped(batcher, message, 350, 538, 330);
            } else if (screenMode.equals("PasswordChars")){
            	String message = "Password must have between 4 and 8 characters.";
            	font.drawWrapped(batcher, message, 350, 538, 330);
            } else if (screenMode.equals("Email")){
            	String message = "The email is not correct.";
            	font.drawWrapped(batcher, message, 350, 538, 330);
            } else if (screenMode.equals("Username")){
            	String message = "Write an user name.";
            	font.drawWrapped(batcher, message, 350, 538, 330);
            } else if (screenMode.equals("NamePassNotValid")){
            	String message = "User and/or password are not valid.";
            	font.drawWrapped(batcher, message, 350, 538, 330);
            } else if (screenMode.equals("RegisterSuccess")){
            	String message = "You are now registered!";
            	font.drawWrapped(batcher, message, 350, 538, 330);
            }
            
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
