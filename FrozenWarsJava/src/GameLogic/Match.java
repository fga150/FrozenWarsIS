package GameLogic;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector3;

import Application.MatchManager.Direction;
import GameLogic.Map.SunkenTypes;
import GameLogic.Map.FissuresTypes;
import GameLogic.Map.TypeSquare;
import GameLogic.Map.WaterTypes;
import Sounds.AppSounds;

public class Match {
	
	public enum TypeGame {Normal,Teams,OneVsAll,Survival,BattleRoyale}
	
	private final static float playerWidth = 1;
	private final static float playerLength = 1;
	private final static float minimalMove = 0.25f;
	private final static int maxSize = 11;
	
	private Map map;
	private LogFile logFile;
	private TimeEventsManager timeEventsManager;
	private HarpoonManager harpoonManager;
	private TypeGame type;
	private ArrayList<Team> teams;
	private Vector3 coord;
	private long matchTime;
	private int myPlayerId;
	private int numPlayers;
	private boolean gameTimeOff;
	private AppSounds myAppSounds;
	
	public Match(int[] upgrades,XMLMapReader xmlMapReader,int playerId, int numPlayers,TypeGame type,
			String namePlayer, AppSounds myAppSounds){
		this.myAppSounds = myAppSounds;
		this.myPlayerId = playerId;
		this.numPlayers = numPlayers;
		this.type = type;
		this.map = new Map(maxSize,maxSize,upgrades,xmlMapReader);
		this.teams = initializeTeams(numPlayers,type);
		this.harpoonManager = new HarpoonManager(numPlayers);
		this.timeEventsManager = new TimeEventsManager(this);
		this.gameTimeOff = false;
		if (type.equals(TypeGame.BattleRoyale)||type.equals(TypeGame.Survival)) timeEventsManager.endGameEvent(type);
		coord=new Vector3();
		this.logFile = new LogFile(namePlayer);
		printGameStatus();
	}
	
	private void printGameStatus() {
		logFile.write("---------------------------------");
		logFile.write("The type of the game is: "+type.toString());
		logFile.write("The number of players is: "+numPlayers);
		logFile.write("The players id is: "+ myPlayerId);
		for (int i=0;i<numPlayers;i++) logFile.write("The player " +i+" is in the team" + getMyTeamId(i));
		logFile.write("---------------------------------");
	}

	private ArrayList<Team> initializeTeams(int numPlayers, TypeGame type) {
		ArrayList<Team> teams = null;
		if (type.equals(TypeGame.Normal)) teams = normalGame(numPlayers,type); 
		else if (type.equals(TypeGame.Teams)) teams = teamsGame(numPlayers,type);
		else if ((type.equals(TypeGame.Survival)) || (type.equals(TypeGame.OneVsAll))) teams = survivalGame(numPlayers,type);
		else if (type.equals(TypeGame.BattleRoyale)) teams = battleRoyalGame(numPlayers,type);
		return teams;
	}

	private ArrayList<Team> survivalGame(int numPlayers, TypeGame type) {
		ArrayList<Team> teams = new ArrayList<Team>();
		int playerId = 0;
		teams.add(new Team(numPlayers,0,1,playerId,type));
		teams.add(new Team(numPlayers,1,numPlayers-1,playerId+1,type));
		return teams;
	}
	
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
	 * Create 4 teams with 1 player each one
	 * @param numPlayers: number of player in each team
	 * @param type : game mode
	 * @return
	 */
	
	private  ArrayList<Team> battleRoyalGame(int numPlayers,TypeGame type){
		//Is like normal game, the same team
		return normalGame(numPlayers,type);
	} 
	
	private  ArrayList<Team> teamsGame(int numPlayers,TypeGame type){
		ArrayList<Team> teams = new ArrayList<Team>();
		int playerId = 0;
		for (int i=0;i<(numPlayers/2);i++){
			teams.add(new Team(numPlayers,i,2,playerId,type, true));
			playerId += 2;
		}
		return teams;
		
	} 
	
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

	private boolean newSquare(int x, int y){
		TypeSquare square = map.getBasicMatrixSquare(x,y);
		return !(square.equals(TypeSquare.unbreakable)|| square.equals(TypeSquare.breakable) 
				  ||square.equals(TypeSquare.harpoon));
	}

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
	
