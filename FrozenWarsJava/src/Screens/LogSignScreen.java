package Screens;


import Application.Assets;
import Application.Desktop;
import Application.LaunchFrozenWars;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

/*TODO: poner singleton*/

public class LogSignScreen implements Screen{
	
	private static LogSignScreen instance;

	public static LogSignScreen getInstance() {
		if (instance == null) instance = new LogSignScreen();
		return instance;
	}
	

		private OrthographicCamera guiCam;
		private SpriteBatch batcher; 
		private Vector3 touchPoint;
		private BoundingBox logInClick;
		private BoundingBox signInClick;
		private BoundingBox backClick;
		private Game game;

		

		public LogSignScreen() {
			instance = this;
			this.game = LaunchFrozenWars.getGame();
			
			guiCam = new OrthographicCamera(1024,629);
			guiCam.position.set(512,315,0);

		    batcher = new SpriteBatch();
		    touchPoint = new Vector3();

		    logInClick = new BoundingBox(new Vector3(330,330,0), new Vector3(490,390,0));
		    signInClick = new BoundingBox(new Vector3(540,330,0), new Vector3(700,380,0));      
		    backClick = new BoundingBox(new Vector3(450,250,0), new Vector3(590,290,0));  
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
	      		if (logInClick.contains(touchPoint)){
	      			game.setScreen(LogInScreen.getInstance());
	      		} else if(signInClick.contains(touchPoint)){ //compruebo si he tocado no
	      			game.setScreen(SignInScreen.getInstance());
	      		} else if(backClick.contains(touchPoint)){ //compruebo si he tocado no
	      			game.setScreen(InitialScreen.getInstance());
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
	            batcher.draw(Assets.logSignWindow, 300, 200);
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
	}
