package com.a51integrated.sfs2x;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class ViewedConfFriend extends BaseClientRequestHandler{

	public void handleClientRequest(User player, ISFSObject params) {
		
		String friend=params.getUtfString("friend"); 
		try {
			Connection connection = getParentExtension().getParentZone().getDBManager().getConnection();// catch the manager of the db
			PreparedStatement stmt= connection.prepareStatement("UPDATE friends SET status=? WHERE name=? AND friend=?;");
			stmt.setString(1, "c");
			stmt.setString(2, player.getName());
			stmt.setString(3, friend);
			stmt.executeUpdate();
			connection.close();	
		}catch(Exception e){
			
		}
	
	}
}
