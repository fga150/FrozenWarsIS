package GameLogic;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.math.Vector3;

import Application.MatchManager;
import GameLogic.Direction;
import GameLogic.Map.SunkenTypes;
import GameLogic.Map.FissuresTypes;
import GameLogic.Map.TypeSquare;
import GameLogic.Map.WaterTypes;
import Sounds.AppSounds;

public class Match {
	
	/**
	 * Represents the number of squares that fills the player
	 * horizontally.
	 */
	private final static float playerWidth = 1;
	
	/**
	 * Represents the number of squares that fills the player
	 * vertically.
	 */
	private final static float playerLength = 1;
	
	/**
	 * Represents the number of squares that the player
	 * move foward with one step.
	 */
	private final static float minimalMove = 0.25f;
	
	/**
	 * Represents the max lenght and width of the square
	 * that the map have.
	 */
	private final static int maxSize = 11;
	
	/**
	 * The map of the match.
	 */
	private Map map;
	
	/**
	 * Used for log actions of the match.
	 */
	private LogFile logFile;
	
	/**
	 * Used for control the time events that happens in the match.
	 */
	private TimeEventsManager timeEventsManager;
	
	/**
	 * Used for control the states of the harpoons in the match.
	 */
	private HarpoonManager harpoonManager;
	
	/**
	 * The type of the match that is playing.
	 */
	private TypeGame type;
	
	/**
	 * The number of the teams that are currently playing.
	 */
	private ArrayList<Team> teams;
	
	/**
	 * Used to make more efficient the method put harpoon
	 */
	private Vector3 coord;
	
	/**
	 * The time of the end of the match.
	 */
	private long matchTime;
	
	/**
	 * The player Id. of the player.
	 */
	private int myPlayerId;
	
	/**
	 * The number of players that are currently playing.
	 */
	private int numPlayers;	
	/**
	 * If there is no game this variable is set to true.
	 */
	private boolean gameTimeOff;
	/**
	 * Used for manage the sounds.
	 */
	private AppSounds myAppSounds;
	/**
	 * Used for comunicating purposes. 	
	 */
	private MatchManager matchManager;
	/** 
	 * losersTeam is an array that save loser players in survival mode
	 */
	private ArrayList<Player> losersTeam;
	
	/**
	 * Creates the match and initialize/load the following classes: Map, Team, TimeEventsManager,
	 * HarpoonManager and LogFile.
	 * @param upgrades The array of upgrades for deploying upgrades in the map.
	 * @param xmlMapReader The reader of the map that will load the map.
	 * @param playerId The player id of the player.
	 * @param numPlayers The number of players that are going to play.
	 * @param type The type of the game that is going to play.
	 * @param namePlayer The name of the player to create logFile.
	 * @see Map, XMLMapReader, Team, AppSounds
	 */
	public Match(XMLMapReader xmlMapReader,int playerId, int numPlayers,TypeGame type, AppSounds myAppSounds,
			MatchManager matchManager){
		this.matchManager = matchManager;
		this.losersTeam = new ArrayList<Player>();
		this.myAppSounds = myAppSounds;
		this.myPlayerId = playerId;
		this.numPlayers = numPlayers;
		this.type = type;
		this.map = new Map(maxSize,maxSize,xmlMapReader);
		this.teams = initializeTeams(numPlayers,type);
		this.harpoonManager = new HarpoonManager(numPlayers);
		this.timeEventsManager = new TimeEventsManager(this);
		this.gameTimeOff = false;
		coord=new Vector3();
		this.logFile = new LogFile("Player: " + Integer.toString(playerId));
		printGameStatus();
	}
	
	/**
	 * <p>Prints the the game Status on the log file in the following way:<p>
	 * <p>The type of the game is: _________ </p>
	 * <p>The number of players is: ___ </p>
	 * <p>The players id is ___</p>
	 * <p>And for each player prints: "The player ___  is on the team ___</p> 
	 * @see LogFile
	 */
	private void printGameStatus() {
		logFile.write("---------------------------------");
		logFile.write("The type of the game is: "+type.toString());
		logFile.write("The number of players is: "+numPlayers);
		logFile.write("The players id is: "+ myPlayerId);
		for (int i=0;i<numPlayers;i++) logFile.write("The player " +i+" is in the team" + getMyTeamId(i));
		logFile.write("---------------------------------");
	}
	
	/**
	 * Creates the teams in function of the number of players and the type of game.
	 * @param numPlayers The number of players of the match.
	 * @param type The type of the match.
	 * @return The an ArrayList with the teams of the match.
	 */
	private ArrayList<Team> initializeTeams(int numPlayers, TypeGame type) {
		ArrayList<Team> teams = null;
		if (type.equals(TypeGame.Normal)) teams = normalGame(numPlayers,type); 
		else if (type.equals(TypeGame.Teams)) teams = teamsGame(numPlayers,type);
		else if ((type.equals(TypeGame.Survival)) || (type.equals(TypeGame.OneVsAll))) teams = survivalGame(numPlayers,type);
		else if (type.equals(TypeGame.BattleRoyale)) teams = battleRoyalGame(numPlayers,type);
		return teams;
	}

	/**
	 * <p>Creates the teams of the Survival game.</p>
	 * <p>There will be two teams the following structure:</p>
	 * <p>Team 0(Exterminators): The player(Id = 0) that will have to defeat all players</p>
	 * <p>Team 1(Survivals): The rest of the players</p>
	 * @param numPlayers The number of players of the match.
	 * @param type The type of the match.
	 * @return Returns an ArrayList with the teams of the match.
	 */
	private ArrayList<Team> survivalGame(int numPlayers, TypeGame type) {
		ArrayList<Team> teams = new ArrayList<Team>();
		int playerId = 0;
		teams.add(new Team(numPlayers,0,1,playerId,type));
		teams.add(new Team(numPlayers,1,numPlayers-1,playerId+1,type));
		return teams;
	}
	
