package Sounds;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;



public class AppMusic{
	private Music happyMusic;
	private Music battleMusic;
	private Music fastBattleMusic;

	public AppMusic(int gameMode){
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
	
	public void playMyInitialMusic(int gameMode){
		switch (gameMode){ 
			case 0: 
			case 1: 
			case 2: playHappyMusic();
					break;
			case 3: 
			case 4: playBattleMusic();
					break;
			default: break;
		}
	}
	
	public void playHappyMusic(){
//		 if(battleMusic.isPlaying()) battleMusic.stop();
//		 if(fastBattleMusic.isPlaying()) fastBattleMusic.stop();
		 if(!happyMusic.isPlaying()){
			 happyMusic.setLooping(true);
			 happyMusic.play();
		 }
	}
	
	public void playBattleMusic(){
//		 if(happyMusic.isPlaying()) happyMusic.stop();
		 if(fastBattleMusic.isPlaying()) fastBattleMusic.stop();
		 if(!battleMusic.isPlaying()){
			 battleMusic.setLooping(true);
			 battleMusic.play();
		 }
	}
	
	public void playfastBattleMusic(){
		 if(battleMusic.isPlaying()) battleMusic.stop();
//		 if(happyMusic.isPlaying()) happyMusic.stop();
		 if(!fastBattleMusic.isPlaying()){
			 fastBattleMusic.setLooping(true);
			 fastBattleMusic.play();
		 }
	}

	public void stopMyMusic() {
		if(happyMusic != null && happyMusic.isPlaying()) happyMusic.dispose();
		if(battleMusic != null && battleMusic.isPlaying()) battleMusic.dispose();
		if(fastBattleMusic != null && fastBattleMusic.isPlaying()) fastBattleMusic.dispose();
		
		
	}
}
