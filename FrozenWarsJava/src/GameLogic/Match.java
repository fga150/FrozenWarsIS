package GameLogic;

import com.badlogic.gdx.math.Vector3;

import Application.MatchManager.Direction;
import GameLogic.Map.SunkenTypes;
import GameLogic.Map.FissuresTypes;
import GameLogic.Map.TypeSquare;
import GameLogic.Map.WaterTypes;

public class Match {
	
	enum TypeGame {Normal}
	
	private int numPlayers = 4;
	private final float playerWidth = 1;
	private final float playerLength = 1;
	private final float minimalMove = 0.25f;
	
	private Map map;
	private TimeEventsManager timeEventsManager;
	private HarpoonManager harpoonManager;
	private TypeGame type;
	private Player[] players;
	private int numUpgrades;
	private long matchTime;
	private Vector3 coord;

	/*** ***/
	
	public Match(){
		this.map = new Map(11,11,"mapaPrueba.xml");
		this.harpoonManager = new HarpoonManager(numPlayers);
		this.players = new Player[numPlayers];
		this.timeEventsManager = new TimeEventsManager(this);
		for (int i=0;i<numPlayers;i++){
			players[i] = new Player(i);
		}
		coord=new Vector3();
		this.numUpgrades = 0;
	}
	
	/*** ***/
	// CHANGEST
	private boolean newSquare(int x, int y){
		TypeSquare square = map.getBasicMatrixSquare(x,y);
		return !(square.equals(TypeSquare.unbreakable)|| square.equals(TypeSquare.breakable) 
				  ||square.equals(TypeSquare.harpoon));
	}
	//END
	public boolean insideBoardMove(Direction dir, int playerId) {
		boolean valid = false;
		
		Vector3 position = players[playerId].getPosition();
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
				(!dir.equals(players[playerId].getLookAt()));
		
		return valid;
	}
	
