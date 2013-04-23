package Application;

import Screens.FriendsListScreen;
import Screens.HelpScreen;
import Screens.IndexScreen;
import Screens.InitialScreen;
import Screens.InviteScreen;
import Screens.LogInScreen;
import Screens.MultiplayerScreen;
import Screens.SignInScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;

public class MyInputProcessor implements InputProcessor {
	private int infoPressed = 0; //0: nothing, 1:LogInUser, 2:LogInPassword
	private Game g = LaunchFrozenWars.getGame();
	
	@Override
	public boolean keyDown(int keycode) {
	    if(keycode == Keys.BACK){
	    	// Do back button handling (show pause menu?)
	    	if(g.getScreen()==InviteScreen.getInstance()){
	    		MultiplayerScreen loadScreen = MultiplayerScreen.getInstance();
	   			g.setScreen(loadScreen);
	    	} else if(g.getScreen()==HelpScreen.getInstance()){
	    		IndexScreen loadScreen = IndexScreen.getInstance();
	    		g.setScreen(loadScreen);
	   	   	} else {
		   		g.setScreen(InitialScreen.getInstance());
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
}
