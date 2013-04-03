package com.a51integrated.sfs2x;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class FriendRequest extends BaseClientRequestHandler {

	public void handleClientRequest(User player, ISFSObject params) {
		String friend=params.getUtfString("friend");
		MyExt parentEx=(MyExt) getParentExtension(); 
		ISFSObject response = new SFSObject();
		Connection connection = null;
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(friend);
		if (matcher.matches()){
			try{
				connection = getParentExtension().getParentZone().getDBManager().getConnection();// catch the manager of the db
				PreparedStatement stmt = connection.prepareStatement("SELECT user FROM users WHERE email=?");
			    stmt.setString(1, friend);
				ResultSet res= stmt.executeQuery(); // send a query to know if there is a user with that email
				if (res.next()){
					friend=res.getString(1);
				}else{
					response.putUtfString("res", "UserNoExits");
			        send("FriendRequestRes", response, player);
				}
			}catch(Exception e){
			}
		}
		try {
			connection = getParentExtension().getParentZone().getDBManager().getConnection();// catch the manager of the db
			PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(name) FROM users WHERE name=?");
		    stmt.setString(1, friend);
			ResultSet res= stmt.executeQuery(); // send a query to know if there is a user with that name
	        if (!res.first()){
	        	response.putUtfString("res", "Error"); // if the query sends nothing send an error to the user
				send("FriendRequestRes", response, player);
	        }else{
				if (res.getInt(1)==0){ // if dosn't exist the friend tell it
					response.putUtfString("res", "UserNoExits");
			        send("FriendRequestRes", response, player);
				}else{
					stmt = connection.prepareStatement("SELECT COUNT(*) FROM friends WHERE name=? and friend=?");
					stmt.setString(1, player.getName());
					stmt.setString(2, friend);
					res= stmt.executeQuery(); // send a query to know if they are friends already
					if (!res.first()){
						response.putUtfString("res", "Error"); // if the query sends nothing send an error to the user
						send("FriendRequestRes", response, player);
					}else{
						if (res.getInt(1)!=0){ // there are friends or in process of being
							response.putUtfString("res", "Friends");
							send("FriendRequestRes", response, player);
						}else{
							stmt= connection.prepareStatement("INSERT INTO friends VALUES (?,?,?)");
							stmt.setString(1, player.getName());
							stmt.setString(2, friend);
							stmt.setString(3, "");
							int rowsafected= stmt.executeUpdate();
							if (rowsafected!=1){ // if the rows affected on the query were more than 1, send an error to the user
					        	response.putUtfString("res", "Error");
								send("FriendRequestRes", response, player);
					        }else{
					        	stmt.setString(1, friend);
					        	stmt.setString(2, player.getName());
					        	stmt.setString(3, "r");
					        	rowsafected= stmt.executeUpdate();
								if (rowsafected!=1){ // if the rows affected on the query were more than 1 send an error to the user
						        	response.putUtfString("res", "Error");
									send("FriendRequestRes", response, player);
						        }else{
						        	response.putUtfString("res", "Success");
						        	send("FriendRequestRes", response, player);
						        	User uFriend=parentEx.getUsers().get(friend);
						        	if(uFriend!=null){
						        		if(uFriend.getJoinedRooms().iterator().next().getName().equals("The Lobby")){//si esta conectado y no jugando el amigo le mando la peticion directamente
						        		ISFSObject response2 = new SFSObject();
						        		ISFSArray friends = new SFSArray();
						        		friends.addUtfString(friend);
						        		response2.putSFSArray("Friends", friends);
						        		send("BeFriends?",response2,uFriend);
						        		}
						        	}
						        }
					        }
						}
					}
				}
	        }
	       connection.close();
	        
		} catch (Exception e) {
			response.putUtfString("res", "Error8");
			send("FriendRequestRes", response, player);
		}
     
	}
}
