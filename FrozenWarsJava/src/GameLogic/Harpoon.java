package GameLogic;

import com.badlogic.gdx.math.Vector3;

public class Harpoon{
	
	private Vector3 position;
	private int range;
	private int playerId;
	
	//Getters and Setters
	
	public Harpoon(int xHarpoonPosition, int yHarpoonPosition, int range,int playerId) {
		this.position = new Vector3(xHarpoonPosition,yHarpoonPosition,range);
		this.range =range;
		this.playerId = playerId;
	}
	public Vector3 getPosition(){
		return position;
	}
	public void setPosition(Vector3 position) {
		this.position = position;
	}
	public int getRange() {
		return range;
	}
	public void setRange(int range) {
		this.range = range;
	}
	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
}
