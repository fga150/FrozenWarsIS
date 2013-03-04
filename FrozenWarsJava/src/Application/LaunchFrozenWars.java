package Application;

import Screens.LoadScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;


public class LaunchFrozenWars extends Game {
	
	private Screen loadScreen;
	@Override
	public void create() {
		Assets.load();
		GameSettings gSettings = new GameSettings("settings.xml");
	    if (gSettings.isSoundOn()){
			Assets.music.setVolume(0.5f);
			//  Assets.music.play();
			Assets.music.setLooping(true);
	    }
		loadScreen = new LoadScreen(this, gSettings);
		setScreen(loadScreen);
	}
}