	/**
	 * <p>Creates the teams of the Normal game.</p>
	 * <p>There will be one team for each player.(4 players -> 4 Teams) </p>
	 * @param numPlayers The number of players of the match.
	 * @param type The type of the match.
	 * @return Returns an ArrayList with the teams of the match.
	 */
	private ArrayList<Team> normalGame(int numPlayers,TypeGame type) {
		ArrayList<Team> teams = new ArrayList<Team>();
		int playerId = 0;
		for (int i=0;i<numPlayers;i++){
			teams.add(new Team(numPlayers,i,1,playerId,type));
			playerId += 1;
		}
		return teams;
	}
	
	/**
	 * <p>Creates the teams of the Battle Royal game.</p>
	 * <p>There will be one team for each player.(4 players -> 4 Teams) </p>
	 * @param numPlayers The number of players of the match.
	 * @param type The type of the match.
	 * @return Returns an ArrayList with the teams of the match.
	 */
	private  ArrayList<Team> battleRoyalGame(int numPlayers,TypeGame type){
		//Is like normal game, the same team
		return normalGame(numPlayers,type);
	} 

	/**
	 * Start the game time used in battle royale or survival mode.
	 */
	public void startGameTime(){
		if (type.equals(TypeGame.BattleRoyale)||type.equals(TypeGame.Survival)) timeEventsManager.endGameEvent(type);
	}
	
	/**
	 * <p>Creates the teams of the Teams game.</p>
	 * <p>There will be two tams with the following structure:</p>
	 * <p>Each team will have at least one player (max 2)</p>
	 * @param numPlayers The number of players of the match.
	 * @param type The type of the match.
	 * @return Returns an ArrayList with the teams of the match.
	 */
	private  ArrayList<Team> teamsGame(int numPlayers,TypeGame type){
		ArrayList<Team> teams = new ArrayList<Team>();
		int playerId = 0;
		for (int i=0;i<(numPlayers/2);i++){
			teams.add(new Team(numPlayers,i,2,playerId,type, true));
			playerId += 2;
		}
		return teams;
		
	} 
	
	/**
	 * Search the player through the ArrayList of teams and if
	 * it's found it returns the player.
	 * @param playerId The player id of the player that want to get.
	 * @return The player if it's found, else it returns null.
	 */
	private Player getPlayer(int playerId){
		Player player = null;
		boolean found = false;
		Team team;
		Iterator<Player> playerIterator;
		Iterator<Team> teamIterator = teams.iterator();
		while (teamIterator.hasNext() && !found){
			team = teamIterator.next();
			playerIterator = team.getPlayers().iterator();
			while (playerIterator.hasNext() && !found){
				player = playerIterator.next();
				found = (playerId == player.getPlayerId());
			}
		}
		return player;
	}

	/**
	 * Check if the newSqure is empty of any objects that could block the movement of the 
	 * player.
	 * @param x The coordinate x of the square that wants to be checked.
	 * @param y The coordinate y of the square that wants to be checked.
	 * @return It returns true if the players can move to the square, false in other case.
	 */
	private boolean newSquare(int x, int y){
		TypeSquare square = map.getBasicMatrixSquare(x,y);
		return !(square.equals(TypeSquare.unbreakable)|| square.equals(TypeSquare.breakable) 
				  ||square.equals(TypeSquare.harpoon));
	}

	/**
	 * Check if the new move of the player is inside the board 
	 * @param dir The direction of player that is moving the player.
	 * @param playerId The id of the player that its moving
	 * @return If the movement is inside the board game it returns true, false in other case.
	 */
	public boolean insideBoardMove(Direction dir, int playerId) {
		Player player = getPlayer(playerId);
		boolean valid = false;
		Vector3 position = player.getPosition();
		float limitX = map.getLength();
		float limitY = map.getWidth();
		float newPositionX = position.x;
		float newPositionY = position.y;
		
		if (dir.equals(Direction.left)) newPositionX -= minimalMove;
		else if (dir.equals(Direction.right)) newPositionX += minimalMove;
		else if (dir.equals(Direction.up)) newPositionY += minimalMove;
		else if (dir.equals(Direction.down)) newPositionY -= minimalMove;
		
		valid = ((newPositionX>=0.0) && (newPositionX<=(limitX-playerLength)) && 
				(newPositionY>=0.0) && (newPositionY<=(limitY-playerWidth))) || 
				(!dir.equals(player.getLookAt()));
		
		return valid;
	}
	
	/** 
	 * Gets the direction of the player where it was doing the special move.
	 * @param myPlayerId The player id of the special move.
	 * @return The direction of special move of the player. 
	 */
	public Direction getSpecialMoveDir(int myPlayerId) {
		Player player = getPlayer(myPlayerId);
		return player.getSpecialMoveDir();
	}
	
