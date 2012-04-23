package event;

import java.util.List;

import main.DoodleTactics;
import main.GameMenuScreen;
import main.Screen;

import controller.Controller;

/**
 * 
 * @author rroelke
 *
 */
public abstract class Event extends Controller {
	
	public Event(DoodleTactics dt) {
		super(dt);
	}
	
	@Override
	public Screen<? extends Controller> getScreen() {
		return null;
	}
	
	public void take() {
		//TODO
	}
	
	public void release() {
		//TODO
	}
}
