	package Application;

import Screens.FriendsListScreen;
import Screens.HelpScreen;
import Screens.IndexScreen;
import Screens.InitialScreen;
import Screens.InviteScreen;
import Screens.LoadScreen;
import Screens.LogInScreen;
import Screens.MultiplayerScreen;
import Screens.SignInScreen;
import Server.SmartFoxServer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;


public class LaunchFrozenWars extends Game implements InputProcessor{
	private static Game instance;
	private int infoPressed = 0; //0: nothing, 1:LogInUser, 2:LogInPassword
	
	public static Game getGame(){
		return instance;
	}
	
	private Screen loadScreen;
	@Override
	public void create() {
		Assets.load();
	    Gdx.input.setInputProcessor(this);
	    Gdx.input.setCatchBackKey(true);
	    Gdx.input.setCatchMenuKey(true);
	    instance = this;
		loadScreen = new LoadScreen();
		setScreen(loadScreen);
	}
	
	@Override
	public boolean keyDown(int keycode) {
	    if(keycode == Keys.BACK){
	    	// Do back button handling (show pause menu?)
	    	if(this.getScreen()==InviteScreen.getInstance()){
	   			this.setScreen(MultiplayerScreen.getInstance());
	    	} else if(this.getScreen()==HelpScreen.getInstance()){
	    		this.setScreen(IndexScreen.getInstance());
	   	   	} else {
		   		this.setScreen(InitialScreen.getInstance());
	   		}
	    }
		return false;
	}

	@Override
	public boolean keyTyped(char key) {
		if (infoPressed == 0) return false;
		if (Integer.valueOf(key) == 0) return false;
		if (Integer.valueOf(key) == 9) {
			infoPressed++;
			if (infoPressed == 3 || infoPressed == 15 || infoPressed == 22) infoPressed = 0;
			return false;
		}
		if (Integer.valueOf(key)==8) {
			if (infoPressed == 1 || infoPressed == 2) LogInScreen.getInstance().del();
			else if (infoPressed == 11 || infoPressed == 12 || infoPressed == 13 || infoPressed == 14)
				SignInScreen.getInstance().del();
			else if (infoPressed == 21) FriendsListScreen.getInstance().del();
		} else if (Integer.valueOf(key)==13){ 
			if (infoPressed == 1 || infoPressed == 2) LogInScreen.getInstance().enter();
			else if (infoPressed == 11 || infoPressed == 12 || infoPressed == 13 || infoPressed == 14)
				SignInScreen.getInstance().enter();
			else if (infoPressed == 21) FriendsListScreen.getInstance().enter();
		} else {
			if (infoPressed == 1 || infoPressed == 2) LogInScreen.getInstance().setMessage(key);
			else if (infoPressed == 11 || infoPressed == 12 || infoPressed == 13 || infoPressed == 14)
				SignInScreen.getInstance().setMessage(key);
			else if (infoPressed == 21) FriendsListScreen.getInstance().setMessage(key);
		}
		return false;
	}

	@Override
	public boolean keyUp(int key) {
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
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public int getInfoPressed() {
		return infoPressed;
	}

	public void setInfoPressed(int infoPressed) {
		this.infoPressed = infoPressed;
	}
	
	
	public void dispose(){
		GameSettings.getInstance().saveSettings();
		if (SmartFoxServer.isInstanced()) {
			SmartFoxServer.getInstance().dispose();
			MultiplayerScreen.getInstance().dispose();
		}
		InitialScreen.getInstance().dispose();
		instance = null;
		System.exit(0);
	}

	

}
