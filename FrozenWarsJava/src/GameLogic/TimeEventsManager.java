package GameLogic;

import java.util.HashMap;

import GameLogic.TimeEventsTask.TypeEvent;

import com.badlogic.gdx.utils.Timer;


/**
 * The main objective of this class is helping class match by controlling
 * game events such as counting where an harpoon must be sunken, the duration
 * of the sunken time, or other time events.
 */

public class TimeEventsManager{
	
	private final float sunkenTime = 0.5f;
	private final float sinkPenguinTime = 1;
	
	private Match match;
	private HashMap<Harpoon,Timer> activeHarpoonTimer;
	
	public TimeEventsManager(Match match){
		this.match = match;
		this.activeHarpoonTimer = new HashMap<Harpoon,Timer>();
	}
	
	public void stopTimer(Harpoon harpoon) {
		Timer timer = activeHarpoonTimer.get(harpoon);
		timer.stop();
		activeHarpoonTimer.remove(harpoon);
	}
	
	public void freezeWaterEvent(Harpoon sunkenHarpoon) {
		TimeEventsTask timerTask = new TimeEventsTask(this,sunkenHarpoon,TypeEvent.freezeWater);
		Timer timer = new Timer();
		timer.scheduleTask(timerTask,sunkenTime);
		timer.start();
	}
	
	public void sinkPenguinEvent(Player player) {
		TimeEventsTask timerTask = new TimeEventsTask(this,player,TypeEvent.sinkPenguin);
		Timer timer = new Timer();
		timer.scheduleTask(timerTask,sinkPenguinTime);
		timer.start();
	}
	
	public void sinkHarpoonEvent(Harpoon harpoon,long time){
		TimeEventsTask timerTask = new TimeEventsTask(this,harpoon,TypeEvent.sinkHarpoon);
		long currentTime = System.currentTimeMillis();
		float waitTime = (time - currentTime)/1000;
		Timer timer = new Timer();
		activeHarpoonTimer.put(harpoon,timer);
		timer.scheduleTask(timerTask,waitTime);
		timer.start();
	}
	
	public void actionPerformed(TypeEvent type, Object taskObject) {
		if (type.equals(TypeEvent.sinkHarpoon)){
			Harpoon harpoon = (Harpoon)taskObject;
			Timer timer = activeHarpoonTimer.get(harpoon);
			activeHarpoonTimer.remove(timer);
			match.sinkHarpoon(harpoon);
		}
		else if (type.equals(TypeEvent.freezeWater)){
			Harpoon harpoon = (Harpoon)taskObject;
			match.freezeWater(harpoon);
		}
		else if (type.equals(TypeEvent.sinkPenguin)){
			Player player = (Player)taskObject;
			match.sinkPenguinFinish(player);
		}
	}

	public float getSunkenTime() {
		return sunkenTime;
	}

	public float getSinkPenguinTime() {
		return sinkPenguinTime;
	}

}
