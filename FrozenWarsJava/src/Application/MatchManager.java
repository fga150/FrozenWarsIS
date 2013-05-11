package Application;
import com.badlogic.gdx.math.Vector3;
import GameLogic.Map.FissuresTypes;
import GameLogic.Map.TypeSquare;
import GameLogic.Map.WaterTypes;
import GameLogic.Map.SunkenTypes;
import GameLogic.Match;
import GameLogic.Match.TypeGame;
import GameLogic.Team;
import GameLogic.XMLMapReader;
import Screens.GameScreen;
import Screens.LoadingScreen;
import Server.SmartFoxServer;
import Sounds.AppMusic;

public class MatchManager {
	
	public enum Direction{left,right,up,down} 
	
	private AppMusic myAppMusic;
	private SmartFoxServer sfsClient;
	private Match match;
	private LoadingScreen loadingScreen;
	private GameScreen gameScreen;
	private int myPlayerId;
	@SuppressWarnings("unused")
	private int numPlayers;
	private long lastMessage;
	private TypeGame mode;
	private static String[] usersNames;
	private XMLMapReader xmlMapReader;

	
	public MatchManager(SmartFoxServer sfs,int mode, AppMusic myAppMusic){
		this.myAppMusic = myAppMusic;
		this.myAppMusic.playMyInitialMusic(mode);
		this.sfsClient=sfs;
		String map = "mapaPrueba.xml";
		this.mode = getTypeGame(mode);
		if ((this.mode.equals(TypeGame.Survival)) || (this.mode.equals(TypeGame.OneVsAll))) map = "SurvivalMap.xml";
		this.xmlMapReader = new XMLMapReader(map);
		this.loadingScreen = new LoadingScreen(this);
		this.sfsClient.addManager(this);
		this.myPlayerId = sfsClient.getMyPlayerId()-1;
	}
	
	public void movePlayer(Direction dir){
		long currentTime = System.currentTimeMillis();
		if ((currentTime-lastMessage)*speedFactor()>=100){
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
	
	private float speedFactor() {
		int speed = match.getPlayerSpeed(myPlayerId);
		if (speed == 1) return 1;
		else if (speed == 2) return 1.3f;
		else if (speed == 3) return 1.5f;
		else if (speed == 4) return 1.7f;
		else if (speed == 5) return 2f;
		return 0;
	}

	public void putHarpoon(){
		if (match.canPutHarpoon(myPlayerId)){
			Vector3 coord=match.getCoord();
			sfsClient.putHarpoon((int)coord.x,(int)coord.y,match.getMyPlayerRange(myPlayerId),myPlayerId);
			this.lastMessage = System.currentTimeMillis();
		}
	}
	
	public void movePlayerEvent(Direction dir, int playerId, float xPlayerPosition, float yPlayerPosition){
		if (match.checkCorrectMove(dir,playerId)){ 
			match.movePlayer(dir,playerId,xPlayerPosition,yPlayerPosition);
			gameScreen.movePlayer(dir,playerId,match.getMyPlayerPosition(playerId));
		}
	}                
	
	public void putHarpoonEvent(int x, int y, int range, int playerId, long time) {
		if (match.checkHarpoon(x,y)){
			match.putHarpoonAt(x,y,range,playerId,time);
		}
	}
	
	public boolean isThePlayerDead(int numPlayer) {
		return match.isThePlayerDead(numPlayer);
	}
	
	public boolean imTheWinner(int numPlayer){
		return match.imTheWinner(numPlayer);

	}
	
	public boolean areAllPlayersDead() {
		return match.areAllPlayersDead();
	}
	
	public void startGame(int[] upgrades, int numPlayers) {
		this.numPlayers = numPlayers;
		match = new Match(upgrades,xmlMapReader,myPlayerId,numPlayers,mode,usersNames[myPlayerId]);
		this.loadingScreen.setLoaded(true);
	}
	
	private TypeGame getTypeGame(int mode) {
		TypeGame type = TypeGame.Normal;
		if (mode == 0) type = TypeGame.Normal;
		else if (mode == 1) type = TypeGame.Teams;
		else if (mode == 2) type = TypeGame.OneVsAll;
		else if (mode == 3) type = TypeGame.Survival;
		else if (mode == 4) type = TypeGame.BattleRoyale;
		return type;
	}

	public void sendAsign(){
		int numBreakable = xmlMapReader.getNumBreakable();
		int[] upgrades = xmlMapReader.getUpgrades();
		sfsClient.sendAsign(numBreakable,upgrades[0],upgrades[1],upgrades[2],upgrades[3]);
	}
	
	public void changeGameScreen() {
		this.gameScreen = new GameScreen(this);	
		LaunchFrozenWars.getGame().setScreen(gameScreen);
	}
	
	public void changeToFastBattleMusic(){
		myAppMusic.playfastBattleMusic();
	}
	
	public void stopTheMusic(){
		myAppMusic.stopMyMusic();
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
		
	public String[] getUsersNames() {
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

	public int getSpeed(int numPlayer){
		return match.getPlayerSpeed(numPlayer);
	}

	public int getHarpoonsAllow(int numPlayer){
		return match.getPlayerHarpoonAllow(numPlayer);
	}
	
	public void setNumPlayers(int numPlayers) {
		match.setNumPlayers(numPlayers);
	}

	public int getRange(int numPlayer) {
		return match.getPlayerRange(numPlayer);
	}

	public boolean isInvisible(int numPlayer) {
		return match.isInvisible(numPlayer);
	}

	public boolean isGameTimeOff(){
		return match.isGameTimeOff();
	}
	
	public Team getMyTeam(int id){
		return match.getMyTeam(id);
	}
	
	public TypeGame getGameType(){
		return match.getType();
	}

	public LoadingScreen getLoadingScreen() {
		return loadingScreen;
	}

	public void setLoadingScreen(LoadingScreen loadingScreen) {
		this.loadingScreen = loadingScreen;
	}

	public int getTeam(int playerId) {
		return match.getMyTeamId(playerId);
	}

	public TypeGame getMode() {
		return this.mode;
	}

	public long getTimeInvisible(int numPlayer) {
		return match.getTimeInvisible(numPlayer);
	}

	public long getTimeMatch() {
		return match.getTimeManager();
	}
	
}
