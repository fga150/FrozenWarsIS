package GameLogic;

import Application.MatchManager.Direction;

import com.badlogic.gdx.math.Vector3;

public class Player {

	private Vector3 position;
	private Vector3 initialPosition;
	private Direction specialMoveDir;
	private Direction lookAt;
	private int lifes;
	private int speed;
	private int range;
	private int maxHarpoonsAllow;
	private boolean throwSkill;
	private boolean specialMove;
	
	public Player(Vector3 position){
		initialitePlayer();
	}
	
	public Player(int i) {
		initialitePlayer();
		if (i == 0){
			initialPosition = new Vector3(0,0,0);
			position = new Vector3(0,0,0);
			this.lookAt = Direction.right;	
		}
		else if (i == 1){
			initialPosition = new Vector3(10,0,0);
			position = new Vector3(10,0,0);
			this.lookAt = Direction.left;	
		}
		else if (i == 2){
			initialPosition = new Vector3(0,10,0);
			position = new Vector3(0,10,0);
			this.lookAt = Direction.right;	
		}
		else if (i == 3){
			initialPosition = new Vector3(10,10,0);
			position = new Vector3(10,10,0);
			this.lookAt = Direction.left;	
		}
	}
	
	public void initialitePlayer(){
		this.lifes = 3;
		this.speed = 1;
		this.range = 3;
		this.maxHarpoonsAllow = 1;
		this.throwSkill=false;
		this.specialMove=false;
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
	public void setPosition(Vector3 position){
		this.position.x = position.x;
		this.position.y = position.y;
		this.position.z = position.z;
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

	public boolean isThePlayerDead() {
		return(this.lifes == 0);
	}

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

	public void removeLive() {
		if (lifes>0) {
			lifes--;
			setPosition(initialPosition);
		}
	}

	public Vector3 getInitialPosition() {
		return initialPosition;
	}

	public void setInitialPosition(Vector3 initialPosition) {
		this.initialPosition = initialPosition;
	}
}
