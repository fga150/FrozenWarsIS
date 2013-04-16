package Application;
import com.badlogic.gdx.math.Vector3;

import GameLogic.Map.FissuresTypes;
import GameLogic.Map.TypeSquare;
import GameLogic.Map.WaterTypes;
import GameLogic.Map.SunkenTypes;
import GameLogic.Match;
import Screens.GameScreen;
import Server.SmartFoxServer;

public class MatchManager {
	
	//FIXME: Do a class for sharing with all classes of the project
	public enum Direction{left,right,up,down} 
	
	private SmartFoxServer sfsClient;
	private Match match;
	private GameScreen gameScreen;
	private int myPlayerId;
	private long lastMessage;
	private static String[] usersNames;
	
	public MatchManager(SmartFoxServer sfs) {
		this.sfsClient=sfs;
		this.match = new Match();
		this.myPlayerId = sfsClient.getMyPlayerId()-1;
		this.lastMessage = System.currentTimeMillis();
	}
	
	public void movePlayer(Direction dir){
		long currentTime = System.currentTimeMillis();
		if ((currentTime-lastMessage)>=100){
			if(match.insideBoardMove(dir,myPlayerId)){
				if (match.isSpecialMove(dir,myPlayerId)){
					sfsClient.sendMove(match.getSpecialMoveDir(myPlayerId),myPlayerId,match.getMyPlayerPosition(myPlayerId));
					this.lastMessage = System.currentTimeMillis();
				}else if(match.isNormalMove(dir,myPlayerId)){
					sfsClient.sendMove(dir,myPlayerId,match.getMyPlayerPosition(myPlayerId));
					this.lastMessage = System.currentTimeMillis();	
				}
			}	
		}
	}
	
	public void putHarpoon(){
		if (match.canPutHarpoon(myPlayerId)){
			Vector3 coord=match.getCoord();
			sfsClient.putHarpoon((int)coord.x,(int)coord.y,match.getMyPlayerRange(myPlayerId));
			this.lastMessage = System.currentTimeMillis();
		}
	}
	
	public void movePlayerEvent(Direction dir, int playerId, float xPlayerPosition, float yPlayerPosition){
		if (match.checkCorrectMove(dir,playerId)){ 
			match.movePlayer(dir,playerId,xPlayerPosition,yPlayerPosition);
			//match.checkUpgrade(dir,playerId);
			gameScreen.movePlayer(dir,playerId,match.getMyPlayerPosition(playerId));
		}
	}                
	
	public void putHarpoonEvent(int x, int y, int range, long time) {
		if (match.checkHarpoon(x,y)){
			match.putHarpoonAt(x,y,range,time);
		}
	}
	
	public boolean isThePlayerDead(int numPlayer) {
		return match.isThePlayerDead(numPlayer);
	}
	
	public boolean imTheWinner(int numPlayer){
		return match.imTheWinner(numPlayer);

	}
	
	
	// Getters and Setters
	
	public SmartFoxServer getSfsClient() {
		return sfsClient;
	}

	public void setSfsClient(SmartFoxServer sfsClient) {
		this.sfsClient = sfsClient;
	}

	public Match getGame() {
		return match;
	}

	public void setGame(Match game) {
		this.match = game;
	}

	public int getMyIdPlayer() {
		return myPlayerId;
	}

	public void setMyIdPlayer(int myIdPlayer) {
		this.myPlayerId = myIdPlayer;
	}

	public GameScreen getGameScreen() {
		return gameScreen;
	}

	public void setGameScreen(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	}

	public Vector3 getPlayerPosition(int playerId) {
		return match.getMyPlayerPosition(playerId);
	}

	public TypeSquare getBasicMatrixSquare(int i, int j) {
		return match.getBasicMatrixSquare(i,j);
	}
	
	public FissuresTypes getFissureMatrixSquare(int i, int j) {
		return match.getFissureMatrixSquare(i,j);
	}
	
	public WaterTypes getWaterMatrixSquare(int i, int j) {
		return match.getWaterMatrixSquare(i,j);
	}
	
	public SunkenTypes getSunkenMatrixSquare(int i, int j) {
		return match.getSunkenMatrixSquare(i,j);
		
	}

	public Direction getPlayerDirection(int i) {
		return match.getPlayerDirection(i);
	}
	
	public int getPlayerLifes(int i) {
			return match.getPlayerLifes(i);
	}
		
	public static String[] getUsersNames() {
		return usersNames;
	}

	public String getMyNamePlayer() {
			return sfsClient.getMyName();		
	}

	public int getNumPlayers() {
		return match.getNumPlayers();
	}

	public Direction getLookAt(int i) {
		return match.getLookAt(i);
	}

	public boolean canPlay(int i) {
		return match.canPlay(i);
	}

	public static void setUserName(String[] names) {
		usersNames = new String[names.length];
		for(int i = 0; i<names.length; i++){
			usersNames[i] = names[i];
		}
	}	
	
	public String getUserName(int id){
		return usersNames[id];
	}

	public void setNumPlayers(int i) {
		match.setNumPlayers(i);
		
	}
}
