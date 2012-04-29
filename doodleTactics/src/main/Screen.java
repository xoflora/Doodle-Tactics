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
		
		setDoubleBuffered(true);
	}
	
	private void removeController(Controller c) {
		removeMouseListener(c);
		removeMouseMotionListener(c);
		removeKeyListener(c);
		
		c.release();
	}
	
	private void addController(Controller c) {
		addMouseListener(c);
		addMouseMotionListener(c);
		addKeyListener(c);
		
		c.take();
	}
	
	/**
	 * sets the current control of the screen to the given controller
	 * @param controller the new controller for the screen
	 */
	public void pushControl(T controller) {
		if (getController() != null)
			removeController(getController());
		
	//	System.out.println("pushing " + controller.getClass());
		_control.push(controller);
		addController(controller);
		
		System.out.println("current stack size is " + _control.size());
	}
	
	/**
	 * removes the current controller, returning control to the previous
	 * @return the controller being removed
	 */
	public T popControl() {
		T toReturn = _control.pop();
		
		removeController(toReturn);
		
		//System.out.println("popping" + toReturn.getClass());
		addController(_control.peek());
		
		
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
}