	public Direction getSpecialMoveDir(int myPlayerId) {
		Player player = getPlayer(myPlayerId);
		return player.getSpecialMoveDir();
	}
		
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
	
	private void loseLife(int playerId) {
		Player player = getPlayer(playerId);
		timeEventsManager.sinkPenguinEvent(player);
		if (type.equals(TypeGame.Normal)||type.equals(TypeGame.BattleRoyale)) checkPlayer(player);
		else if (type.equals(TypeGame.Teams)) sharePlayer(player);
		else if (type.equals(TypeGame.Survival)) checkSurvival(player);
		else if (type.equals(TypeGame.OneVsAll)) checkPlayer(player);
		logFile.writeEvent("The player " + playerId + " has lost a life");
	}

	private void checkSurvival(Player player) {
		Team team = teams.get(1);
		team.getPlayers().remove(player);
		team = teams.get(0);
		team.getPlayers().add(player);
		modifyPlayer(player);
	}

	private void modifyPlayer(Player player) {
		player.resetPositon();
		player.setCanPlay(false);
		player.setLives(1);
		player.makeInvincible();
		player.setMaxHarpoonsAllow(1);
		player.setRange(2);
	}

	private void sharePlayer(Player player) {
		if (player.getLives()>1)
			player.removeLife();
		else if(getMyTeam(player.getPlayerId()).isShare()){
			Player p = getMyTeam(player.getPlayerId()).giveMeOneOfYourLives(player.getPlayerId());
			if(p==null) player.removeLife();
			else {
				timeEventsManager.respawnTimeEvent(p);
				p.removeLife();
			}
		}
	}

	private void checkPlayer(Player player) {
		player.removeLife();
	}

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
	 

	public boolean isSunkenPenguin(int playerId){
		Player player = getPlayer(playerId);
		Vector3[] positions = player.getPositions();
		boolean isSunken = false;
		if(map.getWaterMatrixSquare((int)positions[0].x,(int)positions[0].y)!=WaterTypes.empty){
			map.sunkenObject((int)positions[0].x,(int)positions[0].y);
			if(type.equals(TypeGame.BattleRoyale)){
				Harpoon myHarpoon = harpoonManager.getsinkHarpoon((int)positions[0].x,(int)positions[0].y);
				stealImprovements(player.getPlayerId(),myHarpoon);
			}
			isSunken = true;
		}
		else if(map.getWaterMatrixSquare((int)positions[1].x,(int)positions[1].y)!=WaterTypes.empty){
			map.sunkenObject((int)positions[1].x,(int)positions[1].y);
			if(type.equals(TypeGame.BattleRoyale )){
				Harpoon myHarpoon = harpoonManager.getsinkHarpoon((int)positions[1].x,(int)positions[1].y);
				stealImprovements(player.getPlayerId(),myHarpoon);
			}
			isSunken = true;
		}
		if(isSunken) myAppSounds.playSound("sinkPenguin");
		return isSunken;
	}
	
	public void putHarpoonAt(int x, int y, int range,int playerId, long time){
		Harpoon harpoon = new Harpoon(x,y,range,playerId);
		harpoonManager.addHarpoon(harpoon);
		if (myPlayerId == playerId) harpoonManager.increaseHarpoonCount();
		timeEventsManager.sinkHarpoonEvent(harpoon,time);
		if (map.getBasicMatrixSquare(x, y) == TypeSquare.empty){
			myAppSounds.playSound("putHarpoon");
			map.putHarpoonAt(x,y);
			map.addAllFissures(harpoonManager.getActiveHarpoonList());
			logFile.writeEvent("The player " + playerId + " has put an harpoon on "+ x + " " + y + " with time "+ time);
		}
	}
	
	public void sinkHarpoon(Harpoon harpoon) {
		Vector3 position = harpoon.getPosition();
		harpoonManager.sinkHarpoon(harpoon);
		timeEventsManager.freezeWaterEvent(harpoon);
		logFile.writeEvent("The harpoon in " + harpoon.getPosition().x + " " + harpoon.getPosition().y + " has sunken");		harpoonRangeDamage(harpoon);
		map.putSunkenHarpoonAt((int)position.x,(int)position.y);
		myAppSounds.playSound("breakIce");
		map.paintAllWaters(harpoonManager.getSunkenHarpoonList());
		map.addAllFissures(harpoonManager.getActiveHarpoonList());
	}
	
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

