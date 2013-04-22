package GameLogic;

import com.badlogic.gdx.utils.Timer;

public class TimeEventsTask extends Timer.Task {
	
	public enum TypeEvent{sinkHarpoon,freezeWater,timeMatch,sinkPenguin,invisible, endGame, respawn};
	private TimeEventsManager timeEventsManager;
	private TypeEvent type;
	private Object taskObject;
	
	
	public TimeEventsTask(TimeEventsManager timeEventsManager,Object object,TypeEvent type) {
		this.timeEventsManager = timeEventsManager;
		this.type = type;
		this.taskObject = object;
	}

	public void run() {
		timeEventsManager.actionPerformed(type,taskObject);
	}

}