	public Direction getSpecialMoveDir(int myPlayerId) {
		return players[myPlayerId].getSpecialMoveDir();
	}
	

	
	public boolean isSpecialMove(Direction dir, int myPlayerId) {
		boolean valid=false;
		Vector3 position=players[myPlayerId].getPosition();
		if (players[myPlayerId].getSpecialMove()){
			Direction dirPlayer=players[myPlayerId].getLookAt();
			if(dirPlayer.equals(Direction.left)){
				if(dir.equals(Direction.up)){
					valid=newSquare((int)position.x,(int) position.y+1);
				}else if(dir.equals(Direction.down)){
					valid=newSquare((int)position.x,(int) position.y-1);
				}
				if (valid){
					players[myPlayerId].setSpecialMove(!((int)(position.x-0.25f)==(position.x-0.25f)));
				}
			}else if(dirPlayer.equals(Direction.right)){
				if(dir.equals(Direction.up)){
					valid=newSquare((int)position.x+1,(int) position.y+1);
				}else if(dir.equals(Direction.down)){
					valid=newSquare((int)position.x+1,(int) position.y-1);
				}
				if (valid){
					players[myPlayerId].setSpecialMove(!((int)(position.x+0.25f)==(position.x+0.25f)));
				}
			}else if(dirPlayer.equals(Direction.up)){
				if(dir.equals(Direction.right)){
					valid=newSquare((int)position.x+1,(int) position.y+1);
				}else if(dir.equals(Direction.left)){
					valid=newSquare((int)position.x-1,(int) position.y+1);
				}
				if (valid){
					players[myPlayerId].setSpecialMove(!((int)(position.y+0.25f)==(position.y+0.25f)));
				}
			}else if(dirPlayer.equals(Direction.down)){
				if(dir.equals(Direction.right)){
					valid=newSquare((int)position.x+1,(int) position.y);
				}else if(dir.equals(Direction.left)){
					valid=newSquare((int)position.x-1,(int) position.y);
				}
				if (valid){
					players[myPlayerId].setSpecialMove(!((int)(position.y-0.25f)==(position.y-0.25f)));
				}
			}
		}else{
			if(dir.equals(players[myPlayerId].getLookAt())){
				if(dir.equals(Direction.left)){
					if(((position.y-(int)position.y)==0.5f)||((position.y-(int)position.y)==0.25f)){
							if((newSquare((int)position.x-1,(int)position.y))&&!(newSquare((int)position.x-1,(int)position.y+1))){
								valid=true;
								players[myPlayerId].setSpecialMove(true);
								players[myPlayerId].setSpecialMoveDir(Direction.down);
							}					
						}
					if (((position.y-(int)position.y)==0.75f)||((position.y-(int)position.y)==0.5f)){
						if((newSquare((int)position.x-1,(int)position.y+1))&&!(newSquare((int)position.x-1,(int)position.y))){
							valid=true;
							players[myPlayerId].setSpecialMove(true);
							players[myPlayerId].setSpecialMoveDir(Direction.up);
							}	
						}
					} else if(dir.equals(Direction.right)){
					if(((position.y-(int)position.y)==0.5f)||((position.y-(int)position.y)==0.25f)){
							if((newSquare((int)position.x+1,(int)position.y))&&!(newSquare((int)position.x+1,(int)position.y+1))){
								valid=true;
								players[myPlayerId].setSpecialMove(true);
								players[myPlayerId].setSpecialMoveDir(Direction.down);
							}					
						}
					if (((position.y-(int)position.y)==0.75f)||((position.y-(int)position.y)==0.5f)){
						if((newSquare((int)position.x+1,(int)position.y+1))&&!(newSquare((int)position.x+1,(int)position.y))){
							valid=true;
							players[myPlayerId].setSpecialMove(true);
							players[myPlayerId].setSpecialMoveDir(Direction.up);
							}	
						}
					} else if(dir.equals(Direction.up)){
					if(((position.x-(int)position.x)==0.5f)||((position.x-(int)position.x)==0.25f)){
							if((newSquare((int)position.x,(int)position.y+1))&&!(newSquare((int)position.x+1,(int)position.y+1))){
								valid=true;
								players[myPlayerId].setSpecialMove(true);
								players[myPlayerId].setSpecialMoveDir(Direction.left);
							}					
						}
					if (((position.x-(int)position.x)==0.75f)||((position.x-(int)position.x)==0.5f)){
						if((newSquare((int)position.x+1,(int)position.y+1))&&!(newSquare((int)position.x,(int)position.y+1))){
							valid=true;
							players[myPlayerId].setSpecialMove(true);
							players[myPlayerId].setSpecialMoveDir(Direction.right);
							}	
						}
					}else if(dir.equals(Direction.down)){
					if(((position.x-(int)position.x)==0.5f)||((position.x-(int)position.x)==0.25f)){
							if((newSquare((int)position.x,(int)position.y-1))&&!(newSquare((int)position.x+1,(int)position.y-1))){
								valid=true;
								players[myPlayerId].setSpecialMove(true);
								players[myPlayerId].setSpecialMoveDir(Direction.left);
							}					
						}
					if (((position.x-(int)position.x)==0.75f)||((position.x-(int)position.x)==0.5f)){
						if((newSquare((int)position.x+1,(int)position.y-1))&&!(newSquare((int)position.x,(int)position.y-1))){
							valid=true;
							players[myPlayerId].setSpecialMove(true);
							players[myPlayerId].setSpecialMoveDir(Direction.right);
							}	
						}
					}
			}
		}
		return valid;
		}

