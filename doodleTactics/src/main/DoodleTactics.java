package main;

import java.util.HashMap;
import java.util.Stack;

import javax.swing.JFrame;

import controller.Controller;

/**
 * 
 * @author rroelke
 * the main class; orchestrates a DoodleTactics game session
 */
public class DoodleTactics extends JFrame {
	
	public static final int TILE_ROWS = 17;
	public static final int TILE_COLS = 21;
	private Screen _currentScreen;
	private Stack<Controller> _control;
	private HashMap<String, Character> _allChars;
	
	public DoodleTactics() {
		super("Doodle Tactics");
		this.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		this.setSize(TILE_COLS*map.Tile.TILE_SIZE,TILE_ROWS*map.Tile.TILE_SIZE);
		MainMenu mp = new MainMenu(null);
		this.add(mp);
		//this.pack();
		this.setVisible(true);
	}
	
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
	
	/**
	 * @author czchapma
	 * @return _allChars, the HashMap mapping String to Character name
	 */
	public HashMap<String,Character> getCharacterMap(){
		return _allChars;
	}
	
	public static void main(String[] args) {
		new DoodleTactics();
	}
	
}
