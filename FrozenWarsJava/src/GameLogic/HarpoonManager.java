package GameLogic;

import java.util.ArrayList;
import java.util.Iterator;

public class HarpoonManager{
	
	private final int arrayListSize = 100;
	
	private int currentPlayerHarpoons;
	private ArrayList<Harpoon> activeHarpoonList;
	private ArrayList<Harpoon> sunkenHarpoonList;
	
	public HarpoonManager(int numPlayers){
		this.currentPlayerHarpoons = 0;
		this.activeHarpoonList = new ArrayList<Harpoon>(arrayListSize);
		this.sunkenHarpoonList = new ArrayList<Harpoon>(arrayListSize);
	}
	
	public Harpoon getHarpoon(int x, int y){
		Iterator<Harpoon> iterator = activeHarpoonList.iterator();
		boolean found = false;
		Harpoon harpoonAux = null;
		while (iterator.hasNext() && !found){
			harpoonAux = iterator.next();
			found = checkHarpoon(x,y,harpoonAux);
		}
		if (!found) harpoonAux = null;
		return harpoonAux;
	}
	
	public Harpoon getsinkHarpoon(int x, int y){
		Iterator<Harpoon> iterator = sunkenHarpoonList.iterator();
		boolean found = false;
		Harpoon harpoonAux = null;
		while (iterator.hasNext() && !found){
			harpoonAux = iterator.next();
			if(checkHarpoon(x,y,harpoonAux)||checkRangeHarpoon(x,y,harpoonAux)){
				return harpoonAux;
			}
		}
		return harpoonAux;
	}
	
	private boolean checkHarpoon(int x, int y, Harpoon harpoon){
		return (((int)harpoon.getPosition().x == x) && (y == (int)harpoon.getPosition().y));
	}
	
	private boolean checkRangeHarpoon(int x, int y, Harpoon harpoon){
		return (x <= harpoon.getRange()+(int)harpoon.getPosition().x && x >= harpoon.getRange()-(int)harpoon.getPosition().x
			&& y <= harpoon.getRange()+(int)harpoon.getPosition().y && y >= harpoon.getRange()-(int)harpoon.getPosition().y);
	}
	
	
	public void sinkHarpoon(Harpoon harpoon) {
		activeHarpoonList.remove(harpoon);
		sunkenHarpoonList.add(harpoon);
	}
	
	public void removeHarpoon(Harpoon harpoon) {
		sunkenHarpoonList.remove(harpoon);
	}
	
	public void addHarpoon(Harpoon harpoon) {
		activeHarpoonList.add(harpoon);
	}
	
	public void increaseHarpoonCount() {
		currentPlayerHarpoons++;		
	}
	
	public void decreaseHarpoonCount() {
		currentPlayerHarpoons--;		
	}
	
	// Getters and Setters

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

	public int getCurrentPlayerHarpoons() {
		return currentPlayerHarpoons;
	}

	public void setCurrentPlayerHarpoons(int currentPlayerHarpoons) {
		this.currentPlayerHarpoons = currentPlayerHarpoons;
	}
	
}
