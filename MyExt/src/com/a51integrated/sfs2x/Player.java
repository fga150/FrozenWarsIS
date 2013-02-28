package com.a51integrated.sfs2x;

public class Player {

	private String name;
	private String state;
	
	Player(String name){
		this.name = name;
		state = "Waiting";
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

}
