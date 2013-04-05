package GameLogic;

import com.badlogic.gdx.utils.Timer;

public class TimeEventsTask extends Timer.Task {

	TimeEventsManager tvm;	
	
	public TimeEventsTask(TimeEventsManager timeEventsManager) {
		this.tvm = timeEventsManager;
	}

	public void run() {
		tvm.actionPerformed(this);
	}

}
