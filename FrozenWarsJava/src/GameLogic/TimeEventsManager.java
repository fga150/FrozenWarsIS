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
	
	public void addHarpoonEvent(Harpoon harpoon,long time){
		int waitTime = (int)(time - System.currentTimeMillis());
		Timer t = new Timer(waitTime,this);
		timeEventObject.put(t,harpoon);
	}
	
	/**
	 * 
	 */
	
	public void actionPerformed(ActionEvent e){
		Timer t = (Timer)e.getSource();
		match.sunkHarpoon((Harpoon)timeEventObject.get(t));
	}
	
}
