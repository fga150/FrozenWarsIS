package com.a51integrated.sfs2x;


import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.SFSArray;

public class InvitationRoom {
	
	private String leaderName;
	private ISFSArray acceptedPlayers;	
	private ISFSArray refusedPlayers;
	private ISFSArray waitingPlayers;
	private int gameMode;
	private boolean enableExternalPlayers;
	private int numPlayers;
	
	InvitationRoom(String leaderName, int mode){
		this.leaderName = leaderName;
		acceptedPlayers = new SFSArray();
		refusedPlayers = new SFSArray();
		waitingPlayers = new SFSArray();
		gameMode = mode;
		enableExternalPlayers = false;
		acceptedPlayers.addUtfString(leaderName);
		
		numPlayers = 1;
		
	}
	
	public void putAccepted(String name){
		boolean found = false;
		int i = 0;
		while (!found && i<waitingPlayers.size()) //Searches the player on the vector of the invitation room.
		{
			if (waitingPlayers.getUtfString(i).equals(name)){
				acceptedPlayers.addUtfString(waitingPlayers.getUtfString(i));
				waitingPlayers.removeElementAt(i);
				found = true;
				numPlayers++;
			}
			i++;
		}
		
	}
	
	public void putRefused(String name){
	
		boolean found = false;
		int i = 0;
		
		while (!found && i<waitingPlayers.size()) //Searches the player on the vector of the invitation room.
		{
			if (waitingPlayers.getUtfString(i).equals(name)){ 
				refusedPlayers.addUtfString(waitingPlayers.getUtfString(i)); //Adds the player to the refusedPlayers vector
				waitingPlayers.removeElementAt(i); //Removes that player from the waitingPlayers vector
				found = true;
			}
			i++;
		}
		if (found == false){
		i=0;
		while (!found && i<acceptedPlayers.size()) //Searches the player on the vector of the invitation room.
		{
			if (acceptedPlayers.getUtfString(i).equals(name)){
				refusedPlayers.addUtfString(acceptedPlayers.getUtfString(i)); //Adds the player to the refusedPlayers vector
				acceptedPlayers.removeElementAt(i); //Removes that player from the waitingPlayers vector
				found = true;
				numPlayers--;
			}
			i++;
		}
	    }
	}
	
	public void putWaiting(String name){
		//Adds a new user who needs to answer the invitation.
		waitingPlayers.addUtfString(name);
		boolean found = false;
		int i=0;
		
		while (!found && i<refusedPlayers.size()) //Searches the player on the vector of the invitation room.
		{
			if (refusedPlayers.getUtfString(i).equals(name)){
				refusedPlayers.removeElementAt(i);
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
	
	public ISFSArray getRefusedPlayers() {
		return refusedPlayers;
	}

	public void setRefusedPlayers(ISFSArray refusedPlayers) {
		this.refusedPlayers = refusedPlayers;
	}

	public ISFSArray getWaitingPlayers() {
		return waitingPlayers;
	}

	public void setWaitingPlayers(ISFSArray waitingPlayers) {
		this.waitingPlayers = waitingPlayers;
	}
	
	public ISFSArray getAcceptedPlayers() {
		return acceptedPlayers;
	}

	public void setAcceptedPlayers(ISFSArray acceptedPlayers) {
		this.acceptedPlayers = acceptedPlayers;
	}
	
}
