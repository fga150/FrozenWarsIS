package Screens;

import Application.Assets;
import Application.Desktop;
import Application.LaunchFrozenWars;

import Server.SmartFoxServer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class LogInScreen implements Screen {
	
	private static LogInScreen instance;

	public static LogInScreen getInstance() {
		if (instance == null) instance = new LogInScreen();
		return instance;
	}
	
	private SmartFoxServer sfsClient;
	private OrthographicCamera guiCam;
	private SpriteBatch batcher; 
	private Vector3 touchPoint;
	private BoundingBox okClick;
	private BoundingBox userClick;
	private BoundingBox passClick;
	private BoundingBox backClick;
	private Game game;
	private BitmapFont font;
	private String user;
	private String userShown;
	private String pass;
	private String passShown;	
	private String passS;
	private LaunchFrozenWars proc;

	
	public LogInScreen() {
		instance = this;
		this.game = LaunchFrozenWars.getGame();
		font = new BitmapFont(Gdx.files.internal("data/first.fnt"), Gdx.files.internal("data/first.png"), false);;
		
		this.user = this.pass = this.passShown = this.passS  = "";
	
		guiCam = new OrthographicCamera(1024,630);
		guiCam.position.set(512,315,0);
	
		font = new BitmapFont(Gdx.files.internal("data/first.fnt"), Gdx.files.internal("data/first.png"), false);

	    batcher = new SpriteBatch();
	    touchPoint = new Vector3();
	    

	    //Bounding boxes to check if some component's screen has been touched
	    userClick = new BoundingBox(new Vector3(440,395,0), new Vector3(670,440,0));
	    passClick = new BoundingBox(new Vector3(435,325,0), new Vector3(700,370,0));      
	    okClick = new BoundingBox(new Vector3(335,240,0), new Vector3(500,300,0));  
	    backClick = new BoundingBox(new Vector3(540,245,0), new Vector3(700,300,0)); 
	    
	    sfsClient = SmartFoxServer.getInstance();
	    
	    proc = (LaunchFrozenWars) Gdx.input.getInputProcessor();
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
      
      if (Gdx.input.justTouched()){ 
      		guiCam.unproject(touchPoint.set(Gdx.input.getX(),Gdx.input.getY(),0));
  		
      		if (userClick.contains(touchPoint)){ //Check if we have clicked on user's square 
      			Gdx.input.setOnscreenKeyboardVisible(true);
      			proc.setInfoPressed(1);
      		} else if(passClick.contains(touchPoint)){ //Check if we have clicked on pass's square (primary pass)
      			Gdx.input.setOnscreenKeyboardVisible(true);
      			proc.setInfoPressed(2);
      		} else if(okClick.contains(touchPoint)){ //Check if we have clicked on "ok" button 
      			proc.setInfoPressed(0);
      			sfsClient.conectaSala(user, pass);
      		} else if(backClick.contains(touchPoint)){ //Check if we have clicked on "back" button 
      			proc.setInfoPressed(0);
      			game.setScreen(LogSignScreen.getInstance());
      		} else{ //We haven't clicked on any component
      			proc.setInfoPressed(0);
      		}
      }
      //One batcher in each screen. We should remove it when we don't need it anymore
        	GL10 gl = Gdx.graphics.getGL10(); //references to OpenGL 1.0
            gl.glClearColor(0,1,0,1);
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
 
            guiCam.update();
            batcher.setProjectionMatrix(guiCam.combined);
             
           
            batcher.disableBlending();
            batcher.begin();
            batcher.draw(Assets.backGrey,0,0, 1024, 630);  //Drawning Background
            batcher.end();

          //Dranwing elements on the screen
            batcher.enableBlending();
            batcher.begin();    
            batcher.draw(Assets.logInWindow, 300, 200);
            
            userShown = user;
            passShown = passS;
            
            if (font.getBounds(userShown).width > 240){
				int j = 0;
				while (font.getBounds(userShown.substring(j, userShown.length())).width > 240) j++;
					userShown = userShown.substring(j, userShown.length());
			}
            
            if (font.getBounds(passShown).width > 240){
				int j = 0;
				while (font.getBounds(passShown.substring(j, passShown.length())).width > 240) j++;
					passShown = passShown.substring(j, passShown.length());
			}
            
            
            if (proc.getInfoPressed() != 1) font.drawWrapped(batcher, userShown, 450, 435, 240);
            else { //Draw with Cursor
            	if (System.currentTimeMillis()%1000 > 500) font.drawWrapped(batcher, userShown+"|", 450, 435, 240);
            	else font.drawWrapped(batcher, userShown, 450, 435, 240);
            }
            
            if (proc.getInfoPressed() != 2) font.drawWrapped(batcher, passShown, 450, 375, 240);
            else { //Draw with Cursor
            	if (System.currentTimeMillis()%1000 > 500) {
            		font.drawWrapped(batcher, passShown, 450, 375, 240);
            		font.draw(batcher, "|", 450+font.getBounds(passShown).width, 368);
            	}
            	else font.drawWrapped(batcher, passShown, 450, 375, 240);
            }
            
            batcher.end();	
            
            MultiplayerScreen.getInstance().changeToThisIfNeeded();
            ConfirmScreen.getInstance().createConfirmIfNeeded();
            AcceptScreen.getInstance().createAcceptIfNeeded();
            
	}

	@Override
	public void resize(int arg0, int arg1) {
		if (arg0!=1024 || arg1!=630) Desktop.resetScreenSize();

	}

	@Override
	public void resume() {
	}

	@Override
	public void show() {
	}
	
	
	public void setMessage(char c){
		if (proc.getInfoPressed() == 1){ //If we are writing user's name
        	user += c;    //We add the char pressed
        } else if (proc.getInfoPressed() == 2){  //If we are writing pass
        	pass += c; //We add the char pressed
        	passS +='.'; 
        } 
	}
	
	public void del(){
		if (proc.getInfoPressed() == 1 && user.length()>0){ 
        	user = (String)user.subSequence(0, user.length()-1);
        } else if (proc.getInfoPressed() == 2 && pass.length()>0){
        	pass = (String)pass.subSequence(0, pass.length()-1);
        	passS = (String)passS.subSequence(0, passS.length()-1);
        }
	}
	
	public void enter(){
		if (proc.getInfoPressed() == 1 || proc.getInfoPressed() == 2){
			proc.setInfoPressed(0);
  			sfsClient.conectaSala(user, pass);
        }
	}
}
