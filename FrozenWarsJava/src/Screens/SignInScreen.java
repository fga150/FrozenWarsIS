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

public class SignInScreen implements Screen {
	
	private static SignInScreen instance;

	public static SignInScreen getInstance() {
		if (instance == null) instance = new SignInScreen();
		return instance;
	}
	
	private SmartFoxServer sfsClient;
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
	private String user;
	private String email;
	private String pass1;
	private String pass2;
	private String pass1S;
	private String pass2S;	
	
	private String userShown;
	private String emailShown;
	private String pass1Shown;
	private String pass2Shown;
	
	private LaunchFrozenWars proc;



	public SignInScreen() {
		instance = this;
		this.game = LaunchFrozenWars.getGame();
		font = new BitmapFont(Gdx.files.internal("data/first.fnt"), Gdx.files.internal("data/first.png"), false);;
		
		this.user = this.email = this.pass1 = this.pass2 = this.pass1Shown = this.pass2Shown = this.userShown = this.emailShown = this.pass1S = this.pass2S =  "";
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
      //detectamos si se ha tocado la pantalla
      if (Gdx.input.justTouched()){
      		guiCam.unproject(touchPoint.set(Gdx.input.getX(),Gdx.input.getY(),0));
      		//System.out.println(Integer.toString((int)touchPoint.x).concat(",").concat(Integer.toString((int)touchPoint.y)));

      		//compruebo si he tocado yes 
      		if (userClick.contains(touchPoint)){
      			Gdx.input.setOnscreenKeyboardVisible(true);
      			proc.setInfoPressed(11);
      		} else if(pass1Click.contains(touchPoint)){ //compruebo si he tocado no
      			Gdx.input.setOnscreenKeyboardVisible(true);
      			proc.setInfoPressed(13);
      		} else if(pass2Click.contains(touchPoint)){ //compruebo si he tocado no
      			Gdx.input.setOnscreenKeyboardVisible(true);
      			proc.setInfoPressed(14);
      		} else if(emailClick.contains(touchPoint)){ //compruebo si he tocado no
      			Gdx.input.setOnscreenKeyboardVisible(true);
      			proc.setInfoPressed(12);
      		} else if(okClick.contains(touchPoint)){ //compruebo si he tocado no
      			proc.setInfoPressed(0);
      			sfsClient.register(user, email, pass1, pass2);
      		} else if(backClick.contains(touchPoint)){ //compruebo si he tocado no
      			proc.setInfoPressed(0);
      			game.setScreen(LogSignScreen.getInstance());
      		} else{
      			proc.setInfoPressed(0);
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
            

            userShown = user;
            emailShown = email;
            pass1Shown = pass1S;
            pass2Shown = pass2S;
            
            if (font.getBounds(userShown).width > 240){
				int j = 0;
				while (font.getBounds(userShown.substring(j, userShown.length())).width > 240) j++;
					userShown = userShown.substring(j, userShown.length());
			}
            
            if (font.getBounds(pass1Shown).width > 240){
				int j = 0;
				while (font.getBounds(pass1Shown.substring(j, pass1Shown.length())).width > 240) j++;
					pass1Shown = pass1Shown.substring(j, pass1Shown.length());
			}
            if (font.getBounds(emailShown).width > 240){
				int j = 0;
				while (font.getBounds(emailShown.substring(j, emailShown.length())).width > 240) j++;
					emailShown = emailShown.substring(j, emailShown.length());
			}
            
            if (font.getBounds(pass2Shown).width > 240){
				int j = 0;
				while (font.getBounds(pass2Shown.substring(j, pass2Shown.length())).width > 240) j++;
					pass2Shown = pass2Shown.substring(j, pass2Shown.length());
			}
            
            
            if (proc.getInfoPressed() != 11) font.drawWrapped(batcher, userShown, 450, 500, 240);
            else { //Draw with Cursor
            	if (System.currentTimeMillis()%1000 > 500) font.drawWrapped(batcher, userShown+"|", 450, 500, 240);
            	else font.drawWrapped(batcher, userShown, 450, 500, 240);
            }
            
            if (proc.getInfoPressed() != 12) font.drawWrapped(batcher, emailShown, 450, 440, 240);
            else { //Draw with Cursor
            	if (System.currentTimeMillis()%1000 > 500) font.drawWrapped(batcher, emailShown+"|", 450, 440, 240);
            	else font.drawWrapped(batcher, emailShown, 450, 440, 240);
            }
            
            if (proc.getInfoPressed() != 13) font.drawWrapped(batcher, pass1Shown, 450, 383, 240);
            else { //Draw with Cursor
            	if (System.currentTimeMillis()%1000 > 500) {
            		font.drawWrapped(batcher, pass1Shown, 450, 383, 240);
            		font.draw(batcher, "|", 450+font.getBounds(pass1Shown).width, 376);
            	}
            	else font.drawWrapped(batcher, pass1Shown, 450, 383, 240);
            }
            
            if (proc.getInfoPressed() != 14) font.drawWrapped(batcher, pass2Shown, 450, 318, 240);
            else { //Draw with Cursor
            	if (System.currentTimeMillis()%1000 > 500) {
            		font.drawWrapped(batcher, pass2Shown, 450, 318, 240);
            		font.draw(batcher, "|", 450+font.getBounds(pass2Shown).width, 311);
            	}
            	else font.drawWrapped(batcher, pass2Shown, 450, 318, 240);
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
		if (proc.getInfoPressed() == 11){
        	user += c;
        } else if (proc.getInfoPressed() == 12){
        	email += c;
        } else if (proc.getInfoPressed() == 13){
        	pass1 += c;
        	pass1S +='.';
        } else if (proc.getInfoPressed() == 14){
        	pass2 += c;
        	pass2S +='.';
        }
	}
	
	public void del(){
		if (proc.getInfoPressed() == 11 && user.length()>0){
        	user = (String)user.subSequence(0, user.length()-1);
        } else if (proc.getInfoPressed() == 12 && email.length()>0){
        	email = (String)email.subSequence(0, email.length()-1);
        } else if (proc.getInfoPressed() == 13 && pass1.length()>0){
        	pass1 = (String)pass1.subSequence(0, pass1.length()-1);
        	pass1S = (String)pass1Shown.subSequence(0, pass1Shown.length()-1);
        } else if (proc.getInfoPressed() == 14 && pass2.length()>0){
        	pass2 = (String)pass2.subSequence(0, pass2.length()-1);
        	pass2S = (String)pass2S.subSequence(0, pass2S.length()-1);
        }
	}
	
	public void enter(){
		if (proc.getInfoPressed() == 11 || proc.getInfoPressed() == 12 || proc.getInfoPressed() == 13 || proc.getInfoPressed() == 14){
			proc.setInfoPressed(0);
  			sfsClient.register(user, email, pass1, pass2);
        }
	}
}