	/**
	 * Check if the player can do the special move.
	 * @param dir The direction of the move of the player.
	 * @param playerId The player that is making the move.
	 * @return Returns true if the player can do an special move, false in other case.
	 */
	public boolean isSpecialMove(Direction dir, int playerId) {
		Player player = getPlayer(playerId);
		boolean valid=false;
		Vector3 position=player.getPosition();
		if (player.getSpecialMove()){
			Direction dirPlayer=player.getLookAt();
			if(dirPlayer.equals(Direction.left)){
				if(dir.equals(Direction.up)){
					valid=newSquare((int)position.x,(int) position.y+1);
				}else if(dir.equals(Direction.down)){
					valid=newSquare((int)position.x,(int) position.y-1);
				}
				if (valid){
					player.setSpecialMove(!((int)(position.x-0.25f)==(position.x-0.25f)));
				}
			}else if(dirPlayer.equals(Direction.right)){
				if(dir.equals(Direction.up)){
					valid=newSquare((int)position.x+1,(int) position.y+1);
				}else if(dir.equals(Direction.down)){
					valid=newSquare((int)position.x+1,(int) position.y-1);
				}
				if (valid){
					player.setSpecialMove(!((int)(position.x+0.25f)==(position.x+0.25f)));
				}
			}else if(dirPlayer.equals(Direction.up)){
				if(dir.equals(Direction.right)){
					valid=newSquare((int)position.x+1,(int) position.y+1);
				}else if(dir.equals(Direction.left)){
					valid=newSquare((int)position.x-1,(int) position.y+1);
				}
				if (valid){
					player.setSpecialMove(!((int)(position.y+0.25f)==(position.y+0.25f)));
				}
			}else if(dirPlayer.equals(Direction.down)){
				if(dir.equals(Direction.right)){
					valid=newSquare((int)position.x+1,(int) position.y);
				}else if(dir.equals(Direction.left)){
					valid=newSquare((int)position.x-1,(int) position.y);
				}
				if (valid){
					player.setSpecialMove(!((int)(position.y-0.25f)==(position.y-0.25f)));
				}
			}
		}else{
			if(dir.equals(player.getLookAt())){
				if(dir.equals(Direction.left)){
					if(((position.y-(int)position.y)==0.5f)||((position.y-(int)position.y)==0.25f)){
							if((newSquare((int)position.x-1,(int)position.y))&&!(newSquare((int)position.x-1,(int)position.y+1))){
								valid=true;
								player.setSpecialMove(true);
								player.setSpecialMoveDir(Direction.down);
							}					
						}
					if (((position.y-(int)position.y)==0.75f)||((position.y-(int)position.y)==0.5f)){
						if((newSquare((int)position.x-1,(int)position.y+1))&&!(newSquare((int)position.x-1,(int)position.y))){
							valid=true;
							player.setSpecialMove(true);
							player.setSpecialMoveDir(Direction.up);
							}	
						}
					} else if(dir.equals(Direction.right)){
					if(((position.y-(int)position.y)==0.5f)||((position.y-(int)position.y)==0.25f)){
							if((newSquare((int)position.x+1,(int)position.y))&&!(newSquare((int)position.x+1,(int)position.y+1))){
								valid=true;
								player.setSpecialMove(true);
								player.setSpecialMoveDir(Direction.down);
							}					
						}
					if (((position.y-(int)position.y)==0.75f)||((position.y-(int)position.y)==0.5f)){
						if((newSquare((int)position.x+1,(int)position.y+1))&&!(newSquare((int)position.x+1,(int)position.y))){
							valid=true;
							player.setSpecialMove(true);
							player.setSpecialMoveDir(Direction.up);
							}	
						}
					} else if(dir.equals(Direction.up)){
					if(((position.x-(int)position.x)==0.5f)||((position.x-(int)position.x)==0.25f)){
							if((newSquare((int)position.x,(int)position.y+1))&&!(newSquare((int)position.x+1,(int)position.y+1))){
								valid=true;
								player.setSpecialMove(true);
								player.setSpecialMoveDir(Direction.left);
							}					
						}
					if (((position.x-(int)position.x)==0.75f)||((position.x-(int)position.x)==0.5f)){
						if((newSquare((int)position.x+1,(int)position.y+1))&&!(newSquare((int)position.x,(int)position.y+1))){
							valid=true;
							player.setSpecialMove(true);
							player.setSpecialMoveDir(Direction.right);
							}	
						}
					}else if(dir.equals(Direction.down)){
					if(((position.x-(int)position.x)==0.5f)||((position.x-(int)position.x)==0.25f)){
							if((newSquare((int)position.x,(int)position.y-1))&&!(newSquare((int)position.x+1,(int)position.y-1))){
								valid=true;
								player.setSpecialMove(true);
								player.setSpecialMoveDir(Direction.left);
							}					
						}
					if (((position.x-(int)position.x)==0.75f)||((position.x-(int)position.x)==0.5f)){
						if((newSquare((int)position.x+1,(int)position.y-1))&&!(newSquare((int)position.x,(int)position.y-1))){
							valid=true;
							player.setSpecialMove(true);
							player.setSpecialMoveDir(Direction.right);
							}	
						}
					}
			}
		}
		return valid;
		}

	/**
	 * Check if the player can do a normal move.
	 * @param dir The direction of the movement of the player.
	 * @param playerId The player id of the player that is doing the move.
	 * @return It returns true if the player can make a normal move, false in other case.
	 */
	public boolean isNormalMove(Direction dir, int playerId) {
		Player player = getPlayer(playerId);
		boolean valid = false;
		if(dir.equals(player.getLookAt())){
			Vector3 position=player.getPosition();
			if ((dir.equals(Direction.left)||dir.equals(Direction.right))&&((int)position.y==position.y)){
				if ((int)position.x==position.x){
					if(dir.equals(Direction.left)){
						valid=newSquare((int)position.x-1,(int)position.y);
					}else{
						valid=newSquare((int)position.x+1,(int)position.y);
					}
				}else{
					valid=true;
				}
				
			}else if((dir.equals(Direction.up)||dir.equals(Direction.down))&&((int)position.x==position.x)){
				if ((int)position.y==position.y){
					if(dir.equals(Direction.up)){
						valid=newSquare((int)position.x,(int)position.y+1);
					}else{
						valid=newSquare((int)position.x,(int)position.y-1);
					}
				}else{
					valid=true;
				}
			}
		}else valid=true;
		if (valid) player.setSpecialMove(false);
		return valid;
	}
	
	/**
	 * <p>Take away a life of the player.</p>
	 * <p>It depends on the type of game it does diferents thing when it's the last life.</p>
	 * <p>In Normal,Battle Royale or OneVsAll mode the player dies.</p>
	 * <p>In Survival mode, the player is changed to the exterminator team.</p>
	 * <p>In Teams mode if the teammate has more thah two lives, the life is taken away
	 * of the teammate else its taken away of the player.</p>
	 * @param playerId The player id of the player that lose its life.
	 */
	private void loseLife(int playerId) {
		Player player = getPlayer(playerId);
		timeEventsManager.sinkPenguinEvent(player);
		if (type.equals(TypeGame.Normal)||type.equals(TypeGame.BattleRoyale)) checkPlayer(player);
		else if (type.equals(TypeGame.Teams)) sharePlayer(player);
		else if (type.equals(TypeGame.Survival)) checkSurvival(player);
		else if (type.equals(TypeGame.OneVsAll)) checkPlayer(player);
		logFile.writeEvent("The player " + playerId + " has lost a life");
		if (areGameFinished() && (type.equals(TypeGame.Survival) || type.equals(TypeGame.BattleRoyale))) stopGameTimer();
	}
	
