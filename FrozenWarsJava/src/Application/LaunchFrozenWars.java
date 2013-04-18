	package Application;

import Screens.InitialScreen;
import Screens.LoadScreen;
import Screens.MultiplayerScreen;
import Server.SmartFoxServer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;


public class LaunchFrozenWars extends Game {
	private static Game instance;
	
	public static Game getGame(){
		return instance;
	}
	
	private Screen loadScreen;
	@Override
	public void create() {
		Assets.load();
	    instance = this;
		loadScreen = new LoadScreen();
		setScreen(loadScreen);
	}
	
	public void dispose(){
		if (SmartFoxServer.isInstanced()) {
			SmartFoxServer.getInstance().dispose();
			MultiplayerScreen.getInstance().dispose();
		}
		InitialScreen.getInstance().dispose();
		GameSettings.getInstance().saveSettings();
		instance = null;
		System.exit(0);
	}

}
