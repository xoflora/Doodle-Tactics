package event;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import main.DoodleTactics;
import main.GameMenuScreen;
import main.GameScreen;
import main.Screen;
import map.Tile;

import controller.Controller;
import controller.GameScreenController;

/**
 * 
 * @author rroelke
 *
 */
public abstract class Event extends GameScreenController {
	
	protected boolean _hasOccurred;
	public Event(DoodleTactics dt, boolean hasOccured) {
		super(dt);
		_hasOccurred = hasOccured;
	}
	
	/**
	 * Saves an Event as a String for loading, later
	 * @return the compressed string version of the event
	 */
	public abstract String save();
	/**
	 * Decodes the String and creates an Event
	 * @param dt
	 * @param eventToParse
	 * @throws InvalidEventException 
	 * @throws IOException 
	 * @throws FileNotFoundException
	 */
	public static Event load(DoodleTactics dt,Tile t,String eventToParse) 
		throws InvalidEventException, FileNotFoundException, IOException{
		String[] split = eventToParse.split(",");
		if(split.length != 3)
			throw new InvalidEventException("Could not load event, expected 3 values");
		
		if(split[0].equals("warp"))
			return new Warp(dt,t,split[1],Boolean.getBoolean(split[2]));
		else if(split[0].equals("dialogue"))
			return new Dialogue(dt,split[1],Boolean.getBoolean(split[2]));
		else
			throw new InvalidEventException("Expected \"warp\" or \"dialogue\"");
		}
	
	//Events default to ignoring all key/mouse input, 
	// but subclasses might override and enable certain actions
	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}
	
	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {}
}
