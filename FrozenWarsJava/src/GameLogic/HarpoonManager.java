package GameLogic;

import java.util.ArrayList;

public class HarpoonManager{
	
	private final int arrayListSize = 100;
	
	private int[] currentPlayerHarpoons;
	private ArrayList<Harpoon> activeHarpoonList;
	private ArrayList<Harpoon> sunkenHarpoonList;
	
	public HarpoonManager(int numPlayers){
		this.currentPlayerHarpoons = new int[numPlayers];
		this.activeHarpoonList = new ArrayList<Harpoon>(arrayListSize);
		this.sunkenHarpoonList = new ArrayList<Harpoon>(arrayListSize);
	}
	
	public void addHarpoon(int x, int y, int range, long time) {
		Harpoon h = new Harpoon(x,y,range);
		activeHarpoonList.add(h);
	}
	
	// Getters and Setters
	
	public int[] getCurrentHarpoon() {
		return currentPlayerHarpoons;
	}
	public void setCurrentHarpoon(int[] currentHarpoon) {
		this.currentPlayerHarpoons = currentHarpoon;
	}

	public ArrayList<Harpoon> getActiveHarpoonList() {
		return activeHarpoonList;
	}

	public void setActiveHarpoonList(ArrayList<Harpoon> activeHarpoonList) {
		this.activeHarpoonList = activeHarpoonList;
	}
	
	public ArrayList<Harpoon> getSunkenHarpoonList() {
		return sunkenHarpoonList;
	}

	public void setSunkenHarpoonList(ArrayList<Harpoon> sunkenHarpoonList) {
		this.sunkenHarpoonList = sunkenHarpoonList;
	}
	
}