	private boolean areGameFinished() {
		boolean finished = false;
		if (type.equals(TypeGame.Survival)){
			finished = (teams.get(0).getPlayers().size() == numPlayers);
		}
		else if (type.equals(TypeGame.BattleRoyale)){
			int numPlayersAlive = 0;
			for (int i=0;i<numPlayers;i++) if (getPlayer(i).getLives()>0) numPlayersAlive++;
			finished = (numPlayersAlive == 1);
		}
		return finished;
	}

	private void stopGameTimer() {
		timeEventsManager.stopGameTimer();		
	}

	/**
	 * If a player of the second team(Team 1) dies,it's changed to the exterminators
	 * team(Team 0).
	 * @param player The player that lose its life.
	 */
	private void checkSurvival(Player player) {
		Team team = teams.get(1);
		team.getPlayers().remove(player);
		team = teams.get(0);
		losersTeam.add(player);
		team.getPlayers().add(player);
		modifyPlayer(player);
	}
	


	/**
	 * Used on the Survival mode. It changes the number of lives to 1
	 * the posibility of putting harpoons and make the player invincible.
	 * @param player The player that has lost his life.
	 */
	private void modifyPlayer(Player player) {
		player.resetPositon();
		player.setCanPlay(false);
		player.setLives(1);
		player.makeInvincible();
		player.setMaxHarpoonsAllow(1);
		player.setRange(2);
	}

	/**
	 * <p>Used on teams mode.</p>
	 * <p>In Teams mode if the teammate has more thah two lives and its the last life of the player,
	 * the life is taken away of the teammate else its taken away of the player.</p>  
	 * @param player The player that lost its life.
	 */
	private void sharePlayer(Player player) {
		if (player.getLives()>1)
			player.removeLife();
		else if(getMyTeam(player.getPlayerId()).isShare()){
			Player p = getMyTeam(player.getPlayerId()).giveMeOneOfYourLives(player.getPlayerId());
			if(p==null) checkPlayer(player);
			else {
				//timeEventsManager.respawnTimeEvent(p);
				timeEventsManager.sinkPenguinEvent(p);
				p.resetLookAt(p.getPlayerId());
				p.resetPositon();
				p.setCanPlay(false);
				p.makeInvincible();
				checkPlayer(p);
			}
		}
	}
	
	/**
	 * Used on Normal, Battle Royale and OnevsAll modes.
	 * It takes away a life of the player.
	 * @param player The player that lost his life.
	 */
	private void checkPlayer(Player player) {
		player.removeLife();
	}

	/**
	 * Move the player to a new position calculated with the arguments that are
	 * passed.
	 * @param dir The direction of the player
	 * @param playerId The player that is moving.
	 * @param x The coordinate x of the player.
	 * @param y The coordinate y of the player.
	 */
	public void movePlayer(Direction dir, int playerId, float x, float y) {
		Player player = getPlayer(playerId);
		if (!dir.equals(player.getLookAt())) player.setLookAt(dir);
		else {
			if (dir.equals(Direction.left)) player.setPositionX(x-minimalMove);
			else if (dir.equals(Direction.right)) player.setPositionX(x+minimalMove);
			else if (dir.equals(Direction.up)) player.setPositionY(y+minimalMove);
			else if (dir.equals(Direction.down)) player.setPositionY(y-minimalMove);
		}
		if (!player.isInvincible() && isSunkenPenguin(playerId)){
				loseLife(playerId);
		}
		checkUpgrade(dir,playerId);
		logFile.writeEvent("The player " + playerId + " has moved to "+ x + " " + y + " with dir "+dir.toString());
	}
	 
	/**
	 * Check if the player have moved to a new square where there are water.
	 * @param playerId The player id of the player that has moved
	 * @return It returns true if the player have moved to the water, false in other case.
	 */
	public boolean isSunkenPenguin(int playerId){
		Player player = getPlayer(playerId);
		Vector3[] positions = player.getPositions();
		boolean isSunken = false;
	
		if(map.getWaterMatrixSquare((int)positions[0].x,(int)positions[0].y)!=WaterTypes.empty){
			if((positions[0].x>=0 && positions[0].x<11)&&(positions[0].y>=0 && positions[0].y<11)){
				map.sunkenObject((int)positions[0].x,(int)positions[0].y);
				if(type.equals(TypeGame.BattleRoyale)){
					Harpoon myHarpoon = harpoonManager.getsinkHarpoon((int)positions[0].x,(int)positions[0].y);
					stealImprovements(player.getPlayerId(),myHarpoon);
				}
				isSunken = true;
			}
		}
		else if(map.getWaterMatrixSquare((int)positions[1].x,(int)positions[1].y)!=WaterTypes.empty){
			if((positions[1].x>=0 && positions[1].x<11)&&(positions[1].y>=0 && positions[1].y<11)){
				map.sunkenObject((int)positions[1].x,(int)positions[1].y);
				if(type.equals(TypeGame.BattleRoyale )){
					Harpoon myHarpoon = harpoonManager.getsinkHarpoon((int)positions[1].x,(int)positions[1].y);
					stealImprovements(player.getPlayerId(),myHarpoon);
				}
				isSunken = true;
			}
		}
		if(isSunken) myAppSounds.playSound("sinkPenguin");
		return isSunken;
	}
	
	/**
	 * Puts an harpoon in coordinates the are passed as arguements
	 * @param x The coordinate x of the harpoon.
	 * @param y The coordinate y of the harpoon.
	 * @param range The range that have the harpoon.
	 * @param playerId The player that put the harpoon.
	 * @param time The time when the harpoon will sink.
	 */
	public void putHarpoonAt(int x, int y, int range,int playerId, long time){
		Harpoon harpoon = new Harpoon(x,y,range,playerId);
		harpoonManager.addHarpoon(harpoon);
		timeEventsManager.sinkHarpoonEvent(harpoon,time);
		if (map.getBasicMatrixSquare(x, y) == TypeSquare.empty){
			myAppSounds.playSound("putHarpoon");
			map.putHarpoonAt(x,y);
			map.addAllFissures(harpoonManager.getActiveHarpoonList());
			logFile.writeEvent("The player " + playerId + " has put an harpoon on "+ x + " " + y + " with time "+ time);
		}
	}
	
