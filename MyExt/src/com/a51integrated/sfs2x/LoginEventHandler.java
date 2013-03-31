package com.a51integrated.sfs2x;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.smartfoxserver.bitswarm.sessions.ISession;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.db.IDBManager;
import com.smartfoxserver.v2.exceptions.SFSErrorCode;
import com.smartfoxserver.v2.exceptions.SFSErrorData;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.exceptions.SFSLoginException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

public class LoginEventHandler extends BaseServerEventHandler{
	
	public void handleServerEvent(ISFSEvent event) throws SFSException{
		
		String userName = (String) event.getParameter(SFSEventParam.LOGIN_NAME); // Catch parameters from client request
		String cryptedPass = (String) event.getParameter(SFSEventParam.LOGIN_PASSWORD);
		ISession session = (ISession) event.getParameter(SFSEventParam.SESSION);
		
		IDBManager dbManager = getParentExtension().getParentZone().getDBManager(); // Get password from DB
		Connection connection = null;
		
        try{
	        connection = dbManager.getConnection();
	        PreparedStatement stmt = connection.prepareStatement("SELECT pword FROM users WHERE name=?");
	        stmt.setString(1, userName); // Build a prepared statement
	        
			ResultSet res = stmt.executeQuery();// Execute query
			if (!res.first()){ // Verify that one record was found
				SFSErrorData errData = new SFSErrorData(SFSErrorCode.LOGIN_BAD_USERNAME);
				errData.addParameter(userName);
				throw new SFSLoginException("BadUserName", errData);
			}
			
			String dbPword = res.getString("pword"); // catch the password from the database
			
			
			if (!getApi().checkSecurePassword(session, dbPword, cryptedPass)) // Verify the secure password
			{
				SFSErrorData data = new SFSErrorData(SFSErrorCode.LOGIN_BAD_PASSWORD);
				data.addParameter(userName);
				throw new SFSLoginException("BadUserName", data);
			}
			
        }catch (SQLException e){// User name was not found
        	
        	SFSErrorData errData = new SFSErrorData(SFSErrorCode.GENERIC_ERROR);
        	errData.addParameter("SQL Error: " + e.getMessage());
        	throw new SFSLoginException("A SQL Error occurred: " + e.getMessage(), errData);
        }
		finally{
			// Return connection to the DBManager connection pool
			try {
				connection.close();
			} catch (SQLException e) {
				SFSErrorData errData = new SFSErrorData(SFSErrorCode.GENERIC_ERROR);
	        	errData.addParameter("Error: " + e.getMessage());
	        	throw new SFSLoginException("Something wrong happened " + e.getMessage(), errData);
			}
		}
	}	
}