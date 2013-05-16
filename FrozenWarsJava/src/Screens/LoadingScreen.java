package Screens;

import Application.Assets;
import Application.Desktop;
import Application.MatchManager;
import GameLogic.Direction;
import GameLogic.TypeGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class LoadingScreen implements Screen{
	
	private OrthographicCamera textCam;
	private OrthographicCamera guiCam;
	private SpriteBatch batcher;
	private MatchManager manager;
	private BitmapFont font;
	private BitmapFont font2;
	private boolean loaded;
	private long waitTime;
	private float stateTime;
	private boolean send;
	private int playerId;
	private int numPlayers;
	private PenguinAnimation[] penguinAnimationsTeam0;
	private PenguinAnimation[] penguinAnimationsTeam1;
	private PenguinAnimation loadingPenguin;
	private float xPenguin;
	private float timeWaiting;
	
	public LoadingScreen(MatchManager manager){
		this.loaded = false;
		this.send = false;
		this.xPenguin = 0;
		this.playerId = manager.getMyIdPlayer();
		this.numPlayers = manager.getNumPlayers();
		this.manager = manager;
		font =new BitmapFont(Gdx.files.internal("data/simpleFont.fnt"), Gdx.files.internal("data/simpleFont.png"), false);
		font2 =new BitmapFont(Gdx.files.internal("data/second.fnt"), Gdx.files.internal("data/second.png"), false);
		textCam = new OrthographicCamera(21*49,13*49);
		textCam.position.set((21*49)/2,(13*49)/2,0);
		batcher = new SpriteBatch();
		this.waitTime = System.currentTimeMillis();
		textCam = new OrthographicCamera(21*49,13*49);
		textCam.position.set((21*49)/2,(13*49)/2,0);
		guiCam = new OrthographicCamera(21,13);
		guiCam.position.set(21f/2,13f/2,0);
		loadAnimations();
	}
	

	public void loadAnimations(){
		penguinAnimationsTeam0 = new PenguinAnimation[numPlayers];
		TypeGame mode = manager.getMode();
		if (mode.equals(TypeGame.Normal) || mode.equals(TypeGame.BattleRoyale)){
			penguinAnimationsTeam0 = new PenguinAnimation[numPlayers];
			penguinAnimationsTeam0[0] = new PenguinAnimation(Gdx.files.internal("data/pClarosRojo.png"),Direction.down);
			penguinAnimationsTeam0[1] = new PenguinAnimation(Gdx.files.internal("data/pClarosVerde.png"),Direction.down);
			if (numPlayers>2) penguinAnimationsTeam0[2] = new PenguinAnimation(Gdx.files.internal("data/pClarosAmarillo.png"),Direction.down);
			if (numPlayers>3) penguinAnimationsTeam0[3] = new PenguinAnimation(Gdx.files.internal("data/pClarosAzul.png"),Direction.down);
		}
		else{
			penguinAnimationsTeam0 = new PenguinAnimation[numPlayers];
			penguinAnimationsTeam0[0] = new PenguinAnimation(Gdx.files.internal("data/PingClarosRojosGrupoNegro.png"),Direction.down);
			penguinAnimationsTeam0[1] = new PenguinAnimation(Gdx.files.internal("data/PingClarosVerdesGrupoNegro.png"),Direction.down);
			if (numPlayers>2) penguinAnimationsTeam0[2] = new PenguinAnimation(Gdx.files.internal("data/PingClarosAmarillosGrupoNegro.png"),Direction.down);
			if (numPlayers>3) penguinAnimationsTeam0[3] = new PenguinAnimation(Gdx.files.internal("data/PingClarosAzulesGrupoNegro.png"),Direction.down);
			penguinAnimationsTeam1 = new PenguinAnimation[numPlayers];
			penguinAnimationsTeam1[0] = new PenguinAnimation(Gdx.files.internal("data/PingClarosRojosGrupoAzul.png"),Direction.down);
			penguinAnimationsTeam1[1] = new PenguinAnimation(Gdx.files.internal("data/PingClarosVerdesGrupoAzul.png"),Direction.down);
			if (numPlayers>2) penguinAnimationsTeam1[2] = new PenguinAnimation(Gdx.files.internal("data/PingClarosAmarillosGrupoAzul.png"),Direction.down);
			if (numPlayers>3) penguinAnimationsTeam1[3] = new PenguinAnimation(Gdx.files.internal("data/PingClarosAzulGrupoAzul.png"),Direction.down);
		}
		if (playerId == 0) loadingPenguin = new PenguinAnimation(Gdx.files.internal("data/pClarosRojo.png"),Direction.left);
		else if (playerId == 1) loadingPenguin = new PenguinAnimation(Gdx.files.internal("data/pClarosVerde.png"),Direction.left);
		else if (playerId == 2) loadingPenguin = new PenguinAnimation(Gdx.files.internal("data/pClarosAmarillo.png"),Direction.left);
		else if (playerId == 3) loadingPenguin = new PenguinAnimation(Gdx.files.internal("data/pClarosAzul.png"),Direction.left);
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
	public void render(float delta){
		
		Gdx.gl.glClearColor(1,1,1,1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		stateTime += Gdx.graphics.getDeltaTime();
		batcher.enableBlending();
		batcher.begin();
		batcher.setProjectionMatrix(guiCam.combined);
		Color c = batcher.getColor();
        batcher.setColor(c.r, c.g, c.b, 0.40f); //set alpha to 1
        batcher.draw(Assets.getBackground(),0,0,21,13);
        batcher.setColor(c.r, c.g, c.b,1); //set alpha to 1
		timeWaiting = System.currentTimeMillis()-waitTime;
		if (manager.getMyIdPlayer()==0 && !send && (System.currentTimeMillis()-waitTime>2500)){
				send = true;
				manager.sendAsign();
		}
		if (loaded && (timeWaiting>10000)){
				loaded = false;
				manager.changeGameScreen();
		}
		
		printTypeGame();
		printYourself();
		printUpgrades();
		printTeams();
		printMission();
		printLoading();
		upgradePenguins();
		
		batcher.end();
		guiCam.update();
		textCam.update();
		
		xPenguin += delta;
	}
	
	private void printLoading() {
		if (timeWaiting>9000) printTextFont2("The game starts in 1 ...",0.7f,Color.BLACK,12,2);
		else if (timeWaiting>8000) printTextFont2("The game starts in 2 ...",0.7f,Color.BLACK,12,2);
		else if (timeWaiting>7000) printTextFont2("The game starts in 3 ...",0.7f,Color.BLACK,12,2);
		else printTextFont2("Loading ...",1,Color.BLACK,15,2);
		batcher.draw(loadingPenguin.getCurrentFrame(),xPenguin,1,1.5f,1.5f);
	}

	private void printTypeGame() {
		printTextFont2(manager.getMode().toString(),1,Color.BLACK,9,12);
	}


	private void upgradePenguins(){
		TypeGame mode = manager.getMode();
		for (int i=0;i<numPlayers;i++){
			penguinAnimationsTeam0[i].setCurrentFrame(penguinAnimationsTeam0[i].getwalkAnimationD().getKeyFrame(stateTime,true));
		}
		if (!(mode.equals(TypeGame.Normal) || mode.equals(TypeGame.BattleRoyale))){
			for (int i=0;i<numPlayers;i++){
				penguinAnimationsTeam1[i].setCurrentFrame(penguinAnimationsTeam1[i].getwalkAnimationD().getKeyFrame(stateTime,true));
			}   
		}
		loadingPenguin.setCurrentFrame(loadingPenguin.getwalkAnimationL().getKeyFrame(stateTime,true));
	}

	private void printUpgrades() {
		int lifes = manager.getPlayerLifes(playerId);
		int speedUpgrade = manager.getSpeed(playerId);
		int harpoonUpgrade = manager.getHarpoonsAllow(playerId);
		int rangeUpgrade = manager.getRange(playerId);
		if (playerId == 0) batcher.draw(Assets.getLifeIconRed(),10,9,1,1);
		else if (playerId == 1) batcher.draw(Assets.getLifeIconGreen(),10,9,1,1);
		else if (playerId == 2) batcher.draw(Assets.getLifeIconYellow(),10,9,1,1);
		else if (playerId == 3) batcher.draw(Assets.getLifeIconBlue(),10,9,1,1);
		if (lifes > 1) printTextFont1(Integer.toString(lifes),1,Color.BLACK,10.75f,9.25f);
		if (speedUpgrade<=1) {
			batcher.setColor(new Color(50,50,50,0.25f));
			batcher.draw(Assets.getBootUpgradeMaxSize(),13,9,1,1);
			batcher.setColor(Color.WHITE);
		}
		else{
			batcher.draw(Assets.getBootUpgradeMaxSize(),13,9,1,1);
			if (speedUpgrade==5)printTextFont1("MAX",0.75f,Color.RED,13.75f,9.25f);
			else printTextFont1(Integer.toString(speedUpgrade-1),1,Color.BLACK,13.75f,9.25f);
		}
		if (harpoonUpgrade == 0){
			batcher.setColor(new Color(50,50,50,0.25f));
			batcher.draw(Assets.getNotNumHarpoonUpgradeMaxSize(),16,9,1,1);
			batcher.setColor(Color.WHITE);
		}
		else if (harpoonUpgrade == 1){
			batcher.setColor(new Color(50,50,50,0.25f));
			batcher.draw(Assets.getNumHarpoonUpgradeMaxSize(),16,9,1,1);
			batcher.setColor(Color.WHITE);
		}
		else {
			batcher.draw(Assets.getNumHarpoonUpgradeMaxSize(),16,9,1,1);
			if (harpoonUpgrade==5)printTextFont1("MAX",0.75f,Color.RED,16.75f,9.25f);
			else printTextFont1(Integer.toString(harpoonUpgrade-1),1,Color.BLACK,16.75f,9.25f);
		}
		if (rangeUpgrade<=1) {
			batcher.setColor(new Color(50,50,50,0.25f));
			batcher.draw(Assets.getRangeUpgradeMaxSize(),19,9,1,1);
			batcher.setColor(Color.WHITE);
		}
		else{
			batcher.draw(Assets.getRangeUpgradeMaxSize(),19,9,1,1);
			if (rangeUpgrade==5)printTextFont1("MAX",0.75f,Color.RED,19.75f,9.25f);
			else printTextFont1(Integer.toString(rangeUpgrade-1),1,Color.BLACK,19.75f,9.25f);
		}
	}
	
	private void printMission() {
		printTextFont2("Mission:",1,Color.BLACK,1,8);
		if(manager.getGameType().equals(TypeGame.Normal)|| (manager.getGameType().equals(TypeGame.BattleRoyale)))
			printTextFont2("Defeat the other penguins",0.75f,Color.BLACK,1,6);
		else if(manager.getGameType().equals(TypeGame.Teams))
			printTextFont2("Beat the enemy team",0.5f,Color.BLACK,1,5.5f);
		else if (manager.getGameType().equals(TypeGame.Survival)){
			if(manager.getMyTeam(playerId).getNumTeam()==0){
				printTextFont2("Defeat the other penguins to win",0.5f,Color.BLACK,1,6);
				printTextFont2("If you defeat somebody,s/he will be on your team",0.4f,Color.BLACK,1,4);
			}else{
				printTextFont2("Survive to the end of the match to win",0.5f,Color.BLACK,1,6);
				printTextFont2("If you got defeated, you will be on the enemy team",0.4f,Color.BLACK,1,4);
			}
		}
	}

	private void printTeams(){
		TypeGame mode = manager.getMode();
		if (mode.equals(TypeGame.Normal) || mode.equals(TypeGame.BattleRoyale)){
			batcher.draw(penguinAnimationsTeam0[0].getCurrentFrame(),12,3,1.5f,1.5f);
			batcher.draw(penguinAnimationsTeam0[1].getCurrentFrame(),17,3,1.5f,1.5f);
			if (numPlayers>=3) batcher.draw(penguinAnimationsTeam0[2].getCurrentFrame(),12,6,1.5f,1.5f);
			if (numPlayers==4) batcher.draw(penguinAnimationsTeam0[3].getCurrentFrame(),17,6,1.5f,1.5f);
			printTextFont2("VS",1,Color.RED,14.5f,5);
		}
		else if (mode.equals(TypeGame.Survival) || mode.equals(TypeGame.OneVsAll)){
			batcher.draw(penguinAnimationsTeam0[0].getCurrentFrame(),11,4,1.5f,1.5f);
			batcher.draw(penguinAnimationsTeam1[1].getCurrentFrame(),15f,4,1.5f,1.5f);
			if (numPlayers>=3) batcher.draw(penguinAnimationsTeam1[2].getCurrentFrame(),17,4,1.5f,1.5f);
			if (numPlayers==4) batcher.draw(penguinAnimationsTeam1[3].getCurrentFrame(),19,4,1.5f,1.5f);
			printTextFont2("VS",1,Color.RED,13f,5);
		}
		else {
			batcher.draw(penguinAnimationsTeam0[0].getCurrentFrame(),7,4,1.5f,1.5f);
			batcher.draw(penguinAnimationsTeam0[1].getCurrentFrame(),10,4,1.5f,1.5f);
			if (numPlayers>=3) batcher.draw(penguinAnimationsTeam1[2].getCurrentFrame(),15,4,1.5f,1.5f);
			if (numPlayers==4) batcher.draw(penguinAnimationsTeam1[3].getCurrentFrame(),18,4,1.5f,1.5f);
			printTextFont2("VS",1,Color.RED,12.5f,5);
		}
	}

	private void printYourself(){
		printTextFont2("YOU:",1,Color.BLACK,1,10);
		TypeGame type = manager.getMode();
		if (type.equals(TypeGame.Normal)||type.equals(TypeGame.BattleRoyale)) batcher.draw(penguinAnimationsTeam0[playerId].getCurrentFrame(),4,9,1.5f,1.5f);
		else {
			if (manager.getTeam(playerId)==0)  batcher.draw(penguinAnimationsTeam0[playerId].getCurrentFrame(),4,9,1.5f,1.5f);
			else if (manager.getTeam(playerId)==1) batcher.draw(penguinAnimationsTeam1[playerId].getCurrentFrame(),4,9,1.5f,1.5f);
		}
	}

	private void printTextFont1(String text,float scale,Color color,float posx,float posy){
		batcher.setProjectionMatrix(textCam.combined);
		Color aux = font.getColor();
		font.setColor(color);
		font.setScale(scale);
		font.draw(batcher,text, posx*49 ,posy*49);
		batcher.setProjectionMatrix(guiCam.combined);
		font.setColor(aux);
		font.setScale(1);
	}

	private void printTextFont2(String text,float scale,Color color,float posx,float posy){
		batcher.setProjectionMatrix(textCam.combined);
		Color aux = font2.getColor();
		font2.setColor(color);
		font2.setScale(scale);
		font2.draw(batcher,text, posx*49 ,posy*49);
		batcher.setProjectionMatrix(guiCam.combined);
		font2.setColor(aux);
		font2.setScale(1);
	}
		
	public String cutName(String name){
		String playerName = name;
		if(playerName.length()>8){
			playerName = name.substring(0,8);
			playerName = playerName.concat("...");
		}
		return playerName;
		
	}	

	public MatchManager getManager() {
		return manager;
	}
	
	public void setManager(MatchManager manager) {
		this.manager = manager;
	}
	
	@Override
	public void resize(int width, int height) {
		if (width!=1024 || height!=630) Desktop.resetScreenSize();

	}

	@Override
	public void resume(){
		
	}

	@Override
	public void show(){
		
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}
	
}