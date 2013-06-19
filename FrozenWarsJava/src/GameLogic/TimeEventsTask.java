package GameLogic;

import com.badlogic.gdx.utils.Timer;

public class TimeEventsTask extends Timer.Task {

	private TimeEventsManager timeEventsManager;
	private TypeEvent type;
	private Object taskObject;
	private long endTime;
	
	public TimeEventsTask(TimeEventsManager timeEventsManager,Object object,TypeEvent type) {
		this.timeEventsManager = timeEventsManager;
		this.type = type;
		this.taskObject = object;
	}
	
	public TimeEventsTask(TimeEventsManager timeEventsManager,Object object,TypeEvent type,float time) {
		this.timeEventsManager = timeEventsManager;
		this.type = type;
		this.taskObject = object;
		this.endTime = System.currentTimeMillis() + (long)(time*1000);
	}

	public void run() {
		timeEventsManager.actionPerformed(type,taskObject,this);
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

}
