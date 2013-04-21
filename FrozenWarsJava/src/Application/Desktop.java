package Application;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;

public class Desktop {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		Settings settings = new Settings();
        settings.maxHeight=2048;
        settings.maxWidth=2048;        
		TexturePacker2.process(settings, "./image", "./data","pack");
*/
        new LwjglApplication(new LaunchFrozenWars(), "FrozenWars", 1024, 639, false);
	}

}
