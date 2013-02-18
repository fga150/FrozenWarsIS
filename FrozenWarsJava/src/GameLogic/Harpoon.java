package GameLogic;

import com.badlogic.gdx.math.Vector3;

public class Harpoon {
	
	private Vector3 position;
	private int range;
	
	//Getters and Setters
	
	public Harpoon(int xLancePosition, int yLancePosition) {
		this.position = new Vector3(xLancePosition,yLancePosition,0);
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
