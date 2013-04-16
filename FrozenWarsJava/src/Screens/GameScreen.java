package Screens;

import Application.Assets;
import Application.MatchManager;
import Application.MatchManager.Direction;
import GameLogic.Map.TypeSquare;
import GameLogic.Map.FissuresTypes;
import GameLogic.Map.WaterTypes;
import GameLogic.Map.SunkenTypes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;


public class GameScreen implements Screen{
	private Game game;
	private OrthographicCamera guiCam;
	private OrthographicCamera textCam;
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
	private String name;
	private BitmapFont font;
	private BitmapFont font2;
	private int numPlayer;

	
	public GameScreen(Game game, MatchManager manager){
		this.game=game;
		this.name = manager.getMyNamePlayer();
		numPlayer=manager.getMyIdPlayer();
		this.manager = manager;
		font = new BitmapFont(Gdx.files.internal("data/simpleFont.fnt"), Gdx.files.internal("data/simpleFont.png"), false);
		font2 =new BitmapFont(Gdx.files.internal("data/first.fnt"), Gdx.files.internal("data/first.png"), false);
		font.setColor(Color.BLACK);
		guiCam = new OrthographicCamera(21,13);
		textCam=new OrthographicCamera(21*49,13*49);
		textCam.position.set((21*49)/2,(13*49)/2,0);
		guiCam.position.set(21f/2,13f/2,0);
		batcher = new SpriteBatch();
		touchPoint = new Vector3();
		touchPoint2 = new Vector3();
		fdBounds= new BoundingBox(new Vector3(4.75f,2.5f,0), new Vector3(7.25f,4.5f,0));
		fiBounds= new BoundingBox(new Vector3(0.25f,2.5f,0), new Vector3(2.75f,4.5f,0));
		farBounds= new BoundingBox(new Vector3(2.75f,4.6f,0), new Vector3(4.75f,7,0));
		fabBounds= new BoundingBox(new Vector3(2.75f,0,0), new Vector3(4.75f,2.4f,0));

		harpoonBounds= new BoundingBox(new Vector3(19,0,0), new Vector3(21,4,0));
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		//--------------------------------------------------------------------
		penguinAnimations = new PenguinAnimation[4];
		penguinAnimations[0] = new PenguinAnimation(Gdx.files.internal("data/pClarosRojo.png"),manager.getLookAt(0));
		penguinAnimations[1] = new PenguinAnimation(Gdx.files.internal("data/pClarosVerde.png"),manager.getLookAt(1));
		penguinAnimations[2] = new PenguinAnimation(Gdx.files.internal("data/pClarosAmarillo.png"),manager.getLookAt(2));
		penguinAnimations[3] = new PenguinAnimation(Gdx.files.internal("data/pClarosAzul.png"),manager.getLookAt(3));
        stateTime = 0f;     
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
		batcher.enableBlending();
		batcher.begin();
		batcher.setProjectionMatrix(guiCam.combined);
		
		Color c = batcher.getColor();
        batcher.setColor(c.r, c.g, c.b, 0.40f); //set alpha to 1
        batcher.draw(Assets.getBackground(),0,0,21,13);
        batcher.setColor(c.r, c.g, c.b, 1f);
		
		batcher.draw(Assets.getHorizontalBarDown(),7,0,13,1);
		batcher.draw(Assets.getHorizontalBarUP(),7,12,13,1);
		batcher.draw(Assets.getVerticalBarLeft(),7,1,1,11);
		batcher.draw(Assets.getVerticalBarRigth(),19,1,1,11);
		
		
		for(int i=0;i<11;i++){
			for (int j=0;j<11;j++){
				TextureRegion texture= null;
				TypeSquare typeBasicMatrix = manager.getBasicMatrixSquare(i,j);
				FissuresTypes typeFissureMatrix = manager.getFissureMatrixSquare(i,j);
				WaterTypes typeWaterMatrix = manager.getWaterMatrixSquare(i,j);
				SunkenTypes typeSunkenMatrix = manager.getSunkenMatrixSquare(i,j);
				texture = Assets.getBox(); 
				batcher.draw(texture,i+8,j+1,1,1);
				//First we draw fissures matrix
				if(!typeFissureMatrix.equals(FissuresTypes.empty)){
					if(typeFissureMatrix.equals(FissuresTypes.barrelWithFissure)) texture  = Assets.getBarrelWithFissure();
					else if (typeFissureMatrix.equals(FissuresTypes.crossingFissures)) texture  = Assets.getFissureCrossing();
					else if (typeFissureMatrix.equals(FissuresTypes.fissureC))	texture  = Assets.getFissureCenter();
					else if (typeFissureMatrix.equals(FissuresTypes.fissureSX)) texture  = Assets.getFissureSideX();
					else if (typeFissureMatrix.equals(FissuresTypes.fissureSY)) texture  = Assets.getFissureSideY();
					
					batcher.draw(texture,i+8,j+1,1,1);
				}
				if (!typeBasicMatrix.equals(TypeSquare.empty)){
					if (typeBasicMatrix.equals(TypeSquare.unbreakable)) texture = Assets.getIgloo();
					else if (typeBasicMatrix.equals(TypeSquare.harpoon)) texture = Assets.getHarpoon();				
					else if (typeBasicMatrix.equals(TypeSquare.breakable)) texture  = Assets.getBarrel();
					batcher.draw(texture,i+8,j+1,1,1);
				}
				if(!typeWaterMatrix.equals(WaterTypes.empty)){
					if (typeWaterMatrix.equals(WaterTypes.water1SOpN)) texture  = Assets.getWater1SideOpenN();
					else if (typeWaterMatrix.equals(WaterTypes.water1SOpS)) texture  = Assets.getWater1SideOpenS();
					else if (typeWaterMatrix.equals(WaterTypes.water1SOpE)) texture  = Assets.getWater1SideOpenE();
					else if (typeWaterMatrix.equals(WaterTypes.water1SOpW)) texture  = Assets.getWater1SideOpenW();
					else if (typeWaterMatrix.equals(WaterTypes.water2SOpCornerNW)) texture  = Assets.getWater2SideOpenCornerNW();
					else if (typeWaterMatrix.equals(WaterTypes.water2SOpCornerSE)) texture  = Assets.getWater2SideOpenCornerSE();
					else if (typeWaterMatrix.equals(WaterTypes.water2SOpCornerWS)) texture  = Assets.getWater2SideOpenCornerWS();
					else if (typeWaterMatrix.equals(WaterTypes.water2SOpCornerEN)) texture  = Assets.getWater2SideOpenCornerEN();
					else if (typeWaterMatrix.equals(WaterTypes.water2SOpBridgeX)) texture  = Assets.getWater2SideOpenBridgeX();
					else if (typeWaterMatrix.equals(WaterTypes.water2SOpBridgeY)) texture  = Assets.getWater2SideOpenBridgeY();
					else if (typeWaterMatrix.equals(WaterTypes.water3SOpN)) texture  = Assets.getWater3SideNOpen();
					else if (typeWaterMatrix.equals(WaterTypes.water3SOpS)) texture  = Assets.getWater3SideSOpen();
					else if (typeWaterMatrix.equals(WaterTypes.water3SOpE)) texture  = Assets.getWater3SideEOpen();
					else if (typeWaterMatrix.equals(WaterTypes.water3SOpW)) texture  = Assets.getWater3SideWOpen();
					else if (typeWaterMatrix.equals(WaterTypes.water4SOp)) texture  = Assets.getWater4SideOpen();
					batcher.draw(texture,i+8,j+1,1,1);		
				}
				if(!typeSunkenMatrix.equals(SunkenTypes.empty)){
					 texture  = Assets.getSunkenObject();
					 batcher.draw(texture,i+8,j+1,1,1);		
				}
			}
		}
		
		
		batcher.draw(Assets.getDirectionPanel(),0.25f,0,7,7);
		batcher.draw(Assets.getButtonHarpoon(),19,0,2,4);
		
		for (int i=0;i<manager.getNumPlayers();i++){
			if(manager.canPlay(i)){
				position = manager.getPlayerPosition(i);
				batcher.draw(penguinAnimations[i].getCurrentFrame(),(position.x)+8f,(position.y+1),1,1);
			}
		}
		
		
		batcher.draw(Assets.getLifesPanel(),0.5f,6.75f,6,5);
		paintLifes();
		paintLinesPanelLives();
		paintPlayerStatus();
		paintPlayerName();
		
		batcher.end();
		guiCam.update();
		textCam.update();
		
		if(manager.canPlay(numPlayer)){
			
			//Keyboard
			
			if ((Gdx.input.isKeyPressed(Keys.W))||(Gdx.input.isKeyPressed(Keys.UP)))manager.movePlayer(Direction.up);
			if ((Gdx.input.isKeyPressed(Keys.S))||(Gdx.input.isKeyPressed(Keys.DOWN)))manager.movePlayer(Direction.down);
			if ((Gdx.input.isKeyPressed(Keys.A))||(Gdx.input.isKeyPressed(Keys.LEFT)))manager.movePlayer(Direction.left);
			if ((Gdx.input.isKeyPressed(Keys.D))||(Gdx.input.isKeyPressed(Keys.RIGHT)))manager.movePlayer(Direction.right);
			if (Gdx.input.isKeyPressed(Keys.SPACE)) manager.putHarpoon();
			
			//TouchPad
			
			if (Gdx.input.isTouched()){
				if (Gdx.input.isTouched(0))guiCam.unproject(touchPoint.set(Gdx.input.getX(0),Gdx.input.getY(0),0));
				else touchPoint.set(-1,-1,-1);
				if (Gdx.input.isTouched(1))guiCam.unproject(touchPoint2.set(Gdx.input.getX(1),Gdx.input.getY(1),0));
				else touchPoint2.set(-1,-1,-1);
				if (fdBounds.contains(touchPoint)||fdBounds.contains(touchPoint2)){
					manager.movePlayer(Direction.right);      
				}
				else if (fiBounds.contains(touchPoint)||fiBounds.contains(touchPoint2)){
					manager.movePlayer(Direction.left);
				}
				else if (farBounds.contains(touchPoint)||farBounds.contains(touchPoint2)){
					manager.movePlayer(Direction.up);
				}
				else if (fabBounds.contains(touchPoint)||fabBounds.contains(touchPoint2)){
					manager.movePlayer(Direction.down);
				}
				if (harpoonBounds.contains(touchPoint)||harpoonBounds.contains(touchPoint2)){
					manager.putHarpoon();
					Gdx.input.vibrate(200);
				}
			}
		}
	}
	
