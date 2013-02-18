package GameLogic;

import java.util.List;

public class HarpoonManager {
	
	private int[] currentHarpoon;
	private List<Harpoon> listHarpoon;
	
	// Getters and Setters
	
	public int[] getCurrentLances() {
		return currentHarpoon;
	}
	public void setCurrentLances(int[] currentLances) {
		this.currentHarpoon = currentLances;
	}
	public List<Harpoon> getListLances() {
		return listHarpoon;
	}
	public void setListLances(List<Harpoon> listLances) {
		this.listHarpoon = listLances;
	}
	
	
	
}