	public boolean isNormalMove(Direction dir, int myPlayerId) {
		boolean valid = false;
		if(dir.equals(players[myPlayerId].getLookAt())){
			Vector3 position=players[myPlayerId].getPosition();
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
		if (valid) players[myPlayerId].setSpecialMove(false);
		return valid;
	}
	private void loseLife(int playerId) {
		timeEventsManager.sinkPenguinEvent(players[playerId]);
		players[playerId].removeLive();
	}

	/**
	 * @param yPlayerPosition 
	 * @param xPlayerPosition 
	 **/
	
	public void movePlayer(Direction dir, int playerId, float xPlayerPosition, float yPlayerPosition) {
		Player myPlayer = players[playerId];
		if (!dir.equals(players[playerId].getLookAt())) players[playerId].setLookAt(dir);
		else {
			if (dir.equals(Direction.left)) myPlayer.setPositionX(xPlayerPosition-minimalMove);
			else if (dir.equals(Direction.right)) myPlayer.setPositionX(xPlayerPosition+minimalMove);
			else if (dir.equals(Direction.up)) myPlayer.setPositionY(yPlayerPosition+minimalMove);
			else if (dir.equals(Direction.down)) myPlayer.setPositionY(yPlayerPosition-minimalMove);
		}
		if (!players[playerId].isInvincible() && isSunkenPenguin(playerId)){
			loseLife(playerId);
		}
	}
	
	public boolean isSunkenPenguin(int myPlayerId){
		Vector3[] positions = players[myPlayerId].getPositions();
		boolean isSunken = false;
		if(map.getWaterMatrixSquare((int)positions[0].x,(int)positions[0].y)!=WaterTypes.empty){
			map.sunkenObject((int)positions[0].x,(int)positions[0].y);
			isSunken = true;
		}
		else if(map.getWaterMatrixSquare((int)positions[1].x,(int)positions[1].y)!=WaterTypes.empty){
			map.sunkenObject((int)positions[1].x,(int)positions[1].y);
			isSunken = true;
		}
		return isSunken;
	}
	
	public void putHarpoonAt(int x, int y, int range, long time){
		Harpoon harpoon = new Harpoon(x,y,range);
		harpoonManager.addHarpoon(harpoon);
		timeEventsManager.sinkHarpoonEvent(harpoon,time);
		map.putHarpoonAt(x,y);
		map.addAllFissures(harpoonManager.getActiveHarpoonList());
	}
	
	public void sinkHarpoon(Harpoon harpoon) {
		Vector3 position = harpoon.getPosition();
		harpoonManager.sinkHarpoon(harpoon);
		timeEventsManager.freezeWaterEvent(harpoon);
		harpoonRangeDamage(harpoon);
		map.putSunkenHarpoonAt((int)position.x,(int)position.y);
		map.paintAllWaters(harpoonManager.getSunkenHarpoonList());
		map.addAllFissures(harpoonManager.getActiveHarpoonList());
		
	}
	
	private void harpoonRangeDamage(Harpoon harpoon) {
		boolean[] isCought = new boolean[numPlayers];
		for (int i=0;i<numPlayers;i++) isCought[i] = false;
		harpoonRangeDamageChain(harpoon,isCought);
		for (int i=0;i<numPlayers;i++){
			if (isCought[i]) loseLife(i);
		}
	}

	private void harpoonRangeDamageChain(Harpoon harpoon, boolean[] isCought) {
		boolean[] isBlocked = new boolean [4];
		for (int i=0;i<4;i++) isBlocked[i] = false;
		int range = harpoon.getRange();
		for (int i=0;i<=range;i++){
			for (int j=0;j<numPlayers;j++){
				if (!players[j].isInvincible()){
					Vector3[] positions = players[j].getPositions();
					if (isCought(harpoon,i,positions,isBlocked)){
						isCought[j] = true;
					}
				}
			}
			if (i!=0) checkHarpoonsInRange(harpoon,i,isCought,isBlocked);
			updateBlocked(harpoon,i+1,isBlocked);
		}
		map.putSunkenHarpoonAt((int)harpoon.getPosition().x,(int)harpoon.getPosition().y);
		map.paintAllWaters(harpoonManager.getSunkenHarpoonList());
	}

	private void checkHarpoonsInRange(Harpoon harpoon, int range, boolean[] isCought, boolean[] isBlocked) {
		int x = (int)harpoon.getPosition().x;
		int y = (int)harpoon.getPosition().y;
		if (!isBlocked[0] && (map.getBasicMatrixSquare(x, y+range).equals(TypeSquare.harpoon))){		
			Harpoon newHarpoon = harpoonManager.getHarpoon(x,y+range);
			harpoonManager.sinkHarpoon(newHarpoon);
			timeEventsManager.stopTimer(newHarpoon);
			map.putSunkenHarpoonAt(x,y);
			harpoonRangeDamageChain(newHarpoon,isCought);
			timeEventsManager.freezeWaterEvent(newHarpoon);
		}
		else if (!isBlocked[1] && (map.getBasicMatrixSquare(x, y-range).equals(TypeSquare.harpoon))){
			Harpoon newHarpoon = harpoonManager.getHarpoon(x,y-range);
			harpoonManager.sinkHarpoon(newHarpoon);
			timeEventsManager.stopTimer(newHarpoon);
			map.putSunkenHarpoonAt(x,y);
			harpoonRangeDamageChain(newHarpoon,isCought);
			timeEventsManager.freezeWaterEvent(newHarpoon);
		}
		else if (!isBlocked[2] && (map.getBasicMatrixSquare(x-range,y).equals(TypeSquare.harpoon))){
			Harpoon newHarpoon = harpoonManager.getHarpoon(x-range,y);
			harpoonManager.sinkHarpoon(newHarpoon);
			timeEventsManager.stopTimer(newHarpoon);
			map.putSunkenHarpoonAt(x,y);
			harpoonRangeDamageChain(newHarpoon,isCought);
			timeEventsManager.freezeWaterEvent(newHarpoon);
		}
		else if (!isBlocked[3] && (map.getBasicMatrixSquare(x+range,y).equals(TypeSquare.harpoon))){
			Harpoon newHarpoon = harpoonManager.getHarpoon(x+range,y);
			harpoonManager.sinkHarpoon(newHarpoon);
			timeEventsManager.stopTimer(newHarpoon);
			map.putSunkenHarpoonAt(x,y);
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
	}
	
	private boolean isLegalMove(Direction dir, int playerId){
		boolean valid = false;
		if(dir.equals(players[playerId].getLookAt())){
			Vector3 position=players[playerId].getPosition();
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
	
	public boolean canPutHarpoon(int myPlayerId){
		Vector3 position=players[myPlayerId].getPosition();
		if(players[myPlayerId].getLookAt().equals(Direction.right)){
			if ((position.x-(int)position.x)==0){
				coord.x=position.x;
				coord.y=position.y;
			}else{
				coord.x=position.x+1;
				coord.y=position.y;
			}
		}else if(players[myPlayerId].getLookAt().equals(Direction.left)){
				coord.x=position.x;
				coord.y=position.y;
			
		}else if(players[myPlayerId].getLookAt().equals(Direction.up)){
			if ((position.y-(int)position.y)==0){
				coord.x=position.x;
				coord.y=position.y;
			}else{
				coord.x=position.x;
				coord.y=position.y+1;
			}
		}else if(players[myPlayerId].getLookAt().equals(Direction.down)){
				coord.x=position.x;
				coord.y=position.y;
			
		}
		return (map.isEmptySquare((int)coord.x,(int)coord.y));
	}	

	public boolean imTheWinner(int numPlayer){
		boolean win = !players[numPlayer].isThePlayerDead();
		for(int i = 0; i< numPlayers; i++){
			if(i != numPlayer){
				win = win && players[i].isThePlayerDead();
			}
		}
		return win;
	}
	
	
	public int getIntegerCoordX(int myPlayerId) {
		Player myPlayer = players[myPlayerId];
		Vector3 position = myPlayer.getPosition();
		return (int)position.x;
	}
	
	public int getIntegerCoordY(int myPlayerId) {
		Player myPlayer = players[myPlayerId];
		Vector3 position = myPlayer.getPosition();
		return (int)position.y;
	}
	
	public void sinkPenguinFinish(Player player) {
		player.checkCanPlay();
		player.removeInvincible();
	}

	public int getMyPlayerRange(int myPlayerId) {
		return players[myPlayerId].getRange();
	}
	
	public boolean checkCorrectMove(Direction dir, int playerId){
		return (insideBoardMove(dir, playerId)&& isLegalMove(dir,playerId));
	}
	
	public boolean checkHarpoon(int x, int y) {
		return map.isEmptySquare(x,y);
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
	public Player[] getPlayers() {
		return players;
	}
	
	public Player getPlayers(int i) {
		return players[i];
	}
	
	public void setPlayers(Player[] players) {
		this.players = players;
	}
	public int getNumPlayers() {
		return numPlayers;
	}
	public int getNumUpgrades() {
		return numUpgrades;
	}
	public void setNumUpgrades(int numUpgrades) {
		this.numUpgrades = numUpgrades;
	}
	public long getTime() {
		return matchTime;
	}
	public void setTime(long time) {
		this.matchTime = time;
	}

	public Vector3 getMyPlayerPosition(int playerId) {
		Player myPlayer = players[playerId];
		Vector3 currentPosition = new Vector3(myPlayer.getPosition().x,myPlayer.getPosition().y,0);
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
	

	public Direction getPlayerDirection(int i) {
		return players[i].getLookAt();
	}
	public Vector3 getCoord() {
		return coord;
	}

	public void setCoord(Vector3 coord) {
		this.coord = coord;
	}
	public int getPlayerLifes(int i) {
		return players[i].getLifes();
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

	public boolean isThePlayerDead(int numPlayer) {
		return players[numPlayer].isThePlayerDead();
	}

	public Direction getLookAt(int i) {
		return players[i].getLookAt();
	}

	public boolean canPlay(int i) {
		return players[i].canPlay();
	}

	public void setNumPlayers(int i) {
		this.numPlayers = i;
		
	}

	public void checkUpgrade(Direction dir, int myPlayer) {
		Vector3[] positions = players[myPlayer].getPositions();
		if (map.existUpgrade(positions[0])) UpgradePlayer(positions[0]);
		else if (map.existUpgrade(positions[1]))UpgradePlayer(positions[1]);
	}

	private void UpgradePlayer(Vector3 pos) {

		
	}



}
