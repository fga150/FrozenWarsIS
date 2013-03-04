package GameLogic;

import com.badlogic.gdx.math.Vector3;

public class Harpoon {
	
	private Vector3 position;
	private int range;
	
	//Getters and Setters
	
	public Harpoon(int xHarpoonPosition, int yHarpoonPosition, int range) {
		this.position = new Vector3(xHarpoonPosition,yHarpoonPosition,0);
		this.range =range;
	}
	public Vector3 getPosition() {
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
	
	
}
