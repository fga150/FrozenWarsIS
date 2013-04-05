	package Application;

import Screens.LoadScreen;

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

}
