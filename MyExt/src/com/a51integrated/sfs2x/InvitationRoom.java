package com.a51integrated.sfs2x;

import java.util.Vector;

public class InvitationRoom {
	
	private String leaderName;
	private Vector<Player> acceptedPlayers;	
	public Vector<Player> getAcceptedPlayers() {
		return acceptedPlayers;
	}

	private Vector<Player> refusedPlayers;
	private Vector<Player> waitingPlayers;
	private int gameMode;
	private boolean enableExternalPlayers;
	private int numPlayers;
	
	InvitationRoom(String leaderName){
		this.leaderName = leaderName;
		acceptedPlayers = new Vector<Player>();
		refusedPlayers = new Vector<Player>();
		waitingPlayers = new Vector<Player>();
		gameMode = 0;
		enableExternalPlayers = false;
		
		Player p1= new Player(leaderName); //The player who create the game is accepted by default.
		p1.setState("Accepted");
		acceptedPlayers.add(p1);
		
		numPlayers = 1;
		
	}
	
	public void putAccepted(String name){
		Player player;
		boolean found = false;
		int i = 0;
		while (!found && i<waitingPlayers.size()) //Searches the player on the vector of the invitation room.
		{
			if (waitingPlayers.get(i).getName().equals(name)){
				player = waitingPlayers.get(i);
				acceptedPlayers.add(player);
				waitingPlayers.remove(i);
				found = true;
			}
			i++;
		}
	}
	
	public void putRefused(String name){
		Player player;
		boolean found = false;
		int i = 0;
		while (!found && i<waitingPlayers.size()) //Searches the player on the vector of the invitation room.
		{
			if (waitingPlayers.get(i).getName().equals(name)){
				player = waitingPlayers.get(i); 
				refusedPlayers.add(player); //Adds the player to the refusedPlayers vector
				waitingPlayers.remove(i); //Removes that player from the waitingPlayers vector
				found = true;
			}
			i++;
		}
	}
	
	public void putWaiting(String name){
		Player player = new Player(name); //Adds a new user who needs to answer the invitation.
		waitingPlayers.add(player);
	}

	public String getLeaderName() {
		return leaderName;
	}

	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}

	public int getGameMode() {
		return gameMode;
	}

	public void setGameMode(int gameMode) {
		this.gameMode = gameMode;
	}

	public boolean isEnableExternalPlayers() {
		return enableExternalPlayers;
	}

	public void setEnableExternalPlayers(boolean enableExternalPlayers) {
		this.enableExternalPlayers = enableExternalPlayers;
	}

	public int getNumPlayers() {
		return numPlayers;
	}

	public void setNumPlayers(int numPlayers) {
		this.numPlayers = numPlayers;
	}
	
	public Vector<Player> getRefusedPlayers() {
		return refusedPlayers;
	}

	public void setRefusedPlayers(Vector<Player> refusedPlayers) {
		this.refusedPlayers = refusedPlayers;
	}

	public Vector<Player> getWaitingPlayers() {
		return waitingPlayers;
	}

	public void setWaitingPlayers(Vector<Player> waitingPlayers) {
		this.waitingPlayers = waitingPlayers;
	}

	public void setAcceptedPlayers(Vector<Player> acceptedPlayers) {
		this.acceptedPlayers = acceptedPlayers;
	}
	
}
