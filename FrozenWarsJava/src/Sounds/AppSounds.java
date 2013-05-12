package Sounds;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class AppSounds{
		
		public static boolean activated = true;
		
		private HashMap<String, Sound> sounds;	
		
		public AppSounds(){
			setSounds(new HashMap<String,Sound>());
		}
		
		public void init(){
			loadSound("putHarpoon","put.mp3");
			loadSound("breakIce","putHarpoon.mp3");
			loadSound("gong","gong.mp3");
			loadSound("sinkPenguin","sinkPenguin.mp3");
		}
		
		private boolean loadSound(String eventType,String name){
			Sound sound;
			try{
				sound = Gdx.audio.newSound(Gdx.files.internal("sound/"+name));
				sounds.put(eventType, sound);
			}catch(GdxRuntimeException e){
				System.err.print("Coudn't find the sound "+name);
				return false;
			}
				return true;
		}

		public HashMap<String, Sound> getSounds() {
			return sounds;
		}

		public void setSounds(HashMap<String, Sound> hashMap) {
			this.sounds = hashMap;
		}
		
		
		public boolean playSound(String name){
			Sound sound = sounds.get(name);
			if (sound!=null){ 
//				int i = Hexa.random.nextInt(soundArray.size);
				sound.setVolume((long) 0.5f, 0.5f);
				sound.play();
				return true;
			}else{
				//Sound not found
				return false;
			}
		}
		
}

