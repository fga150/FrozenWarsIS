package GameLogic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.Timer;

/**
 * The main objective of this class is helping class match by controlling
 * game events such as counting where an harpoon must be sunken, the duration
 * of the sunken time, or other time events.
 */

public class TimeEventsManager implements ActionListener {
	
	private final int sunkenTime = 500;
	private enum TypeEvent{sinkHarpoon,freezeWater,timeMatch};
	
	private Match match;
	private HashMap<Timer,Object> timeEventObject;
	private HashMap<Timer,TypeEvent> typeEvent;
	
	public TimeEventsManager(Match match){
		this.match = match;
		this.timeEventObject = new HashMap<Timer,Object>();
		this.typeEvent = new HashMap<Timer,TypeEvent>();
	}
	
	public void freezeWaterEvent(Harpoon sunkenHarpoon) {
		Timer timer = new Timer(sunkenTime,this);
		timeEventObject.put(timer,sunkenHarpoon);
		typeEvent.put(timer,TypeEvent.freezeWater);
		timer.setRepeats(false);
		timer.start();
	}
	
	public void sinkHarpoonEvent(Harpoon harpoon,long time){
		long currentTime = System.currentTimeMillis();
		long waitTime = (time - currentTime);
		Timer timer = new Timer((int)waitTime,this);
		timeEventObject.put(timer,harpoon);
		typeEvent.put(timer,TypeEvent.sinkHarpoon);
		timer.setRepeats(false);
		timer.start();
	}
	
	/**
	 * 
	 */
	
	public void actionPerformed(ActionEvent e){
		Timer timer = (Timer)e.getSource();
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
	}

	public long getSunkenTime() {
		return sunkenTime;
	}
	
}
