package Application;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;

public class Desktop {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//Empaquetar imagenes
		Settings settings = new Settings();
        settings.maxHeight=2048;
        settings.maxWidth=2048;
        TexturePacker2.process(settings, "image", "data","pack");    
		
		/*
		 * Para poder lanzar el juego tenemos que hacer
		- new Demo1Game(). Instancia de la clase que implementa Game
		- "Demo1". Que es el título de la aplicación.
		- 320. Ancho en píxeles.
		- 480. Alto en píxeles.
		- false. Para indicar que no queremos utilizar OpenGL S 2.0 en este caso. Por lo que se utilizará el 1.1
		*/
        new LwjglApplication(new LaunchFrozenWars(), "FrozenWars", 1024, 639, false);
	}

}
