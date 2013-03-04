package com.a51integrated.sfs2x;

import java.util.Vector;

public class InvitationRoom {
	
	private String leaderName;
	private Vector<Player> players;
	private int gameMode;
	private boolean enableExternalPlayers;
	private int acceptedPlayers;
	
	InvitationRoom(String leaderName){
		this.leaderName = leaderName;
		players = new Vector<Player>();
		gameMode = 0;
		enableExternalPlayers = false;
		
		Player p1= new Player(leaderName); //The player who create the game is accepted by default.
		p1.setState("Accepted");
		players.add(p1);
		
		acceptedPlayers = 1;
		
	}

	public String getLeaderName() {
		return leaderName;
	}

	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}

	public Vector<Player> getPlayers() {
		return players;
	}

	public void setPlayers(Vector<Player> players) {
		this.players = players;
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

	public int getAcceptedPlayers() {
		return acceptedPlayers;
	}

	public void setAcceptedPlayers(int acceptedPlayers) {
		this.acceptedPlayers = acceptedPlayers;
	}
	
}
