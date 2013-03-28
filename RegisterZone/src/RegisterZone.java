import com.smartfoxserver.v2.db.IDBManager;
import com.smartfoxserver.v2.extensions.SFSExtension;


public class RegisterZone extends SFSExtension {

	protected IDBManager dbmanager;
	
	public void init() {
		
		dbmanager= getParentZone().getDBManager();// catch the manager of the db
		this.addRequestHandler("register", Register.class ); // add the handler for register the user into the database
	}

}