	/**
	 * <p>This events it dispacthed when the count down of the harpoon its finished.</p>
	 * <p>The harpoon will calculate the damage to the players that are on the explosion
	 * range and update the map(Breaking barrels,deploying upgrades and putting water).</p>
	 * @param harpoon The harpoon that dispatched the event.
	 */
	public void sinkHarpoon(int x,int y) {
		try{
			Harpoon harpoon = harpoonManager.getHarpoon(x,y);
			timeEventsManager.stopHarpoonTimer(harpoon);
			Vector3 position = harpoon.getPosition();
			harpoonManager.sinkHarpoon(harpoon);
			timeEventsManager.freezeWaterEvent(harpoon);
			logFile.writeEvent("The harpoon in " + harpoon.getPosition().x + " " + harpoon.getPosition().y + " has sunken");		
			harpoonRangeDamage(harpoon);
			map.putSunkenHarpoonAt((int)position.x,(int)position.y);
			myAppSounds.playSound("breakIce");
			map.paintAllWaters(harpoonManager.getSunkenHarpoonList());
			map.addAllFissures(harpoonManager.getActiveHarpoonList());
		}
		catch(Exception e){
			
		}
	}
	
	/**
	 * Calculate the damage to the players that cause the sink of the harpoon.
	 * @param harpoon The harpoon that cause the damg
	 */
	private void harpoonRangeDamage(Harpoon harpoon) {
		boolean[] isCought = new boolean[numPlayers];
		for (int i=0;i<numPlayers;i++) isCought[i] = false;
		harpoonRangeDamageChain(harpoon,isCought);
		for (int i=0;i<numPlayers;i++){
			if (isCought[i]){
				loseLife(i);
				myAppSounds.playSound("sinkPenguin");
			}
		}
	}

	/**
	 * Recursive method used when an harpoon that sunk hits another harpoon, it will 
	 * create a chain of sunks. 
	 * @param harpoon The harpoon that cause the chain.
	 * @param isCought An array that indicate which players has been cougth.
	 */
	private void harpoonRangeDamageChain(Harpoon harpoon, boolean[] isCought){
		boolean[] isBlocked = new boolean [4];
		for (int i=0;i<4;i++) isBlocked[i] = false;
		int range = harpoon.getRange();
		for (int i=0;i<=range;i++){
			for (int j=0;j<numPlayers;j++){
				Player player = getPlayer(j);
				if (!player.isInvincible()){
					Vector3[] positions = player.getPositions();
					if (isCought(harpoon,i,positions,isBlocked)){
						isCought[j] = true;
						if (type.equals(TypeGame.BattleRoyale))
						stealImprovements(j, harpoon);
					}
				}
			}
			if (i!=0) checkHarpoonsInRange(harpoon,i,isCought,isBlocked);
			updateBlocked(harpoon,i+1,isBlocked);
		}
		if (harpoon.getPlayerId() == myPlayerId) harpoonManager.decreaseHarpoonCount();
		map.putSunkenHarpoonAt((int)harpoon.getPosition().x,(int)harpoon.getPosition().y);
		map.paintAllWaters(harpoonManager.getSunkenHarpoonList());
	}

	/**
	 * This method is to steal the improvements of another that its hitted by the harpoon harpoon
	 * in the game Battle Royale.
	 * @param playerId The player id of the player hitted.
	 * @param harpoon The harpoon of the hitter.
	 */
	private void stealImprovements(int playerId, Harpoon harpoon) {
		if (playerId != harpoon.getPlayerId()){
		Player hitted = getPlayer(playerId);
		Player hitter = getPlayer(harpoon.getPlayerId());
		
		int maxHarpoonsAllow = (hitted.getMaxHarpoonsAllow()-1) + hitter.getMaxHarpoonsAllow();
		int range = (hitted.getRange()-1) + hitter.getRange();
		int speed = (hitted.getSpeed()-1) + hitter.getSpeed();
		boolean invisible = hitted.isInvisible();
		
		if (invisible){
			timeEventsManager.removeInvisibleTimer(hitted);
			if(hitter.isInvisible()) timeEventsManager.removeInvisibleTimer(hitter);
			timeEventsManager.invisibleEvent(hitter);
			hitter.setInvisible(true);
		}
		
		if (maxHarpoonsAllow<=5)
			hitter.setMaxHarpoonsAllow(maxHarpoonsAllow);
		else
			hitter.setMaxHarpoonsAllow(5);
		
		if (range<=5)
			hitter.setRange(range);
		else
			hitter.setRange(5);
		
		if (speed<=5)
			hitter.setSpeed(speed);
		else
			hitter.setSpeed(5);
		}
	}


	/**
	 * Check if there is any harpoons in the range in the explosion of the harpoon.
	 * @param harpoon The harpoon that has sunken.
	 * @param range The range of the harpoon that has sunken.
	 * @param isCought The players that have been cought.
	 * @param isBlocked Auxiliar array to check if the explosion have been blocked by an unbrekable object.
	 */
	private void checkHarpoonsInRange(Harpoon harpoon, int range, boolean[] isCought, boolean[] isBlocked) {
		int x = (int)harpoon.getPosition().x;
		int y = (int)harpoon.getPosition().y;
		if (!isBlocked[0] && (map.getBasicMatrixSquare(x, y+range).equals(TypeSquare.harpoon))){		
			Harpoon newHarpoon = harpoonManager.getHarpoon(x,y+range);
			harpoonManager.sinkHarpoon(newHarpoon);
			timeEventsManager.stopHarpoonTimer(newHarpoon);
			map.putSunkenHarpoonAt(x,y);
			logFile.writeEvent("The harpoon in " + newHarpoon.getPosition().x + " " + newHarpoon.getPosition().y + " has sunken");
			harpoonRangeDamageChain(newHarpoon,isCought);
			timeEventsManager.freezeWaterEvent(newHarpoon);
		}
		else if (!isBlocked[1] && (map.getBasicMatrixSquare(x, y-range).equals(TypeSquare.harpoon))){
			Harpoon newHarpoon = harpoonManager.getHarpoon(x,y-range);
			harpoonManager.sinkHarpoon(newHarpoon);
			timeEventsManager.stopHarpoonTimer(newHarpoon);
			map.putSunkenHarpoonAt(x,y);
			logFile.writeEvent("The harpoon in " + newHarpoon.getPosition().x + " " + newHarpoon.getPosition().y + " has sunken");
			harpoonRangeDamageChain(newHarpoon,isCought);
			timeEventsManager.freezeWaterEvent(newHarpoon);
		}
		else if (!isBlocked[2] && (map.getBasicMatrixSquare(x-range,y).equals(TypeSquare.harpoon))){
			Harpoon newHarpoon = harpoonManager.getHarpoon(x-range,y);
			harpoonManager.sinkHarpoon(newHarpoon);
			timeEventsManager.stopHarpoonTimer(newHarpoon);
			map.putSunkenHarpoonAt(x,y);
			logFile.writeEvent("The harpoon in " + newHarpoon.getPosition().x + " " + newHarpoon.getPosition().y + " has sunken");
			harpoonRangeDamageChain(newHarpoon,isCought);
			timeEventsManager.freezeWaterEvent(newHarpoon);
		}
		else if (!isBlocked[3] && (map.getBasicMatrixSquare(x+range,y).equals(TypeSquare.harpoon))){
			Harpoon newHarpoon = harpoonManager.getHarpoon(x+range,y);
			harpoonManager.sinkHarpoon(newHarpoon);
			timeEventsManager.stopHarpoonTimer(newHarpoon);
			map.putSunkenHarpoonAt(x,y);
			logFile.writeEvent("The harpoon in " + newHarpoon.getPosition().x + " " + newHarpoon.getPosition().y + " has sunken");
			harpoonRangeDamageChain(newHarpoon,isCought);
			timeEventsManager.freezeWaterEvent(newHarpoon);
		}	
	}

