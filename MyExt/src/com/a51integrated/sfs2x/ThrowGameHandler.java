package com.a51integrated.sfs2x;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class ThrowGameHandler extends BaseClientRequestHandler {

	public void handleClientRequest(User player, ISFSObject params) {

		MyExt parentEx = (MyExt) getParentExtension();
		User u0 = null,u1 = null,u2 = null,u3=null;
		try{
			u0=player;
			u1=parentEx.getUsuarios().get(params.getUtfString("pfriend1"));
			u2=parentEx.getUsuarios().get(params.getUtfString("pfriend2"));
			u3=parentEx.getUsuarios().get(params.getUtfString("pfriend3"));
		}
		catch(Exception e){}
		finally{
			parentEx.creatGame(u0, u1, u2, u3);
		}
	}
	
}
