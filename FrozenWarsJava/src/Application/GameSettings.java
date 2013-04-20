package Application;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class GameSettings {
	private static GameSettings instance; 
	public static GameSettings getInstance() {
		if (instance == null) instance = new GameSettings("settings.xml");
		return instance;
	}
	
	private boolean soundOn;
	private boolean vibrationOn;
	private String userName;
	private String userPassword;
	private boolean confirmedExitOn;
	
	String xml;
	String xmlPath;	
	
	public GameSettings(String xmlPath){
		instance = this;
		this.xmlPath = xmlPath;
		this.loadSettings();
	}

	public boolean isSoundOn() {
		return soundOn;
	}

	public void setSoundOn(boolean soundOn) {
		this.soundOn = soundOn;
	}

	public boolean isVibrationOn() {
		return vibrationOn;
	}

	public void setVibrationOn(boolean vibrationOn) {
		this.vibrationOn = vibrationOn;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public boolean isLoggedIn(){
		return (userName != null && userPassword != null);
	}
	
	public boolean isConfirmedExitOn() {
		return confirmedExitOn;
	}

	public void setConfirmedExitOn(boolean confirmedExitOn) {
		this.confirmedExitOn = confirmedExitOn;
	}

	
	private void loadXML() {
		xml = "";
		try {
			FileHandle handle = Gdx.files.internal("data/".concat(xmlPath));
			xml = handle.readString();
		} catch (Exception e) {
		}
	}
	
	private void saveXML() {
		xml =  "<Settings>\n";
		
		//Sound
		if (soundOn) xml = xml.concat("\t<Sound>on</Sound>\n");
		else xml = xml.concat("\t<Sound>off</Sound>\n");
		
		//Vibration
		if (vibrationOn) xml = xml.concat("\t<Vibration>on</Vibration>\n");
		else xml = xml.concat("\t<Vibration>off</Vibration>\n");
		
		//User data
		xml = xml.concat("\t<Userdata>\n");
		if (userName == null || userPassword == null){ 
			xml = xml.concat("\t\t<Username></Username>\n");
			xml = xml.concat("\t\t<Userpassword></Userpassword>\n");
		} else {
			xml = xml.concat("\t\t<Username>").concat(userName).concat("</Username>\n");
			xml = xml.concat("\t\t<Userpassword>").concat(userPassword).concat("</Userpassword>\n");
		}
		xml = xml.concat("\t</Userdata>\n");
		
		//ConfirmedExit
		if (confirmedExitOn) xml = xml.concat("\t<ConfirmedExit>on</ConfirmedExit>\n");
		else xml = xml.concat("\t<ConfirmedExit>off</ConfirmedExit>\n");
		
		xml = xml.concat("</Settings>");
		
		FileHandle handle = Gdx.files.local("data/".concat(xmlPath));//Investigando, tiene que ser así para que funcione en el móvil también.
		handle.writeString(xml, false);

	}
	
	private void loadSettings(){

		loadXML();
		
		if (!xml.equals("")){
			//Sonido
			loadSound();
			//Vib
			loadVibration();
			//User data
			loadUserData();		
			//Confimated exit
			loadConfirmedExit();
		} else {
			soundOn = true;
			vibrationOn = true;
			userName = null;
			userPassword = null;
			confirmedExitOn = true;
		}
	}
	
	private void loadSound() {
		int begin;
		int end;
		
		begin = xml.indexOf("<Sound>") + "<Sound>".length();
		end = xml.indexOf("</Sound>");
		if (xml.substring(begin, end).toLowerCase().equals("on")) soundOn = true;
		else soundOn = false;
	}
	
	private void loadVibration(){
		int begin;
		int end;
		
		begin = xml.indexOf("<Vibration>") + "<Vibration>".length();
		end = xml.indexOf("</Vibration>");
		if (xml.substring(begin, end).toLowerCase().equals("on")) vibrationOn = true;
		else vibrationOn = false;
	}
	
	private void loadConfirmedExit(){

		int begin;
		int end;
		
		begin = xml.indexOf("<ConfirmedExit>") + "<ConfirmedExit>".length();
		end = xml.indexOf("</ConfirmedExit>");
		if (xml.substring(begin, end).toLowerCase().equals("on")) confirmedExitOn = true;
		else confirmedExitOn = false;
	}
	
	private void loadUserData(){
		int begin;
		int end;
		
		begin = xml.indexOf("<Username>") + "<Username>".length();
		end = xml.indexOf("</Username>");
		userName = xml.substring(begin, end);
		
		begin = xml.indexOf("<Userpassword>") + "<Userpassword>".length();
		end = xml.indexOf("</Userpassword>");
		userPassword = xml.substring(begin, end);
		
		if (userName.equals("") || userPassword.equals("")){
			userName = null;
			userPassword = null;
		}
	}	
	
	public void saveSettings(){
		this.saveXML();
	}
}
