package GameLogic;

import com.badlogic.gdx.math.Vector3;

import Application.MatchManager.Direction;
import GameLogic.Map.TypeSquare;

public class Match {
	
	enum TypeGame {Normal}
	
	private final float playerWidth = 1;
	private final float playerLength = 1;
	private final float minimalMove = 0.25f;
	
	private Map map;
	private TypeGame type;
	private Player[] players;
	private int numPlayers;
	private int numUpgrades;
	private long time;
	
	/*** ***/
	
	public Match(){
		this.map = new Map(11,11,"mapaPrueba.xml");
		this.players = new Player[4];
		for (int i=0;i<4;i++){
			players[i] = new Player(i);
		}
		this.numPlayers = 4;
		this.numUpgrades = 0;
	}
	
	/*** ***/
	
	public boolean isValidPlayerMovement(Direction dir, int myPlayerId) {
		Player myPlayer = players[myPlayerId];
		boolean isValid = false;
		if (!dir.equals(players[myPlayerId].getLookAt())) isValid = true;
		else isValid = isValidMovement(dir,myPlayer.getPosition());
		return isValid;
	}
	
	/*** Check if an object which is moved in a direction(dir) from
	 *   a position it's correct or not 
	 *   @param dir - direction of the object
	 *   @param position - position of the object
	 *   @return Returns true if the object can move 
	 *   ***/
	
	public boolean isValidMovement(Direction dir,Vector3 position) {
		return (insideBoardMove(dir,position) && validNewSquare(dir,position));	
	}

	/*** Check if a movement made in a direction(dir) from a position
	 *   made the object going to a new square on the boardGame. If
	 *   the object goes to new one, it check if square's object can or
	 *   not go through it.
	 *   @param dir - direction of the object
	 *   @param position - position of the object
	 *   @return Returns if the objet can move to the new square
	 * ***/
	
	private boolean validNewSquare(Direction dir, Vector3 position){
		
		/* If valid means that the object located at position can move with direction dir.
		   If player goes to a new square, we must check if that square
		   doesn't contains objects that its collide with. */
		
		boolean valid = true;
		boolean newSquareV = false;
		boolean newSquareH = false;
		float oldPositionX = position.x;
		float oldPositionY = position.y;
		float newPositionX = position.x;
		float newPositionY = position.y;
		
		//Calculate new position
		
		if (dir.equals(Direction.left)) newPositionX -= minimalMove;
		else if (dir.equals(Direction.right)) newPositionX += minimalMove;
		else if (dir.equals(Direction.up)) newPositionY += minimalMove;
		else if (dir.equals(Direction.down)) newPositionY -= minimalMove;
		
		// That truncate the newX and newY position to index on the boardGame
		
		int newSquareX = (int) newPositionX;
		int newSquareY = (int) newPositionY;
		
		/*Check if we moved to new square. If true we get the values of the new squares
		 to check it. */
		/*
		if (dir.equals(Direction.left)) 
			newSquare = ((int)newPositionX) != ((int)oldPositionX);
		else if (dir.equals(Direction.right)){
			newSquareX = (int)(newPositionX + playerLength);
			newSquare = newSquareX != ((int)oldPositionX);
		}
		else if (dir.equals(Direction.up)){
			newSquareY = (int)(newPositionY + playerWidth);
			newSquare = newSquareY != ((int)oldPositionY);
		}
		else if (dir.equals(Direction.down))
			newSquare = ((int)newPositionY) != ((int)oldPositionY);
*/		
		/*If moved to a new square, must check if there's any objects which can't
		go through*/
		/*
		if (newSquare){
			
			
			
			TypeSquare square = map.getposition(newSquareX,newSquareY);
			valid = !(square.equals(TypeSquare.unbreakable)|| square.equals(TypeSquare.breakable) 
					  ||square.equals(TypeSquare.Harpoon));
		}
		*/
		return valid;
	
	}
	
	/*** ***/

	private boolean insideBoardMove(Direction dir, Vector3 position) {
		boolean valid = false;
		
		float limitX = map.getLength();
		float limitY = map.getWidth();
		float newPositionX = position.x;
		float newPositionY = position.y;
		
		if (dir.equals(Direction.left)) newPositionX -= minimalMove;
		else if (dir.equals(Direction.right)) newPositionX += minimalMove;
		else if (dir.equals(Direction.up)) newPositionY += minimalMove;
		else if (dir.equals(Direction.down)) newPositionY -= minimalMove;
		
		valid = (newPositionX>=0.0) && (newPositionX<(limitX-playerLength)) && 
				(newPositionY>=0.0) && (newPositionY<(limitY-playerWidth));
		
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
	
	public void putLanceAt(int xLancePosition, int yLancePosition) {
		map.putLanceAt(xLancePosition,yLancePosition);		
	}
	
	public boolean canPutLance(int myPlayerId){
		int x = getIntegerCoordX(myPlayerId);
		int y = getIntegerCoordY(myPlayerId);
		return (map.isEmptySquare(x,y));
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
	public void setPlayers(Player[] players) {
		this.players = players;
	}
	public int getNumPlayers() {
		return numPlayers;
	}
	public void setNumPlayers(int numPlayers) {
		this.numPlayers = numPlayers;
	}
	public int getNumUpgrades() {
		return numUpgrades;
	}
	public void setNumUpgrades(int numUpgrades) {
		this.numUpgrades = numUpgrades;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}

	public Vector3 getMyPlayerPosition(int playerId) {
		Player myPlayer = players[playerId];
		Vector3 currentPosition = new Vector3(myPlayer.getPosition().x,myPlayer.getPosition().y,0);
		return currentPosition;
	}

	public TypeSquare getSquare(int i, int j) {
		return map.getposition(i,j);
	}

	public Direction getPlayerDirection(int i) {
		return players[i].getLookAt();
	}
}
