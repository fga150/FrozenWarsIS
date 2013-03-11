package com.a51integrated.sfs2x;

import java.util.Vector;

public class InvitationRoom {
	
	private String leaderName;
	private Vector<String> acceptedPlayers;	
	private Vector<String> refusedPlayers;
	private Vector<String> waitingPlayers;
	private int gameMode;
	private boolean enableExternalPlayers;
	private int numPlayers;
	
	InvitationRoom(String leaderName){
		this.leaderName = leaderName;
		acceptedPlayers = new Vector<String>();
		refusedPlayers = new Vector<String>();
		waitingPlayers = new Vector<String>();
		gameMode = 0;
		enableExternalPlayers = false;
		acceptedPlayers.add(leaderName);
		
		numPlayers = 1;
		
	}
	
	public void putAccepted(String name){
		boolean found = false;
		int i = 0;
		while (!found && i<waitingPlayers.size()) //Searches the player on the vector of the invitation room.
		{
			if (waitingPlayers.get(i).equals(name)){
				acceptedPlayers.add(waitingPlayers.get(i));
				waitingPlayers.remove(i);
				found = true;
			}
			i++;
		}
		
	}
	
	public void putRefused(String name){
	
		boolean found = false;
		int i = 0;
		
		while (!found && i<waitingPlayers.size()) //Searches the player on the vector of the invitation room.
		{
			if (waitingPlayers.get(i).equals(name)){ 
				refusedPlayers.add(waitingPlayers.get(i)); //Adds the player to the refusedPlayers vector
				waitingPlayers.remove(i); //Removes that player from the waitingPlayers vector
				found = true;
			}
			i++;
		}
		if (found == false){
		i=0;
		while (!found && i<acceptedPlayers.size()) //Searches the player on the vector of the invitation room.
		{
			if (acceptedPlayers.get(i).equals(name)){
				refusedPlayers.add(acceptedPlayers.get(i)); //Adds the player to the refusedPlayers vector
				acceptedPlayers.remove(i); //Removes that player from the waitingPlayers vector
				found = true;
			}
			i++;
		}
	    }
	}
	
	public void putWaiting(String name){
		//Adds a new user who needs to answer the invitation.
		waitingPlayers.add(name);
		boolean found = false;
		int i=0;
		
		while (!found && i<refusedPlayers.size()) //Searches the player on the vector of the invitation room.
		{
			if (refusedPlayers.get(i).equals(name)){
				refusedPlayers.remove(i);
				found = true;
			}
			i++;
		}
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
	
	public Vector<String> getRefusedPlayers() {
		return refusedPlayers;
	}

	public void setRefusedPlayers(Vector<String> refusedPlayers) {
		this.refusedPlayers = refusedPlayers;
	}

	public Vector<String> getWaitingPlayers() {
		return waitingPlayers;
	}

	public void setWaitingPlayers(Vector<String> waitingPlayers) {
		this.waitingPlayers = waitingPlayers;
	}
	
	public Vector<String> getAcceptedPlayers() {
		return acceptedPlayers;
	}

	public void setAcceptedPlayers(Vector<String> acceptedPlayers) {
		this.acceptedPlayers = acceptedPlayers;
	}
	
}
