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
	
	private Match match;
	private HashMap<Timer,Object> timeEventObject;
	
	public TimeEventsManager(Match match){
		this.match = match;
		this.timeEventObject = new HashMap<Timer,Object>();
	}
	
	public void addHarpoonEvent(Harpoon harpoon,long time){
		long currentTime = System.currentTimeMillis();
		long waitTime = (time - currentTime);
		Timer timer = new Timer((int)waitTime,this);
		timeEventObject.put(timer,harpoon);
		timer.start();
	}
	
	/**
	 * 
	 */
	
	public void actionPerformed(ActionEvent e){
		Timer timer = (Timer)e.getSource();
		match.sinkHarpoon((Harpoon)timeEventObject.get(timer));
	}
	
}
