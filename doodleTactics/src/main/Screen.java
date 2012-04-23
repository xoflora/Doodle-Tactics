package main;

import java.util.EmptyStackException;
import java.util.Stack;

import javax.swing.JPanel;

import controller.Controller;
/**
 * 
 * @author rroelke
 * Represents a screen in a game; for example, the main menu versus normal gameplay
 */
@SuppressWarnings("serial")
public abstract class Screen<T extends Controller> extends JPanel {
	
	protected DoodleTactics _dt;
	protected Stack<T> _control;

	protected abstract T defaultController();
	
	public Screen(DoodleTactics dt) {
		_dt = dt;
		_control = new Stack<T>();
		
		pushControl(defaultController());
	}
	
	/**
	 * sets the current control of the screen to the given controller
	 * @param controller the new controller for the screen
	 */
	public void pushControl(T controller) {
		if (getController() != null) {
			removeMouseListener(getController());
			removeMouseMotionListener(getController());
			removeKeyListener(getController());
		}
		
		addMouseListener(controller);
		addMouseMotionListener(controller);
		addKeyListener(controller);
		
		_control.push(controller);
		controller.take();
	}
	
	/**
	 * removes the current controller, returning control to the previous
	 * @return the controller being removed
	 */
	public T popControl() {
		T toReturn = _control.pop();
		
		toReturn.release();
		
		removeMouseListener(toReturn);
		removeMouseMotionListener(toReturn);
		removeKeyListener(toReturn);
		
		addMouseListener(_control.peek());
		addMouseMotionListener(_control.peek());
		addKeyListener(_control.peek());
		
		return toReturn;
	}
	
	/**
	 * @return the controller current driving this screen
	 */
	public T getController() {
		try {
			return _control.peek();
		} catch(EmptyStackException e) {
			return null;
		}
	}
	
	/**
	 * switches control of the screen from one controller to another
	 * @param c the new controller
	 * @return the old controller
	 */
/*	public Controller switchController(Controller c) {
		Controller toReturn = _controller;
		removeMouseListener(toReturn);
		removeKeyListener(toReturn);
		removeMouseMotionListener(toReturn);
		_controller = c;
		addMouseListener(c);
		addKeyListener(c);
		addMouseMotionListener(c);
		return toReturn;
	}	*/
	
	public abstract void render();
}
