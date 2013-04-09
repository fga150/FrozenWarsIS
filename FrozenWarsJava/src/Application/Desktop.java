package Application;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.badlogic.gdx.backends.jogl.JoglApplication;
import com.badlogic.gdx.tools.imagepacker.TexturePacker;
import com.badlogic.gdx.tools.imagepacker.TexturePacker.Settings;

public class Desktop {

	
	/**
	 * @param args
	 */
	
	static private Thread.UncaughtExceptionHandler exHandler = new Thread.UncaughtExceptionHandler() {
		@Override
		public void uncaughtException(Thread t, Throwable e) {
			e.printStackTrace();
			
			PrintWriter stdOut;
			try {
				stdOut = new PrintWriter(new FileWriter("error.log", true));
				stdOut.append("\n\n ----------------------------------------------------------- \n\n");
				e.printStackTrace(stdOut);
				stdOut.append("\n\n ----------------------------------------------------------- \n\n");
				stdOut.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
	};
	
	public static void main(String[] args) {

		//Empaquetar imagenes
		Settings settings = new Settings();
        settings.padding=2;
        settings.maxHeight=2048;
        settings.maxWidth=2048;
        settings.incremental=true;
        TexturePacker.process(settings, "image", "data");    
		/*
		 * Para poder lanzar el juego tenemos que hacer
		- new Demo1Game(). Instancia de la clase que implementa Game
		- "Demo1". Que es el título de la aplicación.
		- 320. Ancho en píxeles.
		- 480. Alto en píxeles.
		- false. Para indicar que no queremos utilizar OpenGL S 2.0 en este caso. Por lo que se utilizará el 1.1
		*/
        
        Thread.setDefaultUncaughtExceptionHandler(exHandler);
        new JoglApplication(new LaunchFrozenWars(), "FrozenWars", 1024, 630, false);
    }
}
