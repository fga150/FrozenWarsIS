package Screens;

import java.io.IOException;


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

public class InitialScreen implements Screen{


	    /** The gui cam. */
	private OrthographicCamera guiCam;
	
	/** Se utiliza para dibujar y optimizar las imagenes en el renderizado de la pantalla. */
	private SpriteBatch batcher;
	private GameSettings gSettings;
	private Vector3 touchPoint;
	private BoundingBox playClick;
	private BoundingBox settingsClick;
	private BoundingBox helpClick;
	private BoundingBox exitClick;
	private Game game;
	
	public InitialScreen(Game game, GameSettings gSettings) {
		this.game = game;
		this.gSettings = gSettings;
		guiCam = new OrthographicCamera(420,380);
		guiCam.position.set(210,190,0);
		
	    batcher = new SpriteBatch();
	    touchPoint = new Vector3();
	    //Esquina inferior izq y superior derecha
	    playClick = new BoundingBox(new Vector3(45,300,0), new Vector3(195,320,0));
	    settingsClick = new BoundingBox(new Vector3(245,300,0), new Vector3(395,320,0));
	    helpClick = new BoundingBox(new Vector3(45,240,0), new Vector3(195,260,0));
	    exitClick = new BoundingBox(new Vector3(245,240,0), new Vector3(395,260,0));
	              
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
			if (playClick.contains(touchPoint)){
				/*MatchManager manager = new MatchManager(sfsClient);
				sfsClient.addManager(manager);
				GameScreen gameScreen = new GameScreen(game,manager);
				manager.setGameScreen(gameScreen);
  				game.setScreen(gameScreen);*/
				MultiplayerScreen multiplayerScreen = new MultiplayerScreen(game, gSettings, this);
				game.setScreen(multiplayerScreen);

      		}else{
      			//compruebo si he tocado settings
      			if(settingsClick.contains(touchPoint)){
      				System.out.println("Pulsado settings");
      				SettingsScreen settingsScreen = new SettingsScreen(game, gSettings, this);
      				game.setScreen(settingsScreen);
      			}else{
      				//compruebo si he tocado help (Se abre ventana de ayuda)
      				if (helpClick.contains(touchPoint)){
      					System.out.println("Pulsado help");
      				}else{
      					//compruebo si he tocado exit (Se abre ventana de confirmacion)
      					if(exitClick.contains(touchPoint)){
      						try {
								gSettings.saveSettings();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								return;
							}
      						if (!gSettings.isConfirmedExitOn()) {
      							this.dispose();
      						} else{
	      	      				Screen gameScreen = new ConfirmScreen(game, this);   
	      	      				game.setScreen(gameScreen);
	      	      				return;
      						}
      					}
      				}
      			}
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
            batcher.draw(Assets.initialBack,0,0,420,380);
            batcher.end();

            //Dibujando elementos en pantalla activamos el Blending
            batcher.enableBlending();
            batcher.begin();
	                
	          batcher.draw(Assets.play, 25, 300);
	          batcher.draw(Assets.settings, 225, 300);
	          batcher.draw(Assets.help, 25, 240);
	          batcher.draw(Assets.exit, 225, 240);
	          
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
