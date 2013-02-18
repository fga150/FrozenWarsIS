package Screens;

import Application.Assets;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class ConfirmScreen implements Screen{
	private InitialScreen initialScreen;
	private OrthographicCamera guiCam;
	private SpriteBatch batcher; 
	private Vector3 touchPoint;
	private BoundingBox yesClick;
	private BoundingBox noClick;
	private Game game;
	

	public ConfirmScreen(Game game, InitialScreen inScreen) {
		this.game = game;
		this.initialScreen = inScreen;
		guiCam = new OrthographicCamera(420,380);
		guiCam.position.set(210,190,0);

	    batcher = new SpriteBatch();
	    touchPoint = new Vector3();
	    //Esquina inferior izq y superior derecha
	    yesClick = new BoundingBox(new Vector3(120,200,0), new Vector3(200,245,0));
	    noClick = new BoundingBox(new Vector3(225,200,0), new Vector3(305,245,0));
	              
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
      			
      			//compruebo si he tocado yes (para cerrar la aplicacion)
      			if (yesClick.contains(touchPoint)){
      				
      				this.dispose();
      			}else{
      				//compruebo si he tocado no (volvemos a la pantalla de inicio)
      				if(noClick.contains(touchPoint)){
      					game.setScreen(initialScreen);
      					return;	
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
            batcher.draw(Assets.backConf,0,0,420,380);
            batcher.end();

          //Dibujando elementos en pantalla activamos el Blending
            batcher.enableBlending();
            batcher.begin();    
            batcher.draw(Assets.panelConf, 50, 100);
            batcher.draw(Assets.yesConf, 70, 180);
            batcher.draw(Assets.noConf, 175, 180);
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
