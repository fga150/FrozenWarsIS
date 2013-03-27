package Screens;

import Application.Assets;
import Application.LaunchFrozenWars;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
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
	

	private OrthographicCamera guiCam;
	private SpriteBatch batcher; 
	private Vector3 touchPoint;
	private BoundingBox okClick;
	private BoundingBox userClick;
	private BoundingBox passClick;
	private BoundingBox backClick;
	private Game game;
	private BitmapFont font;
	private int infoPressed;
	private String user;
	private String pass;
	private String passShown;	
	private long time;
	
/*TODO : Poner singleton */
	
	public LogInScreen() {
		this.game = LaunchFrozenWars.getGame();
		this.infoPressed = 0;
		this.time = System.nanoTime();
		font = new BitmapFont(Gdx.files.internal("data/first.fnt"), Gdx.files.internal("data/first.png"), false);;
		
		this.user = this.pass = this.passShown = "";

		
		guiCam = new OrthographicCamera(1024,630);
		guiCam.position.set(512,315,0);

		
		font = new BitmapFont(Gdx.files.internal("data/first.fnt"), Gdx.files.internal("data/first.png"), false);;

	    batcher = new SpriteBatch();
	    touchPoint = new Vector3();

	    userClick = new BoundingBox(new Vector3(440,395,0), new Vector3(670,440,0));
	    passClick = new BoundingBox(new Vector3(435,325,0), new Vector3(700,370,0));      
	    okClick = new BoundingBox(new Vector3(335,240,0), new Vector3(500,300,0));  
	    backClick = new BoundingBox(new Vector3(540,245,0), new Vector3(700,300,0));  
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

      		//compruebo si he tocado yes 
      		if (userClick.contains(touchPoint)){
      			Gdx.input.setOnscreenKeyboardVisible(true);
      			this.infoPressed = 1;
      			System.out.println("user");
      		} else if(passClick.contains(touchPoint)){ //compruebo si he tocado no
      			Gdx.input.setOnscreenKeyboardVisible(true);
      			this.infoPressed = 2;
      			System.out.println("pass");
      		} else if(okClick.contains(touchPoint)){ //compruebo si he tocado no
      			System.out.println("ok");
      		} else if(backClick.contains(touchPoint)){ //compruebo si he tocado no
      			game.setScreen(LogSignScreen.getInstance());
      			System.out.println("back");
      		} else{
      			this.infoPressed = 0;
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
            batcher.draw(Assets.backGrey,0,0, 1024, 630);
            batcher.end();

          //Dibujando elementos en pantalla activamos el Blending
            batcher.enableBlending();
            batcher.begin();    
            batcher.draw(Assets.logInWindow, 300, 200);

            font.drawWrapped(batcher, user, 450, 435, 240);
            font.drawWrapped(batcher, passShown, 450, 365, 240);
            batcher.end();	

            
            if ( this.infoPressed !=0 && ((System.nanoTime() - this.time) > 129000000)){
            	this.completeMessagePc();
            	this.time = System.nanoTime();
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
	
	
	private void completeMessagePc(){
		char aux = 0;
		
		if(Gdx.input.isKeyPressed(Keys.ANY_KEY)){
			  aux = ScreensKeyboard.keyPc();
		}

        if (aux != 0){
	        if (this.infoPressed == 1){
	        	user += aux;
	        	System.out.println("user: " + user);
	        } else if (this.infoPressed == 2){
	        	pass += aux;
	        	passShown +='$';
	        	System.out.println("pass: " + pass);
	        } 
        }  
        
        if (ScreensKeyboard.delete()){
        	if (this.infoPressed == 1 && user.length()>0){
	        	user = (String)user.subSequence(0, user.length()-1);
	        } else if (this.infoPressed == 2 && pass.length()>0){
	        	pass = (String)pass.subSequence(0, pass.length()-1);
	        	passShown = (String)passShown.subSequence(0, passShown.length()-1);
	        }
        }
	}
}
