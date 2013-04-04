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

      		if (screenMode.equals("FullTeam") || screenMode.equals("QueueExit") || screenMode.equals("GameNotFound") || screenMode.equals("LeaderLeft") || screenMode.equals("UserOutOfQueue")){
  				MultiplayerScreen.getInstance().setDefault();
  				game.setScreen(MultiplayerScreen.getInstance());
  			} else if (screenMode.equals("DiffPasswords") || screenMode.equals("PasswordChars") || screenMode.equals("Email") || screenMode.equals("Username") || screenMode.equals("NamePassNotValid")
  					   || screenMode.equals("AlreadyLogged") || screenMode.equals("AddFriendAdder")){
  				game.setScreen(ancestor);
  			} else if (screenMode.equals("AddFriendAdded")){
  				//TODO if (user.equals("SuccessYes")) friendsScreen.updateFriends();
  				game.setScreen(ancestor);
  			} else if (screenMode.equals("RegisterSuccess")){
  				if (user.equals("Registered")) game.setScreen(InitialScreen.getInstance()); //TODO conectarse y meterse directamente al MultiplayerScreen + guardar user y contraseña
  				else game.setScreen(ancestor);
  			} else if (screenMode.equals("AcceptedFriendRequest")){
            	//TODO friendsScreen.updateFriends();
  				game.setScreen(ancestor);
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
            String message = "";
            if (screenMode.equals("FullTeam")){
            	message = "You can't join this game because the team is full.";
            } else if (screenMode.equals("QueueExit")){
            	message = "Someone in your team left so you all have left the queue.";
            }  else if (screenMode.equals("GameNotFound")){
            	message = "Game not found.";
            } else if (screenMode.equals("LeaderLeft")){
            	message = "Game's leader left.";
            } else if (screenMode.equals("UserOutOfQueue")){
            	message = "A teammate has disconnected so you all have left the queue.";
            } else if (screenMode.equals("DiffPasswords")){
            	message = "Passwords must be the same.";
            } else if (screenMode.equals("PasswordChars")){
            	message = "Password must have between 4 and 8 characters.";
            } else if (screenMode.equals("Email")){
            	message = "The email is not correct.";
            } else if (screenMode.equals("Username")){
            	message = "Write an user name.";
            } else if (screenMode.equals("NamePassNotValid")){
            	message = "User and/or password are not valid.";
            } else if (screenMode.equals("AlreadyLogged")){
            	message = "There is another person logged with this account";
            } else if (screenMode.equals("AddFriendAdder")){
            	if (user.equals("Error")) message = "Something unexpected happened!";
            	else if (user.equals("UserNoExist")) message = "The user doesnt exist";
            	else if (user.equals("Friends")) message = "The user is already your friend";
            	else if (user.equals("Success")) message = "You have added the user as friend (he has to accept the invitation)";
            } else if (screenMode.equals("AddFriendAdded")){
            	if (user.equals("Error")) message = "Something unexpected happened!";
            	else if (user.equals("SuccessYes")) message = "You have the user as friend";
            	else if (user.equals("SuccessNo")) message = "You will not be friend with that user";
            } else if (screenMode.equals("RegisterSuccess")){
            	if (user.equals("Error")) message = "Something unexpected happened!";
            	else if (user.equals("UserExits")) message = "There is a user with that name already"; //TODO deberia ser UserExists (mirar los demas tambien)
            	else if (user.equals("EmailExits")) message = "There is a user with that email already"; //TODO deberia ser EmailExists
            	else if (user.equals("Registered")) message = "You have been correctly registered";
            	else message = user;
            } else if (screenMode.equals("AcceptedFriendRequest")){
            	message = user.concat(" accepted your friend request");
            }
            
            font.drawWrapped(batcher, message, 350, 538, 330);
            batcher.end();	
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