	/**
	 * Check if the explosion of the harpoon has been blocked.
	 * @param harpoon The harpoon that generate the explosion.
	 * @param range The range of the harpoon.
	 * @param isBlocked The array that is update.
	 */
	private void updateBlocked(Harpoon harpoon, int range, boolean[] isBlocked) {
		int x = (int)harpoon.getPosition().x;
		int y = (int)harpoon.getPosition().y;
		if (!map.canRunThrough(x,y+range)) isBlocked[0] = true;
		if (!map.canRunThrough(x,y-range)) isBlocked[1] = true;
		if (!map.canRunThrough(x-range,y)) isBlocked[2] = true;
		if (!map.canRunThrough(x+range,y)) isBlocked[3] = true;
	}

	/**
	 * Calculate if the harpoon has cought a player. 
	 * @param harpoon The harpoon that has generated the explsion.
	 * @param range The range of the explosion.
	 * @param positions The position of the player.
	 * @param isBlocked Auxiliar array to know if the explosion has been blocked.
	 * @return It returns true if the player has been cought, false in other case.
	 */
	private boolean isCought(Harpoon harpoon, int range, Vector3[] positions,boolean[] isBlocked) {
		int x = (int)harpoon.getPosition().x;
		int y = (int)harpoon.getPosition().y;
		boolean isCought = false;
		// north
		if (!isBlocked[0] && (((positions[0].x==x) && (positions[0].y == y+range)) || 
		     (positions[1].x==x) && (positions[1].y == y+range))){
			isCought = true;
			map.sunkenObject((int)harpoon.getPosition().x,(int)harpoon.getPosition().y+range);
		}
		// south
		else if (!isBlocked[1] && (((positions[0].x==x) && (positions[0].y == y-range)) || 
			     (positions[1].x==x) && (positions[1].y == y-range))){
			isCought = true;
			map.sunkenObject((int)harpoon.getPosition().x,(int)harpoon.getPosition().y-range);
		}
		// west
		else if (!isBlocked[2] && (((positions[0].x==x-range) && (positions[0].y == y)) || 
			     (positions[1].x==x-range) && (positions[1].y == y))){
			isCought = true;
			map.sunkenObject((int)harpoon.getPosition().x-range,(int)harpoon.getPosition().y);
		}
		// east
		else if (!isBlocked[3] && (((positions[0].x==x+range) && (positions[0].y == y)) || 
			     (positions[1].x==x+range) && (positions[1].y == y))){
			isCought = true;
			map.sunkenObject((int)harpoon.getPosition().x+range,(int)harpoon.getPosition().y);
		}
		return isCought;
	}

	/**
	 * Freezes the water updating the map.
	 * @param harpoon Harpoon that caused the explosion.
	 */
	public void freezeWater(Harpoon harpoon) {
		map.emptyHarpoonPosInSunkenMatrix(harpoon);
		harpoonManager.removeHarpoon(harpoon);
		map.paintAllWaters(harpoonManager.getSunkenHarpoonList());
		logFile.writeEvent("The water in " + harpoon.getPosition().x + " " + harpoon.getPosition().y + " has freezed");
	}
	
	/**
	 * Check if the move that make a player its legal.(Event of SmartFoxServer)
	 * @param dir The direction of the move of the player.
	 * @param playerId The player id of the player that its moving.
	 * @return It returns true if its a legal move, false in other case.
	 */
	private boolean isLegalMove(Direction dir, int playerId){
		Player player = getPlayer(playerId);
		boolean valid = false;
		if(dir.equals(player.getLookAt())){
			Vector3 position=player.getPosition();
			if ((dir.equals(Direction.left)||dir.equals(Direction.right))&&((int)position.y==position.y)){
				if ((int)position.x==position.x){
					if(dir.equals(Direction.left)){
						valid=newSquare((int)position.x-1,(int)position.y);
					}else{
						valid=newSquare((int)position.x+1,(int)position.y);
					}
				}else{
					valid=true;
				}
				
			}else if((dir.equals(Direction.up)||dir.equals(Direction.down))&&((int)position.x==position.x)){
				if ((int)position.y==position.y){
					if(dir.equals(Direction.up)){
						valid=newSquare((int)position.x,(int)position.y+1);
					}else{
						valid=newSquare((int)position.x,(int)position.y-1);
					}
				}else{
					valid=true;
				}
			}
		}else valid=true;
		return valid;
	}
	
