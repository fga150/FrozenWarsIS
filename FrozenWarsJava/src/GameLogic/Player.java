package GameLogic;

import Application.MatchManager.Direction;

import com.badlogic.gdx.math.Vector3;

public class Player {

	private Vector3 position;
	private Direction lookAt;
	private int lifes;
	private int speed;
	private int range;
	private int maxHarpoonsAllow;
	private boolean throwSkill;
	private boolean specialMove;
	private Direction specialMoveDir;
	
	public Player(Vector3 position){
		initialitePlayer();
	}
	
	public Player(int i) {
		initialitePlayer();
		if (i == 0){
			position = new Vector3(0,0,0);
			this.lookAt = Direction.right;	
		}
		else if (i == 1){
			position = new Vector3(10,0,0);
			this.lookAt = Direction.left;	
		}
		else if (i == 2){
			position = new Vector3(0,10,0);
			this.lookAt = Direction.right;	
		}
		else if (i == 3){
			position = new Vector3(10,10,0);
			this.lookAt = Direction.left;	
		}
	}
	
	public void initialitePlayer(){
		this.lifes = 3;
		this.speed = 1;
		this.range = 1;
		this.maxHarpoonsAllow = 1;
		this.throwSkill=false;
		this.specialMove=false;
	}

	
	public Vector3 getInitialPosition(int myPlayerId) {
		if (myPlayerId == 0){
			position = new Vector3(0,0,0);
			this.lookAt = Direction.right;	
		}
		else if (myPlayerId == 1){
			position = new Vector3(10,0,0);
			this.lookAt = Direction.left;	
		}
		else if (myPlayerId == 2){
			position = new Vector3(0,10,0);
			this.lookAt = Direction.right;	
		}
		else if (myPlayerId == 3){
			position = new Vector3(10,10,0);
			this.lookAt = Direction.left;	
		}
		return position;
	}
	
	public void setPositionX(float newPositionX) {
		this.position.x=newPositionX;
	}
	
	public void setPositionY(float newPositionY) {
		this.position.y=newPositionY;
	}
	
	//Getters and Setters

	public Vector3 getPosition() {
		return position;
	}
	public void setPosition(Vector3 position) {
		this.position = position;
	}
	public int getLifes() {
		return lifes;
	}
	public void setLifes(int lifes) {
		this.lifes = lifes;
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
	public boolean isThrowSkill() {
		return throwSkill;
	}
	public void setThrowSkill(boolean throwSkill) {
		this.throwSkill = throwSkill;
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
}
