package GameLogic;

import com.badlogic.gdx.math.Vector3;

import Application.MatchManager.Direction;
import GameLogic.Map.SunkenTypes;
import GameLogic.Map.FissuresTypes;
import GameLogic.Map.TypeSquare;
import GameLogic.Map.WaterTypes;

public class Match {
	
	enum TypeGame {Normal}
	
	private final int numPlayers = 4;
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
				  ||square.equals(TypeSquare.Harpoon));
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
		players[myPlayerId].setSpecialMove(false);
		//Commented method is for test sunkObject image when penguin is sunk
		// and dead player this method is called for check if there are a collision
		// between player and water.
		//isSunkenPenguin((int)players[myPlayerId].getPosition().x, (int)players[myPlayerId].getPosition().y, myPlayerId);
		return valid;
	}
	/**
	 * @param yPlayerPosition 
	 * @param xPlayerPosition * ***/
	
	public void movePlayer(Direction dir, int playerId, float xPlayerPosition, float yPlayerPosition) {
		Player myPlayer = players[playerId];
		if (!dir.equals(players[playerId].getLookAt())) players[playerId].setLookAt(dir);
		else {
			if (dir.equals(Direction.left)) myPlayer.setPositionX(xPlayerPosition-minimalMove);
			else if (dir.equals(Direction.right)) myPlayer.setPositionX(xPlayerPosition+minimalMove);
			else if (dir.equals(Direction.up)) myPlayer.setPositionY(yPlayerPosition+minimalMove);
			else if (dir.equals(Direction.down)) myPlayer.setPositionY(yPlayerPosition-minimalMove);
		}
	}
	
	public void isSunkenPenguin(int x, int y, int myPlayerId){
		if(map.getWaterMatrixSquare(x, y)!=WaterTypes.empty){
			map.sunkenObject(x, y);
			if(players[myPlayerId].getLifes()>=1){
				players[myPlayerId].setLifes(players[myPlayerId].getLifes()-1);
				players[myPlayerId].setPosition(players[myPlayerId].getInitialPosition(myPlayerId));
			}
		}
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
		map.putSunkenHarpoonAt((int)position.x,(int)position.y);
		map.paintAllWaters(harpoonManager.getSunkenHarpoonList());
		map.addAllFissures(harpoonManager.getActiveHarpoonList());
	}
	
	public void freezeWater(Harpoon harpoon) {
		map.emptyHarpoonPosInSunkenMatrix(harpoon);
		harpoonManager.removeHarpoon(harpoon);
		map.paintAllWaters(harpoonManager.getSunkenHarpoonList());
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

	public int getMyPlayerRange(int myPlayerId) {
		return players[myPlayerId].getRange();
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

	

}
