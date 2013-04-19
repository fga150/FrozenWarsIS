package GameLogic;

import Application.MatchManager.Direction;
import GameLogic.Map.TypeSquare;
import GameLogic.Match.TypeGame;

import com.badlogic.gdx.math.Vector3;

/** 
 * This class represent the main player status and the position of the penguin. There
 * are another attributes due to calculate efficient such as smart move.
 */

public class Player {

	private Vector3 position;
	private Vector3 initialPosition;
	private Direction specialMoveDir;
	private Direction lookAt;
	private int playerId;
	private int lives;
	private boolean invincible;
	private boolean canPlay;
	private boolean specialMove;
	
	//Upgrades
	
	private int speed;
	private int range;
	private int maxHarpoonsAllow;
	private boolean invisible;
	
	
	/**
	 * The constructor will make a player with normal status (3 lives, 1 speed,
	 * 1 range, 1 harpoon and no throwSkill). Depending of the player(@param i)
	 * it will be placed in one position of the map.
	 * @param i - The number id of the player (max 3)
	 * 
	 */
	
	public Player(int playerId, int numPlayers, TypeGame type) {
		this.playerId = playerId;
		initialitePlayer(playerId,type);
		initializePosition(playerId,numPlayers,type);
	}
	
	private void initializePosition(int playerId, int numPlayers, TypeGame type) {
		if (type.equals(TypeGame.Normal)) initializePositionNormal(playerId,numPlayers);
	}

	private void initializePositionNormal(int playerId, int numPlayers) {
		if (numPlayers==4){
			if (playerId == 0){
				initialPosition = new Vector3(0,0,0);
				position = new Vector3(0,0,0);
				this.lookAt = Direction.right;	
			}
			else if (playerId == 1){
				initialPosition = new Vector3(10,0,0);
				position = new Vector3(10,0,0);
				this.lookAt = Direction.left;	
			}
			else if (playerId == 2){
				initialPosition = new Vector3(0,10,0);
				position = new Vector3(0,10,0);
				this.lookAt = Direction.right;	
			}
			else if (playerId == 3){
				initialPosition = new Vector3(10,10,0);
				position = new Vector3(10,10,0);
				this.lookAt = Direction.left;	
			}
		}
		else if (numPlayers == 3){
			if (playerId == 0){
				initialPosition = new Vector3(0,0,0);
				position = new Vector3(0,0,0);
				this.lookAt = Direction.right;	
			}
			else if (playerId == 1){
				initialPosition = new Vector3(10,0,0);
				position = new Vector3(10,0,0);
				this.lookAt = Direction.left;	
			}
			else if (playerId == 2){
				initialPosition = new Vector3(5,10,0);
				position = new Vector3(5,10,0);
				this.lookAt = Direction.right;	
			}
		}
		else if (numPlayers == 2){
			if (playerId == 0){
				initialPosition = new Vector3(0,0,0);
				position = new Vector3(0,0,0);
				this.lookAt = Direction.right;	
			}
			else if (playerId == 1){
				initialPosition = new Vector3(10,10,0);
				position = new Vector3(10,10,0);
				this.lookAt = Direction.left;	
			}
		}
	}

	/**
	 *  This method initializes the player with default status such as 3 lives,
	 *  1 speed upgrade, 1 range upgrade, 1 harpoon and no throw skill. Also attributes
	 *  to calculate specialMove or if the penguin is invincible are set to false.
	 */
	
	public void initialitePlayer(int playerId,TypeGame type){
		if (type.equals(TypeGame.Normal)){
			this.lives = 3;
			this.speed = 1;
			this.range = 1;
			this.maxHarpoonsAllow = 1;
			this.canPlay=true;
			this.invisible=false;
			this.specialMove=false;
			this.invincible=false;
		}
	}
	
	/**
	 * This method check is the player is dead
	 * @return true if the player is dead, false if not.
	 */
	
	public boolean isThePlayerDead() {
		return(this.lives == 0);
	}
	
	/**
	 * This method returns 2 Vector3. Both are the squares where are the penguin,
	 * if the penguin is in one square, the second component will be the same as
	 * the first
	 * @return Squares of the player where the player is
	 */
	public Vector3[] getPositions() {
		Vector3[] positions = new Vector3[2];
		Vector3 position1 = new Vector3((int)position.x,(int)position.y,0);
		int position2X;
		int position2Y;
		if ((int)position.x-position.x==0) position2X = (int)position.x;
		else position2X = ((int)position.x)+1;
		if ((int)position.y-position.y==0) position2Y = (int)position.y;
		else position2Y = ((int)position.y)+1;
		Vector3 position2 = new Vector3(position2X,position2Y,0);
		positions[0] = position1;
		positions[1] = position2;
		return positions;
	}
	
	/**
	 *  Check if the player can play (lives>0) 
	 */
	
	public void checkCanPlay() {
		if (lives>0) canPlay = true;
		else canPlay = false;
	}
	
	/** 
	 * When a player lose a life it's checked that s/he has at least one life.
	 * After that the player goes to the initial position and he is invincible but
	 * he cannot play (he can do actions in game) 
	 */
	
	public void removeLive() {
		if (lives>0 && !invincible) {
			lives--;
			setPosition(initialPosition);
			invincible = true;
			canPlay = false;
		}
	}
	
	public void upgradePlayer(TypeSquare basicMatrixSquare) {
		if (basicMatrixSquare.equals(TypeSquare.bootUpgrade) && (speed<5)) speed++;
		else if (basicMatrixSquare.equals(TypeSquare.rangeUpgrade) && (range<5)) range++;
		else if (basicMatrixSquare.equals(TypeSquare.numHarpoonUpgrade) && (maxHarpoonsAllow<5)) maxHarpoonsAllow++;
	}
	
	//Getters and Setters

	public void setPositionX(float newPositionX) {
		this.position.x=newPositionX;
	}
	
	public void setPositionY(float newPositionY) {
		this.position.y=newPositionY;
	}
	
	public Vector3 getPosition() {
		return position;
	}
	
	public void setPosition(Vector3 position){
		this.position.x = position.x;
		this.position.y = position.y;
		this.position.z = position.z;
	}
	
	public int getLifes() {
		return lives;
	}
	
	public void setLifes(int lifes) {
		this.lives = lifes;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public int getRange() {
		return range;
	}
	
	public void setRange(int range) {
		this.range = range;
	}
	
	public int getMaxHarpoonsAllow() {
		return maxHarpoonsAllow;
	}
	
	public void setMaxHarpoonsAllow(int maxHarpoonsAllow) {
		this.maxHarpoonsAllow = maxHarpoonsAllow;
	}
	
	public boolean isInvisible() {
		return invisible;
	}
	
	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}

	public Direction getLookAt() {
		return lookAt;
	}

	public void setLookAt(Direction lookAt) {
		this.lookAt = lookAt;
	}
	
	public boolean getSpecialMove() {
		return specialMove;
	}

	public void setSpecialMove(boolean specialMove) {
		this.specialMove = specialMove;
	}
	
	public Direction getSpecialMoveDir() {
		return specialMoveDir;
	}

	public void setSpecialMoveDir(Direction specialMoveDir) {
		this.specialMoveDir = specialMoveDir;
	}

	public Vector3 getInitialPosition() {
		return initialPosition;
	}

	public void setInitialPosition(Vector3 initialPosition) {
		this.initialPosition = initialPosition;
	}

	public boolean isInvincible() {
		return invincible;
	}

	public void removeInvincible(){
		this.invincible = false;
	}

	public boolean canPlay() {
		return canPlay;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
}
