package Sounds;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;



public class AppMusic{
	private Music happyMusic;
	private Music battleMusic;
	private Music fastBattleMusic;

	private int gameMode;
	
	public AppMusic(int gameMode){
		this.gameMode = gameMode;
		switch (gameMode){ 
			case 0: 
			case 1: 
			case 2: happyMusic = Gdx.audio.newMusic(Gdx.files.getFileHandle("data/Cancion1.mp3", FileType.Internal));
					break;
			case 3: 
			case 4: battleMusic = Gdx.audio.newMusic(Gdx.files.getFileHandle("data/Cancion2.mp3", FileType.Internal));
					fastBattleMusic = Gdx.audio.newMusic(Gdx.files.getFileHandle("data/music.mp3", FileType.Internal));
					break;
			default: break;
		}
		
	}
	
	public void playMyInitialMusic(){
		switch (gameMode){ 
			case 0: 
			case 1: 
			case 2: happyMusic.setVolume(0.5f);
					playHappyMusic();
					break;
			case 3: 
			case 4: battleMusic.setVolume(0.5f);
					playBattleMusic();
					break;
			default: break;
		}
	}
	
	public void playHappyMusic(){
//		 if(battleMusic.isPlaying()) battleMusic.stop();
//		 if(fastBattleMusic.isPlaying()) fastBattleMusic.stop();
		 if(!happyMusic.isPlaying()){
			 happyMusic.setLooping(true);
			 happyMusic.setVolume(0.5f);
			 happyMusic.play();
		 }
	}
	
	public void playBattleMusic(){
//		 if(happyMusic.isPlaying()) happyMusic.stop();
		 if(fastBattleMusic.isPlaying()) fastBattleMusic.stop();
		 if(!battleMusic.isPlaying()){
			 battleMusic.setLooping(true);
			 battleMusic.setVolume(0.5f);
			 battleMusic.play();
		 }
	}
	
	public void playfastBattleMusic(){
		 if(battleMusic.isPlaying()) battleMusic.stop();
//		 if(happyMusic.isPlaying()) happyMusic.stop();
		 if(!fastBattleMusic.isPlaying()){
			 fastBattleMusic.setLooping(true);
			 fastBattleMusic.setVolume(0.5f);
			 fastBattleMusic.play();
		 }
	}

	public void stopMyMusic() {
		if(happyMusic != null && happyMusic.isPlaying()) happyMusic.stop();
		if(battleMusic != null && battleMusic.isPlaying()) battleMusic.stop();
		if(fastBattleMusic != null && fastBattleMusic.isPlaying())fastBattleMusic.stop();
		
		
	}
}
