package com.a51integrated.sfs2x;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class Put4Handler extends BaseClientRequestHandler {

	public void handleClientRequest(User player, ISFSObject params) {

		MyExt parentEx = (MyExt) getParentExtension();
		User u0=player;
		User u1=parentEx.getUsuarios().get(params.getUtfString("pfriend1"));
		User u2=parentEx.getUsuarios().get(params.getUtfString("pfriend2"));
		User u3=parentEx.getUsuarios().get(params.getUtfString("pfriend3"));
		parentEx.crearPartida(u0, u1, u2, u3);
	}
	
}
