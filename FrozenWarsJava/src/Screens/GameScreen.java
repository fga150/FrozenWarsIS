package Screens;
import java.util.ArrayList;
import java.util.List;

import Application.Assets;
import Application.MatchManager;
import Application.MatchManager.Direction;
import GameLogic.Harpoon;
import GameLogic.Map.TypeSquare;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;


public class GameScreen implements Screen{
	private Game game;
	private OrthographicCamera guiCam;
	private SpriteBatch batcher;
	private Vector3 touchPoint;
	private Vector3 touchPoint2;
	private BoundingBox fdBounds;
	private BoundingBox fiBounds;
	private BoundingBox farBounds;
	private BoundingBox fabBounds;
	private BoundingBox harpoonBounds;
	private PenguinAnimation[] penguinAnimations;
    private float stateTime;
	private TextureRegion currentFrame;
	private MatchManager manager;
	private List<Harpoon> harpoonList;
	
	public GameScreen(Game game, MatchManager manager){
		this.game=game;
		this.manager = manager;
		this.harpoonList = new ArrayList<Harpoon>();
		guiCam = new OrthographicCamera(21,13);
		guiCam.position.set(21f/2,13f/2,0);
		batcher = new SpriteBatch();
		touchPoint = new Vector3();
		touchPoint2 = new Vector3();
		fdBounds= new BoundingBox(new Vector3(5.5f,2.5f,0), new Vector3(8,4.5f,0));
		fiBounds= new BoundingBox(new Vector3(1,2.5f,0), new Vector3(3.5f,4.5f,0));
		farBounds= new BoundingBox(new Vector3(3.5f,4.6f,0), new Vector3(5.5f,7,0));
		fabBounds= new BoundingBox(new Vector3(3.5f,0,0), new Vector3(5.5f,2.4f,0));
		harpoonBounds= new BoundingBox(new Vector3(19,0,0), new Vector3(21,4,0));
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		//--------------------------------------------------------------------
		penguinAnimations = new PenguinAnimation[4];
		penguinAnimations[0] = new PenguinAnimation(Gdx.files.internal("data/pClarosRojo.png"),manager.getPlayerPosition(0),manager.getPlayerDirection(0));
		penguinAnimations[1] = new PenguinAnimation(Gdx.files.internal("data/pClarosVerde.png"),manager.getPlayerPosition(1),manager.getPlayerDirection(1));
		penguinAnimations[2] = new PenguinAnimation(Gdx.files.internal("data/pClarosAmarillo.png"),manager.getPlayerPosition(2),manager.getPlayerDirection(2));
		penguinAnimations[3] = new PenguinAnimation(Gdx.files.internal("data/pClarosAzul.png"),manager.getPlayerPosition(3),manager.getPlayerDirection(3));
        stateTime = 0f;     

      //--------------------------------------------------------------
}
	@Override
	public void dispose() {
		batcher.dispose();
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void render(float delta) {
		Vector3 position;
		Gdx.gl.glClearColor(1,1,1,1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		stateTime += Gdx.graphics.getDeltaTime();  
		batcher.setProjectionMatrix(guiCam.combined);
		batcher.enableBlending();
		batcher.begin();
		batcher.draw(Assets.getBackground(),0,0,21,13);
		
		batcher.draw(Assets.getHorizontalBarDown(),8,0,11,1);
		batcher.draw(Assets.getHorizontalBarUP(),8,12,11,1);
		batcher.draw(Assets.getVerticalBarLeft(),7,0,1,13);
		batcher.draw(Assets.getVerticalBarRigth(),19,0,1,13);
		
		batcher.draw(Assets.getLifeIconRed(),6,10.805f,0.75f,0.75f);
		batcher.draw(Assets.getLifeIconRed(),4,10.805f,0.75f,0.75f);
		batcher.draw(Assets.getLifeIconRed(),2,10.805f,0.75f,0.75f);
		
		batcher.draw(Assets.getLifeIconGreen(),6,9.68f,0.75f,0.75f);
		batcher.draw(Assets.getLifeIconGreen(),4,9.68f,0.75f,0.75f);
		batcher.draw(Assets.getLifeIconGreen(),2,9.68f,0.75f,0.75f);
		
		batcher.draw(Assets.getLifeIconYellow(),6,8.55f,0.75f,0.75f);
		batcher.draw(Assets.getLifeIconYellow(),4,8.55f,0.75f,0.75f);
		batcher.draw(Assets.getLifeIconYellow(),2,8.55f,0.75f,0.75f);
		
		batcher.draw(Assets.getLifeIconBlue(),6,7.43f,0.75f,0.75f);
		batcher.draw(Assets.getLifeIconBlue(),4,7.43f,0.75f,0.75f);
		batcher.draw(Assets.getLifeIconBlue(),2,7.43f,0.75f,0.75f);
		
		for(int i=0;i<11;i++){
			for (int j=0;j<11;j++){
				TextureRegion texture= null;
				TypeSquare type = manager.getSquare(i,j);
				texture = Assets.getBox(); 
				batcher.draw(texture,i+8,j+1,1,1);
				if (!type.equals(TypeSquare.empty)){
					if (type.equals(TypeSquare.unbreakable)) texture = Assets.getIgloo();
					else if (type.equals(TypeSquare.Harpoon)) texture = Assets.getHarpoon();				
					else if (type.equals(TypeSquare.breakable)) texture  = Assets.getBarrel();
					
					 batcher.draw(texture,i+8,j+1,1,1);
				}				
			}
		}	
		
		batcher.draw(Assets.getDirectionPanel(),1,0,7,7);
		batcher.draw(Assets.getButtonHarpoon(),19,0,2,4);
		
		for (int i=0;i<4;i++){
			position = penguinAnimations[i].getPosition();
			batcher.draw(penguinAnimations[i].getCurrentFrame(),(position.x)+8f,(position.y+1),1,1);
		}
		
		batcher.end();
		guiCam.update();
		
		//utilizo el istouched porque así no tengo k pulsar con deslizar me vale
		if (Gdx.input.isTouched()){
			//como no lo estamos haciendo con píxeles necesitamos utilizar la cam para ver las coordenadas exactas.
			guiCam.unproject(touchPoint.set(Gdx.input.getX(0),Gdx.input.getY(0),0));
			guiCam.unproject(touchPoint2.set(Gdx.input.getX(1),Gdx.input.getY(1),0));
			if (fdBounds.contains(touchPoint)||fdBounds.contains(touchPoint2)){
				//Derecha, llamar al gameManager
				manager.movePlayer(Direction.right);      
			}
			else if (fiBounds.contains(touchPoint)||fiBounds.contains(touchPoint2)){
				//izquierda, llamar al gameManager
				manager.movePlayer(Direction.left);
			}
			else if (farBounds.contains(touchPoint)||farBounds.contains(touchPoint2)){
				//arriba, llamar al gameManager
				manager.movePlayer(Direction.up);
			}
			else if (fabBounds.contains(touchPoint)|| fabBounds.contains(touchPoint2)){
				//abajo, llamar al gameManager
				manager.movePlayer(Direction.down);
			}
			
			if (harpoonBounds.contains(touchPoint)|| fabBounds.contains(touchPoint2)){
				manager.putLance();
			}
		}	
	}
	
	public void movePlayer(Direction dir, int playerId, Vector3 position) {                                     
        if (dir.equals(Direction.right)) currentFrame = penguinAnimations[playerId].getwalkAnimationR().getKeyFrame(stateTime, true); 
        else if (dir.equals(Direction.left)) currentFrame = penguinAnimations[playerId].getwalkAnimationL().getKeyFrame(stateTime, true);
        else if (dir.equals(Direction.up)) currentFrame = penguinAnimations[playerId].getwalkAnimationU().getKeyFrame(stateTime, true);
        else if (dir.equals(Direction.down)) currentFrame = penguinAnimations[playerId].getwalkAnimationD().getKeyFrame(stateTime, true);
        penguinAnimations[playerId].setPosition(position);
        penguinAnimations[playerId].setlookAt(dir);
        penguinAnimations[playerId].setCurrentFrame(currentFrame);     
	}
	
	public void putLanceAt(int xLancePosition, int yLancePosition) {
		Harpoon lance= new Harpoon(xLancePosition,yLancePosition);
		harpoonList.add(lance);		
	}

	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	public MatchManager getManager() {
		return manager;
	}
	public void setManager(MatchManager manager) {
		this.manager = manager;
	}
	
	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}
	public PenguinAnimation[] getPreguinAnimations() {
		return penguinAnimations;
	}
	public void setPreguinAnimations(PenguinAnimation[] preguinAnimations) {
		this.penguinAnimations = preguinAnimations;
	}

}