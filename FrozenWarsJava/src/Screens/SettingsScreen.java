package Screens;

import Application.Assets;
import Application.GameSettings;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class SettingsScreen implements Screen{


	    /** The gui cam. */
	private OrthographicCamera guiCam;
	
	/** Se utiliza para dibujar y optimizar las imagenes en el renderizado de la pantalla. */
	private SpriteBatch batcher;
	private GameSettings gSettings;
	private InitialScreen initialScreen;
	
	private Vector3 touchPoint;
	private BoundingBox soundClick;
	private BoundingBox vibrationClick;
	private BoundingBox loggedClick;
	private BoundingBox confirmedExitClick;
	private BoundingBox volverClick;
	private Game game;
	private boolean confirm;

	public SettingsScreen(Game game, GameSettings gSettings, InitialScreen initialScreen) {
		this.game = game;
		this.gSettings = gSettings;
		this.initialScreen = initialScreen;
		guiCam = new OrthographicCamera(420,380);
		guiCam.position.set(210,190,0);
		
		confirm = gSettings.isConfirmedExitOn(); //True si esta activada la opcion de confirmacion al salir

	    batcher = new SpriteBatch();
	    touchPoint = new Vector3();
	    //Esquina inferior izq y superior derecha
	    soundClick = new BoundingBox(new Vector3(140,300,0), new Vector3(290,320,0));
	    vibrationClick = new BoundingBox(new Vector3(140,250,0), new Vector3(290,270,0));
	    loggedClick = new BoundingBox(new Vector3(140,200,0), new Vector3(290,220,0));
	    confirmedExitClick = new BoundingBox(new Vector3(140,150,0), new Vector3(290,170,0));
	    volverClick = new BoundingBox(new Vector3(140, 80, 0), new Vector3 (290, 100, 0));
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
      			
			//compruebo si he tocado play (se abre ventana de introduccion de usuario si no esta logeado)
			if (soundClick.contains(touchPoint))
				gSettings.setSoundOn(!gSettings.isSoundOn());	      			
      		else if(vibrationClick.contains(touchPoint))
      			gSettings.setVibrationOn(!gSettings.isVibrationOn());
      		else if (loggedClick.contains(touchPoint)){
      			System.out.println("Pulsado loggin");
      		}else if (confirmedExitClick.contains(touchPoint))
      			gSettings.setConfirmedExitOn(!gSettings.isConfirmedExitOn());
      		else if (volverClick.contains(touchPoint))
      			game.setScreen(initialScreen);
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
        batcher.draw(Assets.secondBack,0,0,420,380);
        batcher.end();

        //Dibujando elementos en pantalla activamos el Blending
        batcher.enableBlending();
        batcher.begin();
	             
	        
	   if (gSettings.isSoundOn()) batcher.draw(Assets.soundOn, 120, 300);
	   else batcher.draw(Assets.soundOff, 120, 300);
	   if (gSettings.isVibrationOn()) batcher.draw(Assets.vibrationOn, 120, 250);
	   else batcher.draw(Assets.vibrationOff, 120, 250);
	   if (gSettings.isLoggedIn()) batcher.draw(Assets.loggedIn, 120, 200);
	   else batcher.draw(Assets.loggedOut, 120, 200);
	   if (gSettings.isConfirmedExitOn()) batcher.draw(Assets.confirmedExitOn, 120, 150);
	   else batcher.draw(Assets.confirmedExitOff, 120, 150);
	   batcher.draw(Assets.volver, 120, 80);
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