	/**
	 * Checks if the player can put an harpoon.(Event on SmartFoxServer)
	 * @param playerId The player that puts an harpoon.
	 * @return It returns true if the player its able to put the harpoon, false in other case.
	 */
	public boolean canPutHarpoon(int playerId){
		Player player = getPlayer(playerId);
		Vector3 position=player.getPosition();
		if(player.getLookAt().equals(Direction.right)){
			if ((position.x-(int)position.x)==0){
				coord.x=position.x;
				coord.y=position.y;
			}else{
				coord.x=position.x+1;
				coord.y=position.y;
			}
		}else if(player.getLookAt().equals(Direction.left)){
				coord.x=position.x;
				coord.y=position.y;
			
		}else if(player.getLookAt().equals(Direction.up)){
			if ((position.y-(int)position.y)==0){
				coord.x=position.x;
				coord.y=position.y;
			}else{
				coord.x=position.x;
				coord.y=position.y+1;
			}
		}else if(player.getLookAt().equals(Direction.down)){
				coord.x=position.x;
				coord.y=position.y;
			
		}
		return (map.isEmptySquare((int)coord.x,(int)coord.y) && playerAllowed(playerId));
	}	
	
	/**
	 * Sets the gameTime is off.
	 */
	public void gameTimeOff(){
		gameTimeOff=true;
	}
	
	/**
	 * Check if the player its allowed to put harpoons.
	 * @param playerId The player id of the player that puts the harpoon.
	 * @return It returns true if the player its able to put the harpoon, false in other case.
	 */
	private boolean playerAllowed(int playerId){
		Player player = getPlayer(playerId);
		return (player.getMaxHarpoonsAllow()-harpoonManager.getCurrentPlayerHarpoons())>0;
	}

	/**
	 * Check if the player has won.
	 * @param playerId The player that wants to be check if wins.
	 * @return It returns true if it has won, false in other case.
	 */
	
	public boolean imTheWinner(int playerId){
		Player player = getPlayer(playerId);
		boolean win = !player.isThePlayerDead();
		Team myTeam = getMyTeam(playerId);
		for(int i = 0; i<numPlayers; i++){
			if(i != playerId && getMyTeam(i)!=myTeam){
				player = getPlayer(i);
				win = win && player.isThePlayerDead();
			}
		}
		if (win) makeInvencible(playerId);
		return win;
	}
	
	private void makeInvencible(int playerId) {
		Player player = getPlayer(playerId);
		player.makeInvincible();
	}

	/**
	 * Check if all players all dead. 
	 * @return It returns true if all players have dead, false in other case.
	 */
	public boolean areAllPlayersDead() {
		Player player = null;
		boolean allDead = true;
		for (int i =0; i<numPlayers; i++){
			player = getPlayer(i);
			allDead = allDead &&  player.isThePlayerDead();
		}
		return allDead;
			
	}
		
	/**
	 * Gets the int coord x of the player. 
	 * @param playerId The player which is getting the coordinates.
	 * @return <p>Returns the truncate coord. Examples: </p>
	 * 		   <p> 1.75 -> 1, 1.25 -> 1, 1.00 -> 1 </p>
	 */
	public int getIntegerCoordX(int playerId) {
		Player player = getPlayer(playerId);
		Vector3 position = player.getPosition();
		return (int)position.x;
	}
	
	/**
	 * Gets the int coord y of the player. 
	 * @param playerId The player which is getting the coordinates.
	 * @return <p>Returns the truncate coord. Examples: </p>
	 * 		   <p> 1.75 -> 1, 1.25 -> 1, 1.00 -> 1 </p>
	 */
	public int getIntegerCoordY(int playerId) {
		Player player = getPlayer(playerId);
		Vector3 position = player.getPosition();
		return (int)position.y;
	}
	
	/**
	 * Gets the team where there is the player.
	 * @param playerId The player used for getting the team.
	 * @return The team that the player belongs to.
	 */
	public Team getMyTeam(int playerId){
		Player player = getPlayer(playerId);
		Iterator<Team> it = teams.iterator();
		Team myTeam = null;
		while(it.hasNext()){
			myTeam = it.next();
			if (myTeam.getPlayers().contains(player)) return myTeam;
			
		}
		return null;
	}
	
	/**
	 * Event dispatched when the player respawn. 
	 * @param player The player thats is respawing.
	 */
	public void sinkPenguinFinish(Player player) {
		player.checkCanPlay();
		player.removeInvincible();
		if (type.equals(TypeGame.Survival)) player.makeInvincible();
	}

	/**
	 * Gets the range of the player.
	 * @param playerId The player which is getting the range.
	 * @return The range of the player.
	 */
	public int getMyPlayerRange(int playerId) {
		Player player = getPlayer(playerId);
		return player.getRange();
	}
	
	/**
	 * Checks if the move is correct. (Event of SmartFoxServer)
	 * @param dir The direction of the movement.
	 * @param playerId The player id of the player that its moving.
	 * @return It returns true if the movement is correct, false in other case.
	 */
	public boolean checkCorrectMove(Direction dir, int playerId){
		return (insideBoardMove(dir, playerId)&& isLegalMove(dir,playerId));
	}
	
	public boolean checkHarpoon(int x, int y) {
		return map.isEmptySquare(x,y);
	}
	
	public void checkUpgrade(Direction dir, int playerId) {
		Player player = getPlayer(playerId);
		boolean caughtUpgrade =false;
		Vector3[] positions = player.getPositions();
		if (((positions[0].x>=0 && positions[0].x<11)&&(positions[0].y>=0 && positions[0].y<11))
			&& ((positions[1].x>=0 && positions[1].x<11)&&(positions[1].y>=0 && positions[1].y<11))){
			if (positions[0].equals(positions[1]) && map.existUpgrade(positions[0])) {
				TypeSquare typeSquare = map.getBasicMatrixSquare((int)positions[1].x,(int)positions[1].y);
				if (typeSquare.equals(TypeSquare.invisible)) makeInvisible(player);
				else player.upgradePlayer(typeSquare);
				map.setEmpty((int)positions[0].x,(int)positions[0].y);
				caughtUpgrade = true;
				logFile.writeEvent("The player "+ playerId+ " has taken an upgrade " + typeSquare.toString() + " at "+ positions[0].x + " " + positions[0].y);
			}
			else {
				if (map.existUpgrade(positions[0])){
					TypeSquare typeSquare = map.getBasicMatrixSquare((int)positions[0].x,(int)positions[0].y);
					if (typeSquare.equals(TypeSquare.invisible)) makeInvisible(player);
					else player.upgradePlayer(typeSquare);
					map.setEmpty((int)positions[0].x,(int)positions[0].y);
					caughtUpgrade = true;
					logFile.writeEvent("The player "+ playerId+ " has taken an upgrade " + typeSquare.toString() + " at "+ positions[0].x + " " + positions[0].y);
				}
				else if (map.existUpgrade(positions[1])){
					TypeSquare typeSquare = map.getBasicMatrixSquare((int)positions[1].x,(int)positions[1].y);
					if (typeSquare.equals(TypeSquare.invisible)) makeInvisible(player);
					else player.upgradePlayer(typeSquare);
					map.setEmpty((int)positions[1].x,(int)positions[1].y);
					caughtUpgrade = true;
					logFile.writeEvent("The player "+ playerId+ " has taken an upgrade " + typeSquare.toString() + " at "+ positions[1].x + " " + positions[1].y);
				}
			}
		}
		if (caughtUpgrade) myAppSounds.playSound("cacthUpgrade");
	}
	
