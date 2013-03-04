package GameLogic;

import java.util.ArrayList;


public class HarpoonManager {
	
	private int[] currentHarpoon;
	private ArrayList<Harpoon> listHarpoon;
	
	
	// Getters and Setters
	
	public int[] getCurrentHarpoon() {
		return currentHarpoon;
	}
	public void setCurrentHarpoon(int[] currentHarpoon) {
		this.currentHarpoon = currentHarpoon;
	}
	public ArrayList<Harpoon> getHarpoonList() {
		if(this.listHarpoon == null) return new ArrayList<Harpoon>();
		else return this.listHarpoon;
	}
	public void setHarpoonList(ArrayList<Harpoon> harpoonList) {
		this.listHarpoon = harpoonList;
	}
	
	
	
	
}