	private void harpoonRangeDamageChain(Harpoon harpoon, boolean[] isCought) {
		
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

	private void updateBlocked(Harpoon harpoon, int range, boolean[] isBlocked) {
		int x = (int)harpoon.getPosition().x;
		int y = (int)harpoon.getPosition().y;
		if (!map.canRunThrough(x,y+range)) isBlocked[0] = true;
		if (!map.canRunThrough(x,y-range)) isBlocked[1] = true;
		if (!map.canRunThrough(x-range,y)) isBlocked[2] = true;
		if (!map.canRunThrough(x+range,y)) isBlocked[3] = true;
	}

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

	public void freezeWater(Harpoon harpoon) {
		map.emptyHarpoonPosInSunkenMatrix(harpoon);
		harpoonManager.removeHarpoon(harpoon);
		map.paintAllWaters(harpoonManager.getSunkenHarpoonList());
		logFile.writeEvent("The water in " + harpoon.getPosition().x + " " + harpoon.getPosition().y + " has freezed");
	}
	
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

	public void gameTimeOff(){
		gameTimeOff=true;
	}
	private boolean playerAllowed(int playerId){
		Player player = getPlayer(playerId);
		return (player.getMaxHarpoonsAllow()-harpoonManager.getCurrentPlayerHarpoons())>0;
	}

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
		return win;
	}
	
	public boolean areAllPlayersDead() {
		Player player = null;
		boolean allDead = true;
		for (int i =0; i<numPlayers; i++){
			player = getPlayer(i);
			allDead = allDead &&  player.isThePlayerDead();
		}
		return allDead;
			
	}
		
	public int getIntegerCoordX(int playerId) {
		Player player = getPlayer(playerId);
		Vector3 position = player.getPosition();
		return (int)position.x;
	}
	
	public int getIntegerCoordY(int playerId) {
		Player player = getPlayer(playerId);
		Vector3 position = player.getPosition();
		return (int)position.y;
	}
	
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
	
	public void sinkPenguinFinish(Player player) {
		player.checkCanPlay();
		player.removeInvincible();
	}

	public int getMyPlayerRange(int playerId) {
		Player player = getPlayer(playerId);
		return player.getRange();
	}
	
	public boolean checkCorrectMove(Direction dir, int playerId){
		return (insideBoardMove(dir, playerId)&& isLegalMove(dir,playerId));
	}
	
	public boolean checkHarpoon(int x, int y) {
		return map.isEmptySquare(x,y);
	}
	
	public void checkUpgrade(Direction dir, int playerId) {
		Player player = getPlayer(playerId);
		Vector3[] positions = player.getPositions();
		if (positions[0].equals(positions[1]) && map.existUpgrade(positions[0])) {
			TypeSquare typeSquare = map.getBasicMatrixSquare((int)positions[1].x,(int)positions[1].y);
			if (typeSquare.equals(TypeSquare.invisible)) makeInvisible(player);
			else player.upgradePlayer(typeSquare);
			map.setEmpty((int)positions[0].x,(int)positions[0].y);
			logFile.writeEvent("The player "+ playerId+ " has taken an upgrade " + typeSquare.toString() + " at "+ positions[0].x + " " + positions[0].y);
		}
		else {
			if (map.existUpgrade(positions[0])){
				TypeSquare typeSquare = map.getBasicMatrixSquare((int)positions[0].x,(int)positions[0].y);
				if (typeSquare.equals(TypeSquare.invisible)) makeInvisible(player);
				else player.upgradePlayer(typeSquare);
				map.setEmpty((int)positions[0].x,(int)positions[0].y);
				logFile.writeEvent("The player "+ playerId+ " has taken an upgrade " + typeSquare.toString() + " at "+ positions[0].x + " " + positions[0].y);
			}
			else if (map.existUpgrade(positions[1])){
				TypeSquare typeSquare = map.getBasicMatrixSquare((int)positions[1].x,(int)positions[1].y);
				if (typeSquare.equals(TypeSquare.invisible)) makeInvisible(player);
				else player.upgradePlayer(typeSquare);
				map.setEmpty((int)positions[1].x,(int)positions[1].y);
				logFile.writeEvent("The player "+ playerId+ " has taken an upgrade " + typeSquare.toString() + " at "+ positions[1].x + " " + positions[1].y);
			}
		}
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

	
	
	
}
