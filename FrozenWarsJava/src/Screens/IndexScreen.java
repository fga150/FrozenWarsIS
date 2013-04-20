
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

public class IndexScreen implements Screen{

	private static IndexScreen instance; 
	public static IndexScreen getInstance() {
		if (instance == null) instance = new IndexScreen();
		return instance;
	}
	
	    /** The gui cam. */
	private OrthographicCamera guiCam;
	
	/** Se utiliza para dibujar y optimizar las imagenes en el renderizado de la pantalla. */
	private SpriteBatch batcher;
	private Vector3 touchPoint;
	private Game game;
	private InitialScreen initialScreen;
	    
    private  BoundingBox introductionButtonHelpClick;
    private  BoundingBox upgradesButtonHelpClick;
    private  BoundingBox controlButtonHelpClick; 
    private  BoundingBox createButtonHelpClick;
    private  BoundingBox gameModesButtonHelpClick;
 
    private  BoundingBox backButtonHelpClick;
    
    int window;

    public IndexScreen() {
    	instance=this;
		this.game = LaunchFrozenWars.getGame();
		this.initialScreen = InitialScreen.getInstance();

		guiCam = new OrthographicCamera(1024,630);
		guiCam.position.set(512,315,0);
		
	    batcher = new SpriteBatch();
	    touchPoint = new Vector3();

	    
	    //Esquina inferior izq y superior derecha

	    introductionButtonHelpClick = new BoundingBox(new Vector3(320, 455,0), new Vector3(636,517,0));    
	    upgradesButtonHelpClick = new BoundingBox(new Vector3(320, 385,0), new Vector3(636,442,0));
	    controlButtonHelpClick = new BoundingBox(new Vector3(320, 315,0), new Vector3(636,372,0));
	    createButtonHelpClick = new BoundingBox(new Vector3(320, 245,0), new Vector3(636,302,0));    
	    gameModesButtonHelpClick = new BoundingBox(new Vector3(320, 175,0), new Vector3(636,290,0));
	
	    backButtonHelpClick = new BoundingBox(new Vector3(320, 50,0), new Vector3(636,107,0));
	    
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
			if (introductionButtonHelpClick.contains(touchPoint)){
				HelpScreen helpScreen = new HelpScreen(this,0);
				game.setScreen(helpScreen);
				
			} else if (upgradesButtonHelpClick.contains(touchPoint)){
				HelpScreen helpScreen = new HelpScreen(this,1);
				game.setScreen(helpScreen);
				
			} else if (controlButtonHelpClick.contains(touchPoint)){
				HelpScreen helpScreen = new HelpScreen(this,2);
				game.setScreen(helpScreen);
				
			} else if (createButtonHelpClick.contains(touchPoint)){
				HelpScreen helpScreen = new HelpScreen(this,3);
				game.setScreen(helpScreen);
				
			} else if (gameModesButtonHelpClick.contains(touchPoint)){
				HelpScreen helpScreen = new HelpScreen(this,4);
				game.setScreen(helpScreen);
				
			} else if (backButtonHelpClick.contains(touchPoint)){
				game.setScreen(	initialScreen);
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
            batcher.draw(Assets.indexTitle, 420, 540);

            batcher.draw(Assets.introductionButtonHelp, 320, 455);
            batcher.draw(Assets.upgradesButtonHelp, 320, 385);            
            batcher.draw(Assets.controlButtonHelp, 320, 315);   
            batcher.draw(Assets.createButtonHelp, 320, 245);   
            batcher.draw(Assets.gameModesButtonHelp, 320, 175);   
            
            batcher.draw(Assets.backButtonHelp, 320, 50);   
          
            	          
            batcher.end();

            ConfirmScreen.getInstance().createConfirmIfNeeded();
            AcceptScreen.getInstance().createAcceptIfNeeded();

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