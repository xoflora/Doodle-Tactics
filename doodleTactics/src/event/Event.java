package event;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
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
	
	
	//Events default to ignoring all key/mouse input, 
	// but subclasses might override and enable certain actions
	@Override
	public void mouseClicked(MouseEvent e) {
		// Do Nothing
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// Do Nothing
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// Do Nothing
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Do Nothing	
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// Do Nothing
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		//Do Nothing
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//Do Nothing
	}

	@Override
	public void keyTyped(KeyEvent e) {
		//Do Nothing
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// Do Nothing
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// Do Nothing
		
	}
}
