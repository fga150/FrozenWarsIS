package Screens;

import Application.Assets;
import Application.GameSettings;
import Server.SmartFoxServer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LoadScreen implements Screen{

	private SpriteBatch batcher; 
	private Game game;
	private OrthographicCamera guiCam;
	private GameSettings gSettings;
	private long t0, t1;
	private boolean showed;
	private BitmapFont font;
	 
	public LoadScreen(Game game, GameSettings gSettings){
		this.game = game;
		this.showed = false;
		font = new BitmapFont();
		guiCam = new OrthographicCamera(420,380);
		guiCam.position.set(210,190,0);
		batcher = new SpriteBatch();
		this.gSettings = gSettings; 
		t0 =  System.currentTimeMillis(); //Hora de  creacion de la pantalla	
	}	
	
	@Override
	public void dispose() {
		//font.dispose();
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
		
		t1 = System.currentTimeMillis(); //hora actual
		//comprobamos si han pasado 5 seg o se ha tocado la pantalla
		//en cuyo caso se abre la ventana de inicio
        if ((((t1 - t0) >= (5000)) || Gdx.input.justTouched()) && !showed) { 
        	showed = true;
            InitialScreen initialScreen = new InitialScreen(game,gSettings);
            game.setScreen(initialScreen);
    	return;
        }
 
        GL10 gl = Gdx.graphics.getGL10(); //referencia a OpenGL 1.0
        gl.glClearColor(0,1,0,1);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
        guiCam.update();
        batcher.setProjectionMatrix(guiCam.combined);
         
        //Dibujando el Background
        batcher.disableBlending();
        //se elimina graficamente la transparencia ya que es un fondo
        batcher.begin();
        batcher.draw(Assets.backConf,0,0,420,380);
        batcher.end();
   


		// Begin our batch call
        batcher.enableBlending();
        //se elimina graficamente la transparencia ya que es un fondo
         switch ((int)((t1-t0)/1000)){ //Se mira el tiempo restante para cerrar la ventana para mostrarlo por pantalla
         	case 1:
         		batcher.begin();
         		batcher.draw(Assets.cuatroSec,50,170);
         		batcher.end();
         		break;
         	case 2:
         		batcher.begin();
         		batcher.draw(Assets.tresSec,50,170);
         		batcher.end();
         		break;
         	case 3:
         		batcher.begin();
         		batcher.draw(Assets.dosSec,50,170);
         		batcher.end();
         		break;
         	case 4:
         		batcher.begin();
         		batcher.draw(Assets.unSec,50,170);
         		batcher.end();
         		break;
          }
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
