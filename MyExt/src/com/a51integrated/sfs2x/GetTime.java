package com.a51integrated.sfs2x;
import java.util.Date;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;


public class GetTime extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(User player, ISFSObject params) {
		
		MyExt parentEx = (MyExt) getParentExtension();
		Date time = new Date(); //Create a new Date object
		ISFSObject rtn = new SFSObject();
		rtn.putLong("time", time.getTime()); //Prepare the SFSObject which is going to be sent with the server time.
		parentEx.send("getTime", rtn, player);
	
        
	}


}
