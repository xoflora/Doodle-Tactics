package main;

import java.util.Stack;

import controller.Controller;

/**
 * 
 * @author rroelke
 * the main class; orchestrates a DoodleTactics game session
 */
public class DoodleTactics {
	
	private Screen _currentScreen;
	private Stack<Controller> _control;
	
	/**
	 * changes control of the game to a new source
	 * @param c the new controller for the game
	 */
	public void pushController(Controller c) {
		_control.push(c);
		_currentScreen.switchController(c);
	}
	
	/**
	 * reverts control of the game to the previous controller
	 * @return the controller releasing control
	 */
	public Controller releaseControl() {
		_control.pop();
		return _currentScreen.switchController(_control.peek());
	}
	
}
