package GameLogic;

import java.util.HashMap;

import com.badlogic.gdx.utils.Timer;


/**
 * The main objective of this class is helping class match by controlling
 * game events such as counting where an harpoon must be sunken, the duration
 * of the sunken time, or other time events.
 */

public class TimeEventsManager{
	
	private final float sunkenTime = 0.5f;
	private final float sinkPenguinTime = 1;
	public enum TypeEvent{sinkHarpoon,freezeWater,timeMatch,sinkPenguin};
	
	private Match match;
	private Timer timer;
	private HashMap<TimeEventsTask,Object> timeEventObject;
	private HashMap<TimeEventsTask,TypeEvent> typeEvent;
	
	public TimeEventsManager(Match match){
		this.match = match;
		this.timer = new Timer();
		timer.stop();
		this.timeEventObject = new HashMap<TimeEventsTask,Object>();
		this.typeEvent = new HashMap<TimeEventsTask,TypeEvent>();
	}
	
	public void freezeWaterEvent(Harpoon sunkenHarpoon) {
		TimeEventsTask timerTask = new TimeEventsTask(this);
		timeEventObject.put(timerTask,sunkenHarpoon);
		typeEvent.put(timerTask,TypeEvent.freezeWater);
		timer.scheduleTask(timerTask, sunkenTime);
		timer.start();
	}
	public void sinkPenguinEvent(Player player) {
		TimeEventsTask timerTask = new TimeEventsTask(this);
		timeEventObject.put(timerTask,player);
		typeEvent.put(timerTask,TypeEvent.sinkPenguin);
		timer.scheduleTask(timerTask,sinkPenguinTime);
		timer.start();
	}
	
	public void sinkHarpoonEvent(Harpoon harpoon,long time){
		TimeEventsTask timerTask = new TimeEventsTask(this);
		long currentTime = System.currentTimeMillis();
		float waitTime = (time - currentTime)/1000;
		timeEventObject.put(timerTask,harpoon);
		typeEvent.put(timerTask,TypeEvent.sinkHarpoon);
		timer.scheduleTask(timerTask,waitTime);
		timer.start();
	}
	
	public void actionPerformed(TimeEventsTask timeEventsTask){
		TimeEventsTask timer = timeEventsTask; 
		TypeEvent type = typeEvent.get(timer);
		if (type.equals(TypeEvent.sinkHarpoon)){
			Harpoon harpoon = (Harpoon)timeEventObject.get(timer);
			timeEventObject.remove(timer);
			typeEvent.remove(timer);
			match.sinkHarpoon(harpoon);
		}
		else if (type.equals(TypeEvent.freezeWater)){
			Harpoon harpoon = (Harpoon)timeEventObject.get(timer);
			timeEventObject.remove(timer);
			typeEvent.remove(timer);
			match.freezeWater(harpoon);
		}
		else if (type.equals(TypeEvent.sinkPenguin)){
			Player player = (Player)timeEventObject.get(timer);
			timeEventObject.remove(timer);
			typeEvent.remove(timer);
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
