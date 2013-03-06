package com.a51integrated.sfs2x;

import com.smartfoxserver.v2.entities.User;

public class Player {

	private String name;
	private String state;
	private User user;
	
	Player(String name){
		this.name = name;
		state = "Waiting"; //The users starts waiting by default
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setUser(User user){
		this.user = user;
		
	}
	
	public User getUser() {
		
		return user;
	}

}
