package main;

import java.util.HashMap;
import java.util.Stack;

import javax.swing.JFrame;

import controller.Controller;
import controller.MainMenuController;
import controller.OverworldController;

/**
 * 
 * @author rroelke
 * the main class; orchestrates a DoodleTactics game session
 */
public class DoodleTactics extends JFrame {
	
	public static final int TILE_ROWS = 17;
	public static final int TILE_COLS = 21;
	private Screen _currentScreen;
	private GameScreen _game;
	private GameMenuScreen _gameMenu;
	private MainMenuScreen _mainMenu;
	private Stack<Controller> _control;
	private HashMap<String, Character> _allChars;
	
	public DoodleTactics() {
		super("Doodle Tactics");
		this.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		this.setSize(TILE_COLS*map.Tile.TILE_SIZE,TILE_ROWS*map.Tile.TILE_SIZE);
		_game = new GameScreen(null, this);
		_mainMenu = new MainMenuScreen(null, this);
		_gameMenu = new GameMenuScreen(null, this);
		_control = new Stack<Controller>();
		
//		this.setScreen(_game);
//		this.pushController(new OverworldController(_game));
		
		this.setScreen(_mainMenu);
		this.pushController(new MainMenuController(_mainMenu));
		
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
	 * @param screen, the new screen for the game
	 * 
	 */
	
	public void setScreen(Screen screen) {
		
		/* Check that the current screen is not null before
		 * removing */
		if(_currentScreen != null) {
			this.remove(_currentScreen);
			_currentScreen.setFocusable(false);
		}
			_currentScreen = screen;
			_currentScreen.setFocusable(true);
			this.add(screen);
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
	
	public Screen getGameScreen() {
		return _game;
	}
	
	public Screen getGameMenuScreen() {
		return _gameMenu;
	}
	
	public Screen getMainMenuScreen() {
		return _mainMenu;
	}
	
	public static void main(String[] args) {
		new DoodleTactics();
	}
	
}
