package Application;
import com.badlogic.gdx.math.Vector3;

import GameLogic.Map.TypeSquare;
import GameLogic.Match;
import Screens.GameScreen;
import Server.SmartFoxServer;

public class MatchManager {
	
	public enum Direction{left,right,up,down}
	
	private SmartFoxServer sfsClient;
	private Match match;
	private GameScreen gameScreen;
	private int myPlayerId;
	private long lastMessage;
	
	public MatchManager(SmartFoxServer sfs) {
		this.sfsClient=sfs;
		this.match = new Match();
		this.myPlayerId = sfsClient.getMyPlayerId()-1;
		this.lastMessage = System.currentTimeMillis();
	}
	
	public void movePlayer(Direction dir){
		long currentTime = System.currentTimeMillis();
		if (match.isValidPlayerMovement(dir,myPlayerId) && ((currentTime-lastMessage)>=200)){
			sfsClient.sendMove(dir,myPlayerId);
			this.lastMessage = System.currentTimeMillis();
		}
	}
	
	public void putLance(){
		if (match.canPutLance(myPlayerId)){
			int x = match.getIntegerCoordX(myPlayerId);
			int y = match.getIntegerCoordY(myPlayerId);
			sfsClient.sendLance(x, y);
		}
	}
	
	public void movePlayerEvent(Direction dir,int playerId){
		match.movePlayer(dir,playerId);
		Vector3 position = match.getMyPlayerPosition(playerId);
		gameScreen.movePlayer(dir,playerId,position);
	}
	
	public void putLanceEvent(int xLancePosition, int yLancePosition) {
		match.putLanceAt(xLancePosition,yLancePosition);	
		gameScreen.putLanceAt(xLancePosition,yLancePosition);
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

	public TypeSquare getSquare(int i, int j) {
		return match.getSquare(i,j);
	}
	
}
