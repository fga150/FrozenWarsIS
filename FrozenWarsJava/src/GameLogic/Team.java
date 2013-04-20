package GameLogic;

import java.util.ArrayList;

import GameLogic.Match.TypeGame;

public class Team {
	
	private int numTeam;
	private ArrayList<Player> players;
	private boolean share;
	

	/**
	 * @param numPlayers - The number of players in the game
	 * @param numTeam - The number of the team
	 * @param numPlayersTeam - The number of in the player inside the team
	 * @param playerId - The playerId of the first member of the team
	 * @param type - Type of the game
	 */
	public Team(int numPlayers,int numTeam, int numPlayersTeam,int playerId,TypeGame type){
		this.numTeam = numTeam;
		this.players = new ArrayList<Player>();
		for (int i=0;i<numPlayersTeam;i++){
			players.add(new Player(playerId+2*i,numPlayers,type));
		}
	}
	
	
// Getters and Setters
	public int getNumTeam() {
		return numTeam;
	}
	public void setNumTeam(int numTeam) {
		this.numTeam = numTeam;
	}
	public ArrayList<Player> getPlayers() {
		return players;
	}
	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
	public boolean isShare() {
		return share;
	}
	public void setShare(boolean share) {
		this.share = share;
	}

}