	private void paintPlayerName(){
		
		//Paint player status You are dead/you are RED/BLUE/YELLOW/GREEN
 		if(!manager.isThePlayerDead(numPlayer)){
 			String userMessage = "You are ";
 			batcher.setProjectionMatrix(textCam.combined);
 			if(numPlayer == 0){
 				font.setColor(Color.RED);
 				userMessage = userMessage.concat("RED player: ");
 			}else if(numPlayer == 1){
 				font.setColor(Color.GREEN);
 				userMessage = userMessage.concat("GREEN player: ");
 			}else if(numPlayer == 2){
 				
 				font.setColor(Color.YELLOW);
 				userMessage = userMessage.concat("YELLOW player: ");
 			}else if(numPlayer == 3){
 				font.setColor(Color.BLUE);
 				userMessage = userMessage.concat("BLUE player: ");
 			}
 			font.draw(batcher,userMessage, 0.5f*49 ,12.35f*49);
 		}else{
 			String userMessage = "You are dead !!! ";
 			batcher.setProjectionMatrix(textCam.combined);
 			if(numPlayer == 0){
 				font.setColor(Color.RED);
 			}else if(numPlayer == 1){
 				font.setColor(Color.GREEN);
 			}else if(numPlayer == 2){
 				font.setColor(Color.YELLOW);
 			}else if(numPlayer == 3){
 				font.setColor(Color.BLUE);
 			}
 			font.draw(batcher,userMessage, 1.5f*49 ,12.35f*49);
 			font.setColor(Color.BLACK);
 		}
 		
 		//Write players name in lives panel
		if(manager.getNumPlayers()>1){
			batcher.setProjectionMatrix(textCam.combined);
			font.setColor(Color.RED);
			font.draw(batcher,cutName(manager.getUserName(0)), 0.9f*49 ,10.4f*49);
		
			font.setColor(Color.GREEN);
			font.draw(batcher,cutName(manager.getUserName(1)), 0.9f*49 ,9.4f*49);
		}	
		if (MatchManager.getUsersNames().length>2){//method temporaly getNumPlayer always return 4
			if(manager.getNumPlayers()>2){
				font.setColor(Color.YELLOW);
				font.draw(batcher,cutName(manager.getUserName(2)), 0.9f*49 ,8.4f*49);
			if (MatchManager.getUsersNames().length>3){
				if(manager.getNumPlayers()>3){
					font.setColor(Color.BLUE);
					font.draw(batcher,cutName(manager.getUserName(3)), 0.9f*49 ,7.5f*49);
				}
			}
			}
		}
		
							
		
	}
	
