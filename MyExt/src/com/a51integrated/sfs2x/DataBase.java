package com.a51integrated.sfs2x;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.smartfoxserver.v2.db.IDBManager;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class DataBase extends BaseClientRequestHandler { //class for testing the database nothing to do with it

	@Override
	public void handleClientRequest(User player, ISFSObject params) {
		
		IDBManager dbManager = getParentExtension().getParentZone().getDBManager();// catch the manager of the db
        String sql = params.getUtfString("res");
        MyExt parentEx = (MyExt) getParentExtension();
		try {
			ISFSObject response = new SFSObject();
			Connection connection = dbManager.getConnection();
			PreparedStatement stmt = connection.prepareStatement("SELECT pword FROM users WHERE name=?");
		    stmt.setString(1, sql);
		    //ISFSArray res = dbManager.executeQuery(sql);// send the sql and got the response in res
			ResultSet res= stmt.executeQuery();
		    // Populate the response parameters
	        if (!res.first()){
	        	response.putUtfString("res", "algo no funcionó");
				parentEx.send("db", response, player);
	        }
	        
			String db=res.getString("pword");
			response.putUtfString("res", db);
	        parentEx.send("db", response, player);
	        //response.putSFSArray("resp", res);
		} catch (SQLException e) {
			ISFSObject response = new SFSObject();
			response.putUtfString("res", "no va");
			parentEx.send("db", response, player);
		}
     
        
	}


}