	public void sendSinkHarpoon(Harpoon harpoon){
		matchManager.send((int)harpoon.getPosition().x,(int)harpoon.getPosition().y,harpoon.getPlayerId());		
	}
	
	private void makeInvisible(Player player){
		if (player.isInvisible()) timeEventsManager.removeInvisibleTimer(player);
		player.setInvisible(true);
		timeEventsManager.invisibleEvent(player);
	}
	
	public void endInvisible(Player player) {
		player.setInvisible(false);
		logFile.writeEvent("The player "+ player.getPlayerId()+ " is not invisible anymore");
	}
	
	//Getters and Setters

	public Map getMap() {
		return map;
	}
	public void setMap(Map map) {
		this.map = map;
	}
	public TypeGame getType() {
		return type;
	}
	public void setType(TypeGame type) {
		this.type = type;
	}

	public long getTime() {
		return matchTime;
	}
	public void setTime(long time) {
		this.matchTime = time;
	}

	public Vector3 getMyPlayerPosition(int playerId) {
		Player player = getPlayer(playerId);
		Vector3 currentPosition = new Vector3(player.getPosition().x,player.getPosition().y,0);
		return currentPosition;
	}

	public TypeSquare getBasicMatrixSquare(int i, int j) {
		return map.getBasicMatrixSquare(i,j);
	}
	
	public FissuresTypes getFissureMatrixSquare(int i, int j) {
		return map.getFissureMatrixSquare(i,j);
	}

	
	public WaterTypes getWaterMatrixSquare(int i, int j) {
		return map.getWaterMatrixSquare(i,j);
	}
	
	public SunkenTypes getSunkenMatrixSquare(int i, int j) {
		return map.getSunkenMatrixSquare(i,j);
	}
	

	public Direction getPlayerDirection(int playerId) {
		Player player = getPlayer(playerId);
		return player.getLookAt();
	}
	public Vector3 getCoord() {
		return coord;
	}

	public void setCoord(Vector3 coord) {
		this.coord = coord;
	}
	public int getPlayerLifes(int playerId) {
		Player player = getPlayer(playerId);
		return player.getLives();
	}

	public HarpoonManager getHarpoonManager() {
		return harpoonManager;
	}

	public void setHarpoonManager(HarpoonManager harpoonManager) {
		this.harpoonManager = harpoonManager;
	}

	public TimeEventsManager getTimeEventsManager() {
		return timeEventsManager;
	}

	public void setTimeEventsManager(TimeEventsManager timeEventsManager) {
		this.timeEventsManager = timeEventsManager;
	}

	public boolean isThePlayerDead(int playerId) {
		Player player = getPlayer(playerId);
		return player.isThePlayerDead();
	}

	public Direction getLookAt(int playerId) {
		Player player = getPlayer(playerId);
		return player.getLookAt();
	}

	public boolean canPlay(int playerId) {
		Player player = getPlayer(playerId);
		return (player.canPlay() && !gameTimeOff && !imTheWinner(playerId));
	}

	public int getMyPlayerId() {
		return myPlayerId;
	}

	public void setMyPlayerId(int myPlayerId) {
		this.myPlayerId = myPlayerId;
	}

	public int getPlayerSpeed(int playerId){
		Player player = getPlayer(playerId);
		return player.getSpeed();
	}

	public int getPlayerHarpoonAllow(int playerId) {
		Player player = getPlayer(playerId);
		return player.getMaxHarpoonsAllow();
	}

	public int getPlayerRange(int playerId){
		Player player = getPlayer(playerId);
		return player.getRange();
	}

	public ArrayList<Team> getTeams() {
		return teams;
	}

	public void setTeams(ArrayList<Team> teams) {
		this.teams = teams;
	}

	public int getNumPlayers() {
		return this.numPlayers;
	}

	public void setNumPlayers(int numPlayers) {
		this.numPlayers = numPlayers;
	}
	public boolean isInvisible(int playerId){
		return getPlayer(playerId).isInvisible();
	}

	public boolean isGameTimeOff() {
		return gameTimeOff;
	}

	public int getMyTeamId(int playerId) {
		Player player = getPlayer(playerId);
		int teamId = 0;
		Iterator<Team> it = teams.iterator();
		Team myTeam = null;
		while(it.hasNext()){
			myTeam = it.next();
			if (myTeam.getPlayers().contains(player)) return myTeam.getNumTeam();
			
		}
		return teamId;
	}

	public long getTimeInvisible(int numPlayer) {
		Player player = getPlayer(numPlayer);
		return timeEventsManager.getTimeInvisible(player);
	}

	public long getTimeManager() {
		return timeEventsManager.getTimeMatch();
	}

	public ArrayList<Player> getLosersTeam() {
		return losersTeam;
	}
	
	public void loadUpgrades(int[] upgrades) {
		map.loadUpgrades(upgrades);		
	}

	public void decreaseHarpoonsAllowed() {
		harpoonManager.decreaseHarpoonCount();
	}

	public void increaseHarpoonCount() {
		harpoonManager.increaseHarpoonCount();		
	}

	public boolean isGameTimeRunning() {
		return timeEventsManager.isGameTimeRunning();
	}
	
	
}