	public String cutName(String name){
		String playerName = name;
		if(playerName.length()>8){
			playerName = name.substring(0,8);
			playerName = playerName.concat("...");
		}
		return playerName;
		
	}
	
	private void paintPlayerStatus(){

		if(!manager.isThePlayerDead(numPlayer)){
			if (numPlayer==0){
				batcher.draw(Assets.getRedPlayer(),5.75f,11.75f,1,1);
			}else if(numPlayer==1){
				batcher.draw(Assets.getGreenPlayer(),5.75f,11.75f,1,1);
			}else if(numPlayer==2){
				batcher.draw(Assets.getYellowPlayer(),5.75f,11.75f,1,1);
			}else if(numPlayer==3){
				batcher.draw(Assets.getBluePlayer(),5.75f,11.75f,1,1);
			}
		}else{
			if (numPlayer==0){
				batcher.draw(Assets.getDeadIconRed(),5.5f,11.75f,1,1);
			}else if(numPlayer==1){
				batcher.draw(Assets.getDeadIconGreen(),5.5f,11.75f,1,1);
			}else if(numPlayer==2){
				batcher.draw(Assets.getDeadIconYellow(),5.5f,11.75f,1,1);
			}else if(numPlayer==3){
				batcher.draw(Assets.getDeadIconBlue(),5.5f,11.75f,1,1);
			}
			batcher.draw(Assets.getGameOver(),6,4,14,8);
		}
		if (manager.imTheWinner(numPlayer)){
			batcher.draw(Assets.getYouWin(),6,4,14,8);
		}
		
		batcher.setProjectionMatrix(textCam.combined);
		font2.draw(batcher, "Lives", 0.5f*49, 11.25f*49);
}
			
