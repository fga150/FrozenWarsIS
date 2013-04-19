	package Application;

import Screens.InitialScreen;
import Screens.LoadScreen;
import Screens.MultiplayerScreen;
import Server.SmartFoxServer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;


public class LaunchFrozenWars extends Game implements InputProcessor {
	private static Game instance;
	
	public static Game getGame(){
		return instance;
	}
	
	private Screen loadScreen;
	@Override
	public void create() {
		Gdx.input.setInputProcessor(this);
		Gdx.input.setCatchBackKey(true);
	    Gdx.input.setCatchMenuKey(true);
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

	@Override
	public boolean keyDown(int keycode) {
	       if(keycode == Keys.BACK){
	             // Do back button handling (show pause menu?)
	    	 /*  if(InitialScreen.getInstance().isMultiplayerOn() && this.getScreen()==InviteScreen.getInstance()){
	   			loadScreen = MultiplayerScreen.getInstance();
	   			setScreen(loadScreen);
	    	   }
	   			else if(this.getScreen()==HelpScreen.getInstance()){
		   			loadScreen = IndexScreen.getInstance();
		   			setScreen(loadScreen);
	   			}
	   			else {*/
		   			setScreen(InitialScreen.getInstance());
	   			//}
	       }
		return false;
	}

	@Override
	public boolean keyTyped(char arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchMoved(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

}
