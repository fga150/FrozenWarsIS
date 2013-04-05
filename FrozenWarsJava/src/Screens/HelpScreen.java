
package Screens;

import Application.Assets;
import Application.LaunchFrozenWars;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class HelpScreen implements Screen{


	    /** The gui cam. */
	private OrthographicCamera guiCam;
	
	/** Se utiliza para dibujar y optimizar las imagenes en el renderizado de la pantalla. */
	private SpriteBatch batcher;
	private Vector3 touchPoint;
	private IndexScreen indexScreen;
    
    private  BoundingBox leftArrowClick;
    private  BoundingBox rightArrowClick;
    
    private  BoundingBox backButtonClick;
   
    private int window;

	private Game game;
    

    public HelpScreen(IndexScreen indexScreen, int window) {
		this.game = LaunchFrozenWars.getGame();
		this.indexScreen = indexScreen;
		this.window = window;

		guiCam = new OrthographicCamera(1024,630);
		guiCam.position.set(512,315,0);
		
	    batcher = new SpriteBatch();
	    touchPoint = new Vector3();

	    
	    //Esquina inferior izq y superior derecha
	    
	    rightArrowClick = new BoundingBox(new Vector3(655,540,0), new Vector3(700,600,0));
	    leftArrowClick = new BoundingBox(new Vector3(310, 530,0), new Vector3(365,595,0));
	    
	    backButtonClick = new BoundingBox(new Vector3(375, 50,0), new Vector3(615,90,0));
	    
	    window = 0;
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
			if (backButtonClick.contains(touchPoint)){
				game.setScreen(indexScreen);
      		} else if (leftArrowClick.contains(touchPoint)){
      			window = Math.max(0, window - 1);
      		} else if (rightArrowClick.contains(touchPoint)){
      			window = Math.min(4, window + 1);
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
            batcher.draw(Assets.backGrey,0,0,1024,630);
            batcher.end();

            //Dibujando elementos en pantalla activamos el Blending
            batcher.enableBlending();
            batcher.begin();
            
            if (this.window == 0) {
            	batcher.draw(Assets.introductionButtonHelp, 340, 540);
            	batcher.draw(Assets.introductionHelpText, 0, 0);
            } else if (this.window == 1){
            	batcher.draw(Assets.upgradesButtonHelp, 340, 540);
            	batcher.draw(Assets.upgradesHelpText, 0, 0);
            } else if (this.window == 2){
            	batcher.draw(Assets.controlButtonHelp, 340, 540);
            	batcher.draw(Assets.controlHelpText, 0, 0);
            } else if (this.window == 3){
            	batcher.draw(Assets.createButtonHelp, 340, 540);
            	batcher.draw(Assets.createGameHelpText, 0, 0);
            } else if (this.window == 4){
            	batcher.draw(Assets.gameModesButtonHelp, 340, 540);
            	batcher.draw(Assets.gameModesHelpText, 0, 0);
            }
            
            batcher.draw(Assets.modeLeftArrow, 320, 543);            
            batcher.draw(Assets.modeRightArrow, 670, 543);    
            batcher.draw(Assets.backButton, 375, 50);   
            	          
            batcher.end();

            ConfirmScreen.getInstance().createConfirmIfNeeded();

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