	private void paintLifes() {
		for (int i=0;i<manager.getPlayerLifes(0);i++)
			batcher.draw(Assets.getLifeIconRed(),3.625f+0.975f*i,9.750f,0.75f,0.75f);
		
		for (int i=0;i<manager.getPlayerLifes(1);i++)	
			batcher.draw(Assets.getLifeIconGreen(),3.625f+0.975f*i,8.720f,0.75f,0.75f);
			
		for (int i=0;i<manager.getPlayerLifes(2);i++)
			batcher.draw(Assets.getLifeIconYellow(),3.625f+0.975f*i,7.75f,0.75f,0.75f);
		
		for (int i=0;i<manager.getPlayerLifes(3);i++)
			batcher.draw(Assets.getLifeIconBlue(),3.625f+0.975f*i,6.80f,0.75f,0.75f);
	}
	
	private void paintLinesPanelLives(){
		if(manager.isThePlayerDead(0))
			batcher.draw(Assets.getLineLifesPanel(),3.425f,10.05f,3.225f,0.125f);
		if(manager.isThePlayerDead(1))
			batcher.draw(Assets.getLineLifesPanel(),3.425f,9.05f,3.225f,0.125f);
		if(manager.isThePlayerDead(2))
			batcher.draw(Assets.getLineLifesPanel(),3.425f,8.05f,3.225f,0.125f);
		if(manager.isThePlayerDead(3))
			batcher.draw(Assets.getLineLifesPanel(),3.425f,7.1f,3.225f,0.125f);
	}
	
	public void movePlayer(Direction dir, int playerId, Vector3 position) {                                     
        if (dir.equals(Direction.right)) currentFrame = penguinAnimations[playerId].getwalkAnimationR().getKeyFrame(stateTime, true); 
        else if (dir.equals(Direction.left)) currentFrame = penguinAnimations[playerId].getwalkAnimationL().getKeyFrame(stateTime, true);
        else if (dir.equals(Direction.up)) currentFrame = penguinAnimations[playerId].getwalkAnimationU().getKeyFrame(stateTime, true);
        else if (dir.equals(Direction.down)) currentFrame = penguinAnimations[playerId].getwalkAnimationD().getKeyFrame(stateTime, true);
        penguinAnimations[playerId].setCurrentFrame(currentFrame);     
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
	}

	@Override
	public void show() {		
	}
	
	public PenguinAnimation[] getPreguinAnimations() {
		return penguinAnimations;
	}
	public void setPreguinAnimations(PenguinAnimation[] preguinAnimations) {
		this.penguinAnimations = preguinAnimations;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BitmapFont getFont() {
		return font;
	}
	public void setFont(BitmapFont font) {
		this.font = font;
	}


}