package Screens;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JTextArea;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

import Application.Assets;
import Application.LaunchFrozenWars;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.scenes.scene2d.Stage;

/*TODO: Poner singleton*/

public class SignInScreen implements Screen {
	
	private static SignInScreen instance;

	public static SignInScreen getInstance() {
		if (instance == null) instance = new SignInScreen();
		return instance;
	}
	

	private OrthographicCamera guiCam;
	private SpriteBatch batcher; 
	private Vector3 touchPoint;
	private BoundingBox okClick;
	private BoundingBox userClick;
	private BoundingBox emailClick;
	private BoundingBox pass1Click;
	private BoundingBox pass2Click;
	private BoundingBox backClick;
	private Game game;
	private BitmapFont font;
	private int infoPressed;
	private String user;
	private String email;
	private String pass1;
	private String pass2;
	private String pass1Shown;
	private String pass2Shown;	
	private long time;



	public SignInScreen() {
		this.game = LaunchFrozenWars.getGame();
		this.infoPressed = 0;
		this.time = System.nanoTime();
		font = new BitmapFont(Gdx.files.internal("data/first.fnt"), Gdx.files.internal("data/first.png"), false);;
		
		this.user = this.email = this.pass1 = this.pass2 = this.pass1Shown = this.pass2Shown = "";
		guiCam = new OrthographicCamera(1024,630);
		guiCam.position.set(512,315,0);

		
		font = new BitmapFont(Gdx.files.internal("data/first.fnt"), Gdx.files.internal("data/first.png"), false);;

	    batcher = new SpriteBatch();
	    touchPoint = new Vector3();

	    userClick = new BoundingBox(new Vector3(440,465,0), new Vector3(700,510,0));
	    pass1Click = new BoundingBox(new Vector3(440,335,0), new Vector3(700,380,0));      
	    pass2Click = new BoundingBox(new Vector3(440,270,0), new Vector3(700,320,0));  
	    emailClick = new BoundingBox(new Vector3(440,400,0), new Vector3(700,445,0));  
	    okClick = new BoundingBox(new Vector3(325,175,0), new Vector3(490,300,0));  
	    backClick = new BoundingBox(new Vector3(540,175,0), new Vector3(700,230,0)); 
	    
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
      			System.out.println("user");
      			this.infoPressed = 1;
      		} else if(pass1Click.contains(touchPoint)){ //compruebo si he tocado no
      			Gdx.input.setOnscreenKeyboardVisible(true);
      			System.out.println("pass1");
      			this.infoPressed = 3;
      		} else if(pass2Click.contains(touchPoint)){ //compruebo si he tocado no
      			Gdx.input.setOnscreenKeyboardVisible(true);
      			System.out.println("pass2");
      			this.infoPressed = 4;
      		} else if(emailClick.contains(touchPoint)){ //compruebo si he tocado no
      			Gdx.input.setOnscreenKeyboardVisible(true);
      			System.out.println("email");
      			this.infoPressed = 2;
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
            batcher.draw(Assets.signInWindow, 300, 100);
            

            font.drawWrapped(batcher, user, 450, 500, 240);
            font.drawWrapped(batcher, email, 450, 440, 240);
            font.drawWrapped(batcher, pass1Shown, 450, 375, 240);
            font.drawWrapped(batcher, pass2Shown, 450, 310, 240);
            batcher.end();	
            
            if ( this.infoPressed !=0 && ((System.nanoTime() - this.time) > 129000000)){
            	this.completeMessagePc();
            	this.time = System.nanoTime();
            }
            ConfirmScreen.getInstance().createConfirmIfNeeded();
            AcceptScreen.getInstance().createAcceptIfNeeded();
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

	
	private void completeMessagePc(){ //TODO process also the characters @ and .
		char aux = 0;
				
		if(Gdx.input.isKeyPressed(Keys.ANY_KEY)){
			  aux = ScreensKeyboard.keyPc();
		}

        if (aux != 0){
	        if (this.infoPressed == 1){
	        	user += aux;
	        	System.out.println("user: " + user);
	        } else if (this.infoPressed == 2){
	        	email += aux;
	        	System.out.println("email: " + email);
	        } else if (this.infoPressed == 3){
	        	pass1 += aux;
	        	pass1Shown +='$';
	        	System.out.println("pass1: " + pass1);
	        } else if (this.infoPressed == 4){
	        	pass2 += aux;
	        	pass2Shown +='$';
	        	System.out.println("pass2: " + pass2);
	        }
        }

        if (ScreensKeyboard.delete()){
        	if (this.infoPressed == 1 && user.length()>0){
	        	user = (String)user.subSequence(0, user.length()-1);
	        } else if (this.infoPressed == 2 && email.length()>0){
	        	email = (String)email.subSequence(0, email.length()-1);
	        } else if (this.infoPressed == 3 && pass1.length()>0){
	        	pass1 = (String)pass1.subSequence(0, pass1.length()-1);
	        	pass1Shown = (String)pass1Shown.subSequence(0, pass1Shown.length()-1);
	        } else if (this.infoPressed == 4 && pass2.length()>0){
	        	pass2 = (String)pass2.subSequence(0, pass2.length()-1);
	        	pass2Shown = (String)pass2Shown.subSequence(0, pass2Shown.length()-1);
	        }
        }
	}
}
