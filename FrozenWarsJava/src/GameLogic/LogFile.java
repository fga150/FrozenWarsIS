package GameLogic;

import java.text.DateFormat;
import java.util.Date;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class LogFile {

	private final static String ext = ".txt";
	private final static boolean debug = true;
	
	private String storePath;
	private FileHandle fileHandle;
	
	
	public LogFile(String namePlayer){
		if (debug){
			Date date = new Date();
			storePath = Gdx.files.getExternalStoragePath();
			String dateFile = DateFormat.getTimeInstance(DateFormat.SHORT).format(date).replace(':', '-');
			if (Gdx.files.absolute("C:/hlocal/").exists()){
				fileHandle = Gdx.files.absolute("C:/hlocal/log-"+namePlayer+"---"+dateFile+ext);
			}
			else fileHandle = Gdx.files.external("/FrozenWars/log-"+namePlayer+"---"+dateFile+ext);
			String dateDisplayed = DateFormat.getInstance().format(date);
			fileHandle.writeString("Data from match at "+dateDisplayed+", played as: "+namePlayer+"\n",false);
		}
	}

	public String getStorePath() {
		return storePath;
	}

	public void setStorePath(String storePath) {
		this.storePath = storePath;
	}

	public void write(String string) {
		if (debug){
			fileHandle.writeString(string+"\n",true);
		}		
	}

	public void writeEvent(String string) {
		if (debug){
			long time = System.currentTimeMillis();
			fileHandle.writeString("["+time+"]"+string+"\n",true);
		}		
	}
	
}